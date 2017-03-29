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

    private final Logger logger = Logger.getLogger(NavegationRoomBean.class);

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
        return "/challenge/submits.xhtml?faces-redirect=true&includeViewParams=true";
    }

    public long getTimeEndingChallenge() {
        try {
            logger.debug("INICIA METODO - getTimeEndingChallenge()");
            Calendar endTimeChallenge = Calendar.getInstance();
            Date currentTime = Calendar.getInstance().getTime();
            HttpSession currentSession = FacesUtil.getFacesUtil().getCurrentSession();
            Date dateStarted = (Date) currentSession.getAttribute(IKeysApplication.KEY_SESSION_CHALLENGE_DATE_END);
            endTimeChallenge.setTimeInMillis(dateStarted.getTime() - currentTime.getTime());
            logger.debug("TIME LEFT [" + endTimeChallenge.getTime() + "]");
            return (60 * 60 * 24 * (endTimeChallenge.get(Calendar.DATE) - 1))
                    + (60 * 60 * endTimeChallenge.get(Calendar.HOUR_OF_DAY))
                    + (60 * (endTimeChallenge.get(Calendar.MINUTE)))
                    + endTimeChallenge.get(Calendar.SECOND);
        } catch (Exception ex) {
            logger.error(ex);
        } finally {
            logger.debug("FINALIZA METODO - getTimeEndingChallenge()");
        }
        return 0;
    }

    public ChallengeMessage getChallengeMessageView() {
        return challengeMessageView;
    }

    public void setChallengeMessageView(ChallengeMessage challengeMessageView) {
        this.challengeMessageView = challengeMessageView;
    }

}
