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
    private String resumeStatus;
    private String messageJudge;

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

}
