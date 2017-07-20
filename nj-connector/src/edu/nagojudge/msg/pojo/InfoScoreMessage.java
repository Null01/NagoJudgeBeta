/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.msg.pojo;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author andres.garcia
 */
@XmlRootElement
public class InfoScoreMessage {

    private long idProblem;
    private boolean solved;
    private long timeFirstTrie;
    private int numberTries;

    public InfoScoreMessage() {
    }

    public InfoScoreMessage(long idProblem, boolean solved, long timeFirstTrie, int numberTries) {
        this.idProblem = idProblem;
        this.solved = solved;
        this.timeFirstTrie = timeFirstTrie;
        this.numberTries = numberTries;
    }

    public long getIdProblem() {
        return idProblem;
    }

    public void setIdProblem(long idProblem) {
        this.idProblem = idProblem;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public long getTimeFirstTrie() {
        return timeFirstTrie;
    }

    public void setTimeFirstTrie(long timeFirstTrie) {
        this.timeFirstTrie = timeFirstTrie;
    }

    public int getNumberTries() {
        return numberTries;
    }

    public void setNumberTries(int numberTries) {
        this.numberTries = numberTries;
    }

}
