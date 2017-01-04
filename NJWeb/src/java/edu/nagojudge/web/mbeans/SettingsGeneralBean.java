/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.web.mbeans;

import edu.nagojudge.app.business.dao.beans.ParametersNjFacade;
import edu.nagojudge.app.business.dao.beans.SecurityFacadeDAO;
import edu.nagojudge.app.business.dao.entities.ParametersNj;
import edu.nagojudge.app.utils.FacesUtil;
import edu.nagojudge.msg.pojo.ParametersNJMessage;
import edu.nagojudge.tools.security.constants.TypeSHAEnum;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author andresfelipegarciaduran
 */
@ManagedBean
@ViewScoped
public class SettingsGeneralBean {

    private final Logger logger = Logger.getLogger(SettingsGeneralBean.class);

    @EJB
    private SecurityFacadeDAO securityFacadeDAO;

    @EJB
    private ParametersNjFacade parametersNjFacade;

    private List<ParametersNJMessage> listParametersNJ = new ArrayList<ParametersNJMessage>();

    private ParametersNJMessage parametersNJView = new ParametersNJMessage();

    private UploadedFile uploadedFileParameterBLOB;
    private String parameterString;

    private boolean selectParameterBLOBvsStr;

    private final List<String> KEYS_PUB = new ArrayList<String>();

    public SettingsGeneralBean() {
    }

    @PostConstruct
    public void init() {
        KEYS_PUB.add("KEY-PUB");
        this.listParametersNJ = new ArrayList<ParametersNJMessage>(parseEntityToMessage(parametersNjFacade.findAll()));
    }

    private List<ParametersNJMessage> parseEntityToMessage(List<ParametersNj> parametersNjs) {
        List<ParametersNJMessage> outcome = new ArrayList<ParametersNJMessage>();
        for (ParametersNj parametersNj : parametersNjs) {
            outcome.add(parseEntityToMessage(parametersNj));
        }
        return outcome;
    }

    private ParametersNJMessage parseEntityToMessage(ParametersNj parametersNj) {
        ParametersNJMessage nJMessage = new ParametersNJMessage();
        nJMessage.setContentParameter(parametersNj.getContentParameter());
        nJMessage.setDateCreated(parametersNj.getDateCreated());
        nJMessage.setDateUpdated(parametersNj.getDateUpdated());
        nJMessage.setDescription(parametersNj.getDescription());
        nJMessage.setIdParameter(parametersNj.getIdParameter());
        return nJMessage;
    }

    public void actionListerFileUpload(FileUploadEvent event) {
        this.uploadedFileParameterBLOB = event.getFile();
        logger.debug("uploadedFileParameterBLOB [" + uploadedFileParameterBLOB.getFileName() + "]");
    }

    public void actionViewEditParameter(Object parameter) {
        this.parametersNJView = (ParametersNJMessage) parameter;
        parameterString = String.valueOf(parametersNJView.getContentParameter());
        logger.debug("getIdParameter-view [" + parametersNJView.getIdParameter() + "]");
    }

    public void actionEditParameter() {
        final String MESSAGE_SUCCESSFUL = "El parametro fue modificado, exitosamente.";
        try {
            if (!this.selectParameterBLOBvsStr) { // STRING
                Object encodeAESParameterNJ = null;
                if (KEYS_PUB.contains(parametersNJView.getIdParameter())) {
                    String encodeSHA2 = securityFacadeDAO.encodeSHA2(parameterString, TypeSHAEnum.SHA256_NUM);
                    encodeAESParameterNJ = encodeSHA2.getBytes();
                } else {
                    encodeAESParameterNJ = securityFacadeDAO.encodeAESParameterNJ(parameterString);
                }
                parametersNJView.setContentParameter((byte[]) encodeAESParameterNJ);
            } else { // BLOB
                parametersNJView.setContentParameter(uploadedFileParameterBLOB.getContents());
            }

            parametersNjFacade.updateParametersNJ(parametersNJView);
            parameterString = String.valueOf(parametersNJView.getContentParameter());
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_INFO, MESSAGE_SUCCESSFUL);
        } catch (Exception ex) {
            logger.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        } finally {
            this.listParametersNJ = new ArrayList<ParametersNJMessage>(parseEntityToMessage(parametersNjFacade.findAll()));
        }
    }

    public List<ParametersNJMessage> getListParametersNJ() {
        return listParametersNJ;
    }

    public void setListParametersNJ(List<ParametersNJMessage> listParametersNJ) {
        this.listParametersNJ = listParametersNJ;
    }

    public ParametersNJMessage getParametersNJView() {
        return parametersNJView;
    }

    public void setParametersNJView(ParametersNJMessage parametersNJView) {
        this.parametersNJView = parametersNJView;
    }

    public UploadedFile getUploadedFileParameterBLOB() {
        return uploadedFileParameterBLOB;
    }

    public void setUploadedFileParameterBLOB(UploadedFile uploadedFileParameterBLOB) {
        this.uploadedFileParameterBLOB = uploadedFileParameterBLOB;
    }

    public String getParameterString() {
        return parameterString;
    }

    public void setParameterString(String parameterString) {
        this.parameterString = parameterString;
    }

    public boolean isSelectParameterBLOBvsStr() {
        return selectParameterBLOBvsStr;
    }

    public void setSelectParameterBLOBvsStr(boolean selectParameterBLOBvsStr) {
        this.selectParameterBLOBvsStr = selectParameterBLOBvsStr;
    }

}
