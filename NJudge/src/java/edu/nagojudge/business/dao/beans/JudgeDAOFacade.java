/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.business.dao.beans;

import edu.nagojudge.business.dao.entity.Attachments;
import edu.nagojudge.business.dao.entity.Submit;
import edu.nagojudge.business.dao.entity.SubmitStatus;
import edu.nagojudge.business.logic.java.JudgeServiceJava;
import edu.nagojudge.business.servicios.restful.exceptions.BusinessException;
import edu.nagojudge.business.servicios.restful.exceptions.RunJudgeException;
import edu.nagojudge.msg.pojo.JudgeMessage;
import edu.nagojudge.msg.pojo.LanguageProgrammingMessage;
import edu.nagojudge.msg.pojo.ProblemMessage;
import edu.nagojudge.msg.pojo.SubmitMessage;
import edu.nagojudge.msg.pojo.constants.TypeFilesEnum;
import edu.nagojudge.msg.pojo.constants.TypeLanguageEnum;
import edu.nagojudge.msg.pojo.constants.TypeStateJudgeEnum;
import edu.nagojudge.tools.utils.FormatUtil;
import java.io.File;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.log4j.Logger;

/**
 * @serial
 * https://docs.oracle.com/javase/tutorial/essential/environment/cmdLineArgs.html
 * @author andresfelipegarciaduran
 */
@Stateless
public class JudgeDAOFacade {

    private final Logger logger = Logger.getLogger(JudgeDAOFacade.class);

    public final String PATH_SAVE_CODE_SOURCE_USER = getPathWorkspaceNJUsers();
    public final String PATH_SAVE_CODE_SOURCE_TEAM = getPathWorkspaceNJTeams();

    public final String PATH_SAVE_INPUT_SOURCE = getPathWorkspaceNJInputs();
    public final String PATH_SAVE_OUTPUT_SOURCE = getPathWorkspaceNJOutputs();
    public final String PATH_SAVE_OUTPUT_TEMP = getPathWorkspaceNJOutputsTemp();

    @PersistenceContext(unitName = "NJBusinessPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public SubmitMessage startJudgeByUser(Long idSubmit, String email) throws BusinessException {
        logger.debug("STARTING JUDGE - startJudgeByUser()");
        JudgeMessage judgetState = new JudgeMessage();
        judgetState.setMessageJudge(TypeStateJudgeEnum.CE.name());
        try {
            em = getEntityManager();
            logger.debug("EVALUATE idSubmit [" + idSubmit + "] @ECHO");
            Submit submit = em.find(Submit.class, idSubmit);
            if (submit == null) {
                throw new BusinessException("ID_SUBMIT [" + idSubmit + "] NO EXISTE.");
            }

            logger.debug("getIdSubmit() [" + submit.getIdSubmit() + "]");
            logger.debug("getIdLanguage() [" + submit.getIdLanguage() + "]");
            logger.debug("getIdStatus() [" + submit.getIdStatus().getNameStatus() + "]");
            logger.debug("getIdProblem() [" + submit.getIdProblem() + "]");
            String pathFileCodeSource = PATH_SAVE_CODE_SOURCE_USER
                    + java.io.File.separatorChar + String.valueOf(email)
                    + java.io.File.separatorChar + FormatUtil.getInstance().buildZerosToLeft(submit.getIdProblem().getIdProblem(), 7);
            final String nameFileCodeSource = FormatUtil.getInstance().buildZerosToLeft(submit.getIdSubmit(), 7) + "." + submit.getIdLanguage().getExtension();
            final String fullPathInputFile = PATH_SAVE_INPUT_SOURCE + java.io.File.separatorChar + FormatUtil.getInstance().buildZerosToLeft(submit.getIdProblem().getIdProblem(), 7);

            final String checkSumOutputFile = findCheckSumOfFile(submit.getIdProblem().getAttachmentsList(), TypeFilesEnum.TYPE_FILE_OUT.getExtension());
            logger.debug("checkSumOutputFile [" + checkSumOutputFile + "]");
            logger.debug("pathFileCodeSource [" + pathFileCodeSource + "]");
            logger.debug("nameFileCodeSource [" + nameFileCodeSource + "]");
            logger.debug("fullPathInputFile [" + fullPathInputFile + "]");

            TypeLanguageEnum typeLanguageEnum = TypeLanguageEnum.valueOf(submit.getIdLanguage().getNameLanguage().toUpperCase());
            switch (typeLanguageEnum) {
                case JAVA: {
                    JudgeServiceJava judgeServiceJava = new JudgeServiceJava();
                    try {
                        judgetState = judgeServiceJava.executeJudgment(pathFileCodeSource,
                                nameFileCodeSource,
                                fullPathInputFile,
                                checkSumOutputFile,
                                submit.getTimeUsed().longValue());
                    } catch (RunJudgeException ex) {
                        logger.error(ex);
                        judgetState.setStatusName(TypeStateJudgeEnum.RE.name());
                        judgetState.setResumeStatus(ex.getLocalizedMessage());
                    }
                }
                break;
                case C:
                    break;
                case PYTHON:
                    break;
            }

            logger.debug("JUDGE_FINALLY [" + judgetState.getStatusName() + "]");
            submit.setDateJudge(Calendar.getInstance().getTime());
            submit.setTimeUsed(BigInteger.valueOf(judgetState.getTimeUsed()));
            submit.setIdStatus(em.createQuery("SELECT ss FROM SubmitStatus ss WHERE ss.keyStatus = :id_status ", SubmitStatus.class)
                    .setParameter("id_status", judgetState.getStatusName())
                    .getSingleResult());
            em.merge(submit);
            return parseSubmitEntityToMessage(submit, judgetState);
        } catch (BusinessException ex) {
            logger.error(ex);
            throw ex;
        } finally {
            logger.debug("ENDING JUDGE - startJudgeByUser()");
        }
    }

    /**
     * It is a good idea to specify the CLASSPATH on the command line with the
     * javac command, especially when writing shell scripts or make files. You
     * must determine the location at which this Java code will run. If you run
     * Hello.class on your client system, then it searches the CLASSPATH for all
     * the supporting core classes that Hello.class needs for running. This
     * search should result in locating the dependent classes in one of the
     * following:
     *
     * @param idChallenge
     * @param idSubmit
     * @param idTeam
     * @return
     * @throws BusinessException
     */
    public SubmitMessage startJudgeByTeam(Long idChallenge, Long idSubmit, Long idTeam) throws BusinessException {
        logger.debug("STARTING JUDGE - startJudgeByTeam()");
        JudgeMessage judgetState = new JudgeMessage();
        judgetState.setMessageJudge(TypeStateJudgeEnum.CE.name());
        try {
            em = getEntityManager();
            logger.debug("EVALUATE ID_SUBMIT=" + idSubmit + " @ECHO");
            Submit submit = em.find(Submit.class, idSubmit);
            if (submit == null) {
                throw new BusinessException("idSubmit [" + idSubmit + "] NO EXISTE.");
            }
            logger.debug("getIdSubmit()=" + submit.getIdSubmit());
            logger.debug("getIdLanguage()=" + submit.getIdLanguage());
            logger.debug("getIdStatus()=" + submit.getIdStatus().getNameStatus());
            logger.debug("getIdProblem()=" + submit.getIdProblem());
            final String pathFileCodeSource = PATH_SAVE_CODE_SOURCE_TEAM + java.io.File.separatorChar
                    + FormatUtil.getInstance().buildZerosToLeft(idChallenge, 7) + java.io.File.separatorChar
                    + FormatUtil.getInstance().buildZerosToLeft(idTeam, 7) + java.io.File.separatorChar
                    + FormatUtil.getInstance().buildZerosToLeft(submit.getIdProblem().getIdProblem(), 7);
            final String nameFileCodeSource = FormatUtil.getInstance().buildZerosToLeft(submit.getIdSubmit(), 7) + "." + submit.getIdLanguage().getExtension();
            final String fullPathInputFile = PATH_SAVE_INPUT_SOURCE + java.io.File.separatorChar + FormatUtil.getInstance().buildZerosToLeft(submit.getIdProblem().getIdProblem(), 7);

            final String checkSumOutputFile = findCheckSumOfFile(submit.getIdProblem().getAttachmentsList(), TypeFilesEnum.TYPE_FILE_OUT.getExtension());
            logger.debug("checkSumOutputFile [" + checkSumOutputFile + "]");
            logger.debug("pathFileCodeSource [" + pathFileCodeSource + "]");
            logger.debug("nameFileCodeSource [" + nameFileCodeSource + "]");
            logger.debug("fullPathInputFile [" + fullPathInputFile + "]");

            TypeLanguageEnum typeLanguageEnum = TypeLanguageEnum.valueOf(submit.getIdLanguage().getNameLanguage().toUpperCase());
            switch (typeLanguageEnum) {
                case JAVA: {
                    JudgeServiceJava judgeServiceJava = new JudgeServiceJava();
                    try {
                        judgetState = judgeServiceJava.executeJudgment(pathFileCodeSource,
                                nameFileCodeSource,
                                fullPathInputFile,
                                checkSumOutputFile,
                                submit.getTimeUsed().longValue());
                    } catch (RunJudgeException ex) {
                        logger.error(ex);
                        judgetState.setStatusName(TypeStateJudgeEnum.RE.name());
                        judgetState.setResumeStatus(ex.getLocalizedMessage());
                    }
                }
                break;
                case C:
                    break;
                case PYTHON:
                    break;
            }

            logger.debug("JUDGE_FINALLY [" + judgetState.getStatusName() + "]");
            submit.setDateJudge(Calendar.getInstance().getTime());
            submit.setTimeUsed(BigInteger.valueOf(judgetState.getTimeUsed()));
            submit.setIdStatus(em.createQuery("SELECT ss FROM SubmitStatus ss WHERE ss.keyStatus = :id_status ", SubmitStatus.class)
                    .setParameter("id_status", judgetState.getStatusName())
                    .getSingleResult());
            em.merge(submit);
            return parseSubmitEntityToMessage(submit, judgetState);
        } catch (BusinessException ex) {
            logger.error(ex);
            throw ex;
        } finally {
            logger.debug("ENDING JUDGE - startJudgeByTeam()");
        }
    }

    private String getPathWorkspaceNJUsers() {
        return System.getProperty("user.dir") + File.separatorChar + "workspace-nagojudge" + File.separatorChar + "users";
    }

    private String getPathWorkspaceNJTeams() {
        return System.getProperty("user.dir") + File.separatorChar + "workspace-nagojudge" + File.separatorChar + "teams";
    }

    private String getPathWorkspaceNJInputs() {
        return System.getProperty("user.dir") + File.separatorChar + "workspace-nagojudge" + File.separatorChar + "content" + File.separatorChar + "inputs";
    }

    private String getPathWorkspaceNJOutputs() {
        return System.getProperty("user.dir") + File.separatorChar + "workspace-nagojudge" + File.separatorChar + "content" + File.separatorChar + "outputs";
    }

    private String getPathWorkspaceNJOutputsTemp() {

        return System.getProperty("user.dir") + File.separatorChar + "workspace-nagojudge" + File.separatorChar + "content" + File.separatorChar + "temp-outputs";
    }

    private String findCheckSumOfFile(List<Attachments> attachmentsList, String TYPE_FILE) {
        for (Attachments attachment : attachmentsList) {
            if (attachment.getTypeFileServer().compareTo(TYPE_FILE) == 0) {
                return attachment.getChecksum();
            }
        }
        logger.error("NO EXISTE NINGUN TYPE_FILE=" + TYPE_FILE);
        return null;
    }

    private SubmitMessage parseSubmitEntityToMessage(Submit submit, JudgeMessage judgetState) {
        SubmitMessage submitMessage = new SubmitMessage();
        submitMessage.setIdSubmit(submit.getIdSubmit());
        submitMessage.setDateJudge(submit.getDateJudge() == null ? 0 : submit.getDateJudge().getTime());
        submitMessage.setDateSubmit(submit.getDateSubmit() == null ? 0 : submit.getDateSubmit().getTime());
        submitMessage.setTimeUsed(judgetState.getTimeUsed());
        submitMessage.setMemoUsed(judgetState.getMemoUsed());

        ProblemMessage problemMessage = new ProblemMessage();
        problemMessage.setIdProblem(submit.getIdProblem().getIdProblem());
        problemMessage.setNameProblem(submit.getIdProblem().getNameProblem());
        problemMessage.setTimeLimit(submit.getIdProblem().getTimeLimit());
        problemMessage.setMemoLimit(submit.getIdProblem().getMemoLimit());
        submitMessage.setProblemMessage(problemMessage);

        LanguageProgrammingMessage languageProgrammingMessage = new LanguageProgrammingMessage();
        languageProgrammingMessage.setNameProgramming(submit.getIdLanguage().getNameLanguage());
        submitMessage.setLanguageProgrammingMessage(languageProgrammingMessage);

        submitMessage.setJudgeMessage(judgetState);

        return submitMessage;
    }

}
