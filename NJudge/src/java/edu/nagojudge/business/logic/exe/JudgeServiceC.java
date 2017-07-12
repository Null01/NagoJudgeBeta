/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.business.logic.exe;

import static edu.nagojudge.business.logic.exe.JudgeServiceJava.JAVA_EXTENSION;
import edu.nagojudge.business.servicios.restful.exceptions.RunJudgeException;
import edu.nagojudge.msg.pojo.JudgeMessage;
import edu.nagojudge.msg.pojo.constants.TypeStateJudgeEnum;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
public class JudgeServiceC extends JudgeService {

    private final Logger logger = Logger.getLogger(JudgeServiceC.class);

    public static final String C_EXTENSION = "cpp";
    private final String JAVAC_COMPILE = " javac ";
    private final String NAME_CLASS_FILE_DEFAULT = "Main";
    private final String LINK_EXE = "java " + NAME_CLASS_FILE_DEFAULT;

    private final Runtime runtime = Runtime.getRuntime();
    private Process process;

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
                    runCompile(fullPathCodeSourceTemp);
                    judgeMessage = runExecute(pathCodeSource, fullPathInputFile, checkSumOutputFile, timeLimit);
                } catch (RunJudgeException ex) {
                    logger.error(ex);
                    judgeMessage.setStatusName(TypeStateJudgeEnum.CE.name());
                    judgeMessage.setMessageJudge(ex.getMessage());
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
    public void runCompile(String fullPathCodeSource) throws RunJudgeException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JudgeMessage runExecute(String pathSourceCode, String fullPathInputFileServer, String checkSumOutputFileServer, long timeLimit) throws RunJudgeException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void writeIntoStream(OutputStream outputStream, String fullPathInputFile) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

}
