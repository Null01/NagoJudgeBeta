/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.web.mbeans;

import edu.nagojudge.app.business.dao.beans.ProblemFacade;
import edu.nagojudge.app.business.dao.beans.SubmitFacade;
import edu.nagojudge.app.business.dao.beans.UserFacade;
import edu.nagojudge.app.business.dao.entities.Account;
import edu.nagojudge.app.business.dao.entities.Problem;
import edu.nagojudge.app.business.dao.entities.Submit;
import edu.nagojudge.app.business.dao.entities.User;
import edu.nagojudge.app.utils.FacesUtil;
import edu.nagojudge.msg.pojo.SubmitMessage;
import edu.nagojudge.msg.pojo.constants.TypeStateEnum;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author andresfelipegarciaduran
 */
@ManagedBean
@ViewScoped
public class ProfileBean implements Serializable {

    @EJB
    private SubmitFacade submitFacade;
    @EJB
    private ProblemFacade problemFacade;
    @EJB
    private UserFacade userFacade;

    private final Logger logger = Logger.getLogger(ProfileBean.class);

    private final String KEYS_REQUEST[] = {"idAccount"};

    private boolean showPieChartModel = false;
    private PieChartModel pieChartModelStatistics = new PieChartModel();

    private Account accountView = new Account();
    private User userView = new User();
    private Submit submitView = new Submit();
    private Problem problemView = new Problem();

    private String searchParameter;

    private List<Problem> listProblemsTry;
    private List<Problem> filteredProblemsTry;

    private List<Problem> listProblemsTrySolve;
    private List<Problem> filteredProblemsTrySolve;

    private List<SubmitMessage> listSubmitsByAccount;
    private List<SubmitMessage> filterListSubmitsByAccount;

    private static final List<String> listStatusVisibleWeb = new ArrayList<String>();

    static {
        listStatusVisibleWeb.add(TypeStateEnum.PUBLIC.name());
        listStatusVisibleWeb.add(TypeStateEnum.PRIVATE.name());
    }

    public void actionOnRowSelectProblemTry(SelectEvent event) {
        logger.debug("INICIA METODO actionOnRowSelectProblem()");
        Problem problemSelected = ((Problem) event.getObject());
        createPieModelGraphStatisticsStatus(problemSelected);
        this.showPieChartModel = true;
        logger.debug("FINALIZA METODO actionOnRowSelectProblem()");
    }

    public void actionOnRowSelectProblemSolve(SelectEvent event) {
        logger.debug("INICIA METODO actionOnRowSelectProblem()");
        Problem problemSelected = ((Problem) event.getObject());
        createPieModelGraphStatisticsStatusSolve(problemSelected);
        logger.debug("FINALIZA METODO actionOnRowSelectProblem()");
    }

    public void onRowEditMySubmit(RowEditEvent event) {
        try {
            logger.debug("INICIA METODO onRowEditMySubmit()");
            SubmitMessage submitMessage = (SubmitMessage) event.getObject();
            //submitMessage.setVisibleWeb(TypeStateEnum.valueOf(this.submitView.getVisibleWeb()).getType());
            logger.debug("getVisibleWeb [" + submitMessage.getVisibleWeb() + "]");
            submitFacade.editSubmitMessage(submitMessage);
        } catch (Exception ex) {
            logger.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        } finally {
            logger.debug("FINALIZA METODO onRowEditMySubmit()");
        }
    }

    public void actionPreRenderViewToProfile() {
        logger.debug("getIdAccount [" + accountView.getIdAccount() + "]");
        if (accountView.getIdAccount() != null) {

            User findUser = userFacade.findUserByIdAccount(accountView.getIdAccount());
            if (findUser != null) {

                long idAccount = accountView.getIdAccount();
                this.userView = findUser;
                this.accountView = findUser.getIdAccount();
                this.listProblemsTry = problemFacade.findProblemTryEntities(idAccount);
                this.filteredProblemsTry = new ArrayList<Problem>(this.listProblemsTry);

                this.listProblemsTrySolve = problemFacade.findProblemTrySolveEntities(idAccount);
                this.filteredProblemsTrySolve = new ArrayList<Problem>(this.listProblemsTrySolve);

                this.listSubmitsByAccount = submitFacade.findSubmitEntitiesByAccount(idAccount);
                this.filterListSubmitsByAccount = new ArrayList<SubmitMessage>(this.listSubmitsByAccount);
            }
        }
    }

    public String getSearchParameter() {
        return searchParameter;
    }

    public void setSearchParameter(String searchParameter) {
        this.searchParameter = searchParameter;
    }

    public List<String> getListStatusVisibleWeb() {
        return listStatusVisibleWeb;
    }

    public PieChartModel getPieChartModelStatistics() {
        return pieChartModelStatistics;
    }

    public void setPieChartModelStatistics(PieChartModel pieChartModelStatistics) {
        this.pieChartModelStatistics = pieChartModelStatistics;
    }

    public Account getAccountView() {
        return accountView;
    }

    public void setAccountView(Account accountView) {
        this.accountView = accountView;
    }

    public User getUserView() {
        return userView;
    }

    public void setUserView(User userView) {
        this.userView = userView;
    }

    public Submit getSubmitView() {
        return submitView;
    }

    public void setSubmitView(Submit submitView) {
        this.submitView = submitView;
    }

    public Problem getProblemView() {
        return problemView;
    }

    public void setProblemView(Problem problemView) {
        this.problemView = problemView;
    }

    public List<Problem> getListProblemsTry() {
        return listProblemsTry;
    }

    public void setListProblemsTry(List<Problem> listProblemsTry) {
        this.listProblemsTry = listProblemsTry;
    }

    public List<Problem> getFilteredProblemsTry() {
        return filteredProblemsTry;
    }

    public void setFilteredProblemsTry(List<Problem> filteredProblemsTry) {
        this.filteredProblemsTry = filteredProblemsTry;
    }

    public List<Problem> getListProblemsTrySolve() {
        return listProblemsTrySolve;
    }

    public void setListProblemsTrySolve(List<Problem> listProblemsTrySolve) {
        this.listProblemsTrySolve = listProblemsTrySolve;
    }

    public List<Problem> getFilteredProblemsTrySolve() {
        return filteredProblemsTrySolve;
    }

    public void setFilteredProblemsTrySolve(List<Problem> filteredProblemsTrySolve) {
        this.filteredProblemsTrySolve = filteredProblemsTrySolve;
    }

    public List<SubmitMessage> getListSubmitsByAccount() {
        return listSubmitsByAccount;
    }

    public void setListSubmitsByAccount(List<SubmitMessage> listSubmitsByAccount) {
        this.listSubmitsByAccount = listSubmitsByAccount;
    }

    public List<SubmitMessage> getFilterListSubmitsByAccount() {
        return filterListSubmitsByAccount;
    }

    public void setFilterListSubmitsByAccount(List<SubmitMessage> filterListSubmitsByAccount) {
        this.filterListSubmitsByAccount = filterListSubmitsByAccount;
    }

    public boolean isShowPieChartModel() {
        return showPieChartModel;
    }

    public void setShowPieChartModel(boolean showPieChartModel) {
        this.showPieChartModel = showPieChartModel;
    }

    public String statusView(String viewWeb) {
        for (TypeStateEnum v : TypeStateEnum.values()) {
            if (v.getType().compareTo(viewWeb) == 0) {
                return v.name();
            }
        }
        return "null";
    }

    private void createPieModelGraphStatisticsStatus(Problem problem) {
        logger.debug("INICIA METODO - createPieModelGraphStatisticsStatus()");
        logger.debug("getIdProblem()=" + problem.getIdProblem());
        Map<String, Long> mapStatisticsStatus = problemFacade.findStatisticsStatus(problem.getIdProblem());
        pieChartModelStatistics = new PieChartModel();
        if (!mapStatisticsStatus.isEmpty()) {
            for (Map.Entry<String, Long> row : mapStatisticsStatus.entrySet()) {
                pieChartModelStatistics.set(row.getKey(), row.getValue());
            }
        } else {
            pieChartModelStatistics.set("Unsolved", 0);
        }
        pieChartModelStatistics.setShowDataLabels(true);
        pieChartModelStatistics.setTitle("Estadisticas!");
        pieChartModelStatistics.setLegendPosition("w");
        logger.debug("FINALIZA METODO - createPieModelGraphStatisticsStatus()");
    }

    private void createPieModelGraphStatisticsStatusSolve(Problem problem) {
        logger.debug("INICIA METODO - createPieModelGraphStatisticsStatusSolve()");
        logger.debug("getIdProblem()=" + problem.getIdProblem());
        Map<String, Long> mapStatisticsStatus = problemFacade.findStatisticsStatusByAccount(problem.getIdProblem(), accountView.getIdAccount());

        pieChartModelStatistics = new PieChartModel();
        if (!mapStatisticsStatus.isEmpty()) {
            for (Map.Entry<String, Long> row : mapStatisticsStatus.entrySet()) {
                pieChartModelStatistics.set(row.getKey(), row.getValue());
            }
        }
        pieChartModelStatistics.setShowDataLabels(true);
        pieChartModelStatistics.setTitle("Estadisticas!");
        pieChartModelStatistics.setLegendPosition("w");
        logger.debug("FINALIZA METODO - createPieModelGraphStatisticsStatusSolve()");
    }

}
