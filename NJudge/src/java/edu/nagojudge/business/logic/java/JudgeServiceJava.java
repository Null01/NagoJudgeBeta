package edu.nagojudge.business.logic.java;

import edu.nagojudge.business.servicios.restful.exceptions.BusinessException;
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
import java.security.NoSuchAlgorithmException;
import org.apache.catalina.tribes.util.Arrays;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
public class JudgeServiceJava {

    private final Logger logger = Logger.getLogger(JudgeServiceJava.class);

    private final String NAME_CLASS_FILE_DEFAULT = "Main";
    private final String JAVAC_COMPILE = " javac ";

    private final String TYPE_MODE_SHA = "SHA-256";

    private final String ERROR_TIMEOUT_JAVAC = "Error - timeout in compilation - ";

    private final Runtime runtime = Runtime.getRuntime();
    private Process process;

    public static final String EXTENSION_JAVA = "java";

    public TypeStateJudgeEnum judgeRulingNow(String pathSourceCode, String fullPathInputFileServer, String checkSumOuputServer) throws IOException, NoSuchAlgorithmException, BusinessException {
        try {
            logger.debug("INICIA JUDGE_RULING_NOW_CODE_SOURCE JVM");
            String commands[] = {EXTENSION_JAVA, NAME_CLASS_FILE_DEFAULT};
            logger.debug("COMMANS_RUN [" + Arrays.toString(commands) + "]");
            ProcessBuilder builder = new ProcessBuilder(commands);
            logger.debug("BASE_DIRECTORY [" + pathSourceCode + "]");
            builder.directory(new File(pathSourceCode));
            process = builder.start();
            logger.debug("START_PROCESS @ECHO");
            readDataInputProblem(process.getOutputStream(), fullPathInputFileServer);
            logger.debug("READER @ECHO");
            byte[] outputFileUser = FileUtil.getInstance().parseFromInputStreamToArrayByte(process.getInputStream());
            //FileUtil.getFileUtil().createFileAccordingToByteArrayToFile(outputFileUser, pathSourceCode + java.io.File.separatorChar + "temp_out");
            logger.debug("WRITER @ECHO");
            boolean judge = FileUtil.getInstance().compareFilesByCheckSumSHA(outputFileUser, checkSumOuputServer, TypeSHAEnum.SHA256);
            logger.debug("COMPARE_BYTES_FILES @ECHO");
            logger.debug("RULING_COMPARE_FILES_INPUT_OUTPUT [" + judge + "]");
            if (process.exitValue() != 0) {
                StringBuilder messageStreamError = getStreamError();
                logger.error(messageStreamError);
                throw new BusinessException(messageStreamError.toString());
            }
            logger.debug("EXECUTION SUCCESS @ECHO");
            return (judge) ? TypeStateJudgeEnum.AC : TypeStateJudgeEnum.WR;
        } catch (IOException ex) {
            logger.error(ex);
            throw ex;
        } catch (NoSuchAlgorithmException ex) {
            logger.error(ex);
            throw ex;
        } catch (BusinessException ex) {
            logger.error(ex);
            throw ex;
        } finally {
            logger.debug("FINALIZA JUDGE_RULING_NOW_CODE_SOURCE JVM");
        }
    }

    public TypeStateJudgeEnum judgeProblemProcess(String pathCodeSource, String nameFileCodeSource, String fullPathInputFile, String fullPathOutputFile) throws IOException, BusinessException, NoSuchAlgorithmException, InterruptedException {
        String fullPathCodeSourceTemp = pathCodeSource + File.separatorChar + NAME_CLASS_FILE_DEFAULT + "." + EXTENSION_JAVA;
        String fullPathCodeSource = pathCodeSource + File.separatorChar + nameFileCodeSource;
        logger.debug("REAL_PATH_CODE_SOURCE_TEMP [" + fullPathCodeSourceTemp + "]");
        logger.debug("REAL_PATH_CODE_SOURCE [" + fullPathCodeSource + "]");
        TypeStateJudgeEnum outcome = null;
        synchronized (this) {
            try {
                lockFile(fullPathCodeSource, fullPathCodeSourceTemp);
                int state = runStateCompilation(fullPathCodeSourceTemp);
                if (state != 0) {
                    logger.error(getStreamError());
                    return TypeStateJudgeEnum.CE;
                }
                outcome = judgeRulingNow(pathCodeSource, fullPathInputFile, fullPathOutputFile);
            } catch (IOException ex) {
                logger.error(ex);
                throw ex;
            } catch (BusinessException ex) {
                logger.error(ex);
                throw ex;
            } catch (NoSuchAlgorithmException ex) {
                logger.error(ex);
                throw ex;
            } catch (InterruptedException ex) {
                logger.error(ex);
                throw ex;
            } finally {
                unLockFile(fullPathCodeSource, fullPathCodeSourceTemp);
            }
        }
        return outcome;
    }

    public int runStateCompilation(String fullPathCodeSource) throws InterruptedException, IOException {
        try {
            logger.debug("INICIA COMPILACION JVM");
            process = runtime.exec(JAVAC_COMPILE + fullPathCodeSource);
            logger.debug("COMPILACION EN ESPERA ... @ECHO");
            process.waitFor();
            int exitValue = process.exitValue();
            logger.debug("FLAG_RETORNO [" + exitValue + "]");
            logger.debug("RESUME_ERRORS [" + getStreamError() + "]");
            return exitValue;
        } catch (InterruptedException ex) {
            logger.error(ex);
            throw ex;
        } catch (IOException ex) {
            logger.error(ex);
            throw ex;
        } finally {
            logger.debug("FINALIZA COMPILACION JVM");
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
            reader = FileUtil.getInstance().readFile(fullPathInputFile, null);
            FileUtil.getInstance().copyFile(reader, out);
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

}
