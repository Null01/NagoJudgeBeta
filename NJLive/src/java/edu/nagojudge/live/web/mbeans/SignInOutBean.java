/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.web.mbeans;

import edu.nagojudge.live.business.entity.facade.dao.TeamDAO;
import edu.nagojudge.live.web.exceptions.NagoJudgeLiveException;
import edu.nagojudge.live.web.utils.FacesUtil;
import edu.nagojudge.msg.pojo.TeamMessage;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
@ManagedBean
@ViewScoped
public class SignInOutBean implements Serializable {

    @EJB
    private TeamDAO teamDAO;

    private final Logger logger = Logger.getLogger(SignInOutBean.class);

    private TeamMessage teamMessage = new TeamMessage();

    private final String TARGET_PATH_NEXT_STEP_LOGOUT = "/";

    public SignInOutBean() {
    }

    @PostConstruct
    public void init() {
        teamMessage.setNameTeam("teamyellow");
    }

    public String actionSignInSession() {
        try {
            teamMessage.setIdTeam(teamDAO.findTeamAvailable(teamMessage.getNameTeam(), teamMessage.getPasswordTeam()));
            return "/challenge/select.xhtml?faces-redirect=true&includeViewParams=true";
        } catch (NagoJudgeLiveException ex) {
            logger.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        } catch (Exception ex) {
            logger.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        }
        return null;
    }

    public void actionSignOutSession(ActionEvent event) {
        try {
            HttpSession session = FacesUtil.getFacesUtil().getCurrentSession();
            session.invalidate();
            FacesUtil.getFacesUtil().redirect(TARGET_PATH_NEXT_STEP_LOGOUT);
        } catch (IOException ex) {
            logger.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        }
    }

    public TeamMessage getTeamMessage() {
        return teamMessage;
    }

    public void setTeamMessage(TeamMessage teamMessage) {
        this.teamMessage = teamMessage;
    }

}
