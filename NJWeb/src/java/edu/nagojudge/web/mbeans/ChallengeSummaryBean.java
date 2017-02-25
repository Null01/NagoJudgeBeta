/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.web.mbeans;

import edu.nagojudge.app.business.dao.beans.ChallengeFacade;
import edu.nagojudge.app.business.dao.entities.Challenge;
import edu.nagojudge.app.utils.FacesUtil;
import edu.nagojudge.msg.pojo.ChallengeMessage;
import java.util.Calendar;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
@ManagedBean
@ViewScoped
public class ChallengeSummaryBean {

    @EJB
    private ChallengeFacade challengeFacade;

    private final Logger logger = Logger.getLogger(ChallengeSummaryBean.class);

    private ChallengeMessage challengeMessageView = new ChallengeMessage();

    private String idChallenge;

    public ChallengeSummaryBean() {
    }

    public boolean getActionRenderPanelTimerStatusChallenge() {
        Date currentTime = Calendar.getInstance().getTime();
        return currentTime.after(challengeMessageView.getDateStart()) && currentTime.before(challengeMessageView.getDateEnd());
    }

    public void actionListenerPreRender() {
        boolean validationFailed = FacesUtil.getFacesUtil().isValidationFailed();
        if (validationFailed) {
            logger.error("params {" + this.idChallenge + "}");
            FacesUtil.getFacesUtil().printErrorResponse("Authentication failed, contact administrator.");
        } else {
            Challenge find = challengeFacade.find(idChallenge);
            challengeMessageView = parseChallengeToChallengeMessage(find);
            logger.debug("params [" + challengeMessageView.getIdChallenge() + "]");
        }
    }

    public long getTimeEndingChallenge() {
        Calendar endTimeChallenge = Calendar.getInstance();
        Date currentTime = Calendar.getInstance().getTime();
        endTimeChallenge.setTimeInMillis(challengeMessageView.getDateEnd().getTime() - currentTime.getTime());

        logger.debug("endChallenge: " + challengeMessageView.getDateEnd());
        logger.debug("currentTime: " + currentTime);
        logger.debug("ending a: " + endTimeChallenge.getTime());

        logger.debug(endTimeChallenge.get(Calendar.DATE));

        return (60 * 60 * 24 * (endTimeChallenge.get(Calendar.DATE) - 1))
                + (60 * 60 * endTimeChallenge.get(Calendar.HOUR_OF_DAY))
                + (60 * (endTimeChallenge.get(Calendar.MINUTE)))
                + endTimeChallenge.get(Calendar.SECOND);
    }

    public ChallengeMessage getChallengeMessageView() {
        return challengeMessageView;
    }

    public void setChallengeMessageView(ChallengeMessage challengeMessageView) {
        this.challengeMessageView = challengeMessageView;
    }

    public String getIdChallenge() {
        return idChallenge;
    }

    public void setIdChallenge(String idChallenge) {
        this.idChallenge = idChallenge;
    }

    private ChallengeMessage parseChallengeToChallengeMessage(Challenge find) {
        ChallengeMessage challengeMessage = new ChallengeMessage();
        challengeMessage.setIdChallenge(find.getIdChallenge());
        challengeMessage.setNameChallenge(find.getNameChallenge());
        challengeMessage.setDateStart(find.getDateStart());
        challengeMessage.setDateEnd(find.getDateEnd());
        return challengeMessage;
    }

}
