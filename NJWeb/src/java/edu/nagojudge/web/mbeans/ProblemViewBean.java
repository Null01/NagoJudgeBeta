/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.web.mbeans;

import edu.nagojudge.app.business.dao.beans.ProblemFacade;
import edu.nagojudge.msg.pojo.AccountMessage;
import edu.nagojudge.msg.pojo.ProblemMessage;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;

/**
 *
 * @author andres.garcia
 */
@ManagedBean
@ViewScoped
public class ProblemViewBean implements Serializable {

    private final Logger logger = Logger.getLogger(ProblemViewBean.class);

    @EJB
    private ProblemFacade problemFacade;

    private List<ProblemMessage> listProblems;
    private List<ProblemMessage> filteredProblems;
    private String searchParameter;

    private ProblemMessage problemMessage = new ProblemMessage();
    private AccountMessage accountMessage = new AccountMessage();

    public ProblemViewBean() {
    }

    @PostConstruct
    public void init() {
        if (problemMessage.getIdProblem() == null) {
            this.listProblems = problemFacade.findStatisticsFromAllProblem();
        }
    }

    public String actionRedirectViewToSubmitProblem() {
        logger.debug("getIdProblem [" + problemMessage.getIdProblem() + "]");
        return "/modules/content/problem/view-problem.xhtml?faces-redirect=true&includeViewParams=true";
    }

    public String actionRedirectViewToProfile() {
        logger.debug("getIdAccount=" + accountMessage.getIdAccount());
        return "/modules/user/profile.xhtml?faces-redirect=true&includeViewParams=true";
    }

    public List<ProblemMessage> getListProblems() {
        return listProblems;
    }

    public void setListProblems(List<ProblemMessage> listProblems) {
        this.listProblems = listProblems;
    }

    public List<ProblemMessage> getFilteredProblems() {
        return filteredProblems;
    }

    public void setFilteredProblems(List<ProblemMessage> filteredProblems) {
        this.filteredProblems = filteredProblems;
    }

    public String getSearchParameter() {
        return searchParameter;
    }

    public void setSearchParameter(String searchParameter) {
        this.searchParameter = searchParameter;
    }

    public ProblemMessage getProblemMessage() {
        return problemMessage;
    }

    public void setProblemMessage(ProblemMessage problemMessage) {
        this.problemMessage = problemMessage;
    }

    public AccountMessage getAccountMessage() {
        return accountMessage;
    }

    public void setAccountMessage(AccountMessage accountMessage) {
        this.accountMessage = accountMessage;
    }

}
