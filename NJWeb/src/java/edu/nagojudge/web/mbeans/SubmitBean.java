/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.web.mbeans;

import edu.nagojudge.app.business.dao.beans.SubmitFacadeDAO;
import edu.nagojudge.app.business.dao.complex.SubmitDaoComplex;
import edu.nagojudge.app.business.dao.entities.LanguageProgramming;
import edu.nagojudge.app.business.dao.entities.Problem;
import edu.nagojudge.app.business.dao.entities.Submit;
import edu.nagojudge.app.utils.FacesUtil;
import edu.nagojudge.app.utils.constants.IKeysApplication;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

/**
 *
 * @author andresfelipegarciaduran
 */
@ManagedBean
@ViewScoped
public class SubmitBean implements Serializable {
    
    private final static String CHANNEL = "/notify";
    
    @EJB
    private SubmitDaoComplex submitDaoComplex;
    
    @EJB
    private SubmitFacadeDAO submitFacade;
    
    private final String TARGET_PATH = "/go/to/modules/board/score.xhtml";
    
    private final Logger logger = Logger.getLogger(SubmitBean.class);
    
    private List<Submit> submitsAll;
    
    @ManagedProperty(value = "#{problemsBean.problemView}")
    private Problem problemView = new Problem();
    
    private Submit submitView = new Submit();
    private LanguageProgramming languageProgrammingView = new LanguageProgramming();
    
    private UploadedFile codeSourceFile;

    /**
     * Creates a new instance of SubmitBean
     */
    public SubmitBean() {
    }
    
    @PostConstruct
    public void init() {
        
        if (submitsAll == null) {
            this.submitsAll = submitFacade.findLast100Results();
        }
    }
    
    public void actionAjaxRefreshBoard() {
        this.submitsAll = new ArrayList<Submit>(submitFacade.findLast100Results());
    }
    
    public void actionSubmitSolution() {
        try {
            logger.debug("Incia metdo - actionSubmitSolution()");
            logger.debug("codeSourceFile [" + codeSourceFile + "]");
            if (codeSourceFile == null) {
                String msg = FacesUtil.getFacesUtil().getResourceBundle("validacion.arhivo.noseleccionado",
                        IKeysApplication.KEY_PUBLIC_MSG_RESOURCES);
                throw new Exception(msg);
            } else {
                submitDaoComplex.createSubmitSolve(submitView, problemView, languageProgrammingView, codeSourceFile.getContents());
                actionRequestPush();
                logger.debug("redirect [" + TARGET_PATH + "]");
                FacesUtil.getFacesUtil().redirect(TARGET_PATH);
            }
        } catch (IOException ex) {
            logger.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        } catch (Exception ex) {
            logger.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        } finally {
            logger.debug("Finaliza metdo - actionSubmitSolution()");
        }
    }
    
    public void handleFileUpload(FileUploadEvent event) {
        this.codeSourceFile = event.getFile();
        logger.debug("getFileName() [" + codeSourceFile.getFileName() + "]");
        
    }
    
    public Problem getProblemView() {
        return problemView;
    }
    
    public void setProblemView(Problem problemView) {
        this.problemView = problemView;
    }
    
    public LanguageProgramming getLanguageProgrammingView() {
        return languageProgrammingView;
    }
    
    public void setLanguageProgrammingView(LanguageProgramming languageProgrammingView) {
        this.languageProgrammingView = languageProgrammingView;
    }
    
    public Submit getSubmitView() {
        return submitView;
    }
    
    public void setSubmitView(Submit submitView) {
        this.submitView = submitView;
    }
    
    public List<Submit> getSubmitsAll() {
        return submitsAll;
    }
    
    public void setSubmitsAll(List<Submit> submitsAll) {
        this.submitsAll = submitsAll;
    }
    
    public UploadedFile getCodeSourceFile() {
        return codeSourceFile;
    }
    
    public void setCodeSourceFile(UploadedFile codeSourceFile) {
        this.codeSourceFile = codeSourceFile;
    }
    
    private void actionRequestPush() {
        try {
            EventBus eventBus = EventBusFactory.getDefault().eventBus();
            eventBus.publish(CHANNEL, new FacesMessage(StringEscapeUtils.escapeHtml("Envio @echo"), StringEscapeUtils.escapeHtml("Envio @echo")));
        } catch (Exception ex) {
            logger.error(ex);
        }
    }
    
}
