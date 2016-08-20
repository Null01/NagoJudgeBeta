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
public class SubmitMessage implements Serializable {

    private Long idSubmit;
    private String statusSubmit;
    private Long dateSubmit;
    private Long dateJudge;
    private String msgJudge;
    private String visibleWeb;
    private Long idAccount;
    private String nickname;
    private String nameLanguage;
    private Long idProblem;
    private String nameProblem;

    public SubmitMessage() {
    }

    public SubmitMessage(Long idSubmit, String statusSubmit, Long dateSubmit, Long dateJudge, String msgJudge, String visibleWeb, Long idAccount, String nickname, String nameLanguage, Long idProblem, String nameProblem) {
        this.idSubmit = idSubmit;
        this.statusSubmit = statusSubmit;
        this.dateSubmit = dateSubmit;
        this.dateJudge = dateJudge;
        this.msgJudge = msgJudge;
        this.visibleWeb = visibleWeb;
        this.idAccount = idAccount;
        this.nickname = nickname;
        this.nameLanguage = nameLanguage;
        this.idProblem = idProblem;
        this.nameProblem = nameProblem;
    }

    public Long getIdSubmit() {
        return idSubmit;
    }

    public void setIdSubmit(Long idSubmit) {
        this.idSubmit = idSubmit;
    }

    public String getStatusSubmit() {
        return statusSubmit;
    }

    public void setStatusSubmit(String statusSubmit) {
        this.statusSubmit = statusSubmit;
    }

    public Long getDateSubmit() {
        return dateSubmit;
    }

    public void setDateSubmit(Long dateSubmit) {
        this.dateSubmit = dateSubmit;
    }

    public Long getDateJudge() {
        return dateJudge;
    }

    public void setDateJudge(Long dateJudge) {
        this.dateJudge = dateJudge;
    }

    public String getMsgJudge() {
        return msgJudge;
    }

    public void setMsgJudge(String msgJudge) {
        this.msgJudge = msgJudge;
    }

    public String getVisibleWeb() {
        return visibleWeb;
    }

    public void setVisibleWeb(String visibleWeb) {
        this.visibleWeb = visibleWeb;
    }

    public Long getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(Long idAccount) {
        this.idAccount = idAccount;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNameLanguage() {
        return nameLanguage;
    }

    public void setNameLanguage(String nameLanguage) {
        this.nameLanguage = nameLanguage;
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

}
