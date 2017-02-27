/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.web.mbeans;

import edu.nagojudge.live.web.utils.FacesUtil;
import edu.nagojudge.msg.pojo.ChallengeMessage;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
@ManagedBean
@ViewScoped
public class ClientScoreBean implements Serializable {

    private final Logger logger = Logger.getLogger(ClientScoreBean.class);

    private ChallengeMessage challengeMessageView = new ChallengeMessage();

    public ClientScoreBean() {
    }

    public void actionListenerPreRenderPage() {
        boolean validationFailed = FacesUtil.getFacesUtil().isValidationFailed();
        if (validationFailed) {
            logger.error("params {" + challengeMessageView.getIdChallenge() + "}");
            FacesUtil.getFacesUtil().printErrorResponse("Authentication failed, contact administrator.");
        }else{
            
        }
    }

    public ChallengeMessage getChallengeMessageView() {
        return challengeMessageView;
    }

    public void setChallengeMessageView(ChallengeMessage challengeMessageView) {
        this.challengeMessageView = challengeMessageView;
    }

}
