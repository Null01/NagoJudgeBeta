/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.web.listeners.push;

import edu.nagojudge.msg.pojo.NotifyMessage;
import edu.nagojudge.msg.pojo.SubmitMessage;
import edu.nagojudge.msg.pojo.constants.TypeActionEnum;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.apache.log4j.Logger;
import org.primefaces.push.annotation.OnMessage;
import org.primefaces.push.annotation.PathParam;
import org.primefaces.push.annotation.PushEndpoint;
import org.primefaces.push.impl.JSONEncoder;

/**
 *
 * @author andresfelipegarciaduran
 */
@PushEndpoint("/notifyAddSubmitToContest/{challengeId}")
public class NotifySubmitContest {

    private static final Logger logger = Logger.getLogger(NotifySubmitContest.class);

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");

    @PathParam("challengeId")
    private String challengeId;

    @OnMessage(encoders = {JSONEncoder.class})
    public NotifyMessage onMessage(SubmitMessage submitMessage) {
        NotifyMessage notifyMessage = new NotifyMessage();
        try {
            notifyMessage.setId(submitMessage.getRowIdMetadata());
            notifyMessage.setAction(submitMessage.getRowActionMetadata() == null ? TypeActionEnum.CREATE.name() : submitMessage.getRowActionMetadata().name());
            notifyMessage.setHtml("<tr id=\"" + submitMessage.getRowIdMetadata() + "\"  class=\"ui-widget-content " + getNameColorHTML(submitMessage.getJudgeMessage().getKeyStatus()) + "\" role=\"row\" >"
                    + "<td class=\"text-center\" role=\"gridcell\" >" + ":param_1" + "</td>"
                    + "<td class=\"text-center\" role=\"gridcell\" >" + submitMessage.getIdSubmit() + "</td>"
                    + "<td class=\"text-center\" role=\"gridcell\" >" + submitMessage.getRowLetterMetadata() + "</td>"
                    + "<td class=\"text-left\" role=\"gridcell\" >" + submitMessage.getProblemMessage().getNameProblem() + "</td>"
                    + "<td class=\"text-left\" role=\"gridcell\" >" + submitMessage.getTeamMessage().getNameTeam() + "</td>"
                    + "<td class=\"text-center\" role=\"gridcell\" >" + submitMessage.getDateJudge() + "</td>"
                    + "<td class=\"text-center\" role=\"gridcell\" >" + submitMessage.getJudgeMessage().getStatusName() + "</td>"
                    + "</tr>");
            logger.debug("> [" + submitMessage + "]");
        } catch (Exception ex) {
            logger.error(ex);
        }
        return notifyMessage;
    }

    private String getNameColorHTML(String status) {
        if (status.compareTo("AC") == 0) {
            return "success";
        }
        if (status.compareTo("WR") == 0) {
            return "danger";
        }
        if (status.compareTo("TL") == 0) {
            return "info";
        }
        if (status.compareTo("RE") == 0) {
            return "warning";
        }
        if (status.compareTo("CE") == 0) {
            return "ce";
        }
        return "";
    }

    public String getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(String challengeId) {
        this.challengeId = challengeId;
    }

}
