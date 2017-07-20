/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.web.mbeans;

import edu.nagojudge.live.business.entity.facade.dao.ChallengeSubmitDAO;
import edu.nagojudge.live.web.utils.FacesUtil;
import edu.nagojudge.live.web.utils.clients.NotifyPushClientService;
import edu.nagojudge.live.web.utils.constants.IKeysApplication;
import edu.nagojudge.live.web.utils.constants.IResourcesPath;
import edu.nagojudge.msg.pojo.JudgeMessage;
import edu.nagojudge.msg.pojo.ProblemMessage;
import edu.nagojudge.msg.pojo.SubmitMessage;
import edu.nagojudge.msg.pojo.TeamMessage;
import edu.nagojudge.msg.pojo.constants.TypeActionEnum;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.primefaces.push.EventBusFactory;

/**
 *
 * @author andres.garcia
 */
@ManagedBean
@ViewScoped
public class RoomContestLiveBean implements Serializable {

    @EJB
    private ChallengeSubmitDAO challengeSubmitDAO;

    private static final Logger logger = Logger.getLogger(RoomContestLiveBean.class);

    private List<SubmitMessage> listSubmits = new ArrayList<SubmitMessage>();

    private Map<Long, String> mapLettersGlobs = new HashMap<Long, String>();

    public RoomContestLiveBean() {
    }

    @PostConstruct
    public void init() {
        try {
            HttpSession currentSession = FacesUtil.getFacesUtil().getCurrentSession();
            Long challengeId = (Long) currentSession.getAttribute(IKeysApplication.KEY_SESSION_CHALLENGE_ID);
            listSubmits = new ArrayList<SubmitMessage>(challengeSubmitDAO.findAllSubmitByChallenge(challengeId));
            mapLettersGlobs = FacesUtil.getFacesUtil().getCookieMap(IKeysApplication.KEY_COOKIE_LETTERS);
        } catch (Exception ex) {
            logger.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        }
    }

    public String getChannelContestLive() {
        HttpSession currentSession = FacesUtil.getFacesUtil().getCurrentSession();
        Long challengeId = (Long) currentSession.getAttribute(IKeysApplication.KEY_SESSION_CHALLENGE_ID);
        return IResourcesPath.ENDPOINT_CONTEST_LIVE + File.separatorChar + String.valueOf(challengeId);
    }

    public List<SubmitMessage> getListSubmits() {
        return listSubmits;
    }

    public void setListSubmits(List<SubmitMessage> listSubmits) {
        this.listSubmits = listSubmits;
    }

    public Map<Long, String> getMapLettersGlobs() {
        return mapLettersGlobs;
    }

    public void setMapLettersGlobs(Map<Long, String> mapLettersGlobs) {
        this.mapLettersGlobs = mapLettersGlobs;
    }

}
