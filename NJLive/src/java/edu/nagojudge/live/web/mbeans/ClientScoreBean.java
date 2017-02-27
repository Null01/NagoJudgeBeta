/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.web.mbeans;

import edu.nagojudge.live.business.entity.dao.TeamAccountDAO;
import edu.nagojudge.live.web.utils.FacesUtil;
import edu.nagojudge.live.web.utils.constants.IKeysApplication;
import edu.nagojudge.msg.pojo.ChallengeMessage;
import edu.nagojudge.msg.pojo.ScoreMessage;
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
public class ClientScoreBean implements Serializable {

    @EJB
    private TeamAccountDAO teamAccountDAO;

    private final Logger logger = Logger.getLogger(ClientScoreBean.class);

    private List<ScoreMessage> listScoreTeams = new ArrayList<ScoreMessage>();
    private ChallengeMessage challengeMessageView = new ChallengeMessage();

    public ClientScoreBean() {
    }

    @PostConstruct
    public void init() {
        Long challengeId = (Long) FacesUtil.getFacesUtil().getAttributeCurrentSession(IKeysApplication.KEY_SESSION_CHALLENGE_ID);
        listScoreTeams = new ArrayList<ScoreMessage>(teamAccountDAO.findTeamsScoreByChallenge(challengeId));
    }

    public ChallengeMessage getChallengeMessageView() {
        return challengeMessageView;
    }

    public void setChallengeMessageView(ChallengeMessage challengeMessageView) {
        this.challengeMessageView = challengeMessageView;
    }

    public List<ScoreMessage> getListScoreTeams() {
        return listScoreTeams;
    }

    public void setListScoreTeams(List<ScoreMessage> listScoreTeams) {
        this.listScoreTeams = listScoreTeams;
    }

}
