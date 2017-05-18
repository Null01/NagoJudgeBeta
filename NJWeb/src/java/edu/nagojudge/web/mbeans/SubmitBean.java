/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.web.mbeans;

import edu.nagojudge.app.business.dao.beans.SubmitFacade;
import edu.nagojudge.app.business.dao.entities.LanguageProgramming;
import edu.nagojudge.app.business.dao.entities.Problem;
import edu.nagojudge.app.business.dao.entities.Submit;
import edu.nagojudge.msg.pojo.SubmitMessage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;
import org.primefaces.model.UploadedFile;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

/**
 *
 * @author andresfelipegarciaduran
 */
@ManagedBean
@ViewScoped
public class SubmitBean implements Serializable {

    @EJB
    private SubmitFacade submitFacade;

    private final String TARGET_PATH = "/go/to/modules/board/score.xhtml";

    private final String CHANNEL_BOARD_LIVE = "/notifyBoardLive";

    private final Logger logger = Logger.getLogger(SubmitBean.class);

    private List<SubmitMessage> submitsAll = new ArrayList<SubmitMessage>();

    private Problem problemView = new Problem();

    private Submit submitView = new Submit();
    private LanguageProgramming languageProgrammingView = new LanguageProgramming();

    private UploadedFile codeSourceFile;

    /**
     * Creates a new instance of SubmitBean
     */
    public SubmitBean() {
    }

    @PostConstruct
    public void init() {
        if (this.submitsAll == null || this.submitsAll.isEmpty()) {
            this.submitsAll = submitFacade.findLast100Results();
        }
    }

    public Problem getProblemView() {
        return problemView;
    }

    public void setProblemView(Problem problemView) {
        this.problemView = problemView;
    }

    public LanguageProgramming getLanguageProgrammingView() {
        return languageProgrammingView;
    }

    public void setLanguageProgrammingView(LanguageProgramming languageProgrammingView) {
        this.languageProgrammingView = languageProgrammingView;
    }

    public Submit getSubmitView() {
        return submitView;
    }

    public void setSubmitView(Submit submitView) {
        this.submitView = submitView;
    }

    public List<SubmitMessage> getSubmitsAll() {
        return submitsAll;
    }

    public void setSubmitsAll(List<SubmitMessage> submitsAll) {
        this.submitsAll = submitsAll;
    }

    public UploadedFile getCodeSourceFile() {
        return codeSourceFile;
    }

    public void setCodeSourceFile(UploadedFile codeSourceFile) {
        this.codeSourceFile = codeSourceFile;
    }

    private void actionSendPushBoardLive(SubmitMessage submitMessage) {
        try {
            logger.debug("INICIA METODO - actionSendPushBoardLive()");
            EventBus eventBus = EventBusFactory.getDefault().eventBus();
            eventBus.publish(CHANNEL_BOARD_LIVE, submitMessage);
        } catch (Exception ex) {
            logger.error(ex);
        } finally {
            logger.debug("FINALIZA METODO - actionSendPushBoardLive()");
        }
    }

}
