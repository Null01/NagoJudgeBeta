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
@Table(name = "ACCOUNT", catalog = "njlive", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Account.findAll", query = "SELECT a FROM Account a")})
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_ACCOUNT")
    private Long idAccount;
    @Column(name = "DATE_REGISTER")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateRegister;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "NICKNAME")
    private String nickname;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idAccount", fetch = FetchType.LAZY)
    private List<AccountSubmit> accountSubmitList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idAccount", fetch = FetchType.LAZY)
    private List<ChallengeTeam> challengeTeamList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idAccount", fetch = FetchType.LAZY)
    private List<User> userList;

    public Account() {
    }

    public Account(Long idAccount) {
        this.idAccount = idAccount;
    }

    public Account(Long idAccount, String nickname) {
        this.idAccount = idAccount;
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
    public List<AccountSubmit> getAccountSubmitList() {
        return accountSubmitList;
    }

    public void setAccountSubmitList(List<AccountSubmit> accountSubmitList) {
        this.accountSubmitList = accountSubmitList;
    }

    @XmlTransient
    public List<ChallengeTeam> getChallengeTeamList() {
        return challengeTeamList;
    }

    public void setChallengeTeamList(List<ChallengeTeam> challengeTeamList) {
        this.challengeTeamList = challengeTeamList;
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
        return "edu.nagojudge.business.dao.entity.Account[ idAccount=" + idAccount + " ]";
    }
    
}
