/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.msg.pojo;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author andresfelipegarciaduran
 */
@XmlRootElement
public class ScoreClientMessage  implements Serializable{

    private String nameTeam;
    private int[] solvedProblems;
    private int[] attempstsProblems;
    private int timeUsed;

    public ScoreClientMessage() {
    }

    public ScoreClientMessage(String nameTeam, int[] solvedProblems, int[] attempstsProblems, int timeUsed) {
        this.nameTeam = nameTeam;
        this.solvedProblems = solvedProblems;
        this.attempstsProblems = attempstsProblems;
        this.timeUsed = timeUsed;
    }

    public int[] getAttempstsProblems() {
        return attempstsProblems;
    }

    public void setAttempstsProblems(int[] attempstsProblems) {
        this.attempstsProblems = attempstsProblems;
    }

    public String getNameTeam() {
        return nameTeam;
    }

    public void setNameTeam(String nameTeam) {
        this.nameTeam = nameTeam;
    }

    public int[] getSolvedProblems() {
        return solvedProblems;
    }

    public void setSolvedProblems(int[] solvedProblems) {
        this.solvedProblems = solvedProblems;
    }

    public int getTimeUsed() {
        return timeUsed;
    }

    public void setTimeUsed(int timeUsed) {
        this.timeUsed = timeUsed;
    }

}
