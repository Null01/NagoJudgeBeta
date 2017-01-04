/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.msg.pojo;

import java.util.Date;

/**
 *
 * @author andresfelipegarciaduran
 */
public class ParametersNJMessage {

    private String idParameter;
    private byte[] contentParameter;
    private Date dateCreated;
    private Date dateUpdated;
    private String description;

    public ParametersNJMessage() {
    }

    public ParametersNJMessage(String idParameter, byte[] contentParameter, Date dateCreated, Date dateUpdated, String description) {
        this.idParameter = idParameter;
        this.contentParameter = contentParameter;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
        this.description = description;
    }

    public String getIdParameter() {
        return idParameter;
    }

    public void setIdParameter(String idParameter) {
        this.idParameter = idParameter;
    }

    public byte[] getContentParameter() {
        return contentParameter;
    }

    public void setContentParameter(byte[] contentParameter) {
        this.contentParameter = contentParameter;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

}
