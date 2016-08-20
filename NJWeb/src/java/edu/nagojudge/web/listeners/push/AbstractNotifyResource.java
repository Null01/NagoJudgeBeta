/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.web.listeners.push;

import edu.nagojudge.msg.pojo.SubmitMessage;
import java.io.Serializable;
import org.apache.log4j.Logger;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

/**
 *
 * @author andresfelipegarciaduran
 */
public class AbstractNotifyResource implements Serializable {

    private final Logger logger = Logger.getLogger(AbstractNotifyResource.class);

    private final static String CHANNEL_BOARD_LIVE = "/notifyBoardLive";

    public void pushNotify(SubmitMessage submitMessage) {
        try {
            EventBus eventBus = EventBusFactory.getDefault().eventBus();
            eventBus.publish(CHANNEL_BOARD_LIVE, submitMessage);
        } catch (Exception ex) {
            logger.error(ex);
        }
    }
}
