/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.web.utils.clients;

import edu.nagojudge.msg.pojo.SubmitMessage;
import edu.nagojudge.msg.pojo.constants.TypeActionEnum;
import java.util.Map;
import org.apache.log4j.Logger;
import org.primefaces.push.EventBus;

/**
 *
 * @author andresfelipegarciaduran
 */
public class NotifyPushClientService extends Thread {

    private static final Logger logger = Logger.getLogger(NotifyPushClientService.class);

    private final String channel;
    private final EventBus eventBus;
    private final String host, path;
    private final Object[] objects;
    private final Map<String, Object> params;
    private final Class t;

    private SubmitMessage submitMessage;

    public NotifyPushClientService(String channel, EventBus eventBus, String host, String path, Object[] objects, Map<String, Object> params, Class t) {
        this.channel = channel;
        this.eventBus = eventBus;
        this.host = host;
        this.path = path;
        this.objects = objects;
        this.params = params;
        this.t = t;
    }

    @Override
    public synchronized void start() {
        super.start();
    }

    @Override
    public void run() {
        try {
            final int seconds = 5;
            logger.debug("INICIA PUSH NOTIFY - " + channel);
            logger.debug("> [" + this.submitMessage + "]");
            eventBus.publish(channel, this.submitMessage);
            logger.debug("publish [" + channel + "] @ECHO");
            NotifyPushClientService.sleep(1000 * seconds);
            logger.debug("sleeep on publish [" + channel + "] to [" + seconds + "] @ECHO");
            final SubmitMessage message = (SubmitMessage) ClientService.getInstance().callRestfulGet(host, path, objects, params, t);
            logger.debug("return response client  [" + message + "] @ECHO");
            message.setRowIdMetadata(this.submitMessage.getRowIdMetadata());
            message.setRowActionMetadata(TypeActionEnum.REPLACE);
            message.setRowLetterMetadata(this.submitMessage.getRowLetterMetadata());
            eventBus.publish(channel, message);
            logger.debug("publish [" + channel + "] @ECHO");
        } catch (InterruptedException ex) {
            logger.error(ex);
        } finally {
            logger.debug("FINALIZA PUSH NOTIFY - " + channel);
        }
    }

    public void setSubmitMessage(SubmitMessage submitMessage) {
        this.submitMessage = submitMessage;
    }

}
