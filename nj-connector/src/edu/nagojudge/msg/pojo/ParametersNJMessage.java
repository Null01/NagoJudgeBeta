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
public class ParametersNJMessage implements Serializable {

    private String idParameter;
    private byte[] contentParameter;
    private Long dateCreated;
    private Long dateUpdated;
    private String description;

    public ParametersNJMessage() {
    }

    public ParametersNJMessage(String idParameter, byte[] contentParameter, Long dateCreated, Long dateUpdated, String description) {
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

    public Long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Long getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Long dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
