/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.app.business.dao.beans;

import edu.nagojudge.app.business.dao.entities.Account;
import edu.nagojudge.app.business.dao.entities.LanguageProgramming;
import edu.nagojudge.app.business.dao.entities.Problem;
import edu.nagojudge.app.business.dao.entities.Submit;
import edu.nagojudge.app.utils.FacesUtil;
import edu.nagojudge.app.utils.constants.IKeysApplication;
import edu.nagojudge.app.utils.constants.IResourcesPaths;
import edu.nagojudge.msg.pojo.ResponseMessage;
import edu.nagojudge.msg.pojo.SubmitMessage;
import edu.nagojudge.msg.pojo.constants.TypeFilesEnum;
import edu.nagojudge.msg.pojo.constants.TypeStateEnum;
import edu.nagojudge.msg.pojo.constants.TypeStateJudgeEnum;
import edu.nagojudge.tools.utils.FileUtil;
import edu.nagojudge.tools.utils.FormatUtil;
import edu.nagojudge.web.listeners.push.AbstractNotifyResource;
import edu.nagojudge.web.utils.resources.clients.ClientService;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class SubmitFacadeDAO extends AbstractFacade<Submit> implements Serializable {

    private final String TOKEN = "asd";

    private final Logger logger = Logger.getLogger(SubmitFacadeDAO.class);

    @PersistenceContext(unitName = "NJWebPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SubmitFacadeDAO() {
        super(Submit.class);
    }

    public List<SubmitMessage> findLast100Results() {
        List<SubmitMessage> outcome = new ArrayList<SubmitMessage>();
        StringBuilder sql = new StringBuilder();
        int TOP = 100;
        sql.append("SELECT * FROM SUBMIT ORDER BY DATE_SUBMIT DESC LIMIT ").append(TOP);
        Query query = em.createNativeQuery(sql.toString(), Submit.class);
        List<Submit> resultList = query.getResultList();
        if (resultList != null && !resultList.isEmpty()) {
            for (Submit submit : resultList) {
                SubmitMessage submitMessage = parseSubmitEntityToMessage(submit);
                outcome.add(submitMessage);
            }
        }
        return outcome;
    }

    public SubmitMessage createSubmitSolve(Submit submitView, Problem problemView, LanguageProgramming languageProgrammingView, byte[] contentCodeSource) throws IOException {
        try {
            logger.debug("INICIA METODO - createSubmitSolve()");
            HttpSession session = FacesUtil.getFacesUtil().getSession(true);
            Object accountObject = session.getAttribute(IKeysApplication.KEY_DATA_USER_ACCOUNT);
            final Object email = session.getAttribute(IKeysApplication.KEY_DATA_USER_EMAIL);

            submitView.setIdAccount((Account) accountObject);
            submitView.setIdProblem(problemView);
            submitView.setIdLanguage(languageProgrammingView);
            submitView.setStatusSubmit(TypeStateJudgeEnum.IP.getValue());
            submitView.setDateSubmit(Calendar.getInstance().getTime());
            submitView.setVisibleWeb(TypeStateEnum.PRIVATE.getType());
            create(submitView);

            String pathFile = IResourcesPaths.PATH_SAVE_CODE_SOURCE_LOCAL + java.io.File.separatorChar
                    + String.valueOf(email) + java.io.File.separatorChar + FormatUtil.getInstance().buildZerosToLeft(submitView.getIdProblem().getIdProblem(), 7);
            String nameFile = FormatUtil.getInstance().buildZerosToLeft(submitView.getIdSubmit(), 7) + "." + languageProgrammingView.getExtension();
            FileUtil.getInstance().createFile(contentCodeSource, pathFile, nameFile);
            final Long idSubmit = submitView.getIdSubmit();

            final Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    String path = "submit/verdict/{email}/{idSubmit}";
                    Object objects[] = {String.valueOf(email), String.valueOf(idSubmit)};
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("token", TOKEN);
                    SubmitMessage submitMessage = (SubmitMessage) ClientService.getInstance().callRestfulGet(path, objects, params, SubmitMessage.class);
                    logger.debug("outcome [" + submitMessage + "]");

                    logger.debug(submitMessage.getIdSubmit());
                    logger.debug(submitMessage.getIdProblem());
                    logger.debug(submitMessage.getIdAccount());
                    logger.debug(submitMessage.getNameLanguage());
                    new AbstractNotifyResource().pushNotify(submitMessage);
                }
            });
            thread.start();

            return parseSubmitEntityToMessage(submitView);
        } catch (IOException ex) {
            logger.error(ex);
            throw ex;
        } finally {
            logger.debug("FINALIZA METODO - createSubmitSolve()");
        }
    }

    public List<Submit> findSubmitEntitiesByAccount(long idAccount) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM SUBMIT WHERE ID_ACCOUNT = ? ORDER BY DATE_SUBMIT ");
        Query query = em.createNativeQuery(sql.toString(), Submit.class).setParameter(1, idAccount);
        List<Submit> resultList = query.getResultList();
        return resultList;

    }

    public String findAttachmentSubmit(String idSubmit) {
        try {
            logger.debug("INICIA METODO - findAttachmentSubmit()");
            String path = "submit/codeSource/{idSubmit}";
            Object objects[] = {idSubmit};
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("token", TOKEN);
            Object callRestfulGet = ClientService.getInstance().callRestfulGet(path, objects, params, ResponseMessage.class);
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
                fullPath = IResourcesPaths.PATH_VIEW_PROBLEMS + java.io.File.separatorChar + nameFile;
            }
            logger.debug("PATH_VIEW_FILE_PUBLIC=" + fullPath);
            return fullPath;
        } catch (IOException ex) {
            logger.error(ex);
            throw ex;
        } finally {
            logger.debug("FINALIZA METODO - getFullPathProblem()");
        }
    }

    private final SubmitMessage parseSubmitEntityToMessage(Submit submit) {
        SubmitMessage submitMessage = new SubmitMessage();
        submitMessage.setDateJudge(submit.getDateJudge() == null ? 0 : submit.getDateJudge().getTime());
        submitMessage.setDateSubmit(submit.getDateSubmit() == null ? 0 : submit.getDateSubmit().getTime());
        submitMessage.setIdAccount(submit.getIdAccount().getIdAccount());
        submitMessage.setIdProblem(submit.getIdProblem().getIdProblem());
        submitMessage.setIdSubmit(submit.getIdSubmit());
        submitMessage.setMsgJudge(submit.getMsgJudge());
        submitMessage.setNameLanguage(submit.getIdLanguage().getNameLanguage());
        submitMessage.setNameProblem(submit.getIdProblem().getNameProblem());
        submitMessage.setNickname(submit.getIdAccount().getNickname());
        submitMessage.setStatusSubmit(submit.getStatusSubmit());
        submitMessage.setVisibleWeb(submit.getVisibleWeb());
        return submitMessage;
    }

}
