/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.app.business.dao.beans;

import edu.nagojudge.app.business.dao.entities.Account;
import edu.nagojudge.app.business.dao.entities.AccountSubmit;
import edu.nagojudge.app.business.dao.entities.LanguageProgramming;
import edu.nagojudge.app.business.dao.entities.Problem;
import edu.nagojudge.app.business.dao.entities.Submit;
import edu.nagojudge.app.business.dao.entities.SubmitStatus;
import edu.nagojudge.app.utils.FacesUtil;
import edu.nagojudge.app.utils.constants.IKeysApplication;
import edu.nagojudge.app.utils.constants.IResourcesPaths;
import edu.nagojudge.msg.pojo.AccountMessage;
import edu.nagojudge.msg.pojo.JudgeMessage;
import edu.nagojudge.msg.pojo.LanguageProgrammingMessage;
import edu.nagojudge.msg.pojo.ProblemMessage;
import edu.nagojudge.msg.pojo.SubmitMessage;
import edu.nagojudge.msg.pojo.constants.TypeFilesEnum;
import edu.nagojudge.msg.pojo.constants.TypeStateFileEnum;
import edu.nagojudge.msg.pojo.constants.TypeStateJudgeEnum;
import edu.nagojudge.tools.utils.FileUtil;
import edu.nagojudge.tools.utils.FormatUtil;
import edu.nagojudge.web.utils.resources.clients.ClientService;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
@Stateless
public class SubmitFacade extends AbstractFacade<Submit> {

    @EJB
    private AccountSubmitFacade accountSubmitFacade;

    private final String TOKEN = "asd";

    private final Logger logger = Logger.getLogger(SubmitFacade.class);

    @PersistenceContext(unitName = "NJWebPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SubmitFacade() {
        super(Submit.class);
    }

    public List<SubmitMessage> findLast100Results() {
        List<SubmitMessage> outcome = new ArrayList<SubmitMessage>();
        final int TOP = 100;
        List<AccountSubmit> accountSubmits = em.createQuery("SELECT s FROM AccountSubmit s ORDER BY s.idSubmit.dateSubmit DESC", AccountSubmit.class)
                .setMaxResults(TOP)
                .getResultList();
        if (accountSubmits != null && !accountSubmits.isEmpty()) {
            for (AccountSubmit accountSubmit : accountSubmits) {
                SubmitMessage submitMessage = parseAccountSubmitEntityToMessage(accountSubmit);
                outcome.add(submitMessage);
            }
        }
        return outcome;
    }

    public List<SubmitMessage> findSubmitEntitiesByAccount(long idAccount) {
        List<SubmitMessage> outcome = new ArrayList<SubmitMessage>();
        List<AccountSubmit> accountSubmits = em.createQuery("SELECT ass FROM AccountSubmit ass WHERE ass.idAccount.idAccount = :id_account ORDER BY ass.idSubmit.dateSubmit DESC", AccountSubmit.class)
                .setParameter("id_account", idAccount)
                .getResultList();
        if (accountSubmits != null && !accountSubmits.isEmpty()) {
            for (AccountSubmit accountSubmit : accountSubmits) {
                SubmitMessage submitMessage = parseAccountSubmitEntityToMessage(accountSubmit);
                outcome.add(submitMessage);
            }
        }
        return outcome;
    }

    public String findAttachmentSubmit(String idSubmit) {
        try {
            logger.debug("INICIA METODO - findAttachmentSubmit()");
            String path = "submit/codeSource/{idSubmit}";
            Object objects[] = {idSubmit};
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("token", TOKEN);
            Object callRestfulGet = ClientService.getInstance().callRestfulGet(path, objects, params, String.class);
            logger.debug("outcome [" + callRestfulGet + "]");
            return String.valueOf(callRestfulGet);
        } finally {
            logger.debug("FINALIZA METODO - findAttachmentSubmit()");
        }
    }

    public String getFullPathProblem(Long idProblem, String type) throws IOException {
        String fullPath = "None";
        try {
            logger.debug("INICIA METODO - getFullPathProblem()");
            String nameFile = FormatUtil.getInstance().buildZerosToLeft(idProblem, 7) + TypeFilesEnum.PDF.getExtension();
            if (type.compareTo(TypeFilesEnum.TYPE_FILE_PROBLEM.getExtension()) == 0) {

                String tempFullPath = IResourcesPaths.PATH_ROOT_SAVE_PROBLEMS_WEB + java.io.File.separatorChar + nameFile;
                boolean existFile = FileUtil.getInstance().existFile(tempFullPath);
                if (!existFile) {
                    logger.debug("FILE NOT EXIST FULL_PATH=[" + tempFullPath + "]");
                    String fullPathLocal = IResourcesPaths.PATH_SAVE_PROBLEMS_LOCAL + java.io.File.separatorChar + nameFile;
                    FileUtil.getInstance().copyFile(fullPathLocal, tempFullPath);
                }
                //fullPath = IResourcesPaths.PATH_VIEW_PROBLEMS + java.io.File.separatorChar + nameFile;
            }
            logger.debug("PATH_VIEW_FILE_PUBLIC=" + fullPath);
            return fullPath;
        } finally {
            logger.debug("FINALIZA METODO - getFullPathProblem()");
        }
    }

    public void editSubmitMessage(SubmitMessage submitMessage) {
        try {
            logger.debug("INICIA METODO - editSubmitMessage()");
            StringBuilder sql = new StringBuilder();
            sql.append("select p from AccountSubmit p where p.idSubmit = :id_submit and p.idAccount = :id_account");
            Query query = em.createQuery(sql.toString(), AccountSubmit.class)
                    .setParameter("id_submit", submitMessage.getProblemMessage().getIdProblem())
                    .setParameter("id_account", submitMessage.getAccountMessage().getIdAccount());
            AccountSubmit accountSubmit = (AccountSubmit) query.getSingleResult();
            accountSubmit.setVisibleWeb((TypeStateFileEnum.PRIVATE.getType().compareTo(accountSubmit.getVisibleWeb()) == 0) ? TypeStateFileEnum.PUBLIC.getType() : TypeStateFileEnum.PRIVATE.getType());
            accountSubmitFacade.edit(accountSubmit);
            logger.debug("editSubmit @ECHO");
        } catch (Exception ex) {
            logger.error(ex);
        } finally {
            logger.debug("FINALIZA METODO - editSubmitMessage()");
        }
    }

    private SubmitMessage parseAccountSubmitEntityToMessage(AccountSubmit accountSubmit) {

        SubmitMessage submitMessage = new SubmitMessage();

        Submit submit = accountSubmit.getIdSubmit();

        submitMessage.setIdSubmit(submit.getIdSubmit());
        submitMessage.setDateJudge(submit.getDateJudge() == null ? 0 : submit.getDateJudge().getTime());
        submitMessage.setDateSubmit(submit.getDateSubmit() == null ? 0 : submit.getDateSubmit().getTime());
        submitMessage.setVisibleWeb(accountSubmit.getVisibleWeb());

        ProblemMessage problemMessage = new ProblemMessage();
        problemMessage.setNameProblem(submit.getIdProblem().getNameProblem());
        problemMessage.setIdProblem(submit.getIdProblem().getIdProblem());
        submitMessage.setProblemMessage(problemMessage);

        JudgeMessage judgeMessage = new JudgeMessage();
        judgeMessage.setIdStatusName(submit.getIdStatus().getIdStatus().longValue());
        judgeMessage.setKeyStatus(submit.getIdStatus().getKeyStatus());
        judgeMessage.setStatusName(submit.getIdStatus().getNameStatus());
        judgeMessage.setDescriptionStatus(submit.getIdStatus().getDescription());
        judgeMessage.setTimeUsed(submit.getTimeUsed() == null ? 0 : submit.getTimeUsed().longValue());
        judgeMessage.setMemoUsed(submit.getMemoUsed() == null ? 0 : submit.getMemoUsed().longValue());
        judgeMessage.setMessageJudge(submit.getMsgJudge());
        submitMessage.setJudgeMessage(judgeMessage);

        LanguageProgrammingMessage languageProgrammingMessage = new LanguageProgrammingMessage();
        languageProgrammingMessage.setNameProgramming(submit.getIdLanguage().getNameLanguage());
        submitMessage.setLanguageProgrammingMessage(languageProgrammingMessage);

        AccountMessage accountMessage = new AccountMessage();
        accountMessage.setIdAccount(accountSubmit.getIdAccount().getIdAccount());
        accountMessage.setNickname(accountSubmit.getIdAccount().getNickname());
        submitMessage.setAccountMessage(accountMessage);

        return submitMessage;
    }

    public SubmitMessage createSubmitSolve(SubmitMessage submitMessage, ProblemMessage problemMessage, LanguageProgramming languageProgrammingView, byte[] contentCodeSource)
            throws IOException, Exception {
        try {
            logger.debug("INICIA METODO - createSubmitSolve()");
            HttpSession session = FacesUtil.getFacesUtil().getCurrentSession();
            Object accountObject = session.getAttribute(IKeysApplication.KEY_DATA_USER_ACCOUNT);
            final Object email = session.getAttribute(IKeysApplication.KEY_DATA_USER_EMAIL);

            Submit submit = new Submit();
            submit.setIdStatus(em.createQuery("SELECT a FROM SubmitStatus a WHERE a.keyStatus = :id_status ", SubmitStatus.class)
                    .setParameter("id_status", TypeStateJudgeEnum.IP.name())
                    .getSingleResult());
            submit.setMemoUsed(BigInteger.ZERO);
            submit.setTimeUsed(BigInteger.ZERO);
            submit.setIdProblem(em.find(Problem.class, problemMessage.getIdProblem()));
            submit.setIdLanguage(languageProgrammingView);
            submit.setDateSubmit(Calendar.getInstance().getTime());
            create(submit);

            AccountSubmit accountSubmit = new AccountSubmit();
            accountSubmit.setIdAccount((Account) accountObject);
            accountSubmit.setIdSubmit(submit);
            accountSubmit.setVisibleWeb(TypeStateFileEnum.PRIVATE.getType());
            accountSubmitFacade.create(accountSubmit);

            String pathFile = IResourcesPaths.PATH_SAVE_CODE_SOURCE_LOCAL + java.io.File.separatorChar
                    + String.valueOf(email) + java.io.File.separatorChar + FormatUtil.getInstance().buildZerosToLeft(submit.getIdProblem().getIdProblem(), 7);
            String nameFile = FormatUtil.getInstance().buildZerosToLeft(submit.getIdSubmit(), 7) + "." + languageProgrammingView.getExtension();
            FileUtil.getInstance().createFile(contentCodeSource, pathFile, nameFile);

            final Long idSubmit = submit.getIdSubmit();

            final Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    String path = "submit/verdict/user/{email}/{idSubmit}";
                    Object objects[] = {String.valueOf(email), String.valueOf(idSubmit)};
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("token", TOKEN);
                    SubmitMessage submitMessage = (SubmitMessage) ClientService.getInstance().callRestfulGet(path, objects, params, SubmitMessage.class);
                    logger.debug("outcome [" + submitMessage + "]");
                }
            });
            thread.start();

            return parseAccountSubmitEntityToMessage(accountSubmit);
        } catch (IOException ex) {
            logger.error(ex);
            throw ex;
        } catch (Exception ex) {
            logger.error(ex);
            throw ex;
        } finally {
            logger.debug("FINALIZA METODO - createSubmitSolve()");
        }
    }

}
