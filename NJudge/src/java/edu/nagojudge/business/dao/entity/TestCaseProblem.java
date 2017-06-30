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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author andresfelipegarciaduran
 */
@Entity
@Table(name = "TEST_CASE_PROBLEM", catalog = "njlive", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TestCaseProblem.findAll", query = "SELECT t FROM TestCaseProblem t")})
public class TestCaseProblem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_TEST_CASE_PROBLEM")
    private Long idTestCaseProblem;
    @JoinColumn(name = "ID_PROBLEM", referencedColumnName = "ID_PROBLEM")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Problem idProblem;
    @JoinColumn(name = "ID_TEST_CASE", referencedColumnName = "ID_TEST_CASE")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TestCase idTestCase;

    public TestCaseProblem() {
    }

    public TestCaseProblem(Long idTestCaseProblem) {
        this.idTestCaseProblem = idTestCaseProblem;
    }

    public Long getIdTestCaseProblem() {
        return idTestCaseProblem;
    }

    public void setIdTestCaseProblem(Long idTestCaseProblem) {
        this.idTestCaseProblem = idTestCaseProblem;
    }

    public Problem getIdProblem() {
        return idProblem;
    }

    public void setIdProblem(Problem idProblem) {
        this.idProblem = idProblem;
    }

    public TestCase getIdTestCase() {
        return idTestCase;
    }

    public void setIdTestCase(TestCase idTestCase) {
        this.idTestCase = idTestCase;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTestCaseProblem != null ? idTestCaseProblem.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TestCaseProblem)) {
            return false;
        }
        TestCaseProblem other = (TestCaseProblem) object;
        if ((this.idTestCaseProblem == null && other.idTestCaseProblem != null) || (this.idTestCaseProblem != null && !this.idTestCaseProblem.equals(other.idTestCaseProblem))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.nagojudge.business.dao.entity.TestCaseProblem[ idTestCaseProblem=" + idTestCaseProblem + " ]";
    }
    
}
