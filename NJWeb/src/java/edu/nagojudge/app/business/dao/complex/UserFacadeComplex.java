/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.app.business.dao.complex;

import com.google.zxing.WriterException;
import edu.nagojudge.app.business.dao.beans.AccountFacadeDAO;
import edu.nagojudge.app.business.dao.beans.UserFacadeDAO;
import edu.nagojudge.app.business.dao.entities.Account;
import edu.nagojudge.app.business.dao.entities.User;
import edu.nagojudge.app.business.tools.barcodes.QRGenerator;
import edu.nagojudge.app.business.tools.emails.BuildGenericEmail;
import edu.nagojudge.app.exceptions.NagoJudgeException;
import edu.nagojudge.app.utils.FacesUtil;
import edu.nagojudge.app.utils.constants.IKeysApplication;
import edu.nagojudge.app.utils.constants.IResourcesPaths;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
@Stateless
public class UserFacadeComplex implements Serializable {

    @EJB
    private UserFacadeDAO userFacade;

    @EJB
    private AccountFacadeDAO accountFacade;

    private final Logger logger = Logger.getLogger(UserFacadeComplex.class);

    public void loginCompleteUser(String email, String password) throws NagoJudgeException {
        User isRegisted = userFacade.existUserRegistered(email, password);
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
            accountFacade.validateFieldsUnique(account.getNickname());
            accountFacade.create(account);

            logger.debug("getFirstName=" + user.getFirstName());
            logger.debug("getLastName=" + user.getLastName());
            logger.debug("getIdEmail=" + user.getIdEmail());
            logger.debug("getDateBirthday=" + user.getDateBirthday());
            logger.debug("getIdType=" + user.getIdType());
            user.setIdAccount(account);
            logger.debug("getIdAccount=" + user.getIdAccount());
            userFacade.validateFieldsUnique(user.getIdEmail());
            user.setKeyUser(userFacade.encodeSHA2(user.getKeyUser(), TypeSHAEnum.SHA256_NUM));
            logger.debug("getKeyUser=" + user.getKeyUser());

            userFacade.create(user);
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

}
