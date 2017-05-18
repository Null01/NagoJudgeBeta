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
    private Long dateSubmit;
    private Long dateJudge;
    private Long timeUsed;
    private Long memoUsed;
    private String visibleWeb;
    private String _idStatus;
    private String _nameStatus;
    private LanguageProgrammingMessage languageProgrammingMessage;
    private ProblemMessage problemMessage;
    private AccountMessage accountMessage;
    private JudgeMessage judgeMessage;

    public SubmitMessage() {
    }

    public SubmitMessage(Long idSubmit, Long dateSubmit, Long dateJudge, Long timeUsed, Long memoUsed, String visibleWeb, String _idStatus, String _nameStatus, LanguageProgrammingMessage languageProgrammingMessage, ProblemMessage problemMessage, AccountMessage accountMessage, JudgeMessage judgeMessage) {
        this.idSubmit = idSubmit;
        this.dateSubmit = dateSubmit;
        this.dateJudge = dateJudge;
        this.timeUsed = timeUsed;
        this.memoUsed = memoUsed;
        this.visibleWeb = visibleWeb;
        this._idStatus = _idStatus;
        this._nameStatus = _nameStatus;
        this.languageProgrammingMessage = languageProgrammingMessage;
        this.problemMessage = problemMessage;
        this.accountMessage = accountMessage;
        this.judgeMessage = judgeMessage;
    }

    public Long getIdSubmit() {
        return idSubmit;
    }

    public void setIdSubmit(Long idSubmit) {
        this.idSubmit = idSubmit;
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

    public Long getTimeUsed() {
        return timeUsed;
    }

    public void setTimeUsed(Long timeUsed) {
        this.timeUsed = timeUsed;
    }

    public Long getMemoUsed() {
        return memoUsed;
    }

    public void setMemoUsed(Long memoUsed) {
        this.memoUsed = memoUsed;
    }

    public String getVisibleWeb() {
        return visibleWeb;
    }

    public void setVisibleWeb(String visibleWeb) {
        this.visibleWeb = visibleWeb;
    }

    public String getIdStatus() {
        return _idStatus;
    }

    public void setIdStatus(String _idStatus) {
        this._idStatus = _idStatus;
    }

    public String getNameStatus() {
        return _nameStatus;
    }

    public void setNameStatus(String _nameStatus) {
        this._nameStatus = _nameStatus;
    }

    public LanguageProgrammingMessage getLanguageProgrammingMessage() {
        return languageProgrammingMessage;
    }

    public void setLanguageProgrammingMessage(LanguageProgrammingMessage languageProgrammingMessage) {
        this.languageProgrammingMessage = languageProgrammingMessage;
    }

    public ProblemMessage getProblemMessage() {
        return problemMessage;
    }

    public void setProblemMessage(ProblemMessage problemMessage) {
        this.problemMessage = problemMessage;
    }

    public AccountMessage getAccountMessage() {
        return accountMessage;
    }

    public void setAccountMessage(AccountMessage accountMessage) {
        this.accountMessage = accountMessage;
    }

    public JudgeMessage getJudgeMessage() {
        return judgeMessage;
    }

    public void setJudgeMessage(JudgeMessage judgeMessage) {
        this.judgeMessage = judgeMessage;
    }

}
