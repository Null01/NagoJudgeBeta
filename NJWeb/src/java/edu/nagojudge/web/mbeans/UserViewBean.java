/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.web.mbeans;

import com.google.zxing.WriterException;
import edu.nagojudge.app.business.dao.beans.TypeUserFacade;
import edu.nagojudge.app.business.dao.beans.UserFacade;
import edu.nagojudge.app.business.dao.entities.Account;
import edu.nagojudge.app.business.dao.entities.TypeUser;
import edu.nagojudge.app.business.dao.entities.User;
import edu.nagojudge.app.exceptions.NagoJudgeException;
import edu.nagojudge.app.utils.FacesUtil;
import edu.nagojudge.msg.pojo.UserMessage;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.mail.MessagingException;
import org.apache.log4j.Logger;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author andresfelipegarciaduran
 */
@ManagedBean
@ViewScoped
public class UserViewBean implements Serializable {

    @EJB
    private TypeUserFacade typeUserFacade;

    @EJB
    private UserFacade userFacade;

    private final Logger logger = Logger.getLogger(UserViewBean.class);

    private final String TARGET_PATH = "/go/to/modules/user/search.xhtml";

    private List<UserMessage> listUsers = new ArrayList<UserMessage>();
    private List<UserMessage> filteredUsers = new ArrayList<UserMessage>();
    private User userView = new User();
    private Account accountView = new Account();
    private TypeUser typeUserView = new TypeUser();
    private List<SelectItem> typeUserItems;

    private String searchParameter;
    private UploadedFile imageUserFile;

    public UserViewBean() {
    }

    @PostConstruct
    public void init() {

        this.listUsers = userFacade.findAllUsersMessage();
        this.typeUserItems = parceToSelectItemTypeUsers(typeUserFacade.findAll());
        userView.setPasswordUser(userFacade.textAutoGenerateString());

    }

    public void actionCreateUser(ActionEvent event) {

        try {
            userView.setIdType(typeUserView);
            String idUserAccount = userFacade.createUserCommon(userView, accountView);

            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_INFO, "Creacion exitosa. UserAccount #" + idUserAccount);
            FacesUtil.getFacesUtil().redirect(TARGET_PATH);
        } catch (NagoJudgeException ex) {
            logger.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        } catch (WriterException ex) {
            logger.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        } catch (IOException ex) {
            logger.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        } catch (MessagingException ex) {
            logger.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        } catch (Exception ex) {
            logger.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        }

    }

    public String actionRedirectViewToProfile() {
        logger.debug("getIdAccount [" + accountView.getIdAccount() + "]");
        return "/modules/user/profile.xhtml?faces-redirect=true&includeViewParams=true";
    }

    public void actionChangePasswordGenerate(ActionEvent actionEvent) {
        userView.setPasswordUser(userFacade.textAutoGenerateString());
    }

    public List<UserMessage> getListUsers() {
        return listUsers;
    }

    public void setListUsers(List<UserMessage> listUsers) {
        this.listUsers = listUsers;
    }

    public List<UserMessage> getFilteredUsers() {
        return filteredUsers;
    }

    public void setFilteredUsers(List<UserMessage> filteredUsers) {
        this.filteredUsers = filteredUsers;
    }

    public User getUserView() {
        return userView;
    }

    public void setUserView(User userView) {
        this.userView = userView;
    }

    public Account getAccountView() {
        return accountView;
    }

    public void setAccountView(Account accountView) {
        this.accountView = accountView;
    }

    public TypeUser getTypeUserView() {
        return typeUserView;
    }

    public void setTypeUserView(TypeUser typeUserView) {
        this.typeUserView = typeUserView;
    }

    public List<SelectItem> getTypeUserItems() {
        return typeUserItems;
    }

    public void setTypeUserItems(List<SelectItem> typeUserItems) {
        this.typeUserItems = typeUserItems;
    }

    public String getSearchParameter() {
        return searchParameter;
    }

    public void setSearchParameter(String searchParameter) {
        this.searchParameter = searchParameter;
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

}
