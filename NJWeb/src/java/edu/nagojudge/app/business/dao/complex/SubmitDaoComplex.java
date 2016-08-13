/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.app.business.dao.complex;

import edu.nagojudge.app.business.dao.beans.SubmitFacadeDAO;
import edu.nagojudge.app.business.dao.entities.Account;
import edu.nagojudge.app.business.dao.entities.LanguageProgramming;
import edu.nagojudge.app.business.dao.entities.Problem;
import edu.nagojudge.app.business.dao.entities.Submit;
import edu.nagojudge.app.utils.FacesUtil;
import edu.nagojudge.app.utils.constants.IKeysApplication;
import edu.nagojudge.app.utils.constants.IResourcesPaths;
import edu.nagojudge.msg.pojo.constants.TypeFilesEnum;
import edu.nagojudge.msg.pojo.constants.TypeStateEnum;
import edu.nagojudge.msg.pojo.constants.TypeStateJudgeEnum;
import edu.nagojudge.tools.utils.FileUtil;
import edu.nagojudge.tools.utils.FormatUtil;
import edu.nagojudge.web.utils.resources.clients.ClientService;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import javax.xml.ws.Service;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
@Stateless
public class SubmitDaoComplex implements Serializable {

    @EJB
    private SubmitFacadeDAO submitFacade;

    private final String TOKEN = "asd";

    private final Logger logger = Logger.getLogger(SubmitDaoComplex.class);

    public String createSubmitSolve(Submit submitView, Problem problemView, LanguageProgramming languageProgrammingView, byte[] contentCodeSource) throws IOException {
        try {
            logger.debug("INICIA METODO - createSubmitSolve()");
            HttpSession session = FacesUtil.getFacesUtil().getSession(true);
            Object accountObject = session.getAttribute(IKeysApplication.KEY_DATA_USER_ACCOUNT);
            Object email = session.getAttribute(IKeysApplication.KEY_DATA_USER_EMAIL);
            submitView.setIdAccount((Account) accountObject);
            submitView.setIdProblem(problemView);
            submitView.setIdLanguage(languageProgrammingView);
            submitView.setStatusSubmit(TypeStateJudgeEnum.IP.getValue());
            submitView.setDateSubmit(Calendar.getInstance().getTime());
            submitView.setVisibleWeb(TypeStateEnum.PRIVATE.getType());
            submitFacade.create(submitView);

            String pathFile = IResourcesPaths.PATH_SAVE_CODE_SOURCE_LOCAL + java.io.File.separatorChar
                    + String.valueOf(email) + java.io.File.separatorChar + FormatUtil.getInstance().buildZerosToLeft(submitView.getIdProblem().getIdProblem(), 7);
            String nameFile = FormatUtil.getInstance().buildZerosToLeft(submitView.getIdSubmit(), 7) + "." + languageProgrammingView.getExtension();
            FileUtil.getInstance().createFile(contentCodeSource, pathFile, nameFile);

            String path = "submit/evaluate/{email}/{idSubmit}";
            Object objects[] = {String.valueOf(email), String.valueOf(submitView.getIdSubmit()), TOKEN};
            Object callRestfulGet = ClientService.getInstance().callRestfulGet(path, objects, null);
            logger.debug("outcome [" + callRestfulGet + "]");
            return String.valueOf(submitView.getIdSubmit());
        } catch (IOException ex) {
            logger.error(ex);
            throw ex;
        } finally {
            logger.debug("FINALIZA METODO - createSubmitSolve()");
        }
    }

    public String findAttachmentSubmit(String idSubmit) {
        try {
            logger.debug("INICIA METODO - findAttachmentSubmit()");
            String path = "submit/codeSource/{idSubmit}";
            Object objects[] = {idSubmit, TOKEN};
            Object callRestfulGet = ClientService.getInstance().callRestfulGet(path, objects, null);
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
}
