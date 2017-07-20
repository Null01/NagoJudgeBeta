/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.business.entity;

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
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author andresfelipegarciaduran
 */
@Entity
@Table(name = "CHALLENGE", catalog = "njlive", schema = "")
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
    @Size(min = 1, max = 255)
    @Column(name = "EMAIL_ORGANIZER")
    private String emailOrganizer;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DATE_START")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateStart;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DATE_END")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEnd;
    @Lob
    @Size(max = 65535)
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "DATE_CREATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idChallenge", fetch = FetchType.LAZY)
    private List<ChallengeTeam> challengeTeamList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idChallenge", fetch = FetchType.LAZY)
    private List<ChallengeSubmit> challengeSubmitList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idChallenge", fetch = FetchType.LAZY)
    private List<ChallengeProblem> challengeProblemList;

    public Challenge() {
    }

    public Challenge(Long idChallenge) {
        this.idChallenge = idChallenge;
    }

    public Challenge(Long idChallenge, String nameChallenge, String emailOrganizer, Date dateStart, Date dateEnd) {
        this.idChallenge = idChallenge;
        this.nameChallenge = nameChallenge;
        this.emailOrganizer = emailOrganizer;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
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

    public String getEmailOrganizer() {
        return emailOrganizer;
    }

    public void setEmailOrganizer(String emailOrganizer) {
        this.emailOrganizer = emailOrganizer;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @XmlTransient
    @JsonIgnore
    public List<ChallengeTeam> getChallengeTeamList() {
        return challengeTeamList;
    }

    public void setChallengeTeamList(List<ChallengeTeam> challengeTeamList) {
        this.challengeTeamList = challengeTeamList;
    }

    @XmlTransient
    @JsonIgnore
    public List<ChallengeSubmit> getChallengeSubmitList() {
        return challengeSubmitList;
    }

    public void setChallengeSubmitList(List<ChallengeSubmit> challengeSubmitList) {
        this.challengeSubmitList = challengeSubmitList;
    }

    @XmlTransient
    @JsonIgnore
    public List<ChallengeProblem> getChallengeProblemList() {
        return challengeProblemList;
    }

    public void setChallengeProblemList(List<ChallengeProblem> challengeProblemList) {
        this.challengeProblemList = challengeProblemList;
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
        return "edu.nagojudge.live.business.entity.Challenge[ idChallenge=" + idChallenge + " ]";
    }
    
}
