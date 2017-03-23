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
    private ListTypeMessage<InfoScoreMessage> scoreTeam = new ListTypeMessage<InfoScoreMessage>();

    public TeamMessage getTeam() {
        return team;
    }

    public void setTeam(TeamMessage team) {
        this.team = team;
    }

    public ListTypeMessage<InfoScoreMessage> getScoreTeam() {
        return scoreTeam;
    }

    public void setScoreTeam(ListTypeMessage<InfoScoreMessage> scoreTeam) {
        this.scoreTeam = scoreTeam;
    }

}
