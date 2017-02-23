/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.web.mbeans;

import edu.nagojudge.msg.pojo.ChallengeMessage;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author andresfelipegarciaduran
 */
@ManagedBean
@ViewScoped
public class ConfigChallengeBean {

    private List<ChallengeMessage> listChallengeAvailable = new ArrayList<ChallengeMessage>();
    private ChallengeMessage challengeMessageView = new ChallengeMessage();

    public ConfigChallengeBean() {
    }

    public List<ChallengeMessage> getListChallengeAvailable() {
        return listChallengeAvailable;
    }

    public void setListChallengeAvailable(List<ChallengeMessage> listChallengeAvailable) {
        this.listChallengeAvailable = listChallengeAvailable;
    }

    public ChallengeMessage getChallengeMessageView() {
        return challengeMessageView;
    }

    public void setChallengeMessageView(ChallengeMessage challengeMessageView) {
        this.challengeMessageView = challengeMessageView;
    }

}
