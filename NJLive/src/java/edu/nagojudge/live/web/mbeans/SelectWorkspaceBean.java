/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.web.mbeans;

import edu.nagojudge.live.business.entity.facade.dao.ChallengeDAO;
import edu.nagojudge.live.web.utils.FacesUtil;
import edu.nagojudge.live.web.utils.constants.IKeysApplication;
import edu.nagojudge.msg.pojo.ChallengeMessage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
@ManagedBean
@ViewScoped
public class SelectWorkspaceBean implements Serializable {

    @EJB
    private ChallengeDAO challengeDAO;

    private final Logger logger = Logger.getLogger(SelectWorkspaceBean.class);

    private List<SelectItem> challengeMessageItems = new ArrayList<SelectItem>();
    private ChallengeMessage challengeMessageView = new ChallengeMessage();

    public SelectWorkspaceBean() {
    }

    @PostConstruct
    public void init() {
        HttpSession currentSession = FacesUtil.getFacesUtil().getCurrentSession();
        List<ChallengeMessage> challengeMessages = challengeDAO.findChallengeAviableByUser((String) currentSession.getAttribute(IKeysApplication.KEY_SESSION_USER_EMAIL));
        for (ChallengeMessage challengeMessage : challengeMessages) {
            challengeMessageItems.add(new SelectItem(challengeMessage, challengeMessage.getNameChallenge()));
        }
    }

    public String actionRedirectInitConfigBoard() {
        HttpSession currentSession = FacesUtil.getFacesUtil().getCurrentSession();
        currentSession.setAttribute(IKeysApplication.KEY_SESSION_CHALLENGE_ID, challengeMessageView.getIdChallenge());
        currentSession.setAttribute(IKeysApplication.KEY_SESSION_CHALLENGE_DATE_END, challengeMessageView.getDateEnd());
        return "/challenge/content.xhtml?faces-redirect=true&includeViewParams=true";
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
