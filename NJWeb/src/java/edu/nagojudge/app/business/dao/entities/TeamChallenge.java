/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.app.business.dao.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author andresfelipegarciaduran
 */
@Entity
@Table(name = "TEAM_CHALLENGE", catalog = "njlive", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TeamChallenge.findAll", query = "SELECT t FROM TeamChallenge t")})
public class TeamChallenge implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "ID_NAME_TEAM_CHAL")
    private String idNameTeamChal;
    @Column(name = "DATE_REGISTER")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateRegister;

    public TeamChallenge() {
    }

    public TeamChallenge(String idNameTeamChal) {
        this.idNameTeamChal = idNameTeamChal;
    }

    public String getIdNameTeamChal() {
        return idNameTeamChal;
    }

    public void setIdNameTeamChal(String idNameTeamChal) {
        this.idNameTeamChal = idNameTeamChal;
    }

    public Date getDateRegister() {
        return dateRegister;
    }

    public void setDateRegister(Date dateRegister) {
        this.dateRegister = dateRegister;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idNameTeamChal != null ? idNameTeamChal.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TeamChallenge)) {
            return false;
        }
        TeamChallenge other = (TeamChallenge) object;
        if ((this.idNameTeamChal == null && other.idNameTeamChal != null) || (this.idNameTeamChal != null && !this.idNameTeamChal.equals(other.idNameTeamChal))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.nagojudge.app.business.dao.entities.TeamChallenge[ idNameTeamChal=" + idNameTeamChal + " ]";
    }
    
}
