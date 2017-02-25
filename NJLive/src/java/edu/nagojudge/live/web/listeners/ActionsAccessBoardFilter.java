/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.web.listeners;

import edu.nagojudge.live.web.exceptions.NagoJudgeLiveException;
import edu.nagojudge.live.web.utils.FacesUtil;
import java.io.IOException;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
@WebFilter(filterName = "challengeBoardFilter", urlPatterns = {"/now/challenge/*"})
public class ActionsAccessBoardFilter implements Filter {

    private final Logger logger = Logger.getLogger(ActionsAccessBoardFilter.class);

    public ActionsAccessBoardFilter() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("init - ChallengeBoardFilter");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        NagoJudgeLiveException nagoJudgeException = new NagoJudgeLiveException("Unknown keys, please entry correct URL.");

        final String keysRequired[] = {"key", "id"};
        Map<String, String[]> parameterMap = httpRequest.getParameterMap();
        if (parameterMap == null || parameterMap.isEmpty() || parameterMap.size() > 2) {
            FacesUtil.getFacesUtil().printErrorResponse(nagoJudgeException, response);
        } else {
            for (String value : keysRequired) {
                if (!parameterMap.containsKey(value)) {
                    FacesUtil.getFacesUtil().printErrorResponse(nagoJudgeException, response);
                }
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        logger.info("destroy - ChallengeBoardFilter");
    }

}
