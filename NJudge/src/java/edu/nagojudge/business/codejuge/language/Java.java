/*
 * Codejudge
 * Copyright 2012, Sankha Narayan Guria (sankha93@gmail.com)
 * Licensed under MIT License.
 *
 * Codejudge Compiler Server: Compiler for the Java language
 */
package edu.nagojudge.business.codejuge.language;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import edu.nagojudge.business.codejuge.logic.exe.TimedShell;
import edu.nagojudge.business.servicios.restful.exceptions.RunJudgeException;
import edu.nagojudge.msg.pojo.JudgeMessage;
import edu.nagojudge.msg.pojo.MetadataMessage;
import edu.nagojudge.msg.pojo.constants.TypeStateJudgeEnum;
import edu.nagojudge.tools.utils.FileUtil;
import java.io.File;
import java.util.Calendar;
import org.apache.log4j.Logger;

public class Java extends AbstractLanguage {

    private final Logger logger = Logger.getLogger(Java.class);

    private final String nameFileCodeSource, pathFileCodeSource, fullPathFileInputServer;
    private final long timeout; // Seconds

    public Java(final String nameFileCodeSource, final String pathFileCodeSource,
            final String fullPathFileInputServer, final long timeout, final MetadataMessage metadata) {
        super(metadata);
        this.nameFileCodeSource = nameFileCodeSource;
        this.pathFileCodeSource = pathFileCodeSource;
        this.fullPathFileInputServer = fullPathFileInputServer;
        this.timeout = timeout;
    }

    @Override
    public JudgeMessage compile() {
        JudgeMessage judgeMessage = new JudgeMessage();
        judgeMessage.setKeyStatus(null);
        BufferedWriter out = null;
        try {
            logger.debug("START PROCESS JUDGE - compile()");
            final String pathFileWorkspaceExecute = pathFileCodeSource + File.separatorChar + DIR_WORKSPACE_EXECUTE;
            FileUtil.getInstance().createFolders(pathFileWorkspaceExecute);
            FileUtil.getInstance().copyFile(pathFileCodeSource + File.separatorChar + nameFileCodeSource, pathFileWorkspaceExecute + File.separatorChar + nameFileCodeSource);
            File fullPathFileCodeSource = new File(pathFileWorkspaceExecute, nameFileCodeSource);
            File fullPathFileCodeSourceTemp = new File(pathFileWorkspaceExecute, "Main.java");
            boolean renameToTempCodeSource = fullPathFileCodeSource.renameTo(fullPathFileCodeSourceTemp);
            if (!renameToTempCodeSource) {
                throw new RunJudgeException("The file source [" + fullPathFileCodeSource.getAbsolutePath() + "] was not renamed to temporality file [" + fullPathFileCodeSourceTemp.getAbsolutePath() + "]");
            }

            // create the compiler script
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(pathFileWorkspaceExecute, NAME_FILE_SHELL_COMPILE))));
            out.write("cd \"" + pathFileWorkspaceExecute + "\"\n");
            out.write("javac Main.java 2> " + NAME_FILE_ERROR_COMPILATION);
            out.close();
            Runtime r = Runtime.getRuntime();
            Process p = r.exec("chmod +x " + pathFileWorkspaceExecute + File.separatorChar + NAME_FILE_SHELL_COMPILE);
            p.waitFor();
            p = r.exec(pathFileWorkspaceExecute + File.separatorChar + NAME_FILE_SHELL_COMPILE); // execute the compiler script
            p.waitFor();
            logger.debug("compilation complete @ECHO");

            StringBuilder compileErrors = getCompileErrors(pathFileWorkspaceExecute);
            if (compileErrors != null) {
                throw new RunJudgeException(compileErrors.toString());
            }
        } catch (FileNotFoundException ex) {
            logger.error(ex);
            judgeMessage.setKeyStatus(TypeStateJudgeEnum.CE.name());
            judgeMessage.setMessageJudge(ex.getMessage());
        } catch (IOException ex) {
            logger.error(ex);
            judgeMessage.setKeyStatus(TypeStateJudgeEnum.CE.name());
            judgeMessage.setMessageJudge(ex.getMessage());
        } catch (InterruptedException ex) {
            logger.error(ex);
            judgeMessage.setKeyStatus(TypeStateJudgeEnum.CE.name());
            judgeMessage.setMessageJudge(ex.getMessage());
        } catch (RunJudgeException ex) {
            logger.error(ex);
            judgeMessage.setKeyStatus(TypeStateJudgeEnum.CE.name());
            judgeMessage.setMessageJudge(ex.getMessage());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    logger.error(ex);
                }
            }
            logger.debug("ENDING PROCESS JUDGE - compile()");
        }
        return judgeMessage;
    }

    @Override
    public JudgeMessage execute() {
        JudgeMessage judgeMessage = new JudgeMessage();
        judgeMessage.setKeyStatus(null);
        BufferedWriter out = null;
        try {
            logger.debug("START PROCESS JUDGE - execute()");
            final String pathFileWorkspaceExecute = pathFileCodeSource + File.separatorChar + DIR_WORKSPACE_EXECUTE;
            // create the execution script
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(pathFileWorkspaceExecute, NAME_FILE_SHELL_EXECUTE))));
            out.write("cd \"" + pathFileWorkspaceExecute + "\"\n");
            out.write("chroot .\n");
            out.write("java Main < " + fullPathFileInputServer + " > " + NAME_FILE_OUTPUT_EXECUTE);
            out.close();
            Runtime r = Runtime.getRuntime();
            Process p = r.exec("chmod +x " + pathFileWorkspaceExecute + File.separatorChar + NAME_FILE_SHELL_EXECUTE);
            p.waitFor();
            p = r.exec(pathFileWorkspaceExecute + File.separatorChar + NAME_FILE_SHELL_EXECUTE); // execute the script
            final long startTime = Calendar.getInstance().getTimeInMillis();
            TimedShell shell = new TimedShell(this, p, timeout);
            shell.start();
            p.waitFor();
            final long endTime = Calendar.getInstance().getTimeInMillis();
            judgeMessage.setTimeUsed(endTime - startTime);
            if (this.isTimeout) { // TL
                throw new RunJudgeException(i18.get("label.tl." + metadata.getI18()));
            }
            logger.debug("execute complete @ECHO");
        } catch (FileNotFoundException ex) {
            logger.error(ex);
            judgeMessage.setKeyStatus(TypeStateJudgeEnum.RE.name());
            judgeMessage.setMessageJudge(ex.getMessage());
        } catch (IOException ex) {
            logger.error(ex);
            judgeMessage.setKeyStatus(TypeStateJudgeEnum.RE.name());
            judgeMessage.setMessageJudge(ex.getMessage());
        } catch (InterruptedException ex) {
            logger.error(ex);
            judgeMessage.setKeyStatus(TypeStateJudgeEnum.RE.name());
            judgeMessage.setMessageJudge(ex.getMessage());
        } catch (RunJudgeException ex) {
            logger.error(ex);
            judgeMessage.setKeyStatus(TypeStateJudgeEnum.TL.name());
            judgeMessage.setMessageJudge(ex.getMessage());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    logger.error(ex);
                }
            }
            logger.debug("ENDING PROCESS JUDGE - execute()");
        }
        return judgeMessage;
    }

}
