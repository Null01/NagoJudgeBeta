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
 * @author andres.garcia
 */
@Entity
@Table(name = "challenge_problem", catalog = "njlive", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ChallengeProblem.findAll", query = "SELECT c FROM ChallengeProblem c")})
public class ChallengeProblem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_CHALLENGE_PROBLEM")
    private Long idChallengeProblem;
    @JoinColumn(name = "ID_CHALLENGE", referencedColumnName = "ID_CHALLENGE")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Challenge idChallenge;
    @JoinColumn(name = "ID_PROBLEM", referencedColumnName = "ID_PROBLEM")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Problem idProblem;

    public ChallengeProblem() {
    }

    public ChallengeProblem(Long idChallengeProblem) {
        this.idChallengeProblem = idChallengeProblem;
    }

    public Long getIdChallengeProblem() {
        return idChallengeProblem;
    }

    public void setIdChallengeProblem(Long idChallengeProblem) {
        this.idChallengeProblem = idChallengeProblem;
    }

    public Challenge getIdChallenge() {
        return idChallenge;
    }

    public void setIdChallenge(Challenge idChallenge) {
        this.idChallenge = idChallenge;
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
        hash += (idChallengeProblem != null ? idChallengeProblem.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ChallengeProblem)) {
            return false;
        }
        ChallengeProblem other = (ChallengeProblem) object;
        if ((this.idChallengeProblem == null && other.idChallengeProblem != null) || (this.idChallengeProblem != null && !this.idChallengeProblem.equals(other.idChallengeProblem))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.nagojudge.business.dao.entity.ChallengeProblem[ idChallengeProblem=" + idChallengeProblem + " ]";
    }
    
}
