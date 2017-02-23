/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.web.mbeans;

import edu.nagojudge.live.web.utils.FacesUtil;
import edu.nagojudge.msg.pojo.ChallengeMessage;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
@ManagedBean
@ViewScoped
public class ConfigChallengeBean implements Serializable {

    private final Logger logger = Logger.getLogger(ConfigChallengeBean.class);

    private final String TARGET_PATH_NEXT_STEP_CONFIG = "/now/challenge/begins.xhtml";

    private List<SelectItem> challengeMessageItems = new ArrayList<SelectItem>();
    private ChallengeMessage challengeMessageView = new ChallengeMessage();

    public ConfigChallengeBean() {
    }

    @PostConstruct
    public void init() {
        ChallengeMessage challengeMessage = new ChallengeMessage();
        challengeMessage.setIdChallenge(new Long("1"));
        challengeMessage.setNameChallenge("Competencia Prueba I");
        challengeMessageItems.add(new SelectItem(challengeMessage, challengeMessage.getNameChallenge()));

        challengeMessage = new ChallengeMessage();
        challengeMessage.setIdChallenge(new Long("2"));
        challengeMessage.setNameChallenge("Competencia Prueba II");
        challengeMessageItems.add(new SelectItem(challengeMessage, challengeMessage.getNameChallenge()));
    }

    public void actionInitConfigBoard(ActionEvent event) {
        try {
            logger.debug("actionInitConfigBoard");
            FacesUtil.getFacesUtil().redirect(TARGET_PATH_NEXT_STEP_CONFIG);
        } catch (IOException ex) {
            logger.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        }
    }

    public ChallengeMessage getChallengeMessageView() {
        return challengeMessageView;
    }

    public void setChallengeMessageView(ChallengeMessage challengeMessageView) {
        this.challengeMessageView = challengeMessageView;
    }

    public List<SelectItem> getChallengeMessageItems() {
        return challengeMessageItems;
    }

    public void setChallengeMessageItems(List<SelectItem> challengeMessageItems) {
        this.challengeMessageItems = challengeMessageItems;
    }

}
