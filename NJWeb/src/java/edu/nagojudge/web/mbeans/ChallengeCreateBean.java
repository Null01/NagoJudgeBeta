/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.web.mbeans;

import edu.nagojudge.app.business.dao.beans.ChallengeFacade;
import edu.nagojudge.app.business.dao.beans.ProblemFacade;
import edu.nagojudge.app.business.dao.entities.Challenge;
import edu.nagojudge.app.utils.FacesUtil;
import edu.nagojudge.app.utils.TransformUtil;
import edu.nagojudge.msg.pojo.ProblemMessage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;
import org.primefaces.event.FlowEvent;

/**
 *
 * @author andresfelipegarciaduran
 */
@ManagedBean
@ViewScoped
public class ChallengeCreateBean implements Serializable {

    @EJB
    private ChallengeFacade challengeFacade;

    @EJB
    private ProblemFacade problemFacade;

    private final Logger logger = Logger.getLogger(ChallengeCreateBean.class);

    private final String TARGET_PATH = "/go/to/modules/practice/contest/search.xhtml";

    private Challenge challengeView = new Challenge();
    private Map<Long, Boolean> mapProblemsSelected = new HashMap<Long, Boolean>();
    private Map<Long, ProblemMessage> mapProblemsSelectedObject = new HashMap<Long, ProblemMessage>();
    private List<ProblemMessage> listProblemsSelectedFinally = new ArrayList<ProblemMessage>();

    private List<String> listTimes = new ArrayList<String>();
    private String timeStartSelected;
    private String timeEndSelected;
    private List<String> listDurationsStarted = new ArrayList<String>();
    private String duracionStartSelected;
    private List<String> listQuantityProblems = new ArrayList<String>();
    private String quantityProblemsStartSelected;

    private List<ProblemMessage> listProblems;
    private List<ProblemMessage> filteredProblems;
    private String searchParameter;

    public ChallengeCreateBean() {
    }

    @PostConstruct
    public void init() {

        this.listProblems = problemFacade.findStatisticsFromAllProblem();
        this.listTimes = challengeFacade.obtenerListaHorasInicioCompetencias();
        this.listQuantityProblems = challengeFacade.obtenerListaNumeroEnunciados();
        for (ProblemMessage problemPojo : listProblems) {
            mapProblemsSelected.put(problemPojo.getIdProblem(), Boolean.FALSE);
            mapProblemsSelectedObject.put(problemPojo.getIdProblem(), problemPojo);
        }
    }

    public void actionUpdateListenerProblemsSelected() {
        listProblemsSelectedFinally.clear();
        for (Map.Entry<Long, Boolean> entry : mapProblemsSelected.entrySet()) {
            if (entry.getValue()) {
                listProblemsSelectedFinally.add(mapProblemsSelectedObject.get(entry.getKey()));
            }
        }
    }

    public String actionOnFlowProcessCreateChallenge(FlowEvent event) {
        return event.getNewStep();
    }

    public void actionSaveCompleteChallenge() {
        try {
            logger.debug("INICIA METODO - actionSaveCompleteChallenge()");
            Date start = TransformUtil.getUtilTransform().addFormatHHmmaToDate(challengeView.getDateStart(), timeStartSelected);
            Date end = TransformUtil.getUtilTransform().addFormatHHmmaToDate(challengeView.getDateEnd(), timeEndSelected);
            challengeView.setDateStart(start);
            challengeView.setDateEnd(end);
            String challengeId = challengeFacade.createChallenge(challengeView, listProblemsSelectedFinally);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_INFO, "Creacion exitosa. Competencia #" + challengeId);
            FacesUtil.getFacesUtil().redirect(TARGET_PATH);
        } catch (Exception ex) {
            logger.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        } finally {
            logger.debug("FINALIZA METODO - actionSaveCompleteChallenge()");
        }
    }

    public Challenge getChallengeView() {
        return challengeView;
    }

    public void setChallengeView(Challenge challengeView) {
        this.challengeView = challengeView;
    }

    public Map<Long, Boolean> getMapProblemsSelected() {
        return mapProblemsSelected;
    }

    public void setMapProblemsSelected(Map<Long, Boolean> mapProblemsSelected) {
        this.mapProblemsSelected = mapProblemsSelected;
    }

    public Map<Long, ProblemMessage> getMapProblemsSelectedObject() {
        return mapProblemsSelectedObject;
    }

    public void setMapProblemsSelectedObject(Map<Long, ProblemMessage> mapProblemsSelectedObject) {
        this.mapProblemsSelectedObject = mapProblemsSelectedObject;
    }

    public List<ProblemMessage> getListProblemsSelectedFinally() {
        return listProblemsSelectedFinally;
    }

    public void setListProblemsSelectedFinally(List<ProblemMessage> listProblemsSelectedFinally) {
        this.listProblemsSelectedFinally = listProblemsSelectedFinally;
    }

    public List<String> getListTimes() {
        return listTimes;
    }

    public void setListTimes(List<String> listTimes) {
        this.listTimes = listTimes;
    }

    public String getTimeStartSelected() {
        return timeStartSelected;
    }

    public void setTimeStartSelected(String timeStartSelected) {
        this.timeStartSelected = timeStartSelected;
    }

    public List<String> getListDurationsStarted() {
        return listDurationsStarted;
    }

    public void setListDurationsStarted(List<String> listDurationsStarted) {
        this.listDurationsStarted = listDurationsStarted;
    }

    public String getDuracionStartSelected() {
        return duracionStartSelected;
    }

    public void setDuracionStartSelected(String duracionStartSelected) {
        this.duracionStartSelected = duracionStartSelected;
    }

    public List<String> getListQuantityProblems() {
        return listQuantityProblems;
    }

    public void setListQuantityProblems(List<String> listQuantityProblems) {
        this.listQuantityProblems = listQuantityProblems;
    }

    public String getQuantityProblemsStartSelected() {
        return quantityProblemsStartSelected;
    }

    public void setQuantityProblemsStartSelected(String quantityProblemsStartSelected) {
        this.quantityProblemsStartSelected = quantityProblemsStartSelected;
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

    public String getSearchParameter() {
        return searchParameter;
    }

    public void setSearchParameter(String searchParameter) {
        this.searchParameter = searchParameter;
    }

    public String getTimeEndSelected() {
        return timeEndSelected;
    }

    public void setTimeEndSelected(String timeEndSelected) {
        this.timeEndSelected = timeEndSelected;
    }

}
