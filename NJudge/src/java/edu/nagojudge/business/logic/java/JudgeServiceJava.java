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
import java.util.logging.Level;
import org.apache.catalina.tribes.util.Arrays;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
public class JudgeServiceJava extends JudgeService {

    private final Logger logger = Logger.getLogger(JudgeServiceJava.class);

    public static final String JAVA_EXTENSION = "java";
    private final String JAVAC_COMPILE = " javac ";
    private final String NAME_CLASS_FILE_DEFAULT = "Main";
    private final String LINK_EXE = "java " + NAME_CLASS_FILE_DEFAULT;

    private final Runtime runtime = Runtime.getRuntime();
    private Process process;

    /*
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
     */
    @Override
    public JudgeMessage executeJudgment(String pathCodeSource, String nameCodeSource, String fullPathInputFile, String checkSumOutputFile, long timeLimit) throws RunJudgeException {
        try {
            logger.debug("START PROCESS JUDGE - executeJudgment()");
            JudgeMessage judgeMessage = new JudgeMessage();
            synchronized (this) {
                String fullPathCodeSourceTemp = pathCodeSource + File.separatorChar + NAME_CLASS_FILE_DEFAULT + "." + JAVA_EXTENSION;
                String fullPathCodeSource = pathCodeSource + File.separatorChar + nameCodeSource;
                logger.debug("fullPathCodeSourceTemp [" + fullPathCodeSourceTemp + "]");
                logger.debug("fullPathCodeSource [" + fullPathCodeSource + "]");
                try {
                    lockFile(fullPathCodeSource, fullPathCodeSourceTemp);
                    runCompilation(fullPathCodeSourceTemp);
                    judgeMessage = runRuntime(pathCodeSource, fullPathInputFile, checkSumOutputFile,timeLimit);
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
            logger.debug("ENDING PROCESS JUDGE - executeJudgment()");
        }
    }

    @Override
    public void runCompilation(String fullPathCodeSource) throws RunJudgeException {
        try {
            logger.debug("START - COMPILATION JVM");
            process = runtime.exec(JAVAC_COMPILE + fullPathCodeSource);
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

    @Override
    public JudgeMessage runRuntime(String pathSourceCode, String fullPathInputFileServer, String checkSumOutputFileServer, long timeLimit) throws RunJudgeException {
        JudgeMessage judgeMessage = new JudgeMessage();
        try {
            logger.debug("INICIA JUDGE_RULING_NOW_CODE_SOURCE JVM");
            String commands[] = {"java", "-cp", ".", "Main"};
            logger.debug("COMMANS_RUN [" + Arrays.toString(commands) + "]");
            logger.debug("BASE_DIRECTORY [" + pathSourceCode + "]");
            logger.debug("START_PROCESS @ECHO");
            ProcessBuilder builder = new ProcessBuilder(commands);
            builder.directory(new File(pathSourceCode));
            final long startTime = Calendar.getInstance().getTimeInMillis();
            process = builder.start();
            TimedShell shell = new TimedShell(process, timeLimit);
            shell.start();
            process.waitFor();
            final long endTime = Calendar.getInstance().getTimeInMillis();
            writeIntoStream(process.getOutputStream(), fullPathInputFileServer); // READER_INPUTS_SERVER @ECHO
            byte[] outputFileUser = FileUtil.getInstance().parseFromInputStreamToArrayByte(process.getInputStream()); // WRITER_OUTPUTS_SERVER @ECHO
            //FileUtil.getFileUtil().createFileAccordingToByteArrayToFile(outputFileUser, pathSourceCode + java.io.File.separatorChar + "temp_out");
            boolean judge = FileUtil.getInstance().compareFilesByCheckSumSHA(outputFileUser, checkSumOutputFileServer, TypeSHAEnum.SHA256); // COMPARE FILES GENERATED
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
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(JudgeServiceJava.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            logger.debug("FINALIZA JUDGE_RULING_NOW_CODE_SOURCE JVM");
        }
        return judgeMessage;
    }

    @Override
    public StringBuilder getStreamError() throws IOException {
        if (process != null) {
            try {
                return readStream(process.getErrorStream());
            } catch (IOException ex) {
                logger.error(ex);
                throw ex;
            }
        }
        return null;
    }

    @Override
    public void writeIntoStream(OutputStream outputStream, String fullPathInputFile) throws IOException {
        Reader reader = null;
        OutputStreamWriter out = null;
        try {
            out = new OutputStreamWriter(outputStream);
            logger.debug("@Step 01");
            reader = FileUtil.getInstance().readFile(fullPathInputFile, null);
            logger.debug("@Step 02");
            /*
            //out.write("52 + 90 / 653 * 68 / 63 * 893\n"
            //        + "39 - 94 * 543 / 20 + 40 - 114\n"
            //        + "0 0 0 0 0 0");

            String s = "";
            int n;
            while ((n = reader.read()) > 0) {
                out.write(n);
                s += (char) n;
            }
            logger.debug("s [" + s + "]");
             */
            //copyFile(reader, out);
            FileUtil.getInstance().copyFile(reader, out);
            logger.debug("@Step 03");
        } catch (FileNotFoundException ex) {
            logger.error(ex);
            throw ex;
        } catch (IOException ex) {
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

}
