/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.web.utils;

import edu.nagojudge.live.web.exceptions.NagoJudgeLiveException;
import edu.nagojudge.live.web.utils.constants.IKeysApplication;
import edu.nagojudge.msg.pojo.MetadataMessage;
import edu.nagojudge.msg.pojo.constants.TypeRoleEnum;
import edu.nagojudge.tools.utils.FileUtil;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.servlet.ServletContext;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.primefaces.context.RequestContext;

/**
 *
 * @author andresfelipegarciaduran
 */
public class FacesUtil {

    private static final Logger logger = Logger.getLogger(FacesUtil.class);

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

    public Map<String, String[]> getRequestHeaderValuesMap() {
        Map<String, String[]> requestHeaderValuesMap = FacesContext.getCurrentInstance().getExternalContext().getRequestHeaderValuesMap();
        return requestHeaderValuesMap;
    }

    public Map<String, String[]> getRequestParameterValuesMap() {
        Map<String, String[]> requestHeaderValuesMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterValuesMap();
        return requestHeaderValuesMap;
    }

    public Map<String, Object> getRequestCookieMap() {
        Map<String, Object> requestHeaderValuesMap = FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap();
        return requestHeaderValuesMap;
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

    public void execute(String cmd) {
        RequestContext currentInstance = RequestContext.getCurrentInstance();
        currentInstance.execute(cmd);
    }

    public String getResourceBundle(String key, String basename) {
        ResourceBundle text = ResourceBundle.getBundle(basename, FacesContext.getCurrentInstance().getViewRoot().getLocale());
        return text.getString(key);
    }

    public String getInitParameter(String key) throws NullPointerException {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        String initParameter = externalContext.getInitParameter(key);
        return initParameter;
    }

    public String getParameterWEBINF(String keyFileWEBINF, String key) {
        String outcome = null;
        try {
            String initParameter = getInitParameter(keyFileWEBINF);
            Properties loadFileProperties = FileUtil.getInstance().loadFileProperties(getFacesUtil().getRealPath() + File.separator + initParameter);
            outcome = loadFileProperties.getProperty(key);
        } catch (IOException ex) {
            logger.error(ex);
        }
        return outcome;
    }

    public boolean isValidationFailed() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return !facesContext.isPostback() && facesContext.isValidationFailed();
    }

    public void addCookie(String key, Object value) {
        try {
            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            ObjectMapper mapper = new ObjectMapper();
            String valueJson = mapper.writeValueAsString(value);
            logger.debug(valueJson);
            Cookie cookie = new Cookie(key, valueJson);
            response.addCookie(cookie);
        } catch (IOException ex) {
            logger.error(ex);
        }
    }

    public Map<Long, String> getCookieMap(String key) {
        Map<Long, String> outcome = new HashMap<Long, String>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> requestCookieMap = getRequestCookieMap();
            Cookie cookie = (Cookie) requestCookieMap.get(key);
            outcome = mapper.readValue(cookie.getValue(), new TypeReference<Map<Long, String>>() {
            });
        } catch (IOException ex) {
            logger.error(ex);
        }
        return outcome;
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
                            + "  <link rel=\"stylesheet\" href=\"/live/now/tools/frameworks/bootstrap/css/bootstrap.min.css\"/>\n"
                            + "  <link rel=\"icon\" type=\"image/ico\" href=\"/live/now/img/logo-icon.png\"/>"
                            + "  <title>Nago Judge</title>"
                            + "</head>"
                            + "<body>"
                            + "<div class=\"container\">\n"
                            + "    <div class=\"row\">\n"
                            + "        <div class=\"col-md-12 text-center\">\n"
                            + "            <div class=\"error-template\">\n"
                            + "                <div class=\"error-details\">\n"
                            + "<h1> " + messageThrowable + "</h1>\n"
                            + "                </div>\n"
                            + "                <div class=\"error-actions\">\n"
                            + "                    <a href=\"/live/now/signin.xhtml\" class=\"btn btn-primary btn-lg\"><span class=\"glyphicon glyphicon-home\"></span>\n Return to home </a>"
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

    public void downloadFile(InputStream stream, final String extension) {

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.responseReset();
        externalContext.setResponseContentType(MediaType.APPLICATION_OCTET_STREAM);
        externalContext.setResponseContentLength(1024 * 8);
        externalContext.setResponseHeader("Content-Disposition", "attachment; filename=main." + ((extension != null) ? extension : "txt"));
        OutputStream out = null;
        try {
            byte[] buffer = new byte[1024 * 8];
            out = externalContext.getResponseOutputStream();
            int i;
            while ((i = stream.read(buffer)) != -1) {
                out.write(buffer);
                out.flush();
            }
            FacesContext.getCurrentInstance().getResponseComplete();
        } catch (IOException ex) {
            logger.error(ex);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    logger.error(ex);
                }
            }
        }

    }

    public Map<String, Object> getMetadataRest(final String userId) {
        Map<String, Object> metadataMap = new HashMap<String, Object>();
        metadataMap.put("token", "asd");
        metadataMap.put("i18", getParameterWEBINF("init-config", "judge.nagojudge.i18"));
        metadataMap.put("role", TypeRoleEnum.TEAM);
        metadataMap.put("user", userId);
        return metadataMap;
    }

}
