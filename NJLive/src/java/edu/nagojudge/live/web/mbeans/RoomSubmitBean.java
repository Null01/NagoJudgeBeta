/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.web.mbeans;

import edu.nagojudge.live.business.entity.LanguageProgramming;
import edu.nagojudge.live.business.entity.facade.dao.ChallengeSubmitDAO;
import edu.nagojudge.live.business.entity.facade.dao.LanguageProgrammingDAO;
import edu.nagojudge.live.web.utils.FacesUtil;
import edu.nagojudge.live.web.utils.constants.IKeysApplication;
import edu.nagojudge.msg.pojo.LanguageProgrammingMessage;
import edu.nagojudge.msg.pojo.PairMessage;
import edu.nagojudge.msg.pojo.SubmitMessage;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author andres.garcia
 */
@ManagedBean
@ViewScoped
public class RoomSubmitBean implements Serializable {

    @EJB
    private LanguageProgrammingDAO languageProgrammingDAO;

    @EJB
    private ChallengeSubmitDAO challengeSubmitDAO;

    private final Logger logger = Logger.getLogger(RoomSubmitBean.class);

    private List<SelectItem> listProblemItems = new ArrayList<SelectItem>();
    private PairMessage pairMessageView = new PairMessage();

    private List<SelectItem> listLanguageProgrammingItems = new ArrayList<SelectItem>();
    private LanguageProgrammingMessage languageProgrammingMessage = new LanguageProgrammingMessage();

    private List<SubmitMessage> listSubmits = new ArrayList<SubmitMessage>();

    private Map<Long, String> mapColorsGlogs = new HashMap<Long, String>();
    private Map<Long, String> mapLettersGlobs = new HashMap<Long, String>();

    private UploadedFile fileSourceCode;

    public RoomSubmitBean() {
    }

    @PostConstruct
    public void init() {
        try {
            HttpSession currentSession = FacesUtil.getFacesUtil().getCurrentSession();
            Long challengeId = (Long) currentSession.getAttribute(IKeysApplication.KEY_SESSION_CHALLENGE_ID);
            Long teamId = (Long) currentSession.getAttribute(IKeysApplication.KEY_SESSION_TEAM_ID);

            listSubmits = new ArrayList<SubmitMessage>(challengeSubmitDAO.findAllSubmitByTeam(teamId, challengeId));

            mapColorsGlogs = FacesUtil.getFacesUtil().getCookieMap(IKeysApplication.KEY_COOKIE_GLOBES);
            mapLettersGlobs = FacesUtil.getFacesUtil().getCookieMap(IKeysApplication.KEY_COOKIE_LETTERS);

            listLanguageProgrammingItems = parseToLanguageProgrammingItems(languageProgrammingDAO.findAll());

            if (mapLettersGlobs != null) {
                listProblemItems = parseToPairChallengeProblemItems(mapLettersGlobs);
            }

        } catch (Exception ex) {
            logger.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        }
    }

    public void actionSubmitSourceCode() {
        try {
            logger.debug("INICIA METODO - actionSubmitSourceCode()");
            logger.debug("codeSourceFile [" + fileSourceCode + "]");
            if (fileSourceCode == null) {

            } else {

            }
        } catch (Exception ex) {
            logger.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        } finally {
            logger.debug("FINALIZA METODO - actionSubmitSourceCode()");
        }
    }

    public void actionHandleFileUpload(FileUploadEvent event) {
        logger.debug("INICIA METODO - actionHandleFileUpload()");
        logger.debug("[" + event.getFile() + "]");
        logger.debug("[" + event.getSource() + "]");
        if (fileSourceCode != null) {
            logger.debug("fileSourceCode [" + fileSourceCode + "]");
        }
        logger.debug("FINALIZA METODO - actionHandleFileUpload()");
    }

    public UploadedFile getFileSourceCode() {
        return fileSourceCode;
    }

    public List<SelectItem> getListProblemItems() {
        return listProblemItems;
    }

    public void setListProblemItems(List<SelectItem> listProblemItems) {
        this.listProblemItems = listProblemItems;
    }

    public PairMessage getPairMessageView() {
        return pairMessageView;
    }

    public void setPairMessageView(PairMessage pairMessageView) {
        this.pairMessageView = pairMessageView;
    }

    public void setFileSourceCode(UploadedFile fileSourceCode) {
        this.fileSourceCode = fileSourceCode;
    }

    public List<SubmitMessage> getListSubmits() {
        return listSubmits;
    }

    public void setListSubmits(List<SubmitMessage> listSubmits) {
        this.listSubmits = listSubmits;
    }

    public Map<Long, String> getMapColorsGlogs() {
        return mapColorsGlogs;
    }

    public void setMapColorsGlogs(Map<Long, String> mapColorsGlogs) {
        this.mapColorsGlogs = mapColorsGlogs;
    }

    public Map<Long, String> getMapLettersGlobs() {
        return mapLettersGlobs;
    }

    public void setMapLettersGlobs(Map<Long, String> mapLettersGlobs) {
        this.mapLettersGlobs = mapLettersGlobs;
    }

    public List<SelectItem> getListLanguageProgrammingItems() {
        return listLanguageProgrammingItems;
    }

    public void setListLanguageProgrammingItems(List<SelectItem> listLanguageProgrammingItems) {
        this.listLanguageProgrammingItems = listLanguageProgrammingItems;
    }

    public LanguageProgrammingMessage getLanguageProgrammingMessage() {
        return languageProgrammingMessage;
    }

    public void setLanguageProgrammingMessage(LanguageProgrammingMessage languageProgrammingMessage) {
        this.languageProgrammingMessage = languageProgrammingMessage;
    }

    private List<SelectItem> parseToLanguageProgrammingItems(List<LanguageProgramming> languageProgrammings) {
        List<SelectItem> outcome = new ArrayList<SelectItem>();
        for (LanguageProgramming s : languageProgrammings) {
            LanguageProgrammingMessage lpm = new LanguageProgrammingMessage();
            lpm.setIdLanguage(s.getIdLanguage());
            lpm.setNameProgramming(s.getNameLanguage());
            outcome.add(new SelectItem(lpm, lpm.getNameProgramming()));
        }
        return outcome;

    }

    private List<SelectItem> parseToPairChallengeProblemItems(Map<Long, String> mapLettersGlobs) {
        List<SelectItem> outcome = new ArrayList<SelectItem>();
        for (Map.Entry<Long, String> entry : mapLettersGlobs.entrySet()) {
            PairMessage pm = new PairMessage();
            pm.setFirst(String.valueOf(entry.getKey()));
            pm.setSecond(entry.getValue());
            outcome.add(new SelectItem(pm, pm.getSecond()));
        }
        return outcome;
    }

}
