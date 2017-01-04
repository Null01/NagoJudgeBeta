/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.app.business.dao.beans;

import edu.nagojudge.app.business.dao.entities.Account;
import edu.nagojudge.app.business.dao.entities.User;
import edu.nagojudge.app.business.tools.barcodes.QRGenerator;
import edu.nagojudge.app.business.tools.emails.BuildGenericEmail;
import edu.nagojudge.app.exceptions.NagoJudgeException;
import edu.nagojudge.app.utils.FacesUtil;
import edu.nagojudge.app.utils.constants.IKeysApplication;
import edu.nagojudge.app.utils.constants.IResourcesPaths;
import edu.nagojudge.msg.pojo.constants.TypeFilesEnum;
import edu.nagojudge.tools.utils.FileUtil;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
@Stateless
public class EmailFacadeDAO implements Serializable {

    @EJB
    private SecurityFacadeDAO securityFacadeDAO;

    private final Logger logger = Logger.getLogger(EmailFacadeDAO.class);

    public void sendEmailNewUser(User user, Account account) throws IOException, NagoJudgeException, MessagingException, Exception {
        String fullPathFileQRCode = null;
        try {
            logger.debug("INICIO METODO - sendEmailNewUser()");
            // Configuracion parametros para el envio de correo electronico.
            String fullPathFileProperties = FacesUtil.getFacesUtil().getRealPath() + FacesUtil.getFacesUtil().getInitParameter("auth-email-config");
            logger.debug("fullPathFileProperties [" + fullPathFileProperties + "]");
            Map<String, String> mapMatch = new HashMap<String, String>();
            mapMatch.put(IKeysApplication.KEY_SECURITY_EMAIL, "mail.nagojudge.usuario");
            mapMatch.put(IKeysApplication.KEY_SECURITY_PASS, "mail.nagojudge.password");
            securityFacadeDAO.overrideProperties(fullPathFileProperties, mapMatch);
            logger.debug("CONFIG PROPERTIES-SEND MAIL BY SECURITY @ECHO");

            // Construccion codigo QR
            StringBuilder textQRCode = new StringBuilder();
            textQRCode.append("User: ").append(user.getIdEmail()).append("\n");
            textQRCode.append("Password: ").append(user.getKeyUser()).append("\n\n");
            textQRCode.append("#NagoTeam").append("\n");
            QRGenerator generator = new QRGenerator();
            final BufferedImage bufferedImage = generator.createTextCode(textQRCode.toString(), 300, 300);
            final String fullPathFileImageQR = generator.generateFileImageQRByUser(bufferedImage, fullPathFileQRCode);
            logger.debug("fullPathFileImageQR [" + fullPathFileImageQR + "]");

            // Construccion del template, para enviar por correo electronico.
            String fullPathCodeSourceTemplate = FacesUtil.getFacesUtil().getRealPath() + FacesUtil.getFacesUtil().getInitParameter("template-user-created");
            logger.debug("fullPathCodeSourceTemplate [" + fullPathCodeSourceTemplate + "]");
            Map<String, String> mapValuesReplace = new HashMap<String, String>();
            mapValuesReplace.put("->labelNickName", account.getNickname());
            mapValuesReplace.put("->labelCorreoSoporte", "agarciad1@ucentral.edu.co");

            fullPathFileQRCode = IResourcesPaths.PATH_ROOT_WORKSPACE_RESOURCE_WEB + File.separatorChar + String.valueOf(generator.hashCode()) + String.valueOf(user.hashCode()) + TypeFilesEnum.PNG.getExtension();
            logger.debug("fullPathFileQRCode [" + fullPathFileQRCode + "]");
            final String fullPathImageNjLogo = IResourcesPaths.PATH_WORKSPACE_IMG_RESOURCE_WEB + File.separatorChar + "nj-logo" + TypeFilesEnum.GIF.getExtension();
            logger.debug("fullPathNjLogo [" + fullPathImageNjLogo + "]");

            Map<String, String> mapValuesImgs = new HashMap<String, String>();
            mapValuesImgs.put("code-qr", fullPathFileImageQR);
            mapValuesImgs.put("nj-logo", fullPathImageNjLogo);

            // Envio del correo electronico
            BuildGenericEmail buildGenericEmail = new BuildGenericEmail(fullPathFileProperties);
            final String subject = "#NagoTeam - Confirmación Registro";
            final StringBuilder compositeBody = buildGenericEmail.getSourceCodeTemplate(mapValuesReplace, fullPathFileImageQR);
            final List<MimeBodyPart> contentImage = buildGenericEmail.addContentImage(mapValuesImgs);
            buildGenericEmail.sendEmailHtml(user.getIdEmail(), subject, compositeBody, contentImage);

            FileUtil.getInstance().removeFile(fullPathFileQRCode);

        } catch (IOException ex) {
            logger.error(ex);
            throw ex;
        } catch (NagoJudgeException ex) {
            logger.error(ex);
            throw ex;
        } catch (MessagingException ex) {
            logger.error(ex);
            throw ex;
        } catch (Exception ex) {
            logger.error(ex);
            throw ex;
        } finally {
            if (fullPathFileQRCode != null) {
                FileUtil.getInstance().removeFile(fullPathFileQRCode);
            }
            logger.debug("FIN METODO - sendEmailNewUser()");
        }
    }

    public void sendEmail(final String to, final String subject, final StringBuilder composite) throws IOException, NagoJudgeException, MessagingException, Exception {
        try {
            logger.debug("INICIO METODO - sendEmail()");
            // Configuracion parametros para el envio de correo electronico.
            String fullPathFileProperties = FacesUtil.getFacesUtil().getRealPath() + FacesUtil.getFacesUtil().getInitParameter("auth-email-config");
            logger.debug("fullPathFileProperties [" + fullPathFileProperties + "]");
            Map<String, String> mapMatch = new HashMap<String, String>();
            mapMatch.put(IKeysApplication.KEY_SECURITY_EMAIL, "mail.nagojudge.usuario");
            mapMatch.put(IKeysApplication.KEY_SECURITY_PASS, "mail.nagojudge.password");
            securityFacadeDAO.overrideProperties(fullPathFileProperties, mapMatch);
            logger.debug("CONFIG PROPERTIES-SEND MAIL BY SECURITY @ECHO");

            BuildGenericEmail buildGenericEmail = new BuildGenericEmail(fullPathFileProperties);
            buildGenericEmail.sendEmailPlain(to, subject, composite);

        } catch (IOException ex) {
            logger.error(ex);
            throw ex;
        } catch (NagoJudgeException ex) {
            logger.error(ex);
            throw ex;
        } catch (MessagingException ex) {
            logger.error(ex);
            throw ex;
        } catch (Exception ex) {
            logger.error(ex);
            throw ex;
        } finally {
            logger.debug("FIN METODO - sendEmail()");
        }
    }

}
