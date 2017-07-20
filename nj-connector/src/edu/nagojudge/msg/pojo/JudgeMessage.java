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
public class JudgeMessage implements Serializable {

    private Long _idStatusName;
    private String keyStatus;
    private String statusName;
    private String descriptionStatus;
    private String messageJudge;
    private Long timeUsed;
    private Long memoUsed;
    private ListMessage<CaseTestMessage> listCaseTestMessage = new ListMessage<CaseTestMessage>();

    public JudgeMessage() {
    }

    public JudgeMessage(Long _idStatusName, String keyStatus, String statusName, String descriptionStatus, String messageJudge, Long timeUsed, Long memoUsed) {
        this._idStatusName = _idStatusName;
        this.keyStatus = keyStatus;
        this.statusName = statusName;
        this.descriptionStatus = descriptionStatus;
        this.messageJudge = messageJudge;
        this.timeUsed = timeUsed;
        this.memoUsed = memoUsed;
    }

    public Long getIdStatusName() {
        return _idStatusName;
    }

    public void setIdStatusName(Long _idStatusName) {
        this._idStatusName = _idStatusName;
    }

    public String getKeyStatus() {
        return keyStatus;
    }

    public void setKeyStatus(String keyStatus) {
        this.keyStatus = keyStatus;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getDescriptionStatus() {
        return descriptionStatus;
    }

    public void setDescriptionStatus(String descriptionStatus) {
        this.descriptionStatus = descriptionStatus;
    }

    public String getMessageJudge() {
        return messageJudge;
    }

    public void setMessageJudge(String messageJudge) {
        this.messageJudge = messageJudge;
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

    public ListMessage<CaseTestMessage> getListCaseTestMessage() {
        return listCaseTestMessage;
    }

    public void setListCaseTestMessage(ListMessage<CaseTestMessage> listCaseTestMessage) {
        this.listCaseTestMessage = listCaseTestMessage;
    }

}
