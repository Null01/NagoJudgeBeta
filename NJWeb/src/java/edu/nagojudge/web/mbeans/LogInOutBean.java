/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.web.mbeans;

import edu.nagojudge.app.business.dao.beans.UserFacade;
import edu.nagojudge.app.exceptions.NagoJudgeException;
import edu.nagojudge.app.utils.FacesUtil;
import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
@ManagedBean
@ViewScoped
public class LogInOutBean implements Serializable {

    @EJB
    private UserFacade userFacade;

    private static final Logger logger = Logger.getLogger(LogInOutBean.class);

    private final String TARGET_PATH_LOGOUT = "/go";
    private String username, password;

    /**
     * Creates a new instance of LogInOutBean
     */
    public LogInOutBean() {
    }

    public String actionLoginUser() {
        try {
            userFacade.loginCompleteUser(username, password);
            return "/modules/board/score.xhtml?faces-redirect=true&includeViewParams=false";
        } catch (NagoJudgeException ex) {
            logger.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        }
        return null;
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
