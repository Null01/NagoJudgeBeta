/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.web.mbeans;

import edu.nagojudge.live.business.entity.facade.dao.ChallengeSubmitDAO;
import edu.nagojudge.live.web.utils.FacesUtil;
import edu.nagojudge.live.web.utils.constants.IKeysApplication;
import edu.nagojudge.msg.pojo.ChallengeMessage;
import edu.nagojudge.msg.pojo.ScoreMessage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
@ManagedBean
@ViewScoped
public class RoomScoreBean implements Serializable {

    @EJB
    private ChallengeSubmitDAO challengeSubmitDAO;

    private static final Logger logger = Logger.getLogger(RoomScoreBean.class);

    private int sizeListScoreTeams;
    private List<ScoreMessage> listScoreTeams = new ArrayList<ScoreMessage>();

    private ChallengeMessage challengeMessageView = new ChallengeMessage();

    private Map<Long, String> mapColorsGlogs = new HashMap<Long, String>();
    private Map<Long, String> mapLettersGlobs = new HashMap<Long, String>();

    public RoomScoreBean() {
    }

    @PostConstruct
    public void init() {
        try {
            Long challengeId = (Long) FacesUtil.getFacesUtil().getAttributeCurrentSession(IKeysApplication.KEY_SESSION_CHALLENGE_ID);
            this.listScoreTeams = new ArrayList<ScoreMessage>(challengeSubmitDAO.calculateScoreByChallenge(challengeId));
            if (listScoreTeams != null && !listScoreTeams.isEmpty()) {
                this.sizeListScoreTeams = listScoreTeams.get(0).getResumeScore().getList().size();
                mapColorsGlogs = FacesUtil.getFacesUtil().getCookieMap(IKeysApplication.KEY_COOKIE_GLOBES);
                mapLettersGlobs = FacesUtil.getFacesUtil().getCookieMap(IKeysApplication.KEY_COOKIE_LETTERS);
            }
        } catch (Exception ex) {
            logger.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        }
    }

    public void actionRefreshScore() {
        try {
            Long challengeId = (Long) FacesUtil.getFacesUtil().getAttributeCurrentSession(IKeysApplication.KEY_SESSION_CHALLENGE_ID);
            this.listScoreTeams = new ArrayList<ScoreMessage>(challengeSubmitDAO.calculateScoreByChallenge(challengeId));
        } catch (Exception ex) {
            logger.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        }
    }

    public int getSizeListScoreTeams() {
        return sizeListScoreTeams;
    }

    public void setSizeListScoreTeams(int sizeListScoreTeams) {
        this.sizeListScoreTeams = sizeListScoreTeams;
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

    public ChallengeMessage getChallengeMessageView() {
        return challengeMessageView;
    }

    public void setChallengeMessageView(ChallengeMessage challengeMessageView) {
        this.challengeMessageView = challengeMessageView;
    }

    public List<ScoreMessage> getListScoreTeams() {
        return listScoreTeams;
    }

    public void setListScoreTeams(List<ScoreMessage> listScoreTeams) {
        this.listScoreTeams = listScoreTeams;
    }

}
