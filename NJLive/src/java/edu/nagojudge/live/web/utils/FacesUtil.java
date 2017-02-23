/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.web.utils;

import edu.nagojudge.live.web.exceptions.NagoJudgeLiveException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Map;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.servlet.ServletContext;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        return FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
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

    public HttpSession getCurrentSession() {
        HttpServletRequest request = getRequest();
        return request.getSession();
    }

    public HttpSession getSession(boolean state) {
        HttpServletRequest request = getRequest();
        return request.getSession(state);
    }

    public Object getAttributeCurrentSession(String attribute) {
        return getCurrentSession().getAttribute(attribute);
    }

    public HttpServletRequest getRequest() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return request;
    }

    public HttpServletResponse getResponse() {
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        return response;
    }

    public String getResourceBundle(String key, String basename) {
        ResourceBundle text = ResourceBundle.getBundle(basename, FacesContext.getCurrentInstance().getViewRoot().getLocale());
        return text.getString(key);
    }

    public String getInitParameter(String key) {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        String initParameter = externalContext.getInitParameter(key);
        return initParameter;
    }

    public boolean isValidationFailed() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.isPostback() && facesContext.isValidationFailed();
    }

    public void printErrorResponse(String message) {
        NagoJudgeLiveException nagoJudgeException = new NagoJudgeLiveException(message);
        sendProcessingError(nagoJudgeException, getResponse());
    }

    public void printErrorResponse(Throwable throwable, ServletResponse response) {
        sendProcessingError(throwable, response);
    }

    private void sendProcessingError(Throwable t, ServletResponse response) {
        try {
            logger.debug("INICIA METODO - sendProcessingError()");
            String messageThrowable = t.getMessage();
            if (messageThrowable != null && !messageThrowable.equals("")) {
                PrintStream ps = null;
                PrintWriter pw = null;
                try {
                    ps = new PrintStream(response.getOutputStream());
                    pw = new PrintWriter(ps);
                    response.setContentType("text/html");
                    StringBuilder html = new StringBuilder();
                    html.append("<html>"
                            + "<head>"
                            + "  <meta content='text/html; charset=UTF-8' http-equiv=\"Content-Type\"/>\n"
                            + "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"/>\n"
                            + "  <link rel=\"stylesheet\" href=\"/go/to/tools/frameworks/bootstrap/css/lumen/bootstrap.min.css\"/>\n"
                            + "  <script src=\"/go/to/tools/frameworks/bootstrap/js/bootstrap.min.js\"></script>\n"
                            + "  <link rel=\"icon\" type=\"image/ico\" href=\"/go/to/img/logo-icon.png\"/>"
                            + "  <title>Nago Judge</title>"
                            + "</head>"
                            + "<body>"
                            + "<div class=\"container\">\n"
                            + "    <div class=\"row\">\n"
                            + "        <div class=\"col-md-12\">\n"
                            + "            <div class=\"error-template\">\n"
                            + "                <div class=\"error-details\">\n"
                            + "<h1> " + messageThrowable + "</h1>\n"
                            + "                </div>\n"
                            + "                <div class=\"error-actions\">\n"
                            + "                    <a href=\"/go/to/home.xhtml\" class=\"btn btn-primary btn-lg\"><span class=\"glyphicon glyphicon-home\"></span>\n Return to home </a>"
                            + "                </div>\n"
                            + "            </div>\n"
                            + "        </div>\n"
                            + "    </div>\n"
                            + "</div>"
                            + "</body>"
                            + "</html>");
                    pw.print(html.toString());
                    pw.close();
                    ps.close();
                    response.getOutputStream().close();
                } catch (IOException ex) {
                    logger.error(ex);
                }
            } else {
                PrintStream ps = null;
                try {
                    ps = new PrintStream(response.getOutputStream());
                    t.printStackTrace(ps);
                    response.getOutputStream().close();
                } catch (IOException ex) {
                    logger.error(ex);
                } finally {
                    if (ps != null) {
                        ps.close();
                    }
                }
            }

        } finally {
            logger.debug("FINALIZA METODO - sendProcessingError()");
        }

    }

}
