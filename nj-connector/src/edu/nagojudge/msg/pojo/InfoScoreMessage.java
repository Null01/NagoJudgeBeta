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
    private int tries;
    private long time;

    public void isSolve(boolean tempSolve) {
        this.solved = this.solved ? this.solved : tempSolve;
    }

    public void addTry() {
        this.tries = this.tries + 1;
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

    public int getTries() {
        return tries;
    }

    public void setTries(int tries) {
        this.tries = tries;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
