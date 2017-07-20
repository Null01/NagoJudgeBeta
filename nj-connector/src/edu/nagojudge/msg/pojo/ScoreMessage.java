/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.msg.pojo;

import edu.nagojudge.msg.pojo.collections.ListMessage;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author andres.garcia
 */
@XmlRootElement
public class ScoreMessage implements Serializable {

    private TeamMessage team;
    private ListMessage<InfoScoreMessage> resumeScore = new ListMessage<InfoScoreMessage>();
    private Long timeTotal;
    private int numberSolvedProblem;

    public ScoreMessage() {
    }

    public ScoreMessage(TeamMessage team, Long timeTotal, int numberSolvedProblem) {
        this.team = team;
        this.timeTotal = timeTotal;
        this.numberSolvedProblem = numberSolvedProblem;
    }

    public TeamMessage getTeam() {
        return team;
    }

    public void setTeam(TeamMessage team) {
        this.team = team;
    }

    public ListMessage<InfoScoreMessage> getResumeScore() {
        return resumeScore;
    }

    public void setResumeScore(ListMessage<InfoScoreMessage> resumeScore) {
        this.resumeScore = resumeScore;
    }

    public Long getTimeTotal() {
        return timeTotal;
    }

    public void setTimeTotal(Long timeTotal) {
        this.timeTotal = timeTotal;
    }

    public int getNumberSolvedProblem() {
        return numberSolvedProblem;
    }

    public void setNumberSolvedProblem(int numberSolvedProblem) {
        this.numberSolvedProblem = numberSolvedProblem;
    }

}
