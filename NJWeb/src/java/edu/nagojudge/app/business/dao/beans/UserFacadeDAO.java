/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.app.business.dao.beans;

import com.google.zxing.WriterException;
import edu.nagojudge.app.business.dao.entities.Account;
import edu.nagojudge.app.business.dao.entities.User;
import edu.nagojudge.app.business.tools.barcodes.QRGenerator;
import edu.nagojudge.app.business.tools.emails.BuildGenericEmail;
import edu.nagojudge.app.exceptions.NagoJudgeException;
import edu.nagojudge.app.utils.FacesUtil;
import edu.nagojudge.app.utils.constants.IKeysApplication;
import edu.nagojudge.app.utils.constants.IResourcesPaths;
import edu.nagojudge.msg.pojo.UserMessage;
import edu.nagojudge.msg.pojo.constants.TypeFilesEnum;
import edu.nagojudge.tools.security.constants.TypeSHAEnum;
import edu.nagojudge.tools.utils.FileUtil;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.nio.file.NoSuchFileException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
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
public class UserFacadeDAO extends AbstractFacade<User> implements Serializable {

    @EJB
    private EmailFacadeDAO emailFacadeDAO;

    @EJB
    private SecurityFacadeDAO securityFacadeDAO;

    @EJB
    private AccountFacadeDAO accountFacadeDAO;

    private final Logger logger = Logger.getLogger(UserFacadeDAO.class);

    @PersistenceContext(unitName = "NJWebPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFacadeDAO() {
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
        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT COUNT(0) FROM USER WHERE LOWER(ID_EMAIL) = LOWER('").append(email).append("')");
        Query query = em.createNativeQuery(sb.toString());
        int count = Integer.parseInt(query.getSingleResult().toString());
        if (count >= 1) {
            throw new NagoJudgeException(" EL EMAIL " + email + " YA ESTA REGISTRADO. ");
        }

    }

    public User findUserByIdAccount(long idAccount) {
        EntityManager em = getEntityManager();
        User outcome;
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT p FROM ").append(User.class.getSimpleName()).append(" p WHERE p.idAccount.idAccount = :idAccount ");
        Query query = em.createQuery(sb.toString()).setParameter("idAccount", idAccount);
        outcome = (User) query.getResultList().get(0);
        logger.debug(outcome.getIdAccount());
        return outcome;
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
            user.setKeyUser(securityFacadeDAO.encodeSHA2(user.getKeyUser(), TypeSHAEnum.SHA256));
            logger.debug("getIdAccount [" + user.getIdAccount() + "]");
            logger.debug("getFirstName [" + user.getFirstName() + "]");
            logger.debug("getLastName [" + user.getLastName() + "]");
            logger.debug("getIdEmail [" + user.getIdEmail() + "]");
            logger.debug("getDateBirthday [" + user.getDateBirthday() + "]");
            logger.debug("getIdType [" + user.getIdType() + "]");
            logger.debug("getKeyUser [" + user.getKeyUser() + "]");

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
        sql.append("SELECT p FROM ").append(User.class.getSimpleName()).append(" p WHERE p.idEmail = :email AND p.keyUser = :pass");
        Query query = em.createQuery(sql.toString()).setParameter("email", email).setParameter("pass", passwordEncrypted);
        List resultList = query.getResultList();
        if (resultList != null && resultList.size() == 1) {
            outcome = (User) resultList.get(0);
        }
        return outcome;
    }

    public List<UserMessage> findAllUsersMessage() {
        List<UserMessage> outcome = new ArrayList<UserMessage>();
        List<User> findAll = findAll();
        if (findAll != null && !findAll.isEmpty()) {
            for (User user : findAll) {
                outcome.add(parseUserEntityToMessage(user));
            }
        }
        return outcome;
    }

    public String autoGenerateString() {
        SecureRandom random = new SecureRandom();
        String string = new BigInteger(130, random).toString(32);
        return string.substring(0, 5);
    }

    private UserMessage parseUserEntityToMessage(User user) {
        UserMessage userMessage = new UserMessage();
        userMessage.setDateBirthday(user.getDateBirthday() == null ? 0 : user.getDateBirthday().getTime());
        userMessage.setEmail(user.getIdEmail());
        userMessage.setFirstName(user.getFirstName());
        userMessage.setIdAccount(user.getIdAccount().getIdAccount());
        userMessage.setLastName(user.getLastName());
        userMessage.setNameTypeUser(user.getIdType().getNameType());
        userMessage.setNicknameAccount(user.getIdAccount().getNickname());
        return userMessage;
    }

    public void persist(Object object) {
        em.persist(object);
    }

}
