/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.web.mbeans;

import edu.nagojudge.live.business.entity.dao.ChallengeDAO;
import edu.nagojudge.live.web.utils.FacesUtil;
import edu.nagojudge.msg.pojo.ChallengeMessage;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
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
public class ClientMonitorBoardBean implements Serializable {
    
    @EJB
    private ChallengeDAO challengeDAO;
    
    private final Logger logger = Logger.getLogger(ClientMonitorBoardBean.class);
    
    private ChallengeMessage challengeMessageView = new ChallengeMessage();
    
    public ClientMonitorBoardBean() {
    }
    
    public String actionRedirectContentBoard() {
        return "/challenge/content.xhtml?faces-redirect=true&includeViewParams=true";
    }
    
    public String actionRedirectScoreBoard() {
        return "/challenge/score.xhtml?faces-redirect=true&includeViewParams=true";
    }
    /*
    public void actionListenerPreRenderPage() {
        boolean validationFailed = FacesUtil.getFacesUtil().isValidationFailed();
        if (validationFailed) {
            logger.error("params {" + challengeMessageView.getIdChallenge() + "}");
            FacesUtil.getFacesUtil().printErrorResponse("Authentication failed, contact administrator.");
        } else {
            logger.debug("actionListenerPreRenderPage()");
            challengeMessageView = this.challengeDAO.findChallengeById(challengeMessageView.getIdChallenge());
        }
    }
    */
   
    
    public long getTimeEndingChallenge() {
        try {
            logger.debug("INICIA METODO - getTimeEndingChallenge()");
            Calendar endTimeChallenge = Calendar.getInstance();
            Date currentTime = Calendar.getInstance().getTime();
            logger.debug(challengeMessageView.getIdChallenge());
            logger.debug(challengeMessageView.getDateEnd());
            endTimeChallenge.setTimeInMillis(challengeMessageView.getDateEnd().getTime() - currentTime.getTime());
            logger.debug("OFFSET TIME [" + endTimeChallenge.getTime() + "]");
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
