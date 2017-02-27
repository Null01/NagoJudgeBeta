/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.msg.pojo;

import edu.nagojudge.msg.pojo.collections.ListGenericType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author andres.garcia
 */
@XmlRootElement
public class ScoreMessage {

    private TeamMessage team;
    private ListGenericType<InfoScore> listResult = new ListGenericType<InfoScore>();

    public TeamMessage getTeam() {
        return team;
    }

    public void setTeam(TeamMessage team) {
        this.team = team;
    }

    public ListGenericType<InfoScore> getListResult() {
        return listResult;
    }

    public void setListResult(ListGenericType<InfoScore> listResult) {
        this.listResult = listResult;
    }

}

class InfoScore {

    private boolean solved;
    private int attemps;
    private long timeToBeSolved;

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public int getAttemps() {
        return attemps;
    }

    public void setAttemps(int attemps) {
        this.attemps = attemps;
    }

    public long getTimeToBeSolved() {
        return timeToBeSolved;
    }

    public void setTimeToBeSolved(long timeToBeSolved) {
        this.timeToBeSolved = timeToBeSolved;
    }

}
