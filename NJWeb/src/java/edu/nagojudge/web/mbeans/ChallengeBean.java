/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.web.mbeans;

import edu.nagojudge.app.business.dao.beans.ChallengeFacadeDAO;
import edu.nagojudge.app.business.dao.beans.ProblemFacadeDAO;
import edu.nagojudge.app.business.dao.entities.Challenge;
import edu.nagojudge.app.business.dao.pojo.ProblemPojo;
import edu.nagojudge.app.utils.FacesUtil;
import edu.nagojudge.tools.utils.FormatUtil;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.Flash;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
@ManagedBean
@ViewScoped
public class ChallengeBean implements Serializable {

    @EJB
    private ChallengeFacadeDAO challengeFacade;

    @EJB
    private ProblemFacadeDAO problemFacade;

    private final Logger logger = Logger.getLogger(ChallengeBean.class);

    private final String KEYS_REQUEST[] = {"idProblem", "numProblems"};
    private final String TARGET_PATH = "/go/to/modules/content/problem/search.xhtml";

    private Challenge challengeView = new Challenge();
    private Map<Long, Boolean> mapProblemsSelected = new HashMap<Long, Boolean>();
    private Map<Long, ProblemPojo> mapProblemsSelectedObject = new HashMap<Long, ProblemPojo>();
    private List<ProblemPojo> listProblemsSelectedFinally = new ArrayList<ProblemPojo>();

    private List<String> listTimesStarted = new ArrayList<String>();
    private String timeStartSelected;
    private List<String> listDurationsStarted = new ArrayList<String>();
    private String duracionStartSelected;
    private List<String> listQuantityProblems = new ArrayList<String>();
    private String quantityProblemsStartSelected;

    private List<ProblemPojo> listProblems;
    private List<ProblemPojo> filteredProblems;
    private String searchParameter;

    public ChallengeBean() {
    }

    @PostConstruct
    public void init() {

        this.listProblems = problemFacade.findProblemEntitiesPojo();
        this.listTimesStarted = challengeFacade.obtenerListaHorasInicioCompetencias();
        this.listDurationsStarted = challengeFacade.obtenerListaDuracionCompetencias();
        this.listQuantityProblems = challengeFacade.obtenerListaNumeroEnunciados();
        for (ProblemPojo problemPojo : listProblems) {
            mapProblemsSelected.put(problemPojo.getIdProblem(), Boolean.FALSE);
            mapProblemsSelectedObject.put(problemPojo.getIdProblem(), problemPojo);
        }

        Flash flash = FacesUtil.getFacesUtil().getFlash();
        if (flash.get(KEYS_REQUEST[1]) != null) {
            Integer numProblems = new Integer(String.valueOf(flash.get(KEYS_REQUEST[1])));
            challengeView.setQuantityProblems(numProblems.shortValue());
        }
    }

    public void actionUpdateListProblemsSelected() {
        listProblemsSelectedFinally.clear();
        for (Map.Entry<Long, Boolean> entry : mapProblemsSelected.entrySet()) {
            if (entry.getValue()) {
                listProblemsSelectedFinally.add(mapProblemsSelectedObject.get(entry.getKey()));
            }
        }
    }

    public void actionSaveCompleteCompetition() {
        try {
            logger.debug("INICIA METODO - actionSaveCompleteCompetition()");
            logger.debug("LIST_PROBLEMS_SELECTED=" + listProblemsSelectedFinally);
            logger.debug("OBJ_CHALLENGE=" + challengeView);
            logger.debug("duracionStartSelected=" + duracionStartSelected);
            logger.debug("timeStartSelected=" + timeStartSelected);
            logger.debug("quantityProblemsStartSelected=" + quantityProblemsStartSelected);
            challengeView.setDurationMin(FormatUtil.getInstance().parseTimeToMinutes(duracionStartSelected));
            challengeView.setDateChallenge(FormatUtil.getInstance().addTimeToDate(challengeView.getDateChallenge(), timeStartSelected));
            challengeView.setQuantityProblems(Short.parseShort(quantityProblemsStartSelected));
            String idChallengeCreated = challengeFacade.createChallenge(challengeView, listProblemsSelectedFinally);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_INFO, "Creacion exitosa. Competencia #" + idChallengeCreated);
            FacesUtil.getFacesUtil().redirect(TARGET_PATH);
        } catch (ParseException ex) {
            logger.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        } catch (IOException ex) {
            logger.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        } finally {
            logger.debug("FINALIZA METODO - actionSaveCompleteCompetition()");
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

    public Map<Long, ProblemPojo> getMapProblemsSelectedObject() {
        return mapProblemsSelectedObject;
    }

    public void setMapProblemsSelectedObject(Map<Long, ProblemPojo> mapProblemsSelectedObject) {
        this.mapProblemsSelectedObject = mapProblemsSelectedObject;
    }

    public List<ProblemPojo> getListProblemsSelectedFinally() {
        return listProblemsSelectedFinally;
    }

    public void setListProblemsSelectedFinally(List<ProblemPojo> listProblemsSelectedFinally) {
        this.listProblemsSelectedFinally = listProblemsSelectedFinally;
    }

    public List<String> getListTimesStarted() {
        return listTimesStarted;
    }

    public void setListTimesStarted(List<String> listTimesStarted) {
        this.listTimesStarted = listTimesStarted;
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

    public String getSearchParameter() {
        return searchParameter;
    }

    public void setSearchParameter(String searchParameter) {
        this.searchParameter = searchParameter;
    }

}
