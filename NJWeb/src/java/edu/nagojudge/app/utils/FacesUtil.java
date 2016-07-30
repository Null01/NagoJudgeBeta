/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.app.utils;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
public class FacesUtil {

    private final Logger logger = Logger.getLogger(FacesUtil.class);

    private static FacesUtil FacesUtil;

    public static FacesUtil getFacesUtil() {
        if (FacesUtil == null) {
            FacesUtil = new FacesUtil();
        }
        return FacesUtil;
    }

    public void redirect(String path) throws IOException {

        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(path);
        } catch (IOException ex) {
            logger.error(ex);
            throw ex;
        }

    }

    public String getRealPath() {
        return FacesContext.getCurrentInstance().getExternalContext() .getRealPath("/");
    }

    public String getContextPath() {
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        return servletContext.getContextPath();
    }

    public String getRequestURI() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return request.getRequestURI();
    }

    public String getRequestContextPath() {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
    }

    public void addMessage(FacesMessage.Severity severity, String messageBold) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, messageBold, null));
    }

    public Map<String, Object> getRequestMap() {
        Map<String, Object> requestMap = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
        return requestMap;
    }

    public Flash getFlash() {
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        return flash;
    }

    public Map<String, Object> getSessionMap() {
        Map<String, Object> requestMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        return requestMap;
    }

    public HttpSession getSession(boolean state) {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return request.getSession(state);
    }

    public String getResourceBundle(String key, String pathPackage) {
        ResourceBundle text = ResourceBundle.getBundle(pathPackage, Locale.ENGLISH);
        return text.getString(key);
    }

    public String getInitParameter(String key) {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        String initParameter = externalContext.getInitParameter(key);
        return initParameter;
    }

}
