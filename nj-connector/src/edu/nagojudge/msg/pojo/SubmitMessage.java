/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.msg.pojo;

import edu.nagojudge.msg.pojo.constants.TypeActionEnum;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author andresfelipegarciaduran
 */
@XmlRootElement
public class SubmitMessage implements Serializable {

    private String _rowIdMetadata;
    private TypeActionEnum _rowActionMetadata;
    private String _rowLetterMetadata;
    private Long idSubmit;
    private Long dateSubmit;
    private Long dateJudge;
    private String visibleWeb;
    private LanguageProgrammingMessage languageProgrammingMessage;
    private ProblemMessage problemMessage;
    private AccountMessage accountMessage;
    private TeamMessage teamMessage;
    private JudgeMessage judgeMessage;

    public SubmitMessage() {
    }

    public SubmitMessage(String _rowIdMetadata, TypeActionEnum _rowActionMetadata, String _rowLetterMetadata, Long idSubmit, Long dateSubmit, Long dateJudge, String visibleWeb, LanguageProgrammingMessage languageProgrammingMessage, ProblemMessage problemMessage, AccountMessage accountMessage, TeamMessage teamMessage, JudgeMessage judgeMessage) {
        this._rowIdMetadata = _rowIdMetadata;
        this._rowActionMetadata = _rowActionMetadata;
        this._rowLetterMetadata = _rowLetterMetadata;
        this.idSubmit = idSubmit;
        this.dateSubmit = dateSubmit;
        this.dateJudge = dateJudge;
        this.visibleWeb = visibleWeb;
        this.languageProgrammingMessage = languageProgrammingMessage;
        this.problemMessage = problemMessage;
        this.accountMessage = accountMessage;
        this.teamMessage = teamMessage;
        this.judgeMessage = judgeMessage;
    }

    public String getRowIdMetadata() {
        return _rowIdMetadata;
    }

    public void setRowIdMetadata(String _rowIdMetadata) {
        this._rowIdMetadata = _rowIdMetadata;
    }

    public TypeActionEnum getRowActionMetadata() {
        return _rowActionMetadata;
    }

    public void setRowActionMetadata(TypeActionEnum _rowActionMetadata) {
        this._rowActionMetadata = _rowActionMetadata;
    }

    public String getRowLetterMetadata() {
        return _rowLetterMetadata;
    }

    public void setRowLetterMetadata(String _rowLetterMetadata) {
        this._rowLetterMetadata = _rowLetterMetadata;
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

    public String getVisibleWeb() {
        return visibleWeb;
    }

    public void setVisibleWeb(String visibleWeb) {
        this.visibleWeb = visibleWeb;
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

    public TeamMessage getTeamMessage() {
        return teamMessage;
    }

    public void setTeamMessage(TeamMessage teamMessage) {
        this.teamMessage = teamMessage;
    }

    public JudgeMessage getJudgeMessage() {
        return judgeMessage;
    }

    public void setJudgeMessage(JudgeMessage judgeMessage) {
        this.judgeMessage = judgeMessage;
    }

}
