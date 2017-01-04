/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.app.business.tools.emails;

import edu.nagojudge.app.exceptions.NagoJudgeException;
import edu.nagojudge.tools.utils.FileUtil;
import edu.nagojudge.tools.utils.FormatUtil;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import javax.mail.PasswordAuthentication;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
public class BuildGenericEmail {

    private final Logger logger = Logger.getLogger(BuildGenericEmail.class);

    private enum MESSAGE_REQUEST_ENUM {

        SUCCESS, FAILED, TIME_OUT
    };

    private enum PROTOCOL_ENUM {

        SMTP,
        SMTPS,
        TLS
    };

    private String host = "smtp.gmail.com";
    private String from = "None";
    private String username = ""; // Change User
    private String password = ""; // Change Password
    private int port = 465;
    private boolean debug = false;
    private boolean auth = true;
    private final PROTOCOL_ENUM protocol = PROTOCOL_ENUM.SMTPS;
    private MimeMessage message;

    private BuildGenericEmail() {
    }

    public BuildGenericEmail(String fullPathFileProperties) throws IOException {

        this.message = loadProperties(fullPathFileProperties);
    }

    public void sendEmailHtml(final String to, final String subject, final StringBuilder contentTemplate, final List<MimeBodyPart> contentImages) throws MessagingException, Exception {
        logger.debug("INICIA METODO - sendEmail()");
        try {

            validateEmail(to);

            this.message.setFrom(new InternetAddress(from));
            this.message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(to)});
            this.message.setSubject(subject);
            this.message.setSentDate(Calendar.getInstance().getTime());

            Multipart multipart = new MimeMultipart("alternative");
            MimeBodyPart html = new MimeBodyPart(); // Create the html part
            html.setContent(contentTemplate.toString(), MediaType.TEXT_HTML + ";charset=utf-8");
            multipart.addBodyPart(html);

            if (contentImages != null) {
                for (MimeBodyPart htmlImage : contentImages) {
                    multipart.addBodyPart(htmlImage);
                }
            }

            this.message.setContent(multipart); // Associate multi-part with message

            Transport.send(message);
            logger.debug("CORREO_ELECTRONICO ENVIADO @ECHO");

        } catch (MessagingException ex) {
            logger.error(ex);
            throw ex;
        } finally {
            logger.debug("FINALIZA METODO - sendEmail()");
        }

    }

    public void sendEmailPlain(final String to, final String subject, final StringBuilder content) throws MessagingException, Exception {
        logger.debug("INICIA METODO - sendEmail()");
        try {

            validateEmail(to);

            this.message.setFrom(new InternetAddress(from));
            this.message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(to)});
            this.message.setSubject(subject);
            this.message.setSentDate(Calendar.getInstance().getTime());
            this.message.setContent(content.toString(), MediaType.TEXT_HTML + ";charset=utf-8");

            logger.debug("to [" + to + "]");
            logger.debug("from [" + from + "]");
            logger.debug("subject [" + subject + "]");
            logger.debug("content [" + content.toString() + "]");

            Transport.send(message);
            logger.debug("CORREO_ELECTRONICO ENVIADO @ECHO");

        } catch (MessagingException ex) {
            logger.error(ex);
            throw ex;
        } finally {
            logger.debug("FINALIZA METODO - sendEmail()");
        }
    }

    public List<MimeBodyPart> addContentImage(final Map<String, String> content) throws MessagingException {
        List<MimeBodyPart> outcome = new ArrayList<MimeBodyPart>();
        for (Map.Entry<String, String> it : content.entrySet()) {
            try {
                String key = it.getKey();
                String value = it.getValue();
                MimeBodyPart mimeBodyPart = new MimeBodyPart();
                DataSource dataSource = new FileDataSource(value);
                mimeBodyPart.setDataHandler(new DataHandler(dataSource));
                mimeBodyPart.setHeader("Content-ID", "<" + key + ">");
                logger.debug("ID=[" + key + "]   VALUE=[" + value + "]");
                outcome.add(mimeBodyPart);
            } catch (MessagingException ex) {
                logger.error(ex);
                throw ex;
            }
        }
        return outcome;
    }

    public StringBuilder getSourceCodeTemplate(final Map<String, String> valuesReplaceTemplate, final String fullPathTemplate) throws NagoJudgeException, IOException {
        StringBuilder htmlBody = new StringBuilder();
        try {
            logger.debug("INICIA METODO - getSourceCodeTemplate() ");
            boolean existFile = FileUtil.getInstance().existFile(fullPathTemplate);
            if (!existFile) {
                throw new NagoJudgeException("EL ARCHIVO [" + fullPathTemplate + "] NO EXISTE.");
            }

            BufferedReader in = new BufferedReader(new FileReader(fullPathTemplate));
            String string;
            while ((string = in.readLine()) != null) {
                htmlBody.append(string);
            }
            String content = htmlBody.toString();
            if (valuesReplaceTemplate != null) {
                for (Map.Entry<String, String> row : valuesReplaceTemplate.entrySet()) {
                    logger.debug("KEY={" + row.getKey() + "}   VALUE={" + row.getValue() + "}");
                    content = content.replaceAll(row.getKey(), row.getValue());
                }
            }
            htmlBody = new StringBuilder("<html>" + content + "</html>");
            return htmlBody;
        } catch (FileNotFoundException ex) {
            logger.error(ex);
            throw ex;
        } catch (IOException ex) {
            logger.error(ex);
            throw ex;
        } finally {
            logger.debug("FINALIZA METODO - getSourceCodeTemplate() ");

        }
    }

    private MimeMessage loadProperties(String fullPathFileConfig) throws IOException {
        try {
            logger.debug("INICIA METODO - loadProperties()");
            Properties properties = FileUtil.getInstance().loadFileProperties(fullPathFileConfig);
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                logger.debug("CONTENT_PROPERTIES - KEY [" + entry.getKey() + "]  VALUE [" + entry.getValue() + "]");
            }
            host = (String) FormatUtil.getInstance().nvl(properties.get("mail.smtp.host"), "");
            port = Integer.parseInt((String) FormatUtil.getInstance().nvl(properties.get("mail.smtp.port"), 0));
            from = (String) FormatUtil.getInstance().nvl(properties.get("mail.nagojudge.smtp.from"), "");
            username = (String) FormatUtil.getInstance().nvl(properties.get("mail.nagojudge.user"), "");
            password = (String) FormatUtil.getInstance().nvl(properties.get("mail.nagojudge.password"), "");
            auth = Boolean.valueOf((String) FormatUtil.getInstance().nvl(properties.get("mail.smtp.auth"), false));
            debug = Boolean.valueOf((String) FormatUtil.getInstance().nvl(properties.get("mail.nagojudge.debug"), false));
            String socketFactoryPort = (String) FormatUtil.getInstance().nvl(properties.get("mail.smtp.socketFactory.port"), 0);
            String socketFactoryClass = (String) FormatUtil.getInstance().nvl(properties.get("mail.smtp.socketFactory.class"), "");

            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.port", port);
            properties.put("mail.smtp.from", from);
            properties.put("mail.smtp.auth", auth);
            properties.put("mail.debug", debug);
            properties.put("mail.smtp.socketFactory.port", Integer.parseInt(socketFactoryPort));
            properties.put("mail.smtp.socketFactory.class", socketFactoryClass);

            switch (protocol) {
                case SMTPS:
                    properties.put("mail.smtp.ssl.enable", true);
                    break;
                case TLS:
                    properties.put("mail.smtp.starttls.enable", true);
                    break;
            }

            Authenticator authenticator = null;
            if (auth) {
                properties.put("mail.smtp.auth", true);
                authenticator = new Authenticator() {
                    private final PasswordAuthentication pa = new PasswordAuthentication(username, password);

                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return pa;
                    }

                };
            }
            Session session = Session.getInstance(properties, authenticator);
            session.setDebug(debug);
            return new MimeMessage(session);
        } catch (IOException ex) {
            logger.error(ex);
            throw ex;
        } finally {
            logger.debug("FINALIZA METODO - loadProperties()");
        }
    }

    private void validateEmail(String to) throws AddressException {
        try {
            InternetAddress ia = new InternetAddress(to);
            ia.validate();
        } catch (AddressException ex) {
            logger.error(ex);
            throw ex;
        }
    }

}
