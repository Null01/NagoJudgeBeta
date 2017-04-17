/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.msg.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author andresfelipegarciaduran
 */
@XmlRootElement
public class ChallengeMessage implements Serializable {

    private Long idChallenge;
    private String nameChallenge;
    private String emailOrganizer;
    private Date dateStart;
    private Date dateEnd;
    private String description;
    private Date dateCreated;
    private List<ProblemMessage> problemMessages;

    public ChallengeMessage() {
    }

    public ChallengeMessage(Long idChallenge, String nameChallenge, String emailOrganizer, Date dateStart, Date dateEnd, String description, Date dateCreated, List<ProblemMessage> problemMessages) {
        this.idChallenge = idChallenge;
        this.nameChallenge = nameChallenge;
        this.emailOrganizer = emailOrganizer;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.description = description;
        this.dateCreated = dateCreated;
        this.problemMessages = problemMessages;
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

    public List<ProblemMessage> getProblemMessages() {
        return problemMessages;
    }

    public void setProblemMessages(List<ProblemMessage> problemMessages) {
        this.problemMessages = problemMessages;
    }

}
