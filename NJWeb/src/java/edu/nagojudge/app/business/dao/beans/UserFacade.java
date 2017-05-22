/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.app.business.dao.beans;

import com.google.zxing.WriterException;
import edu.nagojudge.app.business.dao.entities.Account;
import edu.nagojudge.app.business.dao.entities.TypeUser;
import edu.nagojudge.app.business.dao.entities.User;
import edu.nagojudge.app.exceptions.NagoJudgeException;
import edu.nagojudge.app.utils.FacesUtil;
import edu.nagojudge.app.utils.constants.IKeysApplication;
import edu.nagojudge.msg.pojo.AccountMessage;
import edu.nagojudge.msg.pojo.UserMessage;
import edu.nagojudge.tools.security.constants.TypeSHAEnum;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.NoSuchFileException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
@Stateless
public class UserFacade extends AbstractFacade<User> {

    @EJB
    private EmailFacade emailFacadeDAO;

    @EJB
    private SecurityFacade securityFacadeDAO;

    @EJB
    private AccountFacade accountFacadeDAO;

    private final Logger logger = Logger.getLogger(UserFacade.class);

    @PersistenceContext(unitName = "NJWebPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFacade() {
        super(User.class);
    }

    public void loginCompleteUser(String email, String password) throws NagoJudgeException {
        try {
            logger.debug("INICIA METODO - loginCompleteUser()");
            User isRegisted = existUserRegistered(email, password);
            if (isRegisted == null) {
                throw new NagoJudgeException("Usuario no registrado");
            }
            HttpSession session = FacesUtil.getFacesUtil().getSession(true);
            session.setAttribute(IKeysApplication.KEY_DATA_USER_EMAIL, isRegisted.getIdEmail());
            session.setAttribute(IKeysApplication.KEY_DATA_USER_ACCOUNT, isRegisted.getIdAccount());
            session.setAttribute(IKeysApplication.KEY_DATA_TYPE_USER, isRegisted.getIdType().getIdType());
            logger.debug("LOGIN [" + email + "] SUCCESSFUL @ECHO");
        } finally {
            logger.debug("FINALIZA METODO - loginCompleteUser()");
        }
    }

    private void validateFieldsUnique(String email) throws NagoJudgeException {

        Long count = (Long) em.createQuery("SELECT COUNT(0) FROM User a WHERE LOWER(a.idEmail) = LOWER(:id_email)")
                .setParameter("id_email", email)
                .getSingleResult();
        if (count >= 1) {
            throw new NagoJudgeException(" EL EMAIL " + email + " YA ESTA REGISTRADO. ");
        }

    }

    public String createUserCommon(User user, Account account) throws NoSuchFileException, NagoJudgeException, WriterException, IOException, MessagingException, Exception {
        logger.debug("INICIA METODO createUserCommon @ECHO");
        try {

            account.setDateRegister(Calendar.getInstance().getTime());
            logger.debug("getDateRegister [" + account.getDateRegister() + "]");
            logger.debug("getNickname [" + account.getNickname() + "]");
            accountFacadeDAO.validateFieldsUnique(account.getNickname());
            accountFacadeDAO.create(account);

            user.setIdAccount(account);
            user.setPasswordUser(securityFacadeDAO.encodeSHA2(user.getPasswordUser(), TypeSHAEnum.SHA256));
            logger.debug("getIdAccount [" + user.getIdAccount() + "]");
            logger.debug("getFirstName [" + user.getFirstName() + "]");
            logger.debug("getLastName [" + user.getLastName() + "]");
            logger.debug("getIdEmail [" + user.getIdEmail() + "]");
            logger.debug("getDateBirthday [" + user.getDateBirthday() + "]");
            logger.debug("getIdType [" + user.getIdType() + "]");
            logger.debug("getKeyUser [" + user.getPasswordUser() + "]");

            validateFieldsUnique(user.getIdEmail());
            create(user);

            logger.debug("SE CREO EL USUARIO CON LA CUENTA [" + user.getIdAccount() + "] EXITOSAMENTE");

            try {
                logger.info("INICIA LA GENERACION Y ENVIO DEL CORREO ELECTRONICO DE REGISTRO A " + user.getIdEmail());
                emailFacadeDAO.sendEmailNewUser(user, account);
                logger.info("ENVIO DEL CORREO ELECTRONICO A [" + user.getIdEmail() + "] @ECHO");
            } catch (NoSuchFileException ex) {
                logger.error(ex);
                throw ex;
            } catch (IOException ex) {
                logger.error(ex);
                throw ex;
            } catch (MessagingException ex) {
                logger.error(ex);
                throw ex;
            } finally {
                logger.info("FINALIZA LA GENERACION Y ENVIO DEL CORREO ELECTRONICO DE REGISTRO A " + user.getIdEmail());
            }
            return String.valueOf(account.getIdAccount());
        } catch (NagoJudgeException ex) {
            logger.error(ex);
            throw ex;
        } catch (Exception ex) {
            logger.error(ex);
            throw ex;
        } finally {
            logger.debug("FIN METODO createUserCommon @ECHO");
        }
    }

    public User existUserRegistered(String email, String password) {
        EntityManager em = getEntityManager();
        User outcome = null;
        String passwordEncrypted = securityFacadeDAO.encodeSHA2(password, TypeSHAEnum.SHA256_NUM);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p FROM User p WHERE p.idEmail = :email AND p.passwordUser = :pass");
        Query query = em.createQuery(sql.toString()).setParameter("email", email).setParameter("pass", passwordEncrypted);
        List resultList = query.getResultList();
        if (resultList != null && resultList.size() == 1) {
            outcome = (User) resultList.get(0);
        }
        return outcome;
    }

    public List<UserMessage> findAllUsersMessage() {
        List<UserMessage> outcome = new ArrayList<UserMessage>();
        List<Account> accounts = em.createQuery("SELECT a FROM Account a", Account.class)
                .getResultList();
        if (accounts != null) {
            for (Account account : accounts) {
                if (account != null) {
                    if (account.getUserList() != null && !account.getUserList().isEmpty()) {
                        UserMessage userMessage = parseEntityAccountToMessage(account, account.getUserList().get(0));
                        outcome.add(userMessage);
                    }
                }
            }
        }
        return outcome;
    }

    public String textAutoGenerateString() {
        SecureRandom random = new SecureRandom();
        String string = new BigInteger(130, random).toString(32);
        return string.substring(0, 7);
    }

    private UserMessage parseEntityAccountToMessage(Account account, User user) {
        UserMessage userMessage = new UserMessage();
        userMessage.setDateBirthday(user.getDateBirthday() != null ? user.getDateBirthday().getTime() : 0);
        userMessage.setEmail(user.getIdEmail());
        userMessage.setFirstName(user.getFirstName());
        userMessage.setKeyUser(user.getPasswordUser());
        userMessage.setLastName(user.getLastName());
        userMessage.setNameTypeUser(user.getIdType().getNameType());

        AccountMessage accountMessage = new AccountMessage();
        if (account != null) {
            accountMessage.setDateRegister(account.getDateRegister() != null ? account.getDateRegister().getTime() : 0);
            accountMessage.setIdAccount(account.getIdAccount());
            accountMessage.setNickname(account.getNickname());
        }
        userMessage.setAccountMessage(accountMessage);
        return userMessage;
    }

    public long createUserCommon(UserMessage userMessage, AccountMessage accountMessage, TypeUser typeUser, byte[] contents) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
