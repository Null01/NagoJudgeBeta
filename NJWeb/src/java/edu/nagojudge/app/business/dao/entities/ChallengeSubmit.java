/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.app.business.dao.entities;

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
@Table(name = "challenge_submit", catalog = "njlive", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ChallengeSubmit.findAll", query = "SELECT c FROM ChallengeSubmit c")})
public class ChallengeSubmit implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_CHALLENGE_SUBMIT")
    private Long idChallengeSubmit;
    @JoinColumn(name = "ID_CHALLENGE", referencedColumnName = "ID_CHALLENGE")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Challenge idChallenge;
    @JoinColumn(name = "ID_TEAM", referencedColumnName = "ID_TEAM")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Team idTeam;
    @JoinColumn(name = "ID_SUBMIT", referencedColumnName = "ID_SUBMIT")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Submit idSubmit;

    public ChallengeSubmit() {
    }

    public ChallengeSubmit(Long idChallengeSubmit) {
        this.idChallengeSubmit = idChallengeSubmit;
    }

    public Long getIdChallengeSubmit() {
        return idChallengeSubmit;
    }

    public void setIdChallengeSubmit(Long idChallengeSubmit) {
        this.idChallengeSubmit = idChallengeSubmit;
    }

    public Challenge getIdChallenge() {
        return idChallenge;
    }

    public void setIdChallenge(Challenge idChallenge) {
        this.idChallenge = idChallenge;
    }

    public Team getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(Team idTeam) {
        this.idTeam = idTeam;
    }

    public Submit getIdSubmit() {
        return idSubmit;
    }

    public void setIdSubmit(Submit idSubmit) {
        this.idSubmit = idSubmit;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idChallengeSubmit != null ? idChallengeSubmit.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ChallengeSubmit)) {
            return false;
        }
        ChallengeSubmit other = (ChallengeSubmit) object;
        if ((this.idChallengeSubmit == null && other.idChallengeSubmit != null) || (this.idChallengeSubmit != null && !this.idChallengeSubmit.equals(other.idChallengeSubmit))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.nagojudge.app.business.dao.entities.ChallengeSubmit[ idChallengeSubmit=" + idChallengeSubmit + " ]";
    }
    
}
