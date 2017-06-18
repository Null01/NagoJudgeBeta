/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.business.logic.java;

import edu.nagojudge.business.servicios.restful.exceptions.RunJudgeException;
import edu.nagojudge.msg.pojo.JudgeMessage;
import edu.nagojudge.tools.utils.FileUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
public abstract class JudgeService {

    private final String ERROR_TIMEOUT_JAVAC = "ERROR - TIMEOUT COMPILATION";

    private final Logger logger = Logger.getLogger(JudgeService.class);

    public abstract JudgeMessage executeJudgment(
            final String pathCodeSource,
            final String nameCodeSource,
            final String fullPathInputFile,
            final String checkSumOutputFile,
            final long timeLimit) throws RunJudgeException;

    public abstract void runCompilation(
            final String fullPathCodeSource) throws RunJudgeException;

    public abstract JudgeMessage runRuntime(
            final String pathSourceCode,
            final String fullPathInputFileServer,
            final String checkSumOutputFileServer,
            final long timeLimit) throws RunJudgeException;

    public abstract void writeIntoStream(OutputStream outputStream, String fullPathInputFile) throws IOException;

    public abstract StringBuilder getStreamError() throws IOException;

    public void lockFile(String fullPathCodeSource, String fullPathCodeSourceTemp) {
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

    public void unLockFile(String fullPathCodeSource, String fullPathCodeSourceTemp) {
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

    public StringBuilder readStream(InputStream inputStream) throws IOException {
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
}
