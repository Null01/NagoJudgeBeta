/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.web.mbeans;

import edu.nagojudge.live.web.utils.FacesUtil;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
@ManagedBean
@ViewScoped
public class ClientBoardBean {

    private final Logger logger = Logger.getLogger(ClientBoardBean.class);

    private boolean showProblems, showScore, showSubmits;

    private String key, id;

    public ClientBoardBean() {
    }

    @PostConstruct
    public void init() {
        this.showProblems = false;
        this.showSubmits = false;
        this.showScore = true;
    }

    public void actionShowPanelProblems(ActionEvent event) {
        this.showProblems = true;
        this.showSubmits = false;
        this.showScore = false;
        logger.debug("actionShowPanelProblems()");
    }

    public void actionShowPanelSubmits(ActionEvent event) {
        this.showProblems = false;
        this.showSubmits = true;
        this.showScore = false;
        logger.debug("actionShowPanelSubmits()");
    }

    public void actionShowPanelScore(ActionEvent event) {
        this.showProblems = false;
        this.showSubmits = false;
        this.showScore = true;
        logger.debug("actionShowPanelScore()");
    }

    public void actionListenerPreRender() {
        boolean validationFailed = FacesUtil.getFacesUtil().isValidationFailed();
        if (validationFailed) {
            logger.error("params {" + this.key + ", " + this.id + "}");
            FacesUtil.getFacesUtil().printErrorResponse("Authentication failed, contact administrator.");
        }
    }

    public boolean isShowProblems() {
        return showProblems;
    }

    public boolean isShowScore() {
        return showScore;
    }

    public boolean isShowSubmits() {
        return showSubmits;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
