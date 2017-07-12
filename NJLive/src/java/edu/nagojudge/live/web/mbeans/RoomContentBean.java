/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.web.mbeans;

import edu.nagojudge.live.business.entity.facade.dao.ChallengeProblemDAO;
import edu.nagojudge.live.web.utils.FacesUtil;
import edu.nagojudge.live.web.utils.constants.IKeysApplication;
import edu.nagojudge.msg.pojo.ChallengeMessage;
import edu.nagojudge.msg.pojo.ProblemMessage;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author andres.garcia
 */
@ManagedBean
@ViewScoped
public class RoomContentBean implements Serializable {

    @EJB
    private ChallengeProblemDAO challengeProblemFacade;

    private final Logger logger = Logger.getLogger(RoomContentBean.class);

    private List<ProblemMessage> listProblems = new ArrayList<ProblemMessage>();

    private ChallengeMessage challengeMessageView = new ChallengeMessage();

    private Map<Long, String> mapColorsGlogs = new HashMap<Long, String>();
    private Map<Long, String> mapLettersGlobs = new HashMap<Long, String>();

    public RoomContentBean() {
    }

    @PostConstruct
    public void init() {
        HttpSession currentSession = FacesUtil.getFacesUtil().getCurrentSession();
        Long challengeId = (Long) currentSession.getAttribute(IKeysApplication.KEY_SESSION_CHALLENGE_ID);
        listProblems = new ArrayList<ProblemMessage>(challengeProblemFacade.findProblemsByChallenge(challengeId));
        mapColorsGlogs = FacesUtil.getFacesUtil().getCookieMap(IKeysApplication.KEY_COOKIE_GLOBES);
        mapLettersGlobs = FacesUtil.getFacesUtil().getCookieMap(IKeysApplication.KEY_COOKIE_LETTERS);
    }

    public List<ProblemMessage> getListProblems() {
        return listProblems;
    }

    public void setListProblems(List<ProblemMessage> listProblems) {
        this.listProblems = listProblems;
    }

    public ChallengeMessage getChallengeMessageView() {
        return challengeMessageView;
    }

    public void setChallengeMessageView(ChallengeMessage challengeMessageView) {
        this.challengeMessageView = challengeMessageView;
    }

    public Map<Long, String> getMapColorsGlogs() {
        return mapColorsGlogs;
    }

    public void setMapColorsGlogs(Map<Long, String> mapColorsGlogs) {
        this.mapColorsGlogs = mapColorsGlogs;
    }

    public Map<Long, String> getMapLettersGlobs() {
        return mapLettersGlobs;
    }

    public void setMapLettersGlobs(Map<Long, String> mapLettersGlobs) {
        this.mapLettersGlobs = mapLettersGlobs;
    }

    public String getRealPathOfPDF(Long idProblem) {
        String realPath = "none";
        try {
            realPath = challengeProblemFacade.getFullPathProblem(idProblem);
            return realPath;
        } catch (IOException ex) {
            logger.error(ex);
        }
        return realPath;
    }

}
