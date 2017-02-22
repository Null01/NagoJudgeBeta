/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.web.mbeans.clientboard;

import edu.nagojudge.app.exceptions.NagoJudgeLiveException;
import edu.nagojudge.app.utils.FacesUtil;
import java.io.IOException;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
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

    private String username, password;

    private final String TARGET_PATH_LOGIN = "/go/to/home.xhtml";
    private final String TARGET_PATH_LOGOUT = "/go/";

    public SignInOutBean() {
    }

    public void actionLoginUser() {
        try {
            FacesUtil.getFacesUtil().redirect(TARGET_PATH_LOGIN);
        } catch (IOException ex) {
            logger.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        }
    }

    public void actionLogoutUser() {
        try {
            HttpSession session = FacesUtil.getFacesUtil().getCurrentSession();
            session.invalidate();
            FacesUtil.getFacesUtil().redirect(TARGET_PATH_LOGOUT);
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
