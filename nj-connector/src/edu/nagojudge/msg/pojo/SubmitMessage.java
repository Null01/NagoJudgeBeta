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
    private String _idStatus;
    private String nameStatus;
    private String nameLanguage;
    private Long _idProblem;
    private String nameProblem;
    private Long dateSubmit;
    private Long dateJudge;
    private String msgJudge;
    private String visibleWeb;
    private AccountMessage accountMessage;

    public SubmitMessage() {
    }

    public SubmitMessage(Long idSubmit, String _idStatus, String nameStatus, String nameLanguage, Long _idProblem, String nameProblem, Long dateSubmit, Long dateJudge, String msgJudge, String visibleWeb, AccountMessage accountMessage) {
        this.idSubmit = idSubmit;
        this._idStatus = _idStatus;
        this.nameStatus = nameStatus;
        this.nameLanguage = nameLanguage;
        this._idProblem = _idProblem;
        this.nameProblem = nameProblem;
        this.dateSubmit = dateSubmit;
        this.dateJudge = dateJudge;
        this.msgJudge = msgJudge;
        this.visibleWeb = visibleWeb;
        this.accountMessage = accountMessage;
    }

    public Long getIdSubmit() {
        return idSubmit;
    }

    public void setIdSubmit(Long idSubmit) {
        this.idSubmit = idSubmit;
    }

    public String getIdStatus() {
        return _idStatus;
    }

    public void setIdStatus(String _idStatus) {
        this._idStatus = _idStatus;
    }

    public String getNameStatus() {
        return nameStatus;
    }

    public void setNameStatus(String nameStatus) {
        this.nameStatus = nameStatus;
    }

    public String getNameLanguage() {
        return nameLanguage;
    }

    public void setNameLanguage(String nameLanguage) {
        this.nameLanguage = nameLanguage;
    }

    public Long getIdProblem() {
        return _idProblem;
    }

    public void setIdProblem(Long _idProblem) {
        this._idProblem = _idProblem;
    }

    public String getNameProblem() {
        return nameProblem;
    }

    public void setNameProblem(String nameProblem) {
        this.nameProblem = nameProblem;
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

    public AccountMessage getAccountMessage() {
        return accountMessage;
    }

    public void setAccountMessage(AccountMessage accountMessage) {
        this.accountMessage = accountMessage;
    }

}
