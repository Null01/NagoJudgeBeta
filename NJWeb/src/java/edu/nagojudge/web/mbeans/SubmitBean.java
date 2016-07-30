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
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.servlet.http.Part;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
@ManagedBean
@ViewScoped
public class SubmitBean implements Serializable {

    @EJB
    private SubmitDaoComplex submitDaoComplex;

    @EJB
    private SubmitFacadeDAO submitFacade;

    private final String TARGET_PATH = "/go/to/modules/board/score.xhtml";

    private final Logger LOGGER = Logger.getLogger(SubmitBean.class);

    private List<Submit> submitsAll;

    @ManagedProperty(value = "#{problemsBean.problemView}")
    private Problem problemView = new Problem();

    private Submit submitView = new Submit();
    private LanguageProgramming languageProgrammingView = new LanguageProgramming();

    private Part codeSourceFile;

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

    public void actionSubmitSolution(ActionEvent actionEvent) {
        try {
            submitDaoComplex.createSubmitSolve(submitView, problemView, languageProgrammingView, codeSourceFile);
            FacesUtil.getFacesUtil().redirect(TARGET_PATH);
        } catch (IOException ex) {
            LOGGER.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        }
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

    public Part getCodeSolutionFile() {
        return codeSourceFile;
    }

    public void setCodeSolutionFile(Part codeSolutionFile) {
        this.codeSourceFile = codeSolutionFile;
    }

}
