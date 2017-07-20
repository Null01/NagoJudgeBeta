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
 * @author andres.garcia
 */
@XmlRootElement
public class TeamMessage implements Serializable {

    private Long idTeam;
    private String nameTeam;
    private String passwordTeam;
    private String nameInstitution;
    private String nameCity;
    private String nameContry;
    private Long dateRegister;

    public TeamMessage() {
    }

    public TeamMessage(Long idTeam, String nameTeam, String passwordTeam, String nameInstitution, String nameCity, String nameContry, Long dateRegister) {
        this.idTeam = idTeam;
        this.nameTeam = nameTeam;
        this.passwordTeam = passwordTeam;
        this.nameInstitution = nameInstitution;
        this.nameCity = nameCity;
        this.nameContry = nameContry;
        this.dateRegister = dateRegister;
    }

    public Long getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(Long idTeam) {
        this.idTeam = idTeam;
    }

    public String getNameTeam() {
        return nameTeam;
    }

    public void setNameTeam(String nameTeam) {
        this.nameTeam = nameTeam;
    }

    public String getPasswordTeam() {
        return passwordTeam;
    }

    public void setPasswordTeam(String passwordTeam) {
        this.passwordTeam = passwordTeam;
    }

    public String getNameInstitution() {
        return nameInstitution;
    }

    public void setNameInstitution(String nameInstitution) {
        this.nameInstitution = nameInstitution;
    }

    public String getNameCity() {
        return nameCity;
    }

    public void setNameCity(String nameCity) {
        this.nameCity = nameCity;
    }

    public String getNameContry() {
        return nameContry;
    }

    public void setNameContry(String nameContry) {
        this.nameContry = nameContry;
    }

    public Long getDateRegister() {
        return dateRegister;
    }

    public void setDateRegister(Long dateRegister) {
        this.dateRegister = dateRegister;
    }

}
