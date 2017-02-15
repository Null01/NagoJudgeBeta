/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.web.mbeans;

import edu.nagojudge.msg.pojo.ChallengeMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author andresfelipegarciaduran
 */
@ManagedBean
@ViewScoped
public class ChallengeBoardBean {

    private ChallengeMessage challengeMessageView = new ChallengeMessage();

    public ChallengeBoardBean() {
    }

    public ChallengeMessage getChallengeMessageView() {
        return challengeMessageView;
    }

    public void setChallengeMessageView(ChallengeMessage challengeMessageView) {
        this.challengeMessageView = challengeMessageView;
    }

}
