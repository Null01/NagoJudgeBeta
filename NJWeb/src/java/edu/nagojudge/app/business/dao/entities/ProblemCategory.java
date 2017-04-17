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
@Table(name = "problem_category", catalog = "njlive", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProblemCategory.findAll", query = "SELECT p FROM ProblemCategory p")})
public class ProblemCategory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_PROBLEM_CATEGORY")
    private Long idProblemCategory;
    @JoinColumn(name = "ID_CATEGORY", referencedColumnName = "ID_CATEGORY")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Category idCategory;
    @JoinColumn(name = "ID_PROBLEM", referencedColumnName = "ID_PROBLEM")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Problem idProblem;

    public ProblemCategory() {
    }

    public ProblemCategory(Long idProblemCategory) {
        this.idProblemCategory = idProblemCategory;
    }

    public Long getIdProblemCategory() {
        return idProblemCategory;
    }

    public void setIdProblemCategory(Long idProblemCategory) {
        this.idProblemCategory = idProblemCategory;
    }

    public Category getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(Category idCategory) {
        this.idCategory = idCategory;
    }

    public Problem getIdProblem() {
        return idProblem;
    }

    public void setIdProblem(Problem idProblem) {
        this.idProblem = idProblem;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProblemCategory != null ? idProblemCategory.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProblemCategory)) {
            return false;
        }
        ProblemCategory other = (ProblemCategory) object;
        if ((this.idProblemCategory == null && other.idProblemCategory != null) || (this.idProblemCategory != null && !this.idProblemCategory.equals(other.idProblemCategory))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.nagojudge.app.business.dao.entities.ProblemCategory[ idProblemCategory=" + idProblemCategory + " ]";
    }
    
}
