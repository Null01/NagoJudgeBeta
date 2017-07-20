/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.web.listeners;

import edu.nagojudge.live.web.utils.constants.IKeysApplication;
import java.io.IOException;
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
@WebFilter(filterName = "actionsAccessContestFilter", urlPatterns = {"/*"})
public class ActionsAccessContestFilter implements Filter {

    private static final Logger logger = Logger.getLogger(ActionsAccessContestFilter.class);

    public ActionsAccessContestFilter() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("init - actionsAccessContestFilter");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();
        //String tokens[] = requestURI.split("/");
        final String indexURI = "/live";
        final String selectURI = "/live/now/challenge/select.xhtml";
        final String scoreURI = "/live/now/challenge/score.xhtml";
        final HttpSession session = httpRequest.getSession();
        Object teamId = null, challengeId = null;
        if (session != null) {
            teamId = session.getAttribute(IKeysApplication.KEY_SESSION_TEAM_ID);
            challengeId = session.getAttribute(IKeysApplication.KEY_SESSION_CHALLENGE_ID);
        }

        //logger.debug("requestURI [" + requestURI + "]");
        if (teamId == null && challengeId == null) {
            if (requestURI.endsWith(".xhtml")) { // paginas que no perteneces a la aplicacion.
                //boolean isValidPage = requestURI.endsWith("content.xhtml") || requestURI.endsWith("contest.xhtml")
                //        || requestURI.endsWith("score.xhtml") || requestURI.endsWith("select.xhtml") || requestURI.endsWith("submit.xhtml") || requestURI.endsWith("signin.xhtml");
                if (requestURI.endsWith("signin.xhtml")) {
                    chain.doFilter(request, response);
                } else {
                    httpResponse.sendRedirect(indexURI);
                }
            } else {
                chain.doFilter(request, response);
            }
        } else {
            if (teamId != null && challengeId != null) {
                if (requestURI.endsWith("/live/") || requestURI.endsWith("signin.xhtml") || requestURI.endsWith("select.xhtml")) {
                    httpResponse.sendRedirect(scoreURI);
                } else {
                    chain.doFilter(request, response);
                }
            } else {
                if (challengeId == null && requestURI.endsWith(".xhtml") && !requestURI.endsWith(selectURI)) {
                    httpResponse.sendRedirect(selectURI);
                } else {
                    chain.doFilter(request, response);
                }
            }
        }

    }

    @Override
    public void destroy() {
        logger.info("destroy - actionsAccessContestFilter");
    }

}
