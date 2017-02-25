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
@Table(name = "team_account", catalog = "njlive", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TeamAccount.findAll", query = "SELECT t FROM TeamAccount t")})
public class TeamAccount implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_TEAM_ACCOUNT")
    private Long idTeamAccount;
    @JoinColumn(name = "ID_ACCOUNT", referencedColumnName = "ID_ACCOUNT")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Account idAccount;
    @JoinColumn(name = "ID_CHALLENGE", referencedColumnName = "ID_CHALLENGE")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Challenge idChallenge;
    @JoinColumn(name = "ID_TEAM", referencedColumnName = "ID_TEAM")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Team idTeam;

    public TeamAccount() {
    }

    public TeamAccount(Long idTeamAccount) {
        this.idTeamAccount = idTeamAccount;
    }

    public Long getIdTeamAccount() {
        return idTeamAccount;
    }

    public void setIdTeamAccount(Long idTeamAccount) {
        this.idTeamAccount = idTeamAccount;
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
        hash += (idTeamAccount != null ? idTeamAccount.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TeamAccount)) {
            return false;
        }
        TeamAccount other = (TeamAccount) object;
        if ((this.idTeamAccount == null && other.idTeamAccount != null) || (this.idTeamAccount != null && !this.idTeamAccount.equals(other.idTeamAccount))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.nagojudge.app.business.dao.entities.TeamAccount[ idTeamAccount=" + idTeamAccount + " ]";
    }
    
}
