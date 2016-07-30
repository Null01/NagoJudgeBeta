/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.web.mbeans;

import edu.nagojudge.app.business.dao.beans.ProblemFacadeDAO;
import edu.nagojudge.app.business.dao.complex.ProblemFacadeComplex;
import edu.nagojudge.app.business.dao.complex.SubmitDaoComplex;
import edu.nagojudge.app.business.dao.entities.Account;
import edu.nagojudge.app.business.dao.entities.CategoryProblem;
import edu.nagojudge.app.business.dao.entities.DifficultyLevel;
import edu.nagojudge.app.business.dao.entities.Problem;
import edu.nagojudge.app.business.dao.pojo.ProblemPojo;
import edu.nagojudge.app.exceptions.UtilNagoJudgeException;
import edu.nagojudge.app.utils.FacesUtil;
import edu.nagojudge.msg.pojo.constants.TypeFilesEnum;
import edu.nagojudge.tools.utils.FileUtil;
import edu.nagojudge.web.utils.dtos.FilePart;
import java.io.IOException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.Flash;
import javax.faces.event.ActionEvent;
import javax.servlet.http.Part;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
@ManagedBean
@ViewScoped
public class ProblemsBean implements Serializable {

    @EJB
    private ProblemFacadeComplex problemFacadeComplex;

    @EJB
    private ProblemFacadeDAO problemFacade;
    @EJB
    private SubmitDaoComplex submitDaoComplex;

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
    private Part problemFile;
    private Part inputFile;
    private Part outputFile;
    private String textCode;

    public ProblemsBean() {
    }

    @PostConstruct
    public void init() {
        Flash flash = FacesUtil.getFacesUtil().getFlash();

        if (flash.get(KEYS_REQUEST[0]) != null) {
            try {
                problemView.setIdProblem((Long) flash.get(KEYS_REQUEST[0]));
                this.problemView = problemFacade.find(problemView.getIdProblem());
                this.pathSourceProblem = submitDaoComplex.getFullPathProblem(problemView.getIdProblem(), TypeFilesEnum.TYPE_FILE_PROBLEM.getExtension());
            } catch (IOException ex) {
                logger.error(ex);
            }
        } else {
            this.listProblems = problemFacade.findProblemEntitiesPojo();
        }
    }

    public String actionRedirectView() {
        logger.debug("REDIRECT_PROBLEM_VIEW=" + problemView.getIdProblem());
        FacesUtil.getFacesUtil().getFlash().put(KEYS_REQUEST[0], problemView.getIdProblem());
        return "/modules/content/problem/view-problem.xhtml?faces-redirect=true";
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
                byte[] contentProblem = FileUtil.getInstance().parseFromInputStreamToArrayByte(problemFile.getInputStream());
                FilePart problem = new FilePart(problemFile.getName(), problemFile.getContentType(), problemFile.getSize(), contentProblem);

                byte[] contentInput = FileUtil.getInstance().parseFromInputStreamToArrayByte(inputFile.getInputStream());
                FilePart input = new FilePart(inputFile.getName(), inputFile.getContentType(), inputFile.getSize(), contentInput);

                byte[] contentOutput = FileUtil.getInstance().parseFromInputStreamToArrayByte(outputFile.getInputStream());
                FilePart output = new FilePart(outputFile.getName(), outputFile.getContentType(), outputFile.getSize(), contentOutput);
                //FilePart input = new FilePart("INPUT", "text/plain", inputFileText.length(), inputFileText.getBytes(Charset.forName("UTF-8")));
                //FilePart output = new FilePart("OUTPUT", "text/plain", outputFileText.length(), outputFileText.getBytes(Charset.forName("UTF-8")));

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
        String codeText = submitDaoComplex.findAttachmentSubmit(String.valueOf(idSubmit));
        setTextCode(codeText);
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

    public Part getProblemFile() {
        return problemFile;
    }

    public void setProblemFile(Part problemFile) {
        this.problemFile = problemFile;
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

    public Part getInputFile() {
        return inputFile;
    }

    public void setInputFile(Part inputFile) {
        this.inputFile = inputFile;
    }

    public Part getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(Part outputFile) {
        this.outputFile = outputFile;
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

}
