/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.business.entity;

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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author andres.garcia
 */
@Entity
@Table(name = "complexity_algorithm", catalog = "njlive", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ComplexityAlgorithm.findAll", query = "SELECT c FROM ComplexityAlgorithm c")})
public class ComplexityAlgorithm implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_COMPLEXITY_ALGORITHM")
    private Integer idComplexityAlgorithm;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "NAME_COMPLEXITY_ALGORITHM")
    private String nameComplexityAlgorithm;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idComplexityAlgorithm", fetch = FetchType.LAZY)
    private List<Problem> problemList;

    public ComplexityAlgorithm() {
    }

    public ComplexityAlgorithm(Integer idComplexityAlgorithm) {
        this.idComplexityAlgorithm = idComplexityAlgorithm;
    }

    public ComplexityAlgorithm(Integer idComplexityAlgorithm, String nameComplexityAlgorithm) {
        this.idComplexityAlgorithm = idComplexityAlgorithm;
        this.nameComplexityAlgorithm = nameComplexityAlgorithm;
    }

    public Integer getIdComplexityAlgorithm() {
        return idComplexityAlgorithm;
    }

    public void setIdComplexityAlgorithm(Integer idComplexityAlgorithm) {
        this.idComplexityAlgorithm = idComplexityAlgorithm;
    }

    public String getNameComplexityAlgorithm() {
        return nameComplexityAlgorithm;
    }

    public void setNameComplexityAlgorithm(String nameComplexityAlgorithm) {
        this.nameComplexityAlgorithm = nameComplexityAlgorithm;
    }

    @XmlTransient
    @JsonIgnore
    public List<Problem> getProblemList() {
        return problemList;
    }

    public void setProblemList(List<Problem> problemList) {
        this.problemList = problemList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idComplexityAlgorithm != null ? idComplexityAlgorithm.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComplexityAlgorithm)) {
            return false;
        }
        ComplexityAlgorithm other = (ComplexityAlgorithm) object;
        if ((this.idComplexityAlgorithm == null && other.idComplexityAlgorithm != null) || (this.idComplexityAlgorithm != null && !this.idComplexityAlgorithm.equals(other.idComplexityAlgorithm))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.nagojudge.live.business.entity.ComplexityAlgorithm[ idComplexityAlgorithm=" + idComplexityAlgorithm + " ]";
    }
    
}
