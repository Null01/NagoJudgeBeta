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

    @PostConstruct
    public void init() {
        logger.debug("-> idChallenge [" + challengeMessageView.getIdChallenge() + "]");
        logger.debug("idChallenge [" + idChallenge + "]");

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
