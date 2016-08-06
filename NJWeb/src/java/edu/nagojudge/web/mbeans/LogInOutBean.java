/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.web.mbeans;

import edu.nagojudge.app.business.dao.complex.UserFacadeComplex;
import edu.nagojudge.app.exceptions.NagoJudgeException;
import edu.nagojudge.app.utils.FacesUtil;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import javax.ejb.EJB;
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
public class LogInOutBean implements Serializable {

    @EJB
    private UserFacadeComplex userDaoComplex;

    private final Logger logger = Logger.getLogger(LogInOutBean.class);

    private String username = "agarciad1@ucentral.edu.co", password;

    private final String TARGET_PATH_LOGIN = "/go/to/home.xhtml";
    private final String TARGET_PATH_LOGOUT = "/go/";

    /**
     * Creates a new instance of LogInOutBean
     */
    public LogInOutBean() {
    }

    public void actionLoginUser() {
        try {
            userDaoComplex.loginCompleteUser(username, password);
            FacesUtil.getFacesUtil().redirect(TARGET_PATH_LOGIN);
        } catch (NagoJudgeException ex) {
            logger.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        } catch (IOException ex) {
            logger.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        }
    }

    public void actionLogoutUser() {
        try {
            HttpSession session = FacesUtil.getFacesUtil().getSession(true);
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
