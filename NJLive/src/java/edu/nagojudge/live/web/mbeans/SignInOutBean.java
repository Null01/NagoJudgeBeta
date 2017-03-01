/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.web.mbeans;

import edu.nagojudge.live.web.utils.FacesUtil;
import edu.nagojudge.live.web.utils.constants.IKeysApplication;
import java.io.IOException;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
@ManagedBean
@RequestScoped
public class SignInOutBean implements Serializable {

    private final Logger logger = Logger.getLogger(SignInOutBean.class);

    private String username = "agarciad1@ucentral.edu.co", password;

    private final String TARGET_PATH_NEXT_STEP_LOGIN = "/now/challenge/select.xhtml";
    private final String TARGET_PATH_NEXT_STEP_LOGOUT = "/";

    public SignInOutBean() {
    }

    public void actionSignInSession(ActionEvent event) {
        try {
            HttpSession session = FacesUtil.getFacesUtil().getSession(true);
            session.setAttribute(IKeysApplication.KEY_SESSION_USER_EMAIL, username);
            FacesUtil.getFacesUtil().redirect(TARGET_PATH_NEXT_STEP_LOGIN);
        } catch (IOException ex) {
            logger.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        }
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
