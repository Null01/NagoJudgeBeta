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
public class JudgeMessage implements Serializable {

    private Long _idStatusName;
    private String statusName;
    private long _penality;
    private String resumeStatus;
    private String messageJudge;
    private long _timeUsed;
    private long _memoUsed;

    public JudgeMessage() {
    }

    public JudgeMessage(Long _idStatusName, String statusName, String resumeStatus, String messageJudge) {
        this._idStatusName = _idStatusName;
        this.statusName = statusName;
        this.resumeStatus = resumeStatus;
        this.messageJudge = messageJudge;
    }

    public Long getIdStatusName() {
        return _idStatusName;
    }

    public void setIdStatusName(Long _idStatusName) {
        this._idStatusName = _idStatusName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getResumeStatus() {
        return resumeStatus;
    }

    public void setResumeStatus(String resumeStatus) {
        this.resumeStatus = resumeStatus;
    }

    public String getMessageJudge() {
        return messageJudge;
    }

    public void setMessageJudge(String messageJudge) {
        this.messageJudge = messageJudge;
    }

    public long getTimeUsed() {
        return _timeUsed;
    }

    public void setTimeUsed(long _timeUsed) {
        this._timeUsed = _timeUsed;
    }

    public long getMemoUsed() {
        return _memoUsed;
    }

    public void setMemoUsed(long _memoUsed) {
        this._memoUsed = _memoUsed;
    }

    public long getPenality() {
        return _penality;
    }

    public void setPenality(long _penality) {
        this._penality = _penality;
    }

}
