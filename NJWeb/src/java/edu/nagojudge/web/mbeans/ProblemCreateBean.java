/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.web.mbeans;

import edu.nagojudge.app.business.dao.beans.ProblemFacade;
import edu.nagojudge.app.business.dao.beans.SubmitFacade;
import edu.nagojudge.app.business.dao.entities.Category;
import edu.nagojudge.app.business.dao.entities.ComplexityAlgorithm;
import edu.nagojudge.app.business.dao.entities.DifficultyLevel;
import edu.nagojudge.app.utils.FacesUtil;
import edu.nagojudge.msg.pojo.AccountMessage;
import edu.nagojudge.msg.pojo.ProblemMessage;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import org.apache.log4j.Logger;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author andresfelipegarciaduran
 */
@ManagedBean
@ViewScoped
public class ProblemCreateBean implements Serializable {

    @EJB
    private SubmitFacade submitFacadeDAO;

    @EJB
    private ProblemFacade problemFacade;

    private final Logger logger = Logger.getLogger(ProblemCreateBean.class);

    private final String TARGET_PATH = "/go/to/modules/content/problem/search.xhtml";

    private ProblemMessage problemView = new ProblemMessage();
    private AccountMessage accountView = new AccountMessage();

    private ComplexityAlgorithm complexityAlgorithmView = new ComplexityAlgorithm();
    private Category[] selectedCategory = new Category[50];
    private List<Category> listCategorys = new ArrayList<Category>();
    private DifficultyLevel difficultyLevel = new DifficultyLevel();

    private String pathSourceProblem;
    private UploadedFile problemFile;
    private UploadedFile inputFile;
    private UploadedFile outputFile;
    private String viewTextCode;

    public ProblemCreateBean() {
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
                if (selectedCategory != null) {
                    for (Category category : selectedCategory) {
                        categorys.add(category);
                    }
                }
                String idProblemCreated = problemFacade.createProblem(problemView, categorys,
                        difficultyLevel, complexityAlgorithmView,
                        problemFile.getContents(), inputFile.getContents(), outputFile.getContents());
                FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_INFO, "Creacion exitosa. Problema #" + idProblemCreated);
                FacesUtil.getFacesUtil().redirect(TARGET_PATH);
            }
        } catch (IOException ex) {
            logger.error(ex);
        } catch (Exception ex) {
            logger.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        }
    }

    public void actionViewCode(Object idSubmit) {
        String codeText = submitFacadeDAO.findAttachmentSubmit(String.valueOf(idSubmit));
        setViewTextCode(codeText);
    }

    public void actionListenerAddCategory(ValueChangeEvent event) {
        Object newValue = event.getNewValue();
        logger.debug(newValue);
    }

    public String getPathSourceProblem() {
        return pathSourceProblem;
    }

    public void setPathSourceProblem(String pathSourceProblem) {
        this.pathSourceProblem = pathSourceProblem;
    }

    public Category[] getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(Category[] selectedCategory) {
        this.selectedCategory = selectedCategory;
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

    public ProblemMessage getProblemView() {
        return problemView;
    }

    public void setProblemView(ProblemMessage problemView) {
        this.problemView = problemView;
    }

    public AccountMessage getAccountView() {
        return accountView;
    }

    public void setAccountView(AccountMessage accountView) {
        this.accountView = accountView;
    }

    public List<Category> getListCategorys() {
        return listCategorys;
    }

    public void setListCategorys(List<Category> listCategorys) {
        this.listCategorys = listCategorys;
    }

    public String getViewTextCode() {
        return viewTextCode;
    }

    public void setViewTextCode(String viewTextCode) {
        this.viewTextCode = viewTextCode;
    }

}
