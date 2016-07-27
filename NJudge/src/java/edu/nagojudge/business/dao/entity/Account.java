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
@Table(name = "ACCOUNT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Account.findAll", query = "SELECT a FROM Account a")})
public class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_ACCOUNT")
    private Long idAccount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DATE_REGISTER")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateRegister;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "NICKNAME")
    private String nickname;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idAccount", fetch = FetchType.LAZY)
    private List<Submit> submitList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idAccount", fetch = FetchType.LAZY)
    private List<TeamContest> teamContestList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idAccount", fetch = FetchType.LAZY)
    private List<User> userList;

    public Account() {
    }

    public Account(Long idAccount) {
        this.idAccount = idAccount;
    }

    public Account(Long idAccount, Date dateRegister, String nickname) {
        this.idAccount = idAccount;
        this.dateRegister = dateRegister;
        this.nickname = nickname;
    }

    public Long getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(Long idAccount) {
        this.idAccount = idAccount;
    }

    public Date getDateRegister() {
        return dateRegister;
    }

    public void setDateRegister(Date dateRegister) {
        this.dateRegister = dateRegister;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @XmlTransient
    public List<Submit> getSubmitList() {
        return submitList;
    }

    public void setSubmitList(List<Submit> submitList) {
        this.submitList = submitList;
    }

    @XmlTransient
    public List<TeamContest> getTeamContestList() {
        return teamContestList;
    }

    public void setTeamContestList(List<TeamContest> teamContestList) {
        this.teamContestList = teamContestList;
    }

    @XmlTransient
    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAccount != null ? idAccount.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Account)) {
            return false;
        }
        Account other = (Account) object;
        if ((this.idAccount == null && other.idAccount != null) || (this.idAccount != null && !this.idAccount.equals(other.idAccount))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.nagojudge.business.db.entity.Account[ idAccount=" + idAccount + " ]";
    }
    
}
