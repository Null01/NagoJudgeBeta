/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.web.mbeans;

import com.google.zxing.WriterException;
import edu.nagojudge.app.business.dao.beans.TypeUserFacadeDAO;
import edu.nagojudge.app.business.dao.beans.UserFacadeDAO;
import edu.nagojudge.app.business.dao.complex.UserFacadeComplex;
import edu.nagojudge.app.business.dao.entities.Account;
import edu.nagojudge.app.business.dao.entities.TypeUser;
import edu.nagojudge.app.business.dao.entities.User;
import edu.nagojudge.app.exceptions.NagoJudgeException;
import edu.nagojudge.app.utils.FacesUtil;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.mail.MessagingException;
import javax.servlet.http.Part;
import org.apache.log4j.Logger;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author andresfelipegarciaduran
 */
@ManagedBean
@ViewScoped
public class UserBean implements Serializable {

    @EJB
    private UserFacadeComplex userFacadeComplex;

    @EJB
    private TypeUserFacadeDAO typeUserFacade;

    @EJB
    private UserFacadeDAO userFacade;

    private final Logger LOGGER = Logger.getLogger(UserBean.class);

    private final String TARGET_PATH = "/go/to/modules/user/search.xhtml";
    private final String KEYS_REQUEST[] = {"idProblem", "idAccount"};

    private List<User> listUsers = new ArrayList<User>();
    private List<User> filteredUsers;
    private User userView = new User();
    private Account accountView = new Account();
    private TypeUser typeUserView = new TypeUser();
    private List<SelectItem> typeUserItems;

    private String searchParameter;
    private UploadedFile imageUserFile;

    public UserBean() {
    }

    @PostConstruct
    public void init() {
        this.listUsers = userFacade.findAll();
        if (typeUserItems == null) {

            this.typeUserItems = parceToSelectItemTypeUsers(typeUserFacade.findAll());
        }
        userView.setKeyUser(userFacadeComplex.autoGenerateString());
    }

    public void actionCreateUser(ActionEvent event) {

        try {
            userView.setIdType(typeUserView);
            String idUserAccount = userFacadeComplex.createUserCommon(userView, accountView);
            clearObjects();
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_INFO, "Creacion exitosa. UserAccount #" + idUserAccount);
            FacesUtil.getFacesUtil().redirect(TARGET_PATH);
        } catch (NagoJudgeException ex) {
            LOGGER.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        } catch (WriterException ex) {
            LOGGER.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        } catch (IOException ex) {
            LOGGER.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        } catch (MessagingException ex) {
            LOGGER.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        }

    }

    public String actionRedirectViewToProfile() {
        LOGGER.debug("REDIRECT_ACCOUNT_VIEW=" + accountView.getIdAccount());
        FacesUtil.getFacesUtil().getFlash().put(KEYS_REQUEST[1], accountView.getIdAccount());
        return "/modules/user/profile.xhtml?faces-redirect=true";
    }

    public Account getAccountView() {
        return accountView;
    }

    public void setAccountView(Account accountView) {
        this.accountView = accountView;
    }

    public List<SelectItem> getTypeUserItems() {
        return typeUserItems;
    }

    public void setTypeUserItems(List<SelectItem> typeUserItems) {
        this.typeUserItems = typeUserItems;
    }

    public TypeUser getTypeUserView() {
        return typeUserView;
    }

    public void setTypeUserView(TypeUser typeUserView) {
        this.typeUserView = typeUserView;
    }

    public UploadedFile getImageUserFile() {
        return imageUserFile;
    }

    public void setImageUserFile(UploadedFile imageUserFile) {
        this.imageUserFile = imageUserFile;
    }

    public User getUserView() {
        return userView;
    }

    public void setUserView(User userView) {
        this.userView = userView;
    }

    public String getSearchParameter() {
        return searchParameter;
    }

    public void setSearchParameter(String searchParameter) {
        this.searchParameter = searchParameter;
    }

    public List<User> getFilteredUsers() {
        return filteredUsers;
    }

    public void setFilteredUsers(List<User> filteredUsers) {
        this.filteredUsers = filteredUsers;
    }

    public List<User> getListUsers() {
        return listUsers;
    }

    public void setListUsers(List<User> listUsers) {
        this.listUsers = listUsers;
    }

    private List<SelectItem> parceToSelectItemTypeUsers(List<TypeUser> findTypeUserEntities) {
        List<SelectItem> outcome = new ArrayList<SelectItem>();
        for (TypeUser s : findTypeUserEntities) {
            outcome.add(new SelectItem(s, s.getNameType()));
        }
        return outcome;
    }

    private void clearObjects() {
        userView = new User();
        accountView = new Account();
    }

}
