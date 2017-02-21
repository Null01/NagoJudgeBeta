/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.web.mbeans;

import edu.nagojudge.app.business.dao.beans.EmailFacade;
import edu.nagojudge.app.exceptions.NagoJudgeException;
import edu.nagojudge.app.utils.FacesUtil;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.mail.MessagingException;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
@ManagedBean
@ViewScoped
public class SettingsTestingBean {

    @EJB
    private EmailFacade emailFacade;

    private final Logger logger = Logger.getLogger(SettingsTestingBean.class);

    private String sendTo, sendSubject, sendComposite = "";

    public SettingsTestingBean() {
    }

    public void actionSendEmail() {
        final String MESSAGE_SUCCESSFUL = "Correo Electronico enviado exitosamente.";
        try {
            logger.debug("sendComposite [" + sendComposite + "]");
            emailFacade.sendEmail(sendTo, sendSubject, new StringBuilder(sendComposite));
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_INFO, MESSAGE_SUCCESSFUL);
        } catch (NagoJudgeException ex) {
            logger.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        } catch (MessagingException ex) {
            logger.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        } catch (Exception ex) {
            logger.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        } finally {
            sendTo = sendSubject = sendComposite = "";
        }
    }

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public String getSendSubject() {
        return sendSubject;
    }

    public void setSendSubject(String sendSubject) {
        this.sendSubject = sendSubject;
    }

    public String getSendComposite() {
        return sendComposite
                + "<br/><br/>"
                + "--------------------<br/>"
                + "#NagoTeam.";
    }

    public void setSendComposite(String sendComposite) {
        this.sendComposite = sendComposite;
    }

}
