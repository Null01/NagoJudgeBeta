/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.web.mbeans;

import edu.nagojudge.live.web.utils.FacesUtil;
import edu.nagojudge.live.web.utils.constants.IKeysApplication;
import edu.nagojudge.msg.pojo.ChallengeMessage;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
@ManagedBean
@ViewScoped
public class NavegationRoomBean implements Serializable {

    private static final Logger logger = Logger.getLogger(NavegationRoomBean.class);

    private ChallengeMessage challengeMessageView = new ChallengeMessage();

    public NavegationRoomBean() {
    }

    public String actionRedirectContentBoard() {
        return "/challenge/content.xhtml?faces-redirect=true&includeViewParams=true";
    }

    public String actionRedirectScoreBoard() {
        return "/challenge/score.xhtml?faces-redirect=true&includeViewParams=true";
    }

    public String actionRedirectSubmitsBoard() {
        return "/challenge/submit.xhtml?faces-redirect=true&includeViewParams=true";
    }

    public String actionRedirectContestLiveBoard() {
        return "/challenge/contest.xhtml?faces-redirect=true&includeViewParams=true";
    }

    public long getTimeEndingChallenge() {
        try {
            logger.debug("INICIA METODO - getTimeEndingChallenge()");
            Date currentTime = Calendar.getInstance().getTime();
            HttpSession currentSession = FacesUtil.getFacesUtil().getCurrentSession();
            Long dateEnding = (Long) currentSession.getAttribute(IKeysApplication.KEY_SESSION_CHALLENGE_DATE_END);
            logger.debug("dateEnding [" + new Date(dateEnding) + "]");
            logger.debug("currentTime [" + new Date(currentTime.getTime()) + "]");
            return TimeUnit.MILLISECONDS.toSeconds(dateEnding - currentTime.getTime());
        } catch (Exception ex) {
            logger.error(ex);
        } finally {
            logger.debug("FINALIZA METODO - getTimeEndingChallenge()");
        }
        return 0;
    }

    public boolean getShowDaysInTimer() {
        Date currentTime = Calendar.getInstance().getTime();
        HttpSession currentSession = FacesUtil.getFacesUtil().getCurrentSession();
        Long dateEnding = (Long) currentSession.getAttribute(IKeysApplication.KEY_SESSION_CHALLENGE_DATE_END);
        return TimeUnit.MILLISECONDS.toDays(dateEnding - currentTime.getTime()) != 0;
    }

    public ChallengeMessage getChallengeMessageView() {
        return challengeMessageView;
    }

    public void setChallengeMessageView(ChallengeMessage challengeMessageView) {
        this.challengeMessageView = challengeMessageView;
    }

    public String getNameTeamInSession() {
        Object nameTeam = FacesUtil.getFacesUtil().getSessionMap().get(IKeysApplication.KEY_SESSION_TEAM_NAME);
        return String.valueOf(nameTeam);
    }

    public String getNameChallengeInSession() {
        Object nameChallenge = FacesUtil.getFacesUtil().getSessionMap().get(IKeysApplication.KEY_SESSION_CHALLENGE_NAME);
        if (nameChallenge != null) {
            return String.valueOf(nameChallenge);
        }
        return "";
    }

}
