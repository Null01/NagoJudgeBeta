/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.web.mbeans;

import edu.nagojudge.app.business.dao.beans.TypeUserFacade;
import edu.nagojudge.app.business.dao.beans.UserFacade;
import edu.nagojudge.app.business.dao.entities.TypeUser;
import edu.nagojudge.app.utils.FacesUtil;
import edu.nagojudge.msg.pojo.AccountMessage;
import edu.nagojudge.msg.pojo.UserMessage;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import org.apache.log4j.Logger;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author andres.garcia
 */
@ManagedBean
@ViewScoped
public class UserCreateBean implements Serializable {

    private final Logger logger = Logger.getLogger(UserCreateBean.class);

    @EJB
    private UserFacade userFacade;

    @EJB
    private TypeUserFacade typeUserFacade;

    private final String TARGET_PATH = "/go/to/modules/role/user/search.xhtml";

    private TypeUser typeUser = new TypeUser();
    private List<SelectItem> typeUserItems;

    private UploadedFile imageUserFile;

    private AccountMessage accountMessage = new AccountMessage();
    private Date dateBirthdayUser = new Date();
    private UserMessage userMessage = new UserMessage();

    public UserCreateBean() {
    }

    @PostConstruct
    public void init() {
        this.typeUserItems = parceToSelectItemTypeUsers(typeUserFacade.findAll());
        userMessage.setKeyUser(userFacade.textAutoGenerateString());
    }

    public void actionCreateUser() {
        try {
            if (imageUserFile == null || imageUserFile.getSize() == 0) {
                FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, "Por favor, seleccione una image que lo identifique.");
            } else {
                userMessage.setDateBirthday(dateBirthdayUser.getTime());
                long idUser = userFacade.createUserCommon(userMessage, accountMessage, typeUser, imageUserFile.getContents());
                FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_INFO, "Creación exitosa. UserAccount #" + idUser);
                FacesUtil.getFacesUtil().redirect(TARGET_PATH);
            }
        } catch (IOException ex) {
            logger.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        }
    }

    public UserMessage getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(UserMessage userMessage) {
        this.userMessage = userMessage;
    }

    public TypeUser getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(TypeUser typeUser) {
        this.typeUser = typeUser;
    }

    public AccountMessage getAccountMessage() {
        return accountMessage;
    }

    public void setAccountMessage(AccountMessage accountMessage) {
        this.accountMessage = accountMessage;
    }

    public List<SelectItem> getTypeUserItems() {
        return typeUserItems;
    }

    public void setTypeUserItems(List<SelectItem> typeUserItems) {
        this.typeUserItems = typeUserItems;
    }

    public UploadedFile getImageUserFile() {
        return imageUserFile;
    }

    public void setImageUserFile(UploadedFile imageUserFile) {
        this.imageUserFile = imageUserFile;
    }

    private List<SelectItem> parceToSelectItemTypeUsers(List<TypeUser> findTypeUserEntities) {
        List<SelectItem> outcome = new ArrayList<SelectItem>();
        for (TypeUser s : findTypeUserEntities) {
            outcome.add(new SelectItem(s, s.getNameType()));
        }
        return outcome;
    }

    public Date getDateBirthdayUser() {
        return dateBirthdayUser;
    }

    public void setDateBirthdayUser(Date dateBirthdayUser) {
        this.dateBirthdayUser = dateBirthdayUser;
    }

}
