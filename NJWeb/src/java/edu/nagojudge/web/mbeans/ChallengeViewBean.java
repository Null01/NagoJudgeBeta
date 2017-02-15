/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.web.mbeans;

import edu.nagojudge.app.business.dao.beans.ChallengeFacadeDAO;
import edu.nagojudge.app.business.dao.entities.Challenge;
import edu.nagojudge.msg.pojo.ChallengeMessage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
public class ChallengeViewBean implements Serializable {

    private final Logger logger = Logger.getLogger(ChallengeViewBean.class);

    @EJB
    private ChallengeFacadeDAO challengeFacade;

    private List<ChallengeMessage> listChallenges;
    private List<ChallengeMessage> filteredChallenges;
    private String searchParameter;

    private ChallengeMessage challengeMessageView = new ChallengeMessage();

    public ChallengeViewBean() {
    }

    @PostConstruct
    public void init() {
        this.listChallenges = new ArrayList<ChallengeMessage>(this.challengeFacade.getAllChallengeMessage());
    }

    public String actionRedirectViewToBoardChallenge() {
        logger.debug("getIdChallenge [" + challengeMessageView.getIdChallenge() + "]");
        return "/challenge/now.xhtml?faces-redirect=true&includeViewParams=true";
    }

    public ChallengeMessage getChallengeMessageView() {
        return challengeMessageView;
    }

    public void setChallengeMessageView(ChallengeMessage challengeMessageView) {
        this.challengeMessageView = challengeMessageView;
    }

    public List<ChallengeMessage> getListChallenges() {
        return listChallenges;
    }

    public void setListChallenges(List<ChallengeMessage> listChallenges) {
        this.listChallenges = listChallenges;
    }

    public List<ChallengeMessage> getFilteredChallenges() {
        return filteredChallenges;
    }

    public void setFilteredChallenges(List<ChallengeMessage> filteredChallenges) {
        this.filteredChallenges = filteredChallenges;
    }

    public String getSearchParameter() {
        return searchParameter;
    }

    public void setSearchParameter(String searchParameter) {
        this.searchParameter = searchParameter;
    }

}
