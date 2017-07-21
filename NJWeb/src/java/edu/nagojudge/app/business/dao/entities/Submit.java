/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.app.business.dao.entities;

import java.io.Serializable;
import java.math.BigInteger;
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
 * @author andresfelipegarciaduran
 */
@Entity
@Table(name = "SUBMIT", catalog = "njlive", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Submit.findAll", query = "SELECT s FROM Submit s")})
public class Submit implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_SUBMIT")
    private Long idSubmit;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DATE_SUBMIT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSubmit;
    @Column(name = "DATE_JUDGE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateJudge;
    @Column(name = "TIME_USED")
    private BigInteger timeUsed;
    @Column(name = "MEMO_USED")
    private BigInteger memoUsed;
    @Lob
    @Size(max = 65535)
    @Column(name = "MSG_JUDGE")
    private String msgJudge;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idSubmit", fetch = FetchType.LAZY)
    private List<AccountSubmit> accountSubmitList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idSubmit", fetch = FetchType.LAZY)
    private List<ChallengeSubmit> challengeSubmitList;
    @JoinColumn(name = "ID_LANGUAGE", referencedColumnName = "ID_LANGUAGE")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private LanguageProgramming idLanguage;
    @JoinColumn(name = "ID_PROBLEM", referencedColumnName = "ID_PROBLEM")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Problem idProblem;
    @JoinColumn(name = "ID_STATUS", referencedColumnName = "ID_STATUS")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private SubmitStatus idStatus;

    public Submit() {
    }

    public Submit(Long idSubmit) {
        this.idSubmit = idSubmit;
    }

    public Submit(Long idSubmit, Date dateSubmit) {
        this.idSubmit = idSubmit;
        this.dateSubmit = dateSubmit;
    }

    public Long getIdSubmit() {
        return idSubmit;
    }

    public void setIdSubmit(Long idSubmit) {
        this.idSubmit = idSubmit;
    }

    public Date getDateSubmit() {
        return dateSubmit;
    }

    public void setDateSubmit(Date dateSubmit) {
        this.dateSubmit = dateSubmit;
    }

    public Date getDateJudge() {
        return dateJudge;
    }

    public void setDateJudge(Date dateJudge) {
        this.dateJudge = dateJudge;
    }

    public BigInteger getTimeUsed() {
        return timeUsed;
    }

    public void setTimeUsed(BigInteger timeUsed) {
        this.timeUsed = timeUsed;
    }

    public BigInteger getMemoUsed() {
        return memoUsed;
    }

    public void setMemoUsed(BigInteger memoUsed) {
        this.memoUsed = memoUsed;
    }

    public String getMsgJudge() {
        return msgJudge;
    }

    public void setMsgJudge(String msgJudge) {
        this.msgJudge = msgJudge;
    }

    @XmlTransient
    public List<AccountSubmit> getAccountSubmitList() {
        return accountSubmitList;
    }

    public void setAccountSubmitList(List<AccountSubmit> accountSubmitList) {
        this.accountSubmitList = accountSubmitList;
    }

    @XmlTransient
    public List<ChallengeSubmit> getChallengeSubmitList() {
        return challengeSubmitList;
    }

    public void setChallengeSubmitList(List<ChallengeSubmit> challengeSubmitList) {
        this.challengeSubmitList = challengeSubmitList;
    }

    public LanguageProgramming getIdLanguage() {
        return idLanguage;
    }

    public void setIdLanguage(LanguageProgramming idLanguage) {
        this.idLanguage = idLanguage;
    }

    public Problem getIdProblem() {
        return idProblem;
    }

    public void setIdProblem(Problem idProblem) {
        this.idProblem = idProblem;
    }

    public SubmitStatus getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(SubmitStatus idStatus) {
        this.idStatus = idStatus;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSubmit != null ? idSubmit.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Submit)) {
            return false;
        }
        Submit other = (Submit) object;
        if ((this.idSubmit == null && other.idSubmit != null) || (this.idSubmit != null && !this.idSubmit.equals(other.idSubmit))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.nagojudge.app.business.dao.entities.Submit[ idSubmit=" + idSubmit + " ]";
    }
    
}
