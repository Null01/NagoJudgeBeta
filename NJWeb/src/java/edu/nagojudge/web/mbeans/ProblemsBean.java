/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.web.mbeans;

import edu.nagojudge.app.business.dao.beans.ProblemFacadeDAO;
import edu.nagojudge.app.business.dao.beans.SubmitFacadeDAO;
import edu.nagojudge.app.business.dao.complex.ProblemFacadeComplex;
import edu.nagojudge.app.business.dao.entities.Account;
import edu.nagojudge.app.business.dao.entities.CategoryProblem;
import edu.nagojudge.app.business.dao.entities.DifficultyLevel;
import edu.nagojudge.app.business.dao.entities.Problem;
import edu.nagojudge.app.business.dao.pojo.ProblemPojo;
import edu.nagojudge.app.exceptions.UtilNagoJudgeException;
import edu.nagojudge.app.utils.FacesUtil;
import edu.nagojudge.msg.pojo.constants.TypeFilesEnum;
import edu.nagojudge.web.utils.dtos.FilePart;
import java.io.IOException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import org.apache.log4j.Logger;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author andresfelipegarciaduran
 */
@ManagedBean
@ViewScoped
public class ProblemsBean implements Serializable {

    @EJB
    private SubmitFacadeDAO submitFacadeDAO;

    @EJB
    private ProblemFacadeComplex problemFacadeComplex;

    @EJB
    private ProblemFacadeDAO problemFacade;

    private final Logger logger = Logger.getLogger(ProblemsBean.class);

    private final String TARGET_PATH = "/go/to/modules/content/problem/search.xhtml";
    private final String KEYS_REQUEST[] = {"idProblem", "idAccount"};

    private List<ProblemPojo> listProblems;
    private List<ProblemPojo> filteredProblems;
    private String searchParameter;

    private CategoryProblem categoryProblemView = new CategoryProblem();
    private DifficultyLevel difficultyLevel = new DifficultyLevel();

    private Problem problemView = new Problem();
    private Account accountView = new Account();

    private String pathSourceProblem;
    private UploadedFile problemFile;
    private UploadedFile inputFile;
    private UploadedFile outputFile;
    private String textCode;

    public ProblemsBean() {
    }

    @PostConstruct
    public void init() {
        if (problemView.getIdProblem() == null) {
            this.listProblems = problemFacade.findProblemEntitiesPojo();
        }
    }

    public String actionRedirectViewToSubmitProblem() {
        logger.debug("getIdProblem [" + problemView.getIdProblem() + "]");
        return "/modules/content/problem/view-problem.xhtml?faces-redirect=true&includeViewParams=true";
    }

    public String actionRedirectViewToProfile() {
        logger.debug("REDIRECT_ACCOUNT_VIEW=" + accountView.getIdAccount());
        FacesUtil.getFacesUtil().getFlash().put(KEYS_REQUEST[1], accountView.getIdAccount());
        return "/modules/user/profile.xhtml?faces-redirect=true";
    }

    public void actionCreateOneProblem(ActionEvent event) {
        try {
            boolean validateTypeFile_I_O = validateTypeFile_I_O();
            if (validateTypeFile_I_O) {

                byte[] contentProblem = problemFile.getContents();
                FilePart problem = new FilePart(problemFile.getFileName(), problemFile.getContentType(), problemFile.getSize(), contentProblem);

                byte[] contentInput = inputFile.getContents();
                FilePart input = new FilePart(inputFile.getFileName(), inputFile.getContentType(), inputFile.getSize(), contentInput);

                byte[] contentOutput = outputFile.getContents();
                FilePart output = new FilePart(outputFile.getFileName(), outputFile.getContentType(), outputFile.getSize(), contentOutput);

                String idProblemCreated = problemFacadeComplex.createProblem(problemView, categoryProblemView, difficultyLevel, problem, input, output);
                clearObjects();
                FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_INFO, "Creacion exitosa. Problema #" + idProblemCreated);
                FacesUtil.getFacesUtil().redirect(TARGET_PATH);
            }
        } catch (IOException ex) {
            logger.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        } catch (NoSuchAlgorithmException ex) {
            logger.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        } catch (UtilNagoJudgeException ex) {
            logger.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        }
    }

    public void actionViewCode(Object idSubmit) {
        logger.debug("VIEW_CODE_SUBMIT_ID=" + idSubmit);
        String codeText = submitFacadeDAO.findAttachmentSubmit(String.valueOf(idSubmit));
        setTextCode(codeText);
    }

    public void actionPreRenderViewToSubmitProblem() {
        logger.debug("getIdProblem [" + problemView.getIdProblem() + "]");
        if (problemView.getIdProblem() != null) {
            try {
                this.problemView = problemFacade.find(problemView.getIdProblem());
                if (problemView != null) {
                    this.pathSourceProblem = submitFacadeDAO.getFullPathProblem(problemView.getIdProblem(), TypeFilesEnum.TYPE_FILE_PROBLEM.getExtension());
                } else {
                    logger.error("El id [" + problemView.getIdProblem() + "] no se encuentra registrado.");
                }
            } catch (IOException ex) {
                logger.error(ex);
            }
        }
    }

    public List<ProblemPojo> getListProblems() {
        return listProblems;
    }

    public void setListProblems(List<ProblemPojo> listProblems) {
        this.listProblems = listProblems;
    }

    public List<ProblemPojo> getFilteredProblems() {
        return filteredProblems;
    }

    public void setFilteredProblems(List<ProblemPojo> filteredProblems) {
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

    public CategoryProblem getCategoryProblemView() {
        return categoryProblemView;
    }

    public void setCategoryProblemView(CategoryProblem categoryProblemView) {
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
        if (problemFile.getSize() == 0) {
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, "Por favor, seleccione un archivo como enunciado del problema.");
            return false;
        }

        if (inputFile.getSize() == 0) {
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, "Por favor, seleccione un archivo como datos de entrada del problema.");
            return false;
        }

        if (outputFile.getSize() == 0) {
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, "Por favor, seleccione un archivo como datos de salida del problema.");
            return false;
        }

        return true;
    }

    private void clearObjects() {
        this.problemView = new Problem();
    }

    public ProblemFacadeComplex getProblemFacadeComplex() {
        return problemFacadeComplex;
    }

    public void setProblemFacadeComplex(ProblemFacadeComplex problemFacadeComplex) {
        this.problemFacadeComplex = problemFacadeComplex;
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

}
