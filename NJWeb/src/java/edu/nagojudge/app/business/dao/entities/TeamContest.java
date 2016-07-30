/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.app.business.dao.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author andresfelipegarciaduran
 */
@Entity
@Table(name = "TEAM_CONTEST")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TeamContest.findAll", query = "SELECT t FROM TeamContest t")})
public class TeamContest implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_TEAM_CONTEST")
    private Long idTeamContest;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DATE_REGISTER")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateRegister;
    @ManyToMany(mappedBy = "teamContestList", fetch = FetchType.LAZY)
    private List<Challenge> challengeList;
    @JoinColumn(name = "ID_ACCOUNT", referencedColumnName = "ID_ACCOUNT")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Account idAccount;

    public TeamContest() {
    }

    public TeamContest(Long idTeamContest) {
        this.idTeamContest = idTeamContest;
    }

    public TeamContest(Long idTeamContest, Date dateRegister) {
        this.idTeamContest = idTeamContest;
        this.dateRegister = dateRegister;
    }

    public Long getIdTeamContest() {
        return idTeamContest;
    }

    public void setIdTeamContest(Long idTeamContest) {
        this.idTeamContest = idTeamContest;
    }

    public Date getDateRegister() {
        return dateRegister;
    }

    public void setDateRegister(Date dateRegister) {
        this.dateRegister = dateRegister;
    }

    @XmlTransient
    public List<Challenge> getChallengeList() {
        return challengeList;
    }

    public void setChallengeList(List<Challenge> challengeList) {
        this.challengeList = challengeList;
    }

    public Account getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(Account idAccount) {
        this.idAccount = idAccount;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTeamContest != null ? idTeamContest.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TeamContest)) {
            return false;
        }
        TeamContest other = (TeamContest) object;
        if ((this.idTeamContest == null && other.idTeamContest != null) || (this.idTeamContest != null && !this.idTeamContest.equals(other.idTeamContest))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.nagojudge.app.business.dao.entities.TeamContest[ idTeamContest=" + idTeamContest + " ]";
    }
    
}
