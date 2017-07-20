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
 * @author andresfelipegarciaduran
 */
@Entity
@Table(name = "DIFFICULTY_LEVEL", catalog = "njlive", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DifficultyLevel.findAll", query = "SELECT d FROM DifficultyLevel d")})
public class DifficultyLevel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_DIFFICULTY")
    private Integer idDifficulty;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "NAME_DIFFICULTY")
    private String nameDifficulty;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idDifficulty", fetch = FetchType.LAZY)
    private List<Problem> problemList;

    public DifficultyLevel() {
    }

    public DifficultyLevel(Integer idDifficulty) {
        this.idDifficulty = idDifficulty;
    }

    public DifficultyLevel(Integer idDifficulty, String nameDifficulty) {
        this.idDifficulty = idDifficulty;
        this.nameDifficulty = nameDifficulty;
    }

    public Integer getIdDifficulty() {
        return idDifficulty;
    }

    public void setIdDifficulty(Integer idDifficulty) {
        this.idDifficulty = idDifficulty;
    }

    public String getNameDifficulty() {
        return nameDifficulty;
    }

    public void setNameDifficulty(String nameDifficulty) {
        this.nameDifficulty = nameDifficulty;
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
        hash += (idDifficulty != null ? idDifficulty.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DifficultyLevel)) {
            return false;
        }
        DifficultyLevel other = (DifficultyLevel) object;
        if ((this.idDifficulty == null && other.idDifficulty != null) || (this.idDifficulty != null && !this.idDifficulty.equals(other.idDifficulty))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.nagojudge.live.business.entity.DifficultyLevel[ idDifficulty=" + idDifficulty + " ]";
    }
    
}
