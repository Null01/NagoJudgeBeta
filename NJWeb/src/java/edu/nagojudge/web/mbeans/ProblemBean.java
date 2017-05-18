/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.web.mbeans;

import edu.nagojudge.app.business.dao.beans.ProblemFacade;
import edu.nagojudge.app.business.dao.beans.SubmitFacade;
import edu.nagojudge.app.business.dao.entities.Account;
import edu.nagojudge.app.business.dao.entities.Category;
import edu.nagojudge.app.business.dao.entities.ComplexityAlgorithm;
import edu.nagojudge.app.business.dao.entities.DifficultyLevel;
import edu.nagojudge.app.business.dao.entities.Problem;
import edu.nagojudge.app.exceptions.UtilNagoJudgeException;
import edu.nagojudge.app.utils.FacesUtil;
import edu.nagojudge.msg.pojo.ProblemMessage;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author andresfelipegarciaduran
 */
@ManagedBean
@ViewScoped
public class ProblemBean implements Serializable {

    @EJB
    private SubmitFacade submitFacadeDAO;

    @EJB
    private ProblemFacade problemFacade;

    private final Logger logger = Logger.getLogger(ProblemBean.class);

    private final String TARGET_PATH = "/go/to/modules/content/problem/search.xhtml";

    private List<ProblemMessage> listProblems;
    private List<ProblemMessage> filteredProblems;
    private String searchParameter;

    private ComplexityAlgorithm complexityAlgorithmView = new ComplexityAlgorithm();
    private Category categoryProblemView = new Category();
    private DifficultyLevel difficultyLevel = new DifficultyLevel();

    private Problem problemView = new Problem();
    private Account accountView = new Account();

    private String pathSourceProblem;
    private UploadedFile problemFile;
    private UploadedFile inputFile;
    private UploadedFile outputFile;
    private String textCode;

    public ProblemBean() {
    }

    @PostConstruct
    public void init() {
        if (problemView.getIdProblem() == null) {
            this.listProblems = problemFacade.findStatisticsFromAllProblem();
        }
    }

    public String actionRedirectViewToSubmitProblem() {
        logger.debug("getIdProblem [" + problemView.getIdProblem() + "]");
        return "/modules/content/problem/view-problem.xhtml?faces-redirect=true&includeViewParams=true";
    }

    public String actionRedirectViewToProfile() {
        logger.debug("getIdAccount=" + accountView.getIdAccount());
        return "/modules/user/profile.xhtml?faces-redirect=true&includeViewParams=true";
    }

    public void actionCreateProblem() {
        try {
            boolean validateTypeFile_I_O = validateTypeFile_I_O();
            if (validateTypeFile_I_O) {

                List<Category> categorys = new ArrayList<Category>();
                categorys.add(categoryProblemView);

                String idProblemCreated = problemFacade.createProblem(problemView, categorys,
                        difficultyLevel, complexityAlgorithmView,
                        problemFile.getContents(), inputFile.getContents(), outputFile.getContents());
                FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_INFO, "Creacion exitosa. Problema #" + idProblemCreated);
                FacesUtil.getFacesUtil().redirect(TARGET_PATH);
            }
        } catch (IOException ex) {
            logger.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        } catch (UtilNagoJudgeException ex) {
            logger.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        } catch (Exception ex) {
            logger.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        }
    }

    public void actionViewCode(Object idSubmit) {
        String codeText = submitFacadeDAO.findAttachmentSubmit(String.valueOf(idSubmit));
        setTextCode(codeText);
    }

    public List<ProblemMessage> getListProblems() {
        return listProblems;
    }

    public void setListProblems(List<ProblemMessage> listProblems) {
        this.listProblems = listProblems;
    }

    public List<ProblemMessage> getFilteredProblems() {
        return filteredProblems;
    }

    public void setFilteredProblems(List<ProblemMessage> filteredProblems) {
        this.filteredProblems = filteredProblems;
    }

    public String getPathSourceProblem() {
        return pathSourceProblem;
    }

    public void setPathSourceProblem(String pathSourceProblem) {
        this.pathSourceProblem = pathSourceProblem;
    }

    public Problem getProblemView() {
        return problemView;
    }

    public void setProblemView(Problem problemView) {
        this.problemView = problemView;
    }

    public String getSearchParameter() {
        return searchParameter;
    }

    public void setSearchParameter(String searchParameter) {
        this.searchParameter = searchParameter;
    }

    public Category getCategoryProblemView() {
        return categoryProblemView;
    }

    public void setCategoryProblemView(Category categoryProblemView) {
        this.categoryProblemView = categoryProblemView;
    }

    public Account getAccountView() {
        return accountView;
    }

    public void setAccountView(Account AccountView) {
        this.accountView = AccountView;
    }

    public String getTextCode() {
        return textCode;
    }

    public void setTextCode(String textCode) {
        this.textCode = textCode;
    }

    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    private boolean validateTypeFile_I_O() {
        if (problemFile == null || problemFile.getSize() == 0) {
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, "Por favor, seleccione un archivo como enunciado del problema.");
            return false;
        }

        if (inputFile == null || inputFile.getSize() == 0) {
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, "Por favor, seleccione un archivo como datos de entrada del problema.");
            return false;
        }

        if (outputFile == null || outputFile.getSize() == 0) {
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, "Por favor, seleccione un archivo como datos de salida del problema.");
            return false;
        }

        return true;
    }

    public UploadedFile getProblemFile() {
        return problemFile;
    }

    public void setProblemFile(UploadedFile problemFile) {
        this.problemFile = problemFile;
    }

    public UploadedFile getInputFile() {
        return inputFile;
    }

    public void setInputFile(UploadedFile inputFile) {
        this.inputFile = inputFile;
    }

    public UploadedFile getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(UploadedFile outputFile) {
        this.outputFile = outputFile;
    }

    public ComplexityAlgorithm getComplexityAlgorithmView() {
        return complexityAlgorithmView;
    }

    public void setComplexityAlgorithmView(ComplexityAlgorithm complexityAlgorithmView) {
        this.complexityAlgorithmView = complexityAlgorithmView;
    }

}
