/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.web.mbeans;

import edu.nagojudge.live.business.entity.facade.dao.ChallengeDAO;
import edu.nagojudge.live.business.entity.facade.dao.ProblemDAO;
import edu.nagojudge.live.web.utils.FacesUtil;
import edu.nagojudge.live.web.utils.constants.IKeysApplication;
import edu.nagojudge.msg.pojo.ChallengeMessage;
import edu.nagojudge.msg.pojo.TeamMessage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class SelectRoomBean implements Serializable {

    @EJB
    private ProblemDAO problemDAO;

    @EJB
    private ChallengeDAO challengeDAO;

    private static final Logger logger = Logger.getLogger(SelectRoomBean.class);

    private TeamMessage teamMessageView = new TeamMessage();

    private List<SelectItem> challengeMessageItems = new ArrayList<SelectItem>();
    private ChallengeMessage challengeMessageView = new ChallengeMessage();

    public SelectRoomBean() {
    }

    public String actionRedirectInitConfigBoard() {
        HttpSession currentSession = FacesUtil.getFacesUtil().getCurrentSession();
        currentSession.setAttribute(IKeysApplication.KEY_SESSION_CHALLENGE_ID, challengeMessageView.getIdChallenge());
        currentSession.setAttribute(IKeysApplication.KEY_SESSION_CHALLENGE_NAME, challengeMessageView.getNameChallenge());
        currentSession.setAttribute(IKeysApplication.KEY_SESSION_CHALLENGE_DATE_END, challengeMessageView.getDateEnd());
        List<Object[]> idProblems = problemDAO.findProblemsByChallenge((Long) currentSession.getAttribute(IKeysApplication.KEY_SESSION_CHALLENGE_ID));
        buildResourceScore(idProblems, 'A');
        return "/challenge/score.xhtml?faces-redirect=true&includeViewParams=true";
    }

    public void actionListenerPreRender() {
        boolean validationFailed = FacesUtil.getFacesUtil().isValidationFailed();
        if (validationFailed) {
            logger.error("params [" + teamMessageView.getIdTeam() + "]");
            HttpSession session = FacesUtil.getFacesUtil().getCurrentSession();
            session.invalidate();
            FacesUtil.getFacesUtil().printErrorResponse("Authentication failed, contact administrator.");
        } else {
            logger.debug("params [" + +teamMessageView.getIdTeam() + "]");
            try {
                HttpSession currentSession = FacesUtil.getFacesUtil().getCurrentSession();
                List<ChallengeMessage> challengeMessages = challengeDAO.findChallengeAviableByTeam((Long) currentSession.getAttribute(IKeysApplication.KEY_SESSION_TEAM_ID));
                if (challengeMessages != null) {
                    for (ChallengeMessage challengeMessage : challengeMessages) {
                        challengeMessageItems.add(new SelectItem(challengeMessage, challengeMessage.getNameChallenge()));
                    }
                }
            } catch (Exception ex) {
                logger.error(ex);
            }
        }
    }

    private void buildResourceScore(List<Object[]> idProblems, char key) {
        final String PATH_GLOBES = "/live/now/img/globes/";
        List<String> colors = new ArrayList<String>();
        Map<Long, String> cookieColors = new HashMap<Long, String>();
        Map<Long, String> cookieLetters = new HashMap<Long, String>();
        Map<Long, String> cookieNameProblems = new HashMap<Long, String>();
        switch (key) {
            case '0':
                colors.add("aquamarine.svg");
                colors.add("peru.svg");
                colors.add("brown.svg");
                colors.add("crimson.svg");
                colors.add("darkstlateblue.svg");
                colors.add("green.svg");
                colors.add("black.svg");
                colors.add("blue.svg");
                colors.add("lightpink.svg");
                colors.add("lime.svg");
                colors.add("yellow.svg");
                colors.add("orangered.svg");
                colors.add("purple.svg");
                colors.add("steelblue.svg");
                colors.add("white.svg");
                break;
            case '1':
                colors.add("aquamarine.svg");
                colors.add("black.svg");
                colors.add("blue.svg");
                colors.add("brown.svg");
                colors.add("crimson.svg");
                colors.add("darkstlateblue.svg");
                colors.add("green.svg");
                colors.add("lightpink.svg");
                colors.add("lime.svg");
                colors.add("orangered.svg");
                colors.add("peru.svg");
                colors.add("purple.svg");
                colors.add("steelblue.svg");
                colors.add("white.svg");
                colors.add("yellow.svg");
                break;
            default:
                colors.add("darkstlateblue.svg");
                colors.add("green.svg");
                colors.add("blue.svg");
                colors.add("brown.svg");
                colors.add("crimson.svg");
                colors.add("lightpink.svg");
                colors.add("aquamarine.svg");
                colors.add("black.svg");
                colors.add("lime.svg");
                colors.add("steelblue.svg");
                colors.add("orangered.svg");
                colors.add("peru.svg");
                colors.add("purple.svg");
                colors.add("white.svg");
                colors.add("yellow.svg");
                break;
        }
        for (int i = 0; i < idProblems.size(); i++) {
            cookieColors.put((Long) idProblems.get(i)[0], PATH_GLOBES + colors.get(i));
            cookieLetters.put((Long) idProblems.get(i)[0], String.valueOf((char) ('A' + i)));
            cookieNameProblems.put((Long) idProblems.get(i)[0], (String) idProblems.get(i)[1]);
        }
        FacesUtil.getFacesUtil().addCookie(IKeysApplication.KEY_COOKIE_GLOBES, cookieColors);
        FacesUtil.getFacesUtil().addCookie(IKeysApplication.KEY_COOKIE_LETTERS, cookieLetters);
        FacesUtil.getFacesUtil().addCookie(IKeysApplication.KEY_COOKIE_NAME_PROBLEMS, cookieNameProblems);
    }

    public TeamMessage getTeamMessageView() {
        return teamMessageView;
    }

    public void setTeamMessageView(TeamMessage teamMessageView) {
        this.teamMessageView = teamMessageView;
    }

    public List<SelectItem> getChallengeMessageItems() {
        return challengeMessageItems;
    }

    public void setChallengeMessageItems(List<SelectItem> challengeMessageItems) {
        this.challengeMessageItems = challengeMessageItems;
    }

    public ChallengeMessage getChallengeMessageView() {
        return challengeMessageView;
    }

    public void setChallengeMessageView(ChallengeMessage challengeMessageView) {
        this.challengeMessageView = challengeMessageView;
    }

}
