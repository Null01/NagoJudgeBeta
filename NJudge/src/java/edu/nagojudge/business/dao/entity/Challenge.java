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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "CHALLENGE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Challenge.findAll", query = "SELECT c FROM Challenge c")})
public class Challenge implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_CHALLENGE")
    private Long idChallenge;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "NAME_CHALLENGE")
    private String nameChallenge;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_ACCOUNT_ORGANIZER")
    private long idAccountOrganizer;
    @Basic(optional = false)
    @NotNull
    @Column(name = "QUANTITY_PROBLEMS")
    private short quantityProblems;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DATE_CHALLENGE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateChallenge;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DURATION_MIN")
    private int durationMin;
    @Lob
    @Size(max = 65535)
    @Column(name = "DESCRIPTION")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "IDS_PROBLEMS")
    private String idsProblems;
    @JoinTable(name = "CHALLENGE_USER", joinColumns = {
        @JoinColumn(name = "ID_CHALLENGE", referencedColumnName = "ID_CHALLENGE")}, inverseJoinColumns = {
        @JoinColumn(name = "ID_TEAM_CONTEST", referencedColumnName = "ID_TEAM_CONTEST")})
    @ManyToMany(fetch = FetchType.LAZY)
    private List<TeamContest> teamContestList;

    public Challenge() {
    }

    public Challenge(Long idChallenge) {
        this.idChallenge = idChallenge;
    }

    public Challenge(Long idChallenge, String nameChallenge, long idAccountOrganizer, short quantityProblems, Date dateChallenge, int durationMin, String idsProblems) {
        this.idChallenge = idChallenge;
        this.nameChallenge = nameChallenge;
        this.idAccountOrganizer = idAccountOrganizer;
        this.quantityProblems = quantityProblems;
        this.dateChallenge = dateChallenge;
        this.durationMin = durationMin;
        this.idsProblems = idsProblems;
    }

    public Long getIdChallenge() {
        return idChallenge;
    }

    public void setIdChallenge(Long idChallenge) {
        this.idChallenge = idChallenge;
    }

    public String getNameChallenge() {
        return nameChallenge;
    }

    public void setNameChallenge(String nameChallenge) {
        this.nameChallenge = nameChallenge;
    }

    public long getIdAccountOrganizer() {
        return idAccountOrganizer;
    }

    public void setIdAccountOrganizer(long idAccountOrganizer) {
        this.idAccountOrganizer = idAccountOrganizer;
    }

    public short getQuantityProblems() {
        return quantityProblems;
    }

    public void setQuantityProblems(short quantityProblems) {
        this.quantityProblems = quantityProblems;
    }

    public Date getDateChallenge() {
        return dateChallenge;
    }

    public void setDateChallenge(Date dateChallenge) {
        this.dateChallenge = dateChallenge;
    }

    public int getDurationMin() {
        return durationMin;
    }

    public void setDurationMin(int durationMin) {
        this.durationMin = durationMin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIdsProblems() {
        return idsProblems;
    }

    public void setIdsProblems(String idsProblems) {
        this.idsProblems = idsProblems;
    }

    @XmlTransient
    public List<TeamContest> getTeamContestList() {
        return teamContestList;
    }

    public void setTeamContestList(List<TeamContest> teamContestList) {
        this.teamContestList = teamContestList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idChallenge != null ? idChallenge.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Challenge)) {
            return false;
        }
        Challenge other = (Challenge) object;
        if ((this.idChallenge == null && other.idChallenge != null) || (this.idChallenge != null && !this.idChallenge.equals(other.idChallenge))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.nagojudge.business.db.entity.Challenge[ idChallenge=" + idChallenge + " ]";
    }
    
}
