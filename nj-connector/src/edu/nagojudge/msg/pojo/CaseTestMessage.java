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
public class CaseTestMessage implements Serializable {

    private Long idCaseTest;
    private String nameCaseTest;
    private String dataInput;
    private String dataOutput;
    private String descriptionSuccess;
    private String descriptionFailure;
    private Long dateCreated;
    private long timeUsedByUser;
    private long timeUsedByServer;

    public CaseTestMessage() {
    }

    public CaseTestMessage(Long idCaseTest, String nameCaseTest, String dataInput, String dataOutput, String descriptionSuccess, String descriptionFailure, Long dateCreated, long timeUsedByUser, long timeUsedByServer) {
        this.idCaseTest = idCaseTest;
        this.nameCaseTest = nameCaseTest;
        this.dataInput = dataInput;
        this.dataOutput = dataOutput;
        this.descriptionSuccess = descriptionSuccess;
        this.descriptionFailure = descriptionFailure;
        this.dateCreated = dateCreated;
        this.timeUsedByUser = timeUsedByUser;
        this.timeUsedByServer = timeUsedByServer;
    }

    public Long getIdCaseTest() {
        return idCaseTest;
    }

    public void setIdCaseTest(Long idCaseTest) {
        this.idCaseTest = idCaseTest;
    }

    public String getNameCaseTest() {
        return nameCaseTest;
    }

    public void setNameCaseTest(String nameCaseTest) {
        this.nameCaseTest = nameCaseTest;
    }

    public String getDataInput() {
        return dataInput;
    }

    public void setDataInput(String dataInput) {
        this.dataInput = dataInput;
    }

    public String getDataOutput() {
        return dataOutput;
    }

    public void setDataOutput(String dataOutput) {
        this.dataOutput = dataOutput;
    }

    public String getDescriptionSuccess() {
        return descriptionSuccess;
    }

    public void setDescriptionSuccess(String descriptionSuccess) {
        this.descriptionSuccess = descriptionSuccess;
    }

    public String getDescriptionFailure() {
        return descriptionFailure;
    }

    public void setDescriptionFailure(String descriptionFailure) {
        this.descriptionFailure = descriptionFailure;
    }

    public Long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public long getTimeUsedByUser() {
        return timeUsedByUser;
    }

    public void setTimeUsedByUser(long timeUsedByUser) {
        this.timeUsedByUser = timeUsedByUser;
    }

    public long getTimeUsedByServer() {
        return timeUsedByServer;
    }

    public void setTimeUsedByServer(long timeUsedByServer) {
        this.timeUsedByServer = timeUsedByServer;
    }

}
