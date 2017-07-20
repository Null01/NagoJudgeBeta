/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.web.mbeans;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

/**
 *
 * @author andresfelipegarciaduran
 */
@ManagedBean
@ViewScoped
public class NotifyPushBean implements Serializable {

    private static final Logger logger = Logger.getLogger(NotifyPushBean.class);

    private final static String CHANNEL = "/notifyBoardLive";

    public NotifyPushBean() {
    }

    public void actionSendGlobal() {
        try {
            logger.debug("INICIA METODO - actionSendPushBoardLive()");
            EventBus eventBus = EventBusFactory.getDefault().eventBus();
            eventBus.publish(CHANNEL + "*", "usuario" + ": " + "glocalMessage");
        } catch (Exception ex) {
            logger.error(ex);
        } finally {
            logger.debug("FINALIZA METODO - actionSendPushBoardLive()");
        }
    }
}
