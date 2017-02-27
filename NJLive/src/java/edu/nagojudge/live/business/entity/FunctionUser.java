/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.business.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author andres.garcia
 */
@Entity
@Table(name = "function_user", catalog = "njlive", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FunctionUser.findAll", query = "SELECT f FROM FunctionUser f")})
public class FunctionUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_FUNCTION")
    private Long idFunction;
    @Column(name = "ID_PARENT")
    private BigInteger idParent;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "NAME_FUNCTION")
    private String nameFunction;
    @Size(max = 255)
    @Column(name = "URL_FUNCTION")
    private String urlFunction;
    @ManyToMany(mappedBy = "functionUserList", fetch = FetchType.LAZY)
    private List<TypeUser> typeUserList;

    public FunctionUser() {
    }

    public FunctionUser(Long idFunction) {
        this.idFunction = idFunction;
    }

    public FunctionUser(Long idFunction, String nameFunction) {
        this.idFunction = idFunction;
        this.nameFunction = nameFunction;
    }

    public Long getIdFunction() {
        return idFunction;
    }

    public void setIdFunction(Long idFunction) {
        this.idFunction = idFunction;
    }

    public BigInteger getIdParent() {
        return idParent;
    }

    public void setIdParent(BigInteger idParent) {
        this.idParent = idParent;
    }

    public String getNameFunction() {
        return nameFunction;
    }

    public void setNameFunction(String nameFunction) {
        this.nameFunction = nameFunction;
    }

    public String getUrlFunction() {
        return urlFunction;
    }

    public void setUrlFunction(String urlFunction) {
        this.urlFunction = urlFunction;
    }

    @XmlTransient
    public List<TypeUser> getTypeUserList() {
        return typeUserList;
    }

    public void setTypeUserList(List<TypeUser> typeUserList) {
        this.typeUserList = typeUserList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFunction != null ? idFunction.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FunctionUser)) {
            return false;
        }
        FunctionUser other = (FunctionUser) object;
        if ((this.idFunction == null && other.idFunction != null) || (this.idFunction != null && !this.idFunction.equals(other.idFunction))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.nagojudge.live.business.entity.FunctionUser[ idFunction=" + idFunction + " ]";
    }
    
}
