/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.app.business.dao.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author andres.garcia
 */
@Entity
@Table(name = "user", catalog = "njlive", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")})
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "ID_EMAIL")
    private String idEmail;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "KEY_USER")
    private String keyUser;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "LAST_NAME")
    private String lastName;
    @Column(name = "DATE_BIRTHDAY")
    @Temporal(TemporalType.DATE)
    private Date dateBirthday;
    @JoinColumn(name = "ID_ACCOUNT", referencedColumnName = "ID_ACCOUNT")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Account idAccount;
    @JoinColumn(name = "ID_TYPE", referencedColumnName = "ID_TYPE")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TypeUser idType;

    public User() {
    }

    public User(String idEmail) {
        this.idEmail = idEmail;
    }

    public User(String idEmail, String keyUser, String firstName, String lastName) {
        this.idEmail = idEmail;
        this.keyUser = keyUser;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getIdEmail() {
        return idEmail;
    }

    public void setIdEmail(String idEmail) {
        this.idEmail = idEmail;
    }

    public String getKeyUser() {
        return keyUser;
    }

    public void setKeyUser(String keyUser) {
        this.keyUser = keyUser;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateBirthday() {
        return dateBirthday;
    }

    public void setDateBirthday(Date dateBirthday) {
        this.dateBirthday = dateBirthday;
    }

    public Account getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(Account idAccount) {
        this.idAccount = idAccount;
    }

    public TypeUser getIdType() {
        return idType;
    }

    public void setIdType(TypeUser idType) {
        this.idType = idType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEmail != null ? idEmail.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.idEmail == null && other.idEmail != null) || (this.idEmail != null && !this.idEmail.equals(other.idEmail))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.nagojudge.app.business.dao.entities.User[ idEmail=" + idEmail + " ]";
    }
    
}
