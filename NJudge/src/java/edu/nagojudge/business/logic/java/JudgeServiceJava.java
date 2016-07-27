package edu.nagojudge.business.logic.java;

import edu.nagojudge.business.servicios.restful.exceptions.BusinessException;
import edu.nagojudge.msg.pojo.constants.TypeStateJudgeEnum;
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

    private final Logger LOGGER = Logger.getLogger(JudgeServiceJava.class);

    private final String NAME_CLASS_FILE_DEFAULT = "Main";
    private final String JAVAC_COMPILE = " javac ";

    private final String TYPE_MODE_SHA = "SHA-256";

    private final String ERROR_TIMEOUT_JAVAC = "Error - timeout in compilation - ";

    private final Runtime runtime = Runtime.getRuntime();
    private Process process;

    public static final String EXTENSION_JAVA = "java";

    public TypeStateJudgeEnum judgeRulingNow(String pathSourceCode, String fullPathInputFileServer, String checkSumOuputServer) throws IOException, NoSuchAlgorithmException, BusinessException {
        try {
            LOGGER.debug("INICIA JUDGE_RULING_NOW_CODE_SOURCE JVM");
            String commands[] = {EXTENSION_JAVA, NAME_CLASS_FILE_DEFAULT};
            LOGGER.debug("COMMANS_RUN=" + Arrays.toString(commands));
            ProcessBuilder builder = new ProcessBuilder(commands);
            LOGGER.debug("BASE_DIRECTORY=" + pathSourceCode);
            builder.directory(new File(pathSourceCode));
            process = builder.start();
            LOGGER.debug("START_PROCESS @ECHO");
            readDataInputProblem(process.getOutputStream(), fullPathInputFileServer);
            LOGGER.debug("READER @ECHO");
            byte[] outputFileUser = FileUtil.getInstance().parseFromInputStreamToArrayByte(process.getInputStream());
            //FileUtil.getFileUtil().createFileAccordingToByteArrayToFile(outputFileUser, pathSourceCode + java.io.File.separatorChar + "temp_out");
            LOGGER.debug("WRITER @ECHO");
            boolean judge = FileUtil.getInstance().compareFilesByCheckSumSHA(outputFileUser, checkSumOuputServer, TYPE_MODE_SHA);
            LOGGER.debug("COMPARE_BYTES_FILES @ECHO");
            LOGGER.debug("RULING_COMPARE_FILES_INPUT_OUTPUT=" + judge);
            if (process.exitValue() != 0) {
                StringBuilder messageStreamError = getStreamError();
                LOGGER.error(messageStreamError);
                throw new BusinessException(messageStreamError.toString());
            }
            LOGGER.debug("EXECUTION SUCCESS @ECHO");
            return (judge) ? TypeStateJudgeEnum.AC : TypeStateJudgeEnum.WR;
        } catch (IOException ex) {
            LOGGER.error(ex);
            throw ex;
        } catch (NoSuchAlgorithmException ex) {
            LOGGER.error(ex);
            throw ex;
        } catch (BusinessException ex) {
            LOGGER.error(ex);
            throw ex;
        } finally {
            LOGGER.debug("FINALIZA JUDGE_RULING_NOW_CODE_SOURCE JVM");
        }
    }

    public TypeStateJudgeEnum judgeProblemProcess(String pathCodeSource, String nameFileCodeSource, String fullPathInputFile, String fullPathOutputFile) throws IOException, BusinessException, NoSuchAlgorithmException, InterruptedException {
        String fullPathCodeSourceTemp = pathCodeSource + File.separatorChar + NAME_CLASS_FILE_DEFAULT + "." + EXTENSION_JAVA;
        String fullPathCodeSource = pathCodeSource + File.separatorChar + nameFileCodeSource;
        LOGGER.debug("REAL_PATH_CODE_SOURCE_TEMP=" + fullPathCodeSourceTemp);
        LOGGER.debug("REAL_PATH_CODE_SOURCE=" + fullPathCodeSource);
        TypeStateJudgeEnum outcome = null;
        synchronized (this) {
            try {
                lockFile(fullPathCodeSource, fullPathCodeSourceTemp);
                int state = runStateCompilation(fullPathCodeSourceTemp);
                if (state != 0) {
                    LOGGER.error(getStreamError());
                    return TypeStateJudgeEnum.CE;
                }
                outcome = judgeRulingNow(pathCodeSource, fullPathInputFile, fullPathOutputFile);
            } catch (IOException ex) {
                LOGGER.error(ex);
                throw ex;
            } catch (BusinessException ex) {
                LOGGER.error(ex);
                throw ex;
            } catch (NoSuchAlgorithmException ex) {
                LOGGER.error(ex);
                throw ex;
            } catch (InterruptedException ex) {
                LOGGER.error(ex);
                throw ex;
            } finally {
                unLockFile(fullPathCodeSource, fullPathCodeSourceTemp);
            }
        }
        return outcome;
    }

    public int runStateCompilation(String fullPathCodeSource) throws InterruptedException, IOException {
        try {
            LOGGER.debug("INICIA COMPILACION JVM");
            process = runtime.exec(JAVAC_COMPILE + fullPathCodeSource);
            LOGGER.debug("COMPILACION EN ESPERA ... @ECHO");
            process.waitFor();
            int exitValue = process.exitValue();
            LOGGER.debug("FLAG_RETORNO=" + exitValue);
            LOGGER.debug("RESUME_ERRORS=" + getStreamError());
            return exitValue;
        } catch (InterruptedException ex) {
            LOGGER.error(ex);
            throw ex;
        } catch (IOException ex) {
            LOGGER.error(ex);
            throw ex;
        } finally {
            LOGGER.debug("FINALIZA COMPILACION JVM");
        }
    }

    private StringBuilder getStreamError() throws IOException {
        if (process != null) {
            try {
                return readInputStream(process.getErrorStream());
            } catch (IOException ex) {
                LOGGER.error(ex);
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
            LOGGER.error(ex);
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
            reader = FileUtil.getInstance().readFile(fullPathInputFile, null);
            out = new OutputStreamWriter(outputStreamProcess);
            FileUtil.getInstance().copyFile(reader, out);
            out.flush();
        } catch (FileNotFoundException ex) {
            LOGGER.error(ex);
            throw ex;
        } catch (IOException ex) {
            LOGGER.error(ex);
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
        LOGGER.debug("INICIA - CHECK IF IS NECESSARY LOCK_FILE OR WAIT_TO_SECONDS");
        int FORCE_TIME_OUT = 120;
        boolean fileIsUnlock = fileSource.exists();
        if (fileTemp.exists()) {
            LOGGER.debug("TRY UNLOCK FILE=" + fileTemp);
            do {
                try {
                    Thread.sleep(3000);
                    LOGGER.debug("FILE=" + fileTemp + " IS LOCK");
                } catch (InterruptedException ex) {
                    LOGGER.error(ex);
                }
                --FORCE_TIME_OUT;
                if (!fileTemp.exists()) {
                    LOGGER.debug("FILE=" + fileTemp + " IS UNLOCK");
                    fileIsUnlock = true;
                    break;
                }
            } while (FORCE_TIME_OUT != 0);
            if (FORCE_TIME_OUT == 0) {
                LOGGER.error("THE FILE_TEMP=" + fullPathCodeSource + " WAS NOT UNLOCK");
            }
            LOGGER.debug("FINALLY UNLOCK FILE=" + fileTemp);
        }
        if (fileIsUnlock) {
            if (fileSource.exists()) {
                LOGGER.debug("FILE_SOURCE EXIST @ECHO");
                if (fileSource.renameTo(fileTemp)) {
                    LOGGER.debug("FILE_SOURCE RENAMED @ECHO");
                } else {
                    LOGGER.error("THE FILE_SOURCE=" + fullPathCodeSource + " WAS NOT RENAMED TO FILE_TEMP=" + fullPathCodeSourceTemp);
                }
            } else {
                LOGGER.error("THE FILE_SOURCE=" + fullPathCodeSource + " NOT EXIST");
            }
        }
        LOGGER.debug("FINALIZA - CHECK IF IS NECESSARY LOCK_FILE OR WAIT_TO_SECONDS");
    }

    private void unLockFile(String fullPathCodeSource, String fullPathCodeSourceTemp) {
        File fileTemp = new File(fullPathCodeSourceTemp);
        File fileSource = new File(fullPathCodeSource);
        LOGGER.debug("INICIA - CHECK IF IS NECESSARY UNLOCK_FILE");
        if (fileTemp.exists()) {
            LOGGER.debug("FILE_TEMP EXIST @ECHO");
            if (fileTemp.renameTo(fileSource)) {
                LOGGER.debug("FILE_TEMP RENAMED @ECHO");
            } else {
                LOGGER.error("THE FILE_TEMP=" + fullPathCodeSourceTemp + " WAS NOT RENAMED TO FILE_SOURCE=" + fullPathCodeSource);
            }
        }
        LOGGER.debug("FINALIZA - CHECK IF IS NECESSARY UNLOCK_FILE");
    }

}
