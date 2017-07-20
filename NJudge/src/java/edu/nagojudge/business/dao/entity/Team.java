/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.business.dao.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author andresfelipegarciaduran
 */
@Entity
@Table(name = "TEAM", catalog = "njlive", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Team.findAll", query = "SELECT t FROM Team t")})
public class Team implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_TEAM")
    private Long idTeam;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "NAME_TEAM")
    private String nameTeam;
    @Size(max = 255)
    @Column(name = "INSTITUTION_TEAM")
    private String institutionTeam;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "PASSWORD_TEAM")
    private String passwordTeam;
    @Column(name = "DATE_REGISTER")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateRegister;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTeam", fetch = FetchType.LAZY)
    private List<ChallengeTeam> challengeTeamList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTeam", fetch = FetchType.LAZY)
    private List<ChallengeSubmit> challengeSubmitList;

    public Team() {
    }

    public Team(Long idTeam) {
        this.idTeam = idTeam;
    }

    public Team(Long idTeam, String nameTeam, String passwordTeam) {
        this.idTeam = idTeam;
        this.nameTeam = nameTeam;
        this.passwordTeam = passwordTeam;
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

    public String getInstitutionTeam() {
        return institutionTeam;
    }

    public void setInstitutionTeam(String institutionTeam) {
        this.institutionTeam = institutionTeam;
    }

    public String getPasswordTeam() {
        return passwordTeam;
    }

    public void setPasswordTeam(String passwordTeam) {
        this.passwordTeam = passwordTeam;
    }

    public Date getDateRegister() {
        return dateRegister;
    }

    public void setDateRegister(Date dateRegister) {
        this.dateRegister = dateRegister;
    }

    @XmlTransient
    public List<ChallengeTeam> getChallengeTeamList() {
        return challengeTeamList;
    }

    public void setChallengeTeamList(List<ChallengeTeam> challengeTeamList) {
        this.challengeTeamList = challengeTeamList;
    }

    @XmlTransient
    public List<ChallengeSubmit> getChallengeSubmitList() {
        return challengeSubmitList;
    }

    public void setChallengeSubmitList(List<ChallengeSubmit> challengeSubmitList) {
        this.challengeSubmitList = challengeSubmitList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTeam != null ? idTeam.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Team)) {
            return false;
        }
        Team other = (Team) object;
        if ((this.idTeam == null && other.idTeam != null) || (this.idTeam != null && !this.idTeam.equals(other.idTeam))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.nagojudge.business.dao.entity.Team[ idTeam=" + idTeam + " ]";
    }
    
}
