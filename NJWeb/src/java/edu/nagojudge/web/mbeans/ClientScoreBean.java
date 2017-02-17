/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.web.mbeans;

import edu.nagojudge.msg.pojo.ScoreClientMessage;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author andresfelipegarciaduran
 */
@ManagedBean
@ViewScoped
public class ClientScoreBean {

    private List<ScoreClientMessage> listScoreChallenge = new ArrayList<ScoreClientMessage>();
    private List<ScoreClientMessage> filteredScoreChallenge = new ArrayList<ScoreClientMessage>();

    public ClientScoreBean() {
    }

    public List<ScoreClientMessage> getListScoreChallenge() {
        return listScoreChallenge;
    }

    public void setListScoreChallenge(List<ScoreClientMessage> listScoreChallenge) {
        this.listScoreChallenge = listScoreChallenge;
    }

    public List<ScoreClientMessage> getFilteredScoreChallenge() {
        return filteredScoreChallenge;
    }

    public void setFilteredScoreChallenge(List<ScoreClientMessage> filteredScoreChallenge) {
        this.filteredScoreChallenge = filteredScoreChallenge;
    }

}
