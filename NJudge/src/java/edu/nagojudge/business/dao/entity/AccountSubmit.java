/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.business.dao.entity;

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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author andres.garcia
 */
@Entity
@Table(name = "account_submit", catalog = "njlive", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AccountSubmit.findAll", query = "SELECT a FROM AccountSubmit a")})
public class AccountSubmit implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_ACCOUNT_SUBMIT")
    private Long idAccountSubmit;
    @Size(max = 10)
    @Column(name = "VISIBLE_WEB")
    private String visibleWeb;
    @JoinColumn(name = "ID_ACCOUNT", referencedColumnName = "ID_ACCOUNT")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Account idAccount;
    @JoinColumn(name = "ID_SUBMIT", referencedColumnName = "ID_SUBMIT")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Submit idSubmit;

    public AccountSubmit() {
    }

    public AccountSubmit(Long idAccountSubmit) {
        this.idAccountSubmit = idAccountSubmit;
    }

    public Long getIdAccountSubmit() {
        return idAccountSubmit;
    }

    public void setIdAccountSubmit(Long idAccountSubmit) {
        this.idAccountSubmit = idAccountSubmit;
    }

    public String getVisibleWeb() {
        return visibleWeb;
    }

    public void setVisibleWeb(String visibleWeb) {
        this.visibleWeb = visibleWeb;
    }

    public Account getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(Account idAccount) {
        this.idAccount = idAccount;
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
        hash += (idAccountSubmit != null ? idAccountSubmit.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AccountSubmit)) {
            return false;
        }
        AccountSubmit other = (AccountSubmit) object;
        if ((this.idAccountSubmit == null && other.idAccountSubmit != null) || (this.idAccountSubmit != null && !this.idAccountSubmit.equals(other.idAccountSubmit))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.nagojudge.business.dao.entity.AccountSubmit[ idAccountSubmit=" + idAccountSubmit + " ]";
    }
    
}
