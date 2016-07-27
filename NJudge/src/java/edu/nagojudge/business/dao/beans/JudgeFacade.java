/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.business.dao.beans;

import edu.nagojudge.business.dao.entity.Attachments;
import edu.nagojudge.business.dao.entity.Submit;
import edu.nagojudge.business.logic.java.JudgeServiceJava;
import edu.nagojudge.business.servicios.restful.exceptions.BusinessException;
import edu.nagojudge.msg.pojo.constants.TypeFilesEnum;
import edu.nagojudge.msg.pojo.constants.TypeStateJudgeEnum;
import edu.nagojudge.tools.utils.FormatUtil;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
@Stateless
public class JudgeFacade {

    private final Logger LOGGER = Logger.getLogger(JudgeFacade.class);

    public final String PATH_SAVE_CODE_SOURCE = getPathWorkspaceNJ();
    public final String PATH_SAVE_INPUT_SOURCE = getPathWorkspaceNJInputs();
    public final String PATH_SAVE_OUTPUT_SOURCE = getPathWorkspaceNJOutputs();
    public final String PATH_SAVE_OUTPUT_TEMP = getPathWorkspaceNJOutputsTemp();

    @PersistenceContext(unitName = "NJBusinessPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public void startJudge(String idSubmit, String email) throws BusinessException {
        LOGGER.debug("STARTING JUDGE - startJudge()");
        TypeStateJudgeEnum stateJudge = TypeStateJudgeEnum.CS;
        EntityManager em = null;
        try {
            em = getEntityManager();
            LOGGER.debug("EVALUATE ID_SUBMIT=" + idSubmit + " @ECHO");
            Submit submit = em.find(Submit.class, new Long(idSubmit));
            if (submit == null) {
                throw new BusinessException("ID_PROBLEM [" + idSubmit + "] NO EXISTE.");
            }

            LOGGER.debug("getIdSubmit()=" + submit.getIdSubmit());
            LOGGER.debug("getIdLanguage()=" + submit.getIdLanguage());
            LOGGER.debug("getStatusSubmit()=" + submit.getStatusSubmit());
            LOGGER.debug("getIdProblem()=" + submit.getIdProblem());
            String pathFileCodeSource = PATH_SAVE_CODE_SOURCE + java.io.File.separatorChar + String.valueOf(email)
                    + java.io.File.separatorChar + FormatUtil.getInstance().buildZerosToLeft(submit.getIdProblem().getIdProblem(), 7);
            String nameFileCodeSource = FormatUtil.getInstance().buildZerosToLeft(submit.getIdSubmit(), 7) + "." + submit.getIdLanguage().getExtension();
            String fullPathInputFile = PATH_SAVE_INPUT_SOURCE + java.io.File.separatorChar + FormatUtil.getInstance().buildZerosToLeft(submit.getIdProblem().getIdProblem(), 7);
            String fullPathOutputFile = PATH_SAVE_OUTPUT_SOURCE + java.io.File.separatorChar + FormatUtil.getInstance().buildZerosToLeft(submit.getIdProblem().getIdProblem(), 7);
            LOGGER.debug("pathFileCodeSource=" + pathFileCodeSource);
            LOGGER.debug("nameFileCodeSource=" + nameFileCodeSource);
            LOGGER.debug("fullPathInputFile=" + fullPathInputFile);
            LOGGER.debug("fullPathOutputFile=" + fullPathOutputFile);

            String checkSumOutputFile = findCheckSumOfFile(submit.getIdProblem().getAttachmentsList(), TypeFilesEnum.TYPE_FILE_OUT.getExtension());

            if (submit.getIdLanguage().getExtension().toLowerCase().compareTo(JudgeServiceJava.EXTENSION_JAVA) == 0) {
                try {
                    JudgeServiceJava judgeServiceJava = new JudgeServiceJava();
                    stateJudge = judgeServiceJava.judgeProblemProcess(pathFileCodeSource, nameFileCodeSource, fullPathInputFile, checkSumOutputFile);
                } catch (IOException ex) {
                    LOGGER.error(ex);
                    stateJudge = TypeStateJudgeEnum.RE;
                } catch (NoSuchAlgorithmException ex) {
                    LOGGER.error(ex);
                    stateJudge = TypeStateJudgeEnum.RE;
                } catch (InterruptedException ex) {
                    LOGGER.error(ex);
                    stateJudge = TypeStateJudgeEnum.RE;
                }
            }

            LOGGER.debug("JUDGE_FINALLY=" + stateJudge.getValue());
            submit.setDateJudge(Calendar.getInstance().getTime());
            submit.setStatusSubmit(stateJudge.getValue());
            em.merge(submit);

        } catch (BusinessException ex) {
            LOGGER.error(ex);
            throw ex;
        } finally {
            LOGGER.debug("ENDING JUDGE - startJudge()");
        }
    }

    private String getPathWorkspaceNJ() {
        return System.getProperty("user.dir") + File.separatorChar + "workspace" + File.separatorChar + "users";
    }

    private String getPathWorkspaceNJInputs() {
        return System.getProperty("user.dir") + File.separatorChar + "workspace" + File.separatorChar + "inputs";
    }

    private String getPathWorkspaceNJOutputs() {
        return System.getProperty("user.dir") + File.separatorChar + "workspace" + File.separatorChar + "outputs";
    }

    private String getPathWorkspaceNJOutputsTemp() {
        return System.getProperty("user.dir") + File.separatorChar + "workspace" + File.separatorChar + "temp_outputs";
    }

    private String findCheckSumOfFile(List<Attachments> attachmentsList, String TYPE_FILE) {
        for (Attachments attachment : attachmentsList) {
            if (attachment.getTypeFileServer().compareTo(TYPE_FILE) == 0) {
                return attachment.getChecksum();
            }
        }
        LOGGER.error("NO EXISTE NINGUN TYPE_FILE=" + TYPE_FILE);
        return null;
    }

}
