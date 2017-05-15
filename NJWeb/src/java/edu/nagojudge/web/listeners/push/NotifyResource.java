/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.web.listeners.push;

import edu.nagojudge.msg.pojo.SubmitMessage;
import org.apache.log4j.Logger;
import org.primefaces.push.annotation.OnMessage;
import org.primefaces.push.annotation.PushEndpoint;
import org.primefaces.push.impl.JSONEncoder;

/**
 *
 * @author andresfelipegarciaduran
 */
@PushEndpoint("/notifyBoardLive")
public class NotifyResource {

    private final Logger logger = Logger.getLogger(NotifyResource.class);

    @OnMessage(encoders = {JSONEncoder.class})
    public SubmitMessage onMessage(SubmitMessage submitMessage) {
        logger.debug("> request @echo");
        logger.debug(submitMessage);
        logger.debug(submitMessage.getAccountMessage().getIdAccount());
        logger.debug(submitMessage.getIdSubmit());
        return submitMessage;
    }
}
