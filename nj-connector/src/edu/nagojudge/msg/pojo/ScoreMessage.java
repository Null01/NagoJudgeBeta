/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.msg.pojo;

import edu.nagojudge.msg.pojo.collections.ListMessage;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author andres.garcia
 */
@XmlRootElement
public class ScoreMessage {

    private TeamMessage team;
    private ListMessage<InfoScoreMessage> resumeScore = new ListMessage<InfoScoreMessage>();
    private Long time;
    private Long solved;

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

    public Long getSolved() {
        return solved;
    }

    public void setSolved(Long solved) {
        this.solved = solved;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

}
