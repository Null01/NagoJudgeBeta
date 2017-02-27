/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.business.entity;

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
 * @author andres.garcia
 */
@Entity
@Table(name = "team", catalog = "njlive", schema = "")
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
    @Column(name = "ID_NAME_TEAM")
    private String idNameTeam;
    @Column(name = "DATE_REGISTER")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateRegister;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTeam", fetch = FetchType.LAZY)
    private List<TeamChallengeSubmit> teamChallengeSubmitList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTeam", fetch = FetchType.LAZY)
    private List<TeamAccount> teamAccountList;

    public Team() {
    }

    public Team(Long idTeam) {
        this.idTeam = idTeam;
    }

    public Team(Long idTeam, String idNameTeam) {
        this.idTeam = idTeam;
        this.idNameTeam = idNameTeam;
    }

    public Long getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(Long idTeam) {
        this.idTeam = idTeam;
    }

    public String getIdNameTeam() {
        return idNameTeam;
    }

    public void setIdNameTeam(String idNameTeam) {
        this.idNameTeam = idNameTeam;
    }

    public Date getDateRegister() {
        return dateRegister;
    }

    public void setDateRegister(Date dateRegister) {
        this.dateRegister = dateRegister;
    }

    @XmlTransient
    public List<TeamChallengeSubmit> getTeamChallengeSubmitList() {
        return teamChallengeSubmitList;
    }

    public void setTeamChallengeSubmitList(List<TeamChallengeSubmit> teamChallengeSubmitList) {
        this.teamChallengeSubmitList = teamChallengeSubmitList;
    }

    @XmlTransient
    public List<TeamAccount> getTeamAccountList() {
        return teamAccountList;
    }

    public void setTeamAccountList(List<TeamAccount> teamAccountList) {
        this.teamAccountList = teamAccountList;
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
        return "edu.nagojudge.live.business.entity.Team[ idTeam=" + idTeam + " ]";
    }
    
}
