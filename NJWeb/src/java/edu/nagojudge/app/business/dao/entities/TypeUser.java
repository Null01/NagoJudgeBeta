/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.app.business.dao.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author andresfelipegarciaduran
 */
@Entity
@Table(name = "TYPE_USER", catalog = "njlive", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TypeUser.findAll", query = "SELECT t FROM TypeUser t")})
public class TypeUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_TYPE")
    private Long idType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "NAME_TYPE")
    private String nameType;
    @JoinTable(name = "ACCESS_USER", joinColumns = {
        @JoinColumn(name = "ID_TYPE", referencedColumnName = "ID_TYPE")}, inverseJoinColumns = {
        @JoinColumn(name = "ID_FUNCTION", referencedColumnName = "ID_FUNCTION")})
    @ManyToMany(fetch = FetchType.LAZY)
    private List<FunctionUser> functionUserList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idType", fetch = FetchType.LAZY)
    private List<User> userList;

    public TypeUser() {
    }

    public TypeUser(Long idType) {
        this.idType = idType;
    }

    public TypeUser(Long idType, String nameType) {
        this.idType = idType;
        this.nameType = nameType;
    }

    public Long getIdType() {
        return idType;
    }

    public void setIdType(Long idType) {
        this.idType = idType;
    }

    public String getNameType() {
        return nameType;
    }

    public void setNameType(String nameType) {
        this.nameType = nameType;
    }

    @XmlTransient
    public List<FunctionUser> getFunctionUserList() {
        return functionUserList;
    }

    public void setFunctionUserList(List<FunctionUser> functionUserList) {
        this.functionUserList = functionUserList;
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
        hash += (idType != null ? idType.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TypeUser)) {
            return false;
        }
        TypeUser other = (TypeUser) object;
        if ((this.idType == null && other.idType != null) || (this.idType != null && !this.idType.equals(other.idType))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.nagojudge.app.business.dao.entities.TypeUser[ idType=" + idType + " ]";
    }
    
}
