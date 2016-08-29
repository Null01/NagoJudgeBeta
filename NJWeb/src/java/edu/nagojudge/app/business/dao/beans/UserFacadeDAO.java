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
    
    public User existUserRegistered(String email, String password) {
        EntityManager em = getEntityManager();
        User outcome = null;
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT SHA2('").append(password).append("', ").append(TypeSHAEnum.SHA256_NUM.getTypeSha()).append(") ");
        String passwordEncrypted = (String) em.createNativeQuery(sql.toString()).getSingleResult();
        sql = new StringBuilder();
        sql.append("SELECT p FROM ").append(User.class.getSimpleName()).append(" p WHERE p.idEmail = :email AND p.keyUser = :pass");
        Query query = em.createQuery(sql.toString()).setParameter("email", email).setParameter("pass", passwordEncrypted);
        List resultList = query.getResultList();
        if (resultList != null && resultList.size() == 1) {
            outcome = (User) resultList.get(0);
        }
        
        return outcome;
    }
    
    public void validateFieldsUnique(String email) throws NagoJudgeException {
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
        User outcome = null;
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT p FROM ").append(User.class.getSimpleName()).append(" p WHERE p.idAccount.idAccount = :idAccount ");
        Query query = em.createQuery(sb.toString()).setParameter("idAccount", idAccount);
        outcome = (User) query.getResultList().get(0);
        logger.debug(outcome.getIdAccount());
        return outcome;
    }
    
    public String encodeSHA2(String string, TypeSHAEnum typeSHAEnum) {
        EntityManager em = getEntityManager();
        String outcome = null;
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT SHA2('").append(string).append("', ").append(typeSHAEnum.getTypeSha()).append(") ");
        outcome = (String) em.createNativeQuery(sql.toString()).getSingleResult();
        
        return outcome;
    }
    
    public void loginCompleteUser(String email, String password) throws NagoJudgeException {
        User isRegisted = existUserRegistered(email, password);
        if (isRegisted == null) {
            throw new NagoJudgeException("Usuario no registrado");
        }
        HttpSession session = FacesUtil.getFacesUtil().getSession(true);
        session.setAttribute(IKeysApplication.KEY_DATA_USER_EMAIL, isRegisted.getIdEmail());
        session.setAttribute(IKeysApplication.KEY_DATA_USER_ACCOUNT, isRegisted.getIdAccount());
        session.setAttribute(IKeysApplication.KEY_DATA_TYPE_USER, isRegisted.getIdType().getIdType());
        
    }
    
    public String createUserCommon(User user, Account account) throws NoSuchFileException, NagoJudgeException, WriterException, IOException, MessagingException {
        logger.debug("INICIA METODO createUserCommon @ECHO");
        try {
            
            logger.debug("getNickname=" + account.getNickname());
            account.setDateRegister(Calendar.getInstance().getTime());
            logger.debug("getDateRegister=" + account.getDateRegister());
            accountFacadeDAO.validateFieldsUnique(account.getNickname());
            accountFacadeDAO.create(account);
            
            logger.debug("getFirstName=" + user.getFirstName());
            logger.debug("getLastName=" + user.getLastName());
            logger.debug("getIdEmail=" + user.getIdEmail());
            logger.debug("getDateBirthday=" + user.getDateBirthday());
            logger.debug("getIdType=" + user.getIdType());
            user.setIdAccount(account);
            logger.debug("getIdAccount=" + user.getIdAccount());
            validateFieldsUnique(user.getIdEmail());
            user.setKeyUser(encodeSHA2(user.getKeyUser(), TypeSHAEnum.SHA256_NUM));
            logger.debug("getKeyUser=" + user.getKeyUser());
            
            create(user);
            logger.debug("SE CREO EL USUARIO CON LA CUENTA [" + user.getIdAccount() + "] EXITOSAMENTE");
            
            StringBuilder textQRCode = new StringBuilder();
            textQRCode.append("User: ").append(user.getIdEmail()).append("\n");
            textQRCode.append("Password: ").append(user.getKeyUser()).append("\n\n");
            textQRCode.append(IKeysApplication.KEY_PUBLIC_LABEL_TEAM).append("\n");
            
            Map<String, String> mapValuesReplace = new HashMap<String, String>();
            mapValuesReplace.put("->labelNickName", account.getNickname());
            mapValuesReplace.put("->labelCorreoSoporte", "agarciad1@ucentral.edu.co");
            
            String fullPathFileProperties = FacesUtil.getFacesUtil().getRealPath() + FacesUtil.getFacesUtil().getInitParameter("auth-email-config");
            logger.debug("fullPathFileProperties=" + fullPathFileProperties);
            String fullPathCodeSourceTemplate = FacesUtil.getFacesUtil().getRealPath() + FacesUtil.getFacesUtil().getInitParameter("template-success-register");
            logger.debug("fullPathCodeSourceTemplate=" + fullPathCodeSourceTemplate);
            
            try {
                BuildGenericEmail buildGenericEmail = new BuildGenericEmail(fullPathFileProperties);
                QRGenerator generator = new QRGenerator();
                BufferedImage bufferedImage = generator.createTextCode(textQRCode.toString(), 300, 300);
                String fullPathFileQRCode = IResourcesPaths.PATH_ROOT_WORKSPACE_RESOURCE_WEB + File.separatorChar + String.valueOf(generator.hashCode()) + String.valueOf(user.hashCode()) + TypeFilesEnum.PNG.getExtension();
                logger.debug("fullPathFileQRCode=" + fullPathFileQRCode);
                String generateFileImageQRByUser = generator.generateFileImageQRByUser(bufferedImage, fullPathFileQRCode);
                String fullPathNjLogo = IResourcesPaths.PATH_WORKSPACE_IMG_RESOURCE_WEB + File.separatorChar + "nj-logo" + TypeFilesEnum.GIF.getExtension();
                logger.debug("fullPathNjLogo=" + fullPathNjLogo);
                
                Map<String, String> mapValuesImgs = new HashMap<String, String>();
                mapValuesImgs.put("code-qr", generateFileImageQRByUser);
                mapValuesImgs.put("nj-logo", fullPathNjLogo);
                
                logger.info(" INICIA LA GENERACION Y ENVIO DEL CORREO ELECTRONICO DE REGISTRO A " + user.getIdEmail());
                String subject = IKeysApplication.KEY_PUBLIC_LABEL_TEAM + " - Confirmación Registro";
                StringBuilder contentBody = buildGenericEmail.getSourceCodeTemplate(mapValuesReplace, fullPathCodeSourceTemplate);
                List<MimeBodyPart> contentImage = buildGenericEmail.contentImage(mapValuesImgs);
                buildGenericEmail.sendEmail(user.getIdEmail(), subject, contentBody, contentImage);
                FileUtil.getInstance().removeFile(fullPathFileQRCode);
            } catch (NoSuchFileException ex) {
                logger.error(ex);
                throw ex;
            } catch (WriterException ex) {
                logger.error(ex);
                throw ex;
            } catch (IOException ex) {
                logger.error(ex);
                throw ex;
            } catch (MessagingException ex) {
                logger.error(ex);
                throw ex;
            } finally {
                logger.info(" FINALIZA LA GENERACION Y ENVIO DEL CORREO ELECTRONICO DE REGISTRO A " + user.getIdEmail());
            }
            return String.valueOf(account.getIdAccount());
        } catch (NagoJudgeException ex) {
            logger.error(ex);
            throw ex;
        } finally {
            logger.debug("FIN METODO createUserCommon @ECHO");
        }
    }
    
    public String autoGenerateString() {
        SecureRandom random = new SecureRandom();
        String string = new BigInteger(130, random).toString(32);
        return string.substring(0, 5);
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
    
}
