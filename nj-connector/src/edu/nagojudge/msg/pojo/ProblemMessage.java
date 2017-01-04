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
public class ProblemMessage implements Serializable {

    private Long idProblem;
    private String nameProblem;
    private String author;
    private String nameCategory;
    private String nameDifficulty;
    private String description;
    private int timeLimitSeg;
    private int totalStatus;
    private int statusAC;
    private int statusCE;
    private int statusCS;
    private int statusIP;
    private int statusRE;
    private int statusTL;
    private int statusWR;

    public ProblemMessage() {
    }

    public ProblemMessage(Long idProblem, String nameProblem, String author, String nameCategory, String nameDifficulty, String description, int timeLimitSeg, int totalStatus, int statusAC, int statusCE, int statusCS, int statusIP, int statusRE, int statusTL, int statusWR) {
        this.idProblem = idProblem;
        this.nameProblem = nameProblem;
        this.author = author;
        this.nameCategory = nameCategory;
        this.nameDifficulty = nameDifficulty;
        this.description = description;
        this.timeLimitSeg = timeLimitSeg;
        this.totalStatus = totalStatus;
        this.statusAC = statusAC;
        this.statusCE = statusCE;
        this.statusCS = statusCS;
        this.statusIP = statusIP;
        this.statusRE = statusRE;
        this.statusTL = statusTL;
        this.statusWR = statusWR;
    }

    public Long getIdProblem() {
        return idProblem;
    }

    public void setIdProblem(Long idProblem) {
        this.idProblem = idProblem;
    }

    public String getNameProblem() {
        return nameProblem;
    }

    public void setNameProblem(String nameProblem) {
        this.nameProblem = nameProblem;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTimeLimitSeg() {
        return timeLimitSeg;
    }

    public void setTimeLimitSeg(int timeLimitSeg) {
        this.timeLimitSeg = timeLimitSeg;
    }

    public int getStatusAC() {
        return statusAC;
    }

    public void setStatusAC(int statusAC) {
        this.statusAC = statusAC;
    }

    public int getStatusCE() {
        return statusCE;
    }

    public void setStatusCE(int statusCE) {
        this.statusCE = statusCE;
    }

    public int getStatusCS() {
        return statusCS;
    }

    public void setStatusCS(int statusCS) {
        this.statusCS = statusCS;
    }

    public int getStatusIP() {
        return statusIP;
    }

    public void setStatusIP(int statusIP) {
        this.statusIP = statusIP;
    }

    public int getStatusRE() {
        return statusRE;
    }

    public void setStatusRE(int statusRE) {
        this.statusRE = statusRE;
    }

    public int getStatusTL() {
        return statusTL;
    }

    public void setStatusTL(int statusTL) {
        this.statusTL = statusTL;
    }

    public int getStatusWR() {
        return statusWR;
    }

    public void setStatusWR(int statusWR) {
        this.statusWR = statusWR;
    }

    public int getTotalStatus() {
        return totalStatus;
    }

    public void setTotalStatus(int totalStatus) {
        this.totalStatus = totalStatus;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public String getNameDifficulty() {
        return nameDifficulty;
    }

    public void setNameDifficulty(String nameDifficulty) {
        this.nameDifficulty = nameDifficulty;
    }

}
