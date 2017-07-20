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
 * @author andresfelipegarciaduran
 */
@Entity
@Table(name = "CHALLENGE_TEAM", catalog = "njlive", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ChallengeTeam.findAll", query = "SELECT c FROM ChallengeTeam c")})
public class ChallengeTeam implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_CHALENGE_TEAM")
    private Long idChalengeTeam;
    @JoinColumn(name = "ID_ACCOUNT", referencedColumnName = "ID_ACCOUNT")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Account idAccount;
    @JoinColumn(name = "ID_CHALLENGE", referencedColumnName = "ID_CHALLENGE")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Challenge idChallenge;
    @JoinColumn(name = "ID_TEAM", referencedColumnName = "ID_TEAM")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Team idTeam;

    public ChallengeTeam() {
    }

    public ChallengeTeam(Long idChalengeTeam) {
        this.idChalengeTeam = idChalengeTeam;
    }

    public Long getIdChalengeTeam() {
        return idChalengeTeam;
    }

    public void setIdChalengeTeam(Long idChalengeTeam) {
        this.idChalengeTeam = idChalengeTeam;
    }

    public Account getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(Account idAccount) {
        this.idAccount = idAccount;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idChalengeTeam != null ? idChalengeTeam.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ChallengeTeam)) {
            return false;
        }
        ChallengeTeam other = (ChallengeTeam) object;
        if ((this.idChalengeTeam == null && other.idChalengeTeam != null) || (this.idChalengeTeam != null && !this.idChalengeTeam.equals(other.idChalengeTeam))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.nagojudge.live.business.entity.ChallengeTeam[ idChalengeTeam=" + idChalengeTeam + " ]";
    }
    
}
