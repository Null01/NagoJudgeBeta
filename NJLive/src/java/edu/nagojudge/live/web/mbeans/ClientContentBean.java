/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.web.mbeans;

import edu.nagojudge.live.business.entity.dao.ChallengeProblemDAO;
import edu.nagojudge.live.web.utils.FacesUtil;
import edu.nagojudge.msg.pojo.ChallengeMessage;
import edu.nagojudge.msg.pojo.ProblemMessage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;

/**
 *
 * @author andres.garcia
 */
@ManagedBean
@ViewScoped
public class ClientContentBean implements Serializable {

    @EJB
    private ChallengeProblemDAO challengeProblemFacade;

    private final Logger logger = Logger.getLogger(ClientContentBean.class);

    private List<ProblemMessage> listProblems = new ArrayList<ProblemMessage>();

    private ChallengeMessage challengeMessageView = new ChallengeMessage();

    public ClientContentBean() {
    }

    public void actionListenerPreRenderPage() {
        boolean validationFailed = FacesUtil.getFacesUtil().isValidationFailed();
        if (validationFailed) {
            logger.error("params {" + challengeMessageView.getIdChallenge() + "}");
            FacesUtil.getFacesUtil().printErrorResponse("Authentication failed, contact administrator.");
        } else {
            this.listProblems = new ArrayList<ProblemMessage>(challengeProblemFacade.findProblemsByChallenge(challengeMessageView.getIdChallenge()));
        }
    }

    public List<ProblemMessage> getListProblems() {
        return listProblems;
    }

    public void setListProblems(List<ProblemMessage> listProblems) {
        this.listProblems = listProblems;
    }

    public ChallengeMessage getChallengeMessageView() {
        return challengeMessageView;
    }

    public void setChallengeMessageView(ChallengeMessage challengeMessageView) {
        this.challengeMessageView = challengeMessageView;
    }

}
