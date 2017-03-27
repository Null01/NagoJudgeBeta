/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.msg.pojo;

import edu.nagojudge.msg.pojo.collections.ListTypeMessage;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author andres.garcia
 */
@XmlRootElement
public class ScoreMessage {

    private TeamMessage team;
    private ListTypeMessage<InfoScoreMessage> resumeScore = new ListTypeMessage<InfoScoreMessage>();
    private Long time;

    public TeamMessage getTeam() {
        return team;
    }

    public void setTeam(TeamMessage team) {
        this.team = team;
    }

    public ListTypeMessage<InfoScoreMessage> getResumeScore() {
        return resumeScore;
    }

    public void setResumeScore(ListTypeMessage<InfoScoreMessage> resumeScore) {
        this.resumeScore = resumeScore;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

}
