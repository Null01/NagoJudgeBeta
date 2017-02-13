/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.web.mbeans;

import edu.nagojudge.app.business.dao.beans.ChallengeFacadeDAO;
import edu.nagojudge.app.business.dao.entities.Challenge;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
@ManagedBean
@ViewScoped
public class ChallengeViewBean implements Serializable {

    private final Logger logger = Logger.getLogger(ChallengeViewBean.class);

    @EJB
    private ChallengeFacadeDAO challengeFacade;

    private List<Challenge> listChallenges;
    private List<Challenge> filteredChallenges;
    private String searchParameter;

    public ChallengeViewBean() {
    }

    @PostConstruct
    public void init() {
        this.listChallenges = new ArrayList<Challenge>(this.challengeFacade.findAll());
    }

    public List<Challenge> getListChallenges() {
        return listChallenges;
    }

    public void setListChallenges(List<Challenge> listChallenges) {
        this.listChallenges = listChallenges;
    }

    public List<Challenge> getFilteredChallenges() {
        return filteredChallenges;
    }

    public void setFilteredChallenges(List<Challenge> filteredChallenges) {
        this.filteredChallenges = filteredChallenges;
    }

    public String getSearchParameter() {
        return searchParameter;
    }

    public void setSearchParameter(String searchParameter) {
        this.searchParameter = searchParameter;
    }

}
