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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
 * @author andres.garcia
 */
@Entity
@Table(name = "problem", catalog = "njlive", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Problem.findAll", query = "SELECT p FROM Problem p")})
public class Problem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_PROBLEM")
    private Long idProblem;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "NAME_PROBLEM")
    private String nameProblem;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "AUTHOR")
    private String author;
    @Lob
    @Size(max = 65535)
    @Column(name = "DESCRIPTION")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TIME_LIMIT_SEG")
    private int timeLimitSeg;
    @Column(name = "DATE_CREATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @ManyToMany(mappedBy = "problemList", fetch = FetchType.LAZY)
    private List<Challenge> challengeList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idProblem", fetch = FetchType.LAZY)
    private List<Attachments> attachmentsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idProblem", fetch = FetchType.LAZY)
    private List<Submit> submitList;
    @JoinColumn(name = "ID_COMPLEXITY_ALGORITHM_MAX", referencedColumnName = "ID_COMPLEXITY_ALGORITHM")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ComplexityAlgorithm idComplexityAlgorithmMax;
    @JoinColumn(name = "ID_DIFFICULTY", referencedColumnName = "ID_DIFFICULTY")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private DifficultyLevel idDifficulty;
    @JoinColumn(name = "ID_CATEGORY", referencedColumnName = "ID_CATEGORY")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private CategoryProblem idCategory;

    public Problem() {
    }

    public Problem(Long idProblem) {
        this.idProblem = idProblem;
    }

    public Problem(Long idProblem, String nameProblem, String author, int timeLimitSeg) {
        this.idProblem = idProblem;
        this.nameProblem = nameProblem;
        this.author = author;
        this.timeLimitSeg = timeLimitSeg;
    }

    public Long getIdProblem() {
        return idProblem;
    }

    public void setIdProblem(Long idProblem) {
        this.idProblem = idProblem;
    }

    public String getNameProblem() {
        return nameProblem;
    }

    public void setNameProblem(String nameProblem) {
        this.nameProblem = nameProblem;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTimeLimitSeg() {
        return timeLimitSeg;
    }

    public void setTimeLimitSeg(int timeLimitSeg) {
        this.timeLimitSeg = timeLimitSeg;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @XmlTransient
    public List<Challenge> getChallengeList() {
        return challengeList;
    }

    public void setChallengeList(List<Challenge> challengeList) {
        this.challengeList = challengeList;
    }

    @XmlTransient
    public List<Attachments> getAttachmentsList() {
        return attachmentsList;
    }

    public void setAttachmentsList(List<Attachments> attachmentsList) {
        this.attachmentsList = attachmentsList;
    }

    @XmlTransient
    public List<Submit> getSubmitList() {
        return submitList;
    }

    public void setSubmitList(List<Submit> submitList) {
        this.submitList = submitList;
    }

    public ComplexityAlgorithm getIdComplexityAlgorithmMax() {
        return idComplexityAlgorithmMax;
    }

    public void setIdComplexityAlgorithmMax(ComplexityAlgorithm idComplexityAlgorithmMax) {
        this.idComplexityAlgorithmMax = idComplexityAlgorithmMax;
    }

    public DifficultyLevel getIdDifficulty() {
        return idDifficulty;
    }

    public void setIdDifficulty(DifficultyLevel idDifficulty) {
        this.idDifficulty = idDifficulty;
    }

    public CategoryProblem getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(CategoryProblem idCategory) {
        this.idCategory = idCategory;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProblem != null ? idProblem.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Problem)) {
            return false;
        }
        Problem other = (Problem) object;
        if ((this.idProblem == null && other.idProblem != null) || (this.idProblem != null && !this.idProblem.equals(other.idProblem))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.nagojudge.app.business.dao.entities.Problem[ idProblem=" + idProblem + " ]";
    }
    
}
