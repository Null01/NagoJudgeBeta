/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.business.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author andres.garcia
 */
@Entity
@Table(name = "team_challenge_submit", catalog = "njlive", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TeamChallengeSubmit.findAll", query = "SELECT t FROM TeamChallengeSubmit t")})
public class TeamChallengeSubmit implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_TEAM_CHALLENGE_SUBMIT")
    private Long idTeamChallengeSubmit;
    @Column(name = "TIME_SOLVED")
    private Integer timeSolved;
    @JoinColumn(name = "ID_SUBMIT", referencedColumnName = "ID_SUBMIT")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Submit idSubmit;
    @JoinColumn(name = "ID_TEAM", referencedColumnName = "ID_TEAM")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Team idTeam;

    public TeamChallengeSubmit() {
    }

    public TeamChallengeSubmit(Long idTeamChallengeSubmit) {
        this.idTeamChallengeSubmit = idTeamChallengeSubmit;
    }

    public Long getIdTeamChallengeSubmit() {
        return idTeamChallengeSubmit;
    }

    public void setIdTeamChallengeSubmit(Long idTeamChallengeSubmit) {
        this.idTeamChallengeSubmit = idTeamChallengeSubmit;
    }

    public Integer getTimeSolved() {
        return timeSolved;
    }

    public void setTimeSolved(Integer timeSolved) {
        this.timeSolved = timeSolved;
    }

    public Submit getIdSubmit() {
        return idSubmit;
    }

    public void setIdSubmit(Submit idSubmit) {
        this.idSubmit = idSubmit;
    }

    public Team getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(Team idTeam) {
        this.idTeam = idTeam;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTeamChallengeSubmit != null ? idTeamChallengeSubmit.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TeamChallengeSubmit)) {
            return false;
        }
        TeamChallengeSubmit other = (TeamChallengeSubmit) object;
        if ((this.idTeamChallengeSubmit == null && other.idTeamChallengeSubmit != null) || (this.idTeamChallengeSubmit != null && !this.idTeamChallengeSubmit.equals(other.idTeamChallengeSubmit))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.nagojudge.live.business.entity.TeamChallengeSubmit[ idTeamChallengeSubmit=" + idTeamChallengeSubmit + " ]";
    }
    
}
