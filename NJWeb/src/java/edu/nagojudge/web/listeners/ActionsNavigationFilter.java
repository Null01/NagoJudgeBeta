/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.web.listeners;

import edu.nagojudge.app.utils.constants.IKeysApplication;
import java.io.IOException;
import java.util.Arrays;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
@WebFilter(filterName = "actionsNavigationFilter", urlPatterns = {"/*"})
public class ActionsNavigationFilter implements Filter {

    private final Logger logger = Logger.getLogger(ActionsNavigationFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("init - ActionsNavigationFilter");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();
        String tokens[] = requestURI.split("/");
        HttpSession session = httpRequest.getSession(false);
        String indexURI = "/go/";

        boolean createSession = false;
        if (session != null) {
            Object object = session.getAttribute(IKeysApplication.KEY_DATA_USER_EMAIL);
            if (object != null) {
                createSession = true;
            }
        }

        if (tokens[tokens.length - 1].endsWith(".xhtml")) {
            if (!createSession) {
                httpResponse.sendRedirect(indexURI);
            }
            chain.doFilter(request, response);
        } else {
            chain.doFilter(request, response);
        }

    }

    @Override
    public void destroy() {
        logger.info("destroy - ActionsNavigationFilter");
    }

}
