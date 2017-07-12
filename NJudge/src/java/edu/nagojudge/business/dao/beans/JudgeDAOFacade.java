/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.business.dao.beans;

import edu.nagojudge.business.codejuge.language.AbstractLanguage;
import edu.nagojudge.business.codejuge.language.C;
import edu.nagojudge.business.codejuge.language.Cpp;
import edu.nagojudge.business.codejuge.language.Java;
import edu.nagojudge.business.dao.entity.Attachments;
import edu.nagojudge.business.dao.entity.Submit;
import edu.nagojudge.business.dao.entity.SubmitStatus;
import edu.nagojudge.business.logic.exe.JudgeServiceJava;
import edu.nagojudge.business.servicios.restful.exceptions.BusinessException;
import edu.nagojudge.business.servicios.restful.exceptions.RunJudgeException;
import edu.nagojudge.msg.pojo.JudgeMessage;
import edu.nagojudge.msg.pojo.LanguageProgrammingMessage;
import edu.nagojudge.msg.pojo.MetadataMessage;
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
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import org.apache.log4j.Logger;

/**
 * @serial
 * https://docs.oracle.com/javase/tutorial/essential/environment/cmdLineArgs.html
 * @author andresfelipegarciaduran
 */
@Stateless
public class JudgeDAOFacade {

    private final Logger logger = Logger.getLogger(JudgeDAOFacade.class);

    @Context
    private ServletContext context;

    @Context
    private HttpServletRequest servletRequest;

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
            final String pathFileCodeSource = getPathFileCodeSourceFromUser(email, submit.getIdProblem().getIdProblem());
            final String nameFileCodeSource = getNameFileSubmit(submit.getIdSubmit(), submit.getIdLanguage().getExtension());
            final String fullPathInputFile = getFullPathInputFile(submit.getIdProblem().getIdProblem());
            final String checkSumOutputFile = findCheckSumOfFile(submit.getIdProblem().getAttachmentsList(), TypeFilesEnum.TYPE_FILE_OUT.getExtension());
            logger.debug("checkSumOutputFile [" + checkSumOutputFile + "]");
            logger.debug("pathFileCodeSource [" + pathFileCodeSource + "]");
            logger.debug("nameFileCodeSource [" + nameFileCodeSource + "]");
            logger.debug("fullPathInputFile [" + fullPathInputFile + "]");

            TypeLanguageEnum typeLanguageEnum = TypeLanguageEnum.valueOf(submit.getIdLanguage().getNameLanguage().toUpperCase());
            switch (typeLanguageEnum) {
                case JAVA: {

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
     * @param metadata
     * @return
     * @throws BusinessException
     */
    public SubmitMessage startJudgeByTeam(Long idChallenge, Long idSubmit, Long idTeam, MetadataMessage metadata) throws BusinessException {
        logger.debug("STARTING JUDGE - startJudgeByTeam()");
        JudgeMessage judgetState = new JudgeMessage();
        try {
            em = getEntityManager();
            logger.debug("EVALUATE ID_SUBMIT=" + idSubmit + " @ECHO");
            Submit submit = em.find(Submit.class, idSubmit);
            if (submit == null) {
                throw new BusinessException("idSubmit [" + idSubmit + "] NO EXISTE.");
            }
            logger.debug("getIdSubmit()=" + submit.getIdSubmit());
            logger.debug("getIdLanguage()=" + submit.getIdLanguage());
            logger.debug("getExtension [" + ((submit.getIdLanguage() != null) ? submit.getIdLanguage().getExtension().toUpperCase() : "none") + "]");
            logger.debug("getIdStatus()=" + submit.getIdStatus().getNameStatus());
            logger.debug("getIdProblem()=" + submit.getIdProblem());
            logger.debug("getTimeLimit [" + submit.getIdProblem().getTimeLimit() + "]");
            logger.debug("getMemoLimit [" + submit.getIdProblem().getMemoLimit() + "]");
            final String pathFileCodeSource = getPathFileCodeSourceFromTeam(idChallenge, idTeam, submit.getIdProblem().getIdProblem());
            final String nameFileCodeSource = getNameFileSubmit(submit.getIdSubmit(), submit.getIdLanguage().getExtension());
            final String fullPathFileInputServer = getFullPathInputFile(submit.getIdProblem().getIdProblem());
            final String fullPathFileOutputServer = getFullPathOutputFile(submit.getIdProblem().getIdProblem());
            final String checkSumFileOutputServer = findCheckSumOfFile(submit.getIdProblem().getAttachmentsList(), TypeFilesEnum.TYPE_FILE_OUT.getExtension());
            logger.debug("checkSumFileOutputServer [" + checkSumFileOutputServer + "]");
            logger.debug("pathFileCodeSource [" + pathFileCodeSource + "]");
            logger.debug("nameFileCodeSource [" + nameFileCodeSource + "]");
            logger.debug("fullPathFileInputServer [" + fullPathFileInputServer + "]");

            TypeLanguageEnum typeLanguageEnum = TypeLanguageEnum.valueOf(submit.getIdLanguage().getExtension().toUpperCase());
            AbstractLanguage language = null;
            switch (typeLanguageEnum) {
                case JAVA: {
                    language = new Java(nameFileCodeSource,
                            pathFileCodeSource,
                            fullPathFileInputServer,
                            (submit.getIdProblem() != null ? submit.getIdProblem().getTimeLimit() * 1000 : 0),
                            metadata);
                }
                break;
                case C: {
                    language = new C(nameFileCodeSource,
                            pathFileCodeSource,
                            fullPathFileInputServer,
                            (submit.getIdProblem() != null ? submit.getIdProblem().getTimeLimit() * 1000 : 0),
                            metadata);
                }
                break;
                case CPP: {
                    language = new Cpp(nameFileCodeSource,
                            pathFileCodeSource,
                            fullPathFileInputServer,
                            (submit.getIdProblem() != null ? submit.getIdProblem().getTimeLimit() * 1000 : 0),
                            metadata);
                }
                break;
                case PYTHON:
                    break;
            }
            if (language != null) {
                logger.debug("Language selected @ECHO");
                judgetState = language.compile();
                if (judgetState.getKeyStatus() == null) {
                    judgetState = language.execute();
                    logger.debug("getTimeUsed() [" + judgetState.getTimeUsed() + "]");
                    submit.setTimeUsed(judgetState.getTimeUsed() != null ? BigInteger.valueOf(judgetState.getTimeUsed()) : BigInteger.ZERO);
                    if (judgetState.getKeyStatus() == null) {
                        judgetState = language.analyze(pathFileCodeSource, fullPathFileOutputServer, checkSumFileOutputServer);
                    }
                }
            }
            judgetState.setKeyStatus(judgetState.getKeyStatus() != null ? judgetState.getKeyStatus() : TypeStateJudgeEnum.CS.name());
            submit.setDateJudge(Calendar.getInstance().getTime());
            submit.setMsgJudge(judgetState.getMessageJudge());
            submit.setIdStatus(em.createQuery("SELECT ss FROM SubmitStatus ss WHERE ss.keyStatus = :id_status ", SubmitStatus.class)
                    .setParameter("id_status", judgetState.getKeyStatus())
                    .getSingleResult());
            em.merge(submit);
            logger.debug("JUDGE_FINALLY [" + submit.getIdStatus().getNameStatus() + "]");
            return parseSubmitEntityToMessage(submit, judgetState);
        } catch (BusinessException ex) {
            logger.error(ex);
            throw ex;
        } finally {
            logger.debug("ENDING JUDGE - startJudgeByTeam()");
        }
    }

    public String getNameFileSubmit(Long idSubmit) {
        Submit submit = em.find(Submit.class, idSubmit);
        return FormatUtil.getInstance().buildZerosToLeft(submit.getIdSubmit(), 7) + "." + submit.getIdLanguage().getExtension();
    }

    public String getNameFileSubmit(Long idSubmit, String extension) {
        return FormatUtil.getInstance().buildZerosToLeft(idSubmit, 7) + "." + extension;
    }

    public String getPathFileCodeSourceFromUser(String email, Long idProblem) {
        String root = System.getProperty("user.dir") + File.separatorChar + "workspace-nagojudge" + File.separatorChar + "users";
        root += File.separatorChar + String.valueOf(email);
        root += File.separatorChar + FormatUtil.getInstance().buildZerosToLeft(idProblem, 7);
        return root;
    }

    public String getPathFileCodeSourceFromTeam(Long idChallenge, Long idTeam, Long idProblem) {
        String root = System.getProperty("user.dir") + File.separatorChar + "workspace-nagojudge" + File.separatorChar + "teams";
        root += File.separatorChar + FormatUtil.getInstance().buildZerosToLeft(idChallenge, 7);
        root += File.separatorChar + FormatUtil.getInstance().buildZerosToLeft(idTeam, 7);
        root += File.separatorChar + FormatUtil.getInstance().buildZerosToLeft(idProblem, 7);
        return root;
    }

    public String getFullPathInputFile(Long idProblem) {
        String root = System.getProperty("user.dir") + File.separatorChar + "workspace-nagojudge" + File.separatorChar + "content" + File.separatorChar + "inputs";
        root += File.separatorChar + FormatUtil.getInstance().buildZerosToLeft(idProblem, 7);
        return root;
    }

    public String getFullPathOutputFile(Long idProblem) {
        String root = System.getProperty("user.dir") + File.separatorChar + "workspace-nagojudge" + File.separatorChar + "content" + File.separatorChar + "outputs";
        root += File.separatorChar + FormatUtil.getInstance().buildZerosToLeft(idProblem, 7);
        return root;
    }

    public String getFullPathProblemFile(Long idProblem) {
        String root = System.getProperty("user.dir") + File.separatorChar + "workspace-nagojudge" + File.separatorChar + "problems";
        root += File.separatorChar + FormatUtil.getInstance().buildZerosToLeft(idProblem, 7) + ".pdf";
        return root;
    }

    public String getFullPathProblemFileFromWEB(Long idProblem) {
        String root = "http://" + servletRequest.getServerName() + ":" + servletRequest.getServerPort() + context.getContextPath()
                + File.separatorChar + "external" + File.separatorChar
                + "problems" + File.separatorChar + FormatUtil.getInstance().buildZerosToLeft(idProblem, 7) + ".pdf";
        return root;
    }

    public String getFullPathProblemFileToWrite(Long idProblem) {
        String root = context.getRealPath("/") + "external" + File.separatorChar
                + "problems" + File.separatorChar + FormatUtil.getInstance().buildZerosToLeft(idProblem, 7) + ".pdf";
        return root;
    }

    private String findCheckSumOfFile(List<Attachments> attachmentsList, String TYPE_FILE) {
        for (Attachments attachment : attachmentsList) {
            if (attachment.getTypeFileServer().compareTo(TYPE_FILE) == 0) {
                return attachment.getChecksum();
            }
        }
        logger.error("NO EXISTE NINGUN TYPE_FILE [" + TYPE_FILE + "]");
        return null;
    }

    private SubmitMessage parseSubmitEntityToMessage(Submit submit, JudgeMessage judgetState) {
        SubmitMessage submitMessage = new SubmitMessage();
        submitMessage.setIdSubmit(submit.getIdSubmit());
        submitMessage.setDateJudge(submit.getDateJudge() == null ? 0 : submit.getDateJudge().getTime());
        submitMessage.setDateSubmit(submit.getDateSubmit() == null ? 0 : submit.getDateSubmit().getTime());

        ProblemMessage problemMessage = new ProblemMessage();
        problemMessage.setIdProblem(submit.getIdProblem().getIdProblem());
        problemMessage.setNameProblem(submit.getIdProblem().getNameProblem());
        problemMessage.setTimeLimit(submit.getIdProblem().getTimeLimit());
        problemMessage.setMemoLimit(submit.getIdProblem().getMemoLimit());
        submitMessage.setProblemMessage(problemMessage);

        LanguageProgrammingMessage languageProgrammingMessage = new LanguageProgrammingMessage();
        languageProgrammingMessage.setNameProgramming(submit.getIdLanguage().getNameLanguage());
        languageProgrammingMessage.setExtension(submit.getIdLanguage().getExtension());
        submitMessage.setLanguageProgrammingMessage(languageProgrammingMessage);

        judgetState.setStatusName(submit.getIdStatus().getNameStatus());
        judgetState.setDescriptionStatus(submit.getIdStatus().getDescription());
        judgetState.setTimeUsed(submit.getTimeUsed() != null ? submit.getTimeUsed().longValue() : 0);
        //judgetState.setMemoUsed(submit.getMemoUsed() != null ? submit.getMemoUsed().longValue() : 0);
        submitMessage.setJudgeMessage(judgetState);

        return submitMessage;
    }

}
