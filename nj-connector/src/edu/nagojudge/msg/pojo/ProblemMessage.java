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
 * @author andresfelipegarciaduran
 */
@XmlRootElement
public class ProblemMessage implements Serializable {

    private Long idProblem;
    private String nameProblem;
    private String author;
    private ListMessage<CategoryMessage> listCategoryMessage;
    private String nameDifficulty;
    private String description;
    private String bestComplexity;
    private int timeLimit;
    private int memoLimit;
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

    public ProblemMessage(Long idProblem, String nameProblem, String author, ListMessage<CategoryMessage> listCategoryMessage, String nameDifficulty, String description, String bestComplexity, int timeLimitSeg, int memoLimit, int totalStatus, int statusAC, int statusCE, int statusCS, int statusIP, int statusRE, int statusTL, int statusWR) {
        this.idProblem = idProblem;
        this.nameProblem = nameProblem;
        this.author = author;
        this.listCategoryMessage = listCategoryMessage;
        this.nameDifficulty = nameDifficulty;
        this.description = description;
        this.bestComplexity = bestComplexity;
        this.timeLimit = timeLimitSeg;
        this.memoLimit = memoLimit;
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

    public ListMessage<CategoryMessage> getListCategoryMessage() {
        return listCategoryMessage;
    }

    public void setListCategoryMessage(ListMessage<CategoryMessage> listCategoryMessage) {
        this.listCategoryMessage = listCategoryMessage;
    }

    public String getNameDifficulty() {
        return nameDifficulty;
    }

    public void setNameDifficulty(String nameDifficulty) {
        this.nameDifficulty = nameDifficulty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBestComplexity() {
        return bestComplexity;
    }

    public void setBestComplexity(String bestComplexity) {
        this.bestComplexity = bestComplexity;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getMemoLimit() {
        return memoLimit;
    }

    public void setMemoLimit(int memoLimit) {
        this.memoLimit = memoLimit;
    }

    public int getTotalStatus() {
        return totalStatus;
    }

    public void setTotalStatus(int totalStatus) {
        this.totalStatus = totalStatus;
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

 

}
