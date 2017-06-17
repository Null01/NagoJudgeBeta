package edu.nagojudge.business.logic.java;

import edu.nagojudge.business.servicios.restful.exceptions.RunJudgeException;
import edu.nagojudge.msg.pojo.JudgeMessage;
import edu.nagojudge.msg.pojo.constants.TypeStateJudgeEnum;
import edu.nagojudge.tools.security.constants.TypeSHAEnum;
import edu.nagojudge.tools.utils.FileUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import org.apache.catalina.tribes.util.Arrays;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
public class JudgeServiceJava {
    
    private final Logger logger = Logger.getLogger(JudgeServiceJava.class);
    
    public static final String JAVA_EXTENSION = "java";
    private final String JAVAC_COMPILE = " javac ";
    private final String NAME_CLASS_FILE_DEFAULT = "Main";
    private final String LINK_EXE = "java " + NAME_CLASS_FILE_DEFAULT;
    
    private final String ERROR_TIMEOUT_JAVAC = "ERROR - TIMEOUT COMPILATION";
    
    private final Runtime runtime = Runtime.getRuntime();
    private Process process;
    
    public JudgeMessage runExecuteJudge(String pathSourceCode, String fullPathInputFileServer,
            final String checkSumOutputFileServer)
            throws RunJudgeException {
        JudgeMessage judgeMessage = new JudgeMessage();
        try {
            logger.debug("INICIA JUDGE_RULING_NOW_CODE_SOURCE JVM");
            String commands[] = {"java", "-cp", ".", "Main"};
            logger.debug("COMMANS_RUN [" + Arrays.toString(commands) + "]");
            logger.debug("BASE_DIRECTORY [" + pathSourceCode + "]");
            ProcessBuilder builder = new ProcessBuilder(commands);
            builder.directory(new File(pathSourceCode));
            logger.debug("START_PROCESS @ECHO");
            long startTime = Calendar.getInstance().getTimeInMillis();
            process = builder.start();
            readDataInputProblem(process.getOutputStream(), fullPathInputFileServer); // READER_INPUTS_SERVER @ECHO
            byte[] outputFileUser = FileUtil.getInstance().parseFromInputStreamToArrayByte(process.getInputStream()); // WRITER_OUTPUTS_SERVER @ECHO
            //FileUtil.getFileUtil().createFileAccordingToByteArrayToFile(outputFileUser, pathSourceCode + java.io.File.separatorChar + "temp_out");
            boolean judge = FileUtil.getInstance().compareFilesByCheckSumSHA(outputFileUser, checkSumOutputFileServer, TypeSHAEnum.SHA256); // COMPARE FILES GENERATED
            long endTime = Calendar.getInstance().getTimeInMillis();
            if (process.exitValue() != 0) {
                StringBuilder messageStreamError = getStreamError();
                logger.error(messageStreamError);
                throw new RunJudgeException(messageStreamError.toString());
            }
            logger.debug("COMPARE_FILES_INPUT_OUTPUT [" + judge + "] - EXECUTION SUCCESS @ECHO");
            judgeMessage.setStatusName((judge) ? TypeStateJudgeEnum.AC.name() : TypeStateJudgeEnum.WR.name());
            judgeMessage.setTimeUsed(endTime - startTime);
        } catch (RunJudgeException ex) {
            logger.error(ex);
            throw ex;
        } catch (IOException ex) {
            logger.error(ex);
            throw new RunJudgeException(ex);
        } catch (NoSuchAlgorithmException ex) {
            logger.error(ex);
            throw new RunJudgeException(ex);
        } finally {
            logger.debug("FINALIZA JUDGE_RULING_NOW_CODE_SOURCE JVM");
        }
        return judgeMessage;
    }
    
    public JudgeMessage judgeProblemProcess(String pathCodeSource, String nameFileCodeSource, String fullPathInputFile,
            final String checkSumOutputFile)
            throws RunJudgeException {
        try {
            logger.debug("START PROCESS JUDGE - judgeProblemProcess()");
            JudgeMessage judgeMessage = new JudgeMessage();
            synchronized (this) {
                String fullPathCodeSourceTemp = pathCodeSource + File.separatorChar + NAME_CLASS_FILE_DEFAULT + "." + JAVA_EXTENSION;
                String fullPathCodeSource = pathCodeSource + File.separatorChar + nameFileCodeSource;
                logger.debug("fullPathCodeSourceTemp [" + fullPathCodeSourceTemp + "]");
                logger.debug("fullPathCodeSource [" + fullPathCodeSource + "]");
                try {
                    lockFile(fullPathCodeSource, fullPathCodeSourceTemp);
                    runCompilation(fullPathCodeSourceTemp);
                    judgeMessage = runExecuteJudge(pathCodeSource, fullPathInputFile, checkSumOutputFile);
                } catch (RunJudgeException ex) {
                    logger.error(ex);
                    judgeMessage.setStatusName(TypeStateJudgeEnum.CE.name());
                    judgeMessage.setResumeStatus(ex.getMessage());
                } finally {
                    unLockFile(fullPathCodeSource, fullPathCodeSourceTemp);
                }
            }
            return judgeMessage;
        } finally {
            logger.debug("ENDING PROCESS JUDGE - judgeProblemProcess()");
        }
    }
    
    private void runCompilation(String fullPathCodeSource) throws RunJudgeException {
        try {
            logger.debug("START - COMPILATION JVM");
            process = runtime.exec(JAVAC_COMPILE + fullPathCodeSource);
            logger.debug("COMPILATION IN WAIT... @ECHO");
            process.waitFor();
            int returnExitValue = process.exitValue();
            logger.debug("COMPILATION SUCCESS... @ECHO");
            logger.debug("FLAG_RETORNO [" + returnExitValue + "]");
            if (returnExitValue != 0) {
                final StringBuilder streamError = getStreamError();
                throw new RunJudgeException(streamError.toString());
            }
        } catch (InterruptedException ex) {
            logger.error(ex);
            throw new RunJudgeException(ex);
        } catch (IOException ex) {
            logger.error(ex);
            throw new RunJudgeException(ex);
        } finally {
            logger.debug("FINALLY - COMPILATION JVM");
        }
    }
    
    private StringBuilder getStreamError() throws IOException {
        if (process != null) {
            try {
                return readInputStream(process.getErrorStream());
            } catch (IOException ex) {
                logger.error(ex);
                throw ex;
            }
        }
        return null;
    }
    
    private StringBuilder readInputStream(InputStream inputStream) throws IOException {
        String line;
        BufferedReader bufferedReader = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            bufferedReader = FileUtil.getInstance().readFile(inputStream);
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        } catch (IOException ex) {
            logger.error(ex);
            throw ex;
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
        return stringBuilder;
    }
    
    private void readDataInputProblem(OutputStream outputStreamProcess, String fullPathInputFile) throws IOException {
        Reader reader = null;
        OutputStreamWriter out = null;
        try {
            out = new OutputStreamWriter(outputStreamProcess);
            logger.debug("@Step 01");
            reader = FileUtil.getInstance().readFile(fullPathInputFile, null);
            logger.debug("@Step 02");
            //out.write("52 + 90 / 653 * 68 / 63 * 893\n"
            //        + "39 - 94 * 543 / 20 + 40 - 114\n"
            //        + "0 0 0 0 0 0");

            /*
            String s = "";
            int n;
            while ((n = reader.read()) > 0) {
                out.write(n);
                s += (char) n;
            }
            logger.debug("s [" + s + "]");
             */
            copyFile(reader, out);
            logger.debug("@Step 03");
        } catch (FileNotFoundException ex) {
            logger.error(ex);
            throw ex;
        } catch (IOException ex) {
            ex.printStackTrace();
            logger.error(ex);
            throw ex;
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }
    
    private void lockFile(String fullPathCodeSource, String fullPathCodeSourceTemp) {
        File fileTemp = new File(fullPathCodeSourceTemp);
        File fileSource = new File(fullPathCodeSource);
        logger.debug("INICIA - CHECK IF IS NECESSARY LOCK_FILE OR WAIT_TO_SECONDS");
        int FORCE_TIME_OUT = 120;
        boolean fileIsUnlock = fileSource.exists();
        if (fileTemp.exists()) {
            logger.debug("TRY UNLOCK FILE=" + fileTemp);
            do {
                try {
                    Thread.sleep(3000);
                    logger.debug("FILE [" + fileTemp + "] IS LOCK");
                } catch (InterruptedException ex) {
                    logger.error(ex);
                }
                --FORCE_TIME_OUT;
                if (!fileTemp.exists()) {
                    logger.debug("FILE [" + fileTemp + "] IS UNLOCK");
                    fileIsUnlock = true;
                    break;
                }
            } while (FORCE_TIME_OUT != 0);
            if (FORCE_TIME_OUT == 0) {
                logger.error("THE FILE_TEMP [" + fullPathCodeSource + "] WAS NOT UNLOCK");
            }
            logger.debug("FINALLY UNLOCK FILE=" + fileTemp);
        }
        if (fileIsUnlock) {
            if (fileSource.exists()) {
                logger.debug("FILE_SOURCE EXIST @ECHO");
                if (fileSource.renameTo(fileTemp)) {
                    logger.debug("FILE_SOURCE RENAMED @ECHO");
                } else {
                    logger.error("THE FILE_SOURCE [" + fullPathCodeSource + "] WAS NOT RENAMED TO FILE_TEMP [" + fullPathCodeSourceTemp + "]");
                }
            } else {
                logger.error("THE FILE_SOURCE=" + fullPathCodeSource + " NOT EXIST");
            }
        }
        logger.debug("FINALIZA - CHECK IF IS NECESSARY LOCK_FILE OR WAIT_TO_SECONDS");
    }
    
    private void unLockFile(String fullPathCodeSource, String fullPathCodeSourceTemp) {
        File fileTemp = new File(fullPathCodeSourceTemp);
        File fileSource = new File(fullPathCodeSource);
        logger.debug("INICIA - CHECK IF IS NECESSARY UNLOCK_FILE");
        if (fileTemp.exists()) {
            logger.debug("FILE_TEMP EXIST @ECHO");
            if (fileTemp.renameTo(fileSource)) {
                logger.debug("FILE_TEMP RENAMED @ECHO");
            } else {
                logger.error("THE FILE_TEMP [" + fullPathCodeSourceTemp + "] WAS NOT RENAMED TO FILE_SOURCE [" + fullPathCodeSource + "]");
            }
        }
        logger.debug("FINALIZA - CHECK IF IS NECESSARY UNLOCK_FILE");
    }
    
    public void copyFile(Reader input, Writer output) throws IOException {
        try {
            int DEFAULT_BUFFER_SIZE = 1024 * 8;
            if (input == null || output == null) {
                throw new IOException("#NJ - OBJETOS READER/WRITER NO SE ENCUENTRAN INICIALIZADOS");
            }
            char[] buffer = new char[DEFAULT_BUFFER_SIZE];
            int n;
            logger.debug("copyFile() 01 - @ECHO");
            while ((n = input.read(buffer)) != -1) {
                output.write(buffer, 0, n);
            }
            logger.debug("copyFile() 02 - @ECHO");
            output.flush();
            logger.debug("copyFile() 03 - @ECHO");
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
        }
    }
    
}
