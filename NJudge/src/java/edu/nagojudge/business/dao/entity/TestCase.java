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
import javax.persistence.Lob;
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
@Table(name = "test_case", catalog = "njlive", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TestCase.findAll", query = "SELECT t FROM TestCase t")})
public class TestCase implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_TEST_CASE")
    private Long idTestCase;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "NAME_CASE")
    private String nameCase;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "DATA_IN")
    private String dataIn;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "DATA_OUT")
    private String dataOut;
    @Lob
    @Size(max = 65535)
    @Column(name = "DESCRIPTION_FAILURE")
    private String descriptionFailure;
    @Column(name = "DATE_CREATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTestCase", fetch = FetchType.LAZY)
    private List<TestCaseProblem> testCaseProblemList;

    public TestCase() {
    }

    public TestCase(Long idTestCase) {
        this.idTestCase = idTestCase;
    }

    public TestCase(Long idTestCase, String nameCase, String dataIn, String dataOut) {
        this.idTestCase = idTestCase;
        this.nameCase = nameCase;
        this.dataIn = dataIn;
        this.dataOut = dataOut;
    }

    public Long getIdTestCase() {
        return idTestCase;
    }

    public void setIdTestCase(Long idTestCase) {
        this.idTestCase = idTestCase;
    }

    public String getNameCase() {
        return nameCase;
    }

    public void setNameCase(String nameCase) {
        this.nameCase = nameCase;
    }

    public String getDataIn() {
        return dataIn;
    }

    public void setDataIn(String dataIn) {
        this.dataIn = dataIn;
    }

    public String getDataOut() {
        return dataOut;
    }

    public void setDataOut(String dataOut) {
        this.dataOut = dataOut;
    }

    public String getDescriptionFailure() {
        return descriptionFailure;
    }

    public void setDescriptionFailure(String descriptionFailure) {
        this.descriptionFailure = descriptionFailure;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @XmlTransient
    public List<TestCaseProblem> getTestCaseProblemList() {
        return testCaseProblemList;
    }

    public void setTestCaseProblemList(List<TestCaseProblem> testCaseProblemList) {
        this.testCaseProblemList = testCaseProblemList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTestCase != null ? idTestCase.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TestCase)) {
            return false;
        }
        TestCase other = (TestCase) object;
        if ((this.idTestCase == null && other.idTestCase != null) || (this.idTestCase != null && !this.idTestCase.equals(other.idTestCase))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.nagojudge.business.dao.entity.TestCase[ idTestCase=" + idTestCase + " ]";
    }
    
}
