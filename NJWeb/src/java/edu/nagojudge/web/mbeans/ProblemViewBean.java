/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.web.mbeans;

import edu.nagojudge.app.business.dao.beans.ProblemFacade;
import edu.nagojudge.app.business.dao.beans.SubmitFacade;
import edu.nagojudge.app.business.dao.entities.LanguageProgramming;
import edu.nagojudge.app.utils.FacesUtil;
import edu.nagojudge.app.utils.constants.IKeysApplication;
import edu.nagojudge.msg.pojo.ProblemMessage;
import edu.nagojudge.msg.pojo.SubmitMessage;
import edu.nagojudge.msg.pojo.constants.TypeFilesEnum;
import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author andresfelipegarciaduran
 */
@ManagedBean
@ViewScoped
public class ProblemViewBean implements Serializable {

    @EJB
    private SubmitFacade submitFacadeDAO;

    @EJB
    private ProblemFacade problemFacade;

    private final Logger logger = Logger.getLogger(ProblemViewBean.class);

    private ProblemMessage problemMessage = new ProblemMessage();
    private SubmitMessage submitMessage = new SubmitMessage();

    private LanguageProgramming languageProgrammingView = new LanguageProgramming();

    private UploadedFile codeSourceFile;
    private String pathSourceProblem;

    public ProblemViewBean() {
    }

    public String actionSubmitSolution() {
        try {
            logger.debug("INICIA METODO - actionSubmitSolution()");
            logger.debug("codeSourceFile [" + codeSourceFile + "]");
            if (languageProgrammingView == null) {
                String msg = FacesUtil.getFacesUtil().getResourceBundle("validacion.listaItem.noseleccionado",
                        IKeysApplication.KEY_PUBLIC_MSG_RESOURCES);
                throw new Exception(msg);
            } else {
                if (codeSourceFile == null) {
                    String msg = FacesUtil.getFacesUtil().getResourceBundle("validacion.arhivo.noseleccionado",
                            IKeysApplication.KEY_PUBLIC_MSG_RESOURCES);
                    throw new Exception(msg);
                } else {
                    SubmitMessage message = submitFacadeDAO.createSubmitSolve(submitMessage, problemMessage, languageProgrammingView, codeSourceFile.getContents());
                    //actionSendPushBoardLive(submitMessage);
                    return "/modules/board/score.xhtml?faces-redirect=true&includeViewParams=false";
                }
            }
        } catch (IOException ex) {
            logger.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        } catch (Exception ex) {
            logger.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        } finally {
            logger.debug("FINALIZA METODO - actionSubmitSolution()");
        }
        return null;
    }

    public void actionPreRenderViewToSubmitProblem() {
        boolean validationFailed = FacesUtil.getFacesUtil().isValidationFailed();
        if (validationFailed) {
            logger.error("params [" + problemMessage.getIdProblem() + "]");
            HttpSession session = FacesUtil.getFacesUtil().getCurrentSession();
            session.invalidate();
            FacesUtil.getFacesUtil().printErrorResponse("Authentication failed, contact administrator.");
        } else {
            try {
                logger.debug("params [" + +problemMessage.getIdProblem() + "]");
                this.problemMessage = problemFacade.findProblemById(problemMessage.getIdProblem());
                this.pathSourceProblem = submitFacadeDAO.getFullPathProblem(problemMessage.getIdProblem(), TypeFilesEnum.TYPE_FILE_PROBLEM.getExtension());
            } catch (IOException ex) {
                logger.error(ex);
                FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
            }

        }
    }

    public String getPathSourceProblem() {
        return pathSourceProblem;
    }

    public void setPathSourceProblem(String pathSourceProblem) {
        this.pathSourceProblem = pathSourceProblem;
    }

    public ProblemMessage getProblemMessage() {
        return problemMessage;
    }

    public void setProblemMessage(ProblemMessage problemMessage) {
        this.problemMessage = problemMessage;
    }

    public SubmitMessage getSubmitMessage() {
        return submitMessage;
    }

    public void setSubmitMessage(SubmitMessage submitMessage) {
        this.submitMessage = submitMessage;
    }

    public LanguageProgramming getLanguageProgrammingView() {
        return languageProgrammingView;
    }

    public void setLanguageProgrammingView(LanguageProgramming languageProgrammingView) {
        this.languageProgrammingView = languageProgrammingView;
    }

    public UploadedFile getCodeSourceFile() {
        return codeSourceFile;
    }

    public void setCodeSourceFile(UploadedFile codeSourceFile) {
        this.codeSourceFile = codeSourceFile;
    }

}
