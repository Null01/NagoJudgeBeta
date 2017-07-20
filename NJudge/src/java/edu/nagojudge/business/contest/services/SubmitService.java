/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.business.contest.services;

import edu.nagojudge.business.dao.beans.AttachmentsDAOFacade;
import edu.nagojudge.business.dao.beans.AuthenticationDAOFacade;
import edu.nagojudge.business.dao.beans.JudgeDAOFacade;
import edu.nagojudge.business.servicios.restful.exceptions.BusinessException;
import edu.nagojudge.business.servicios.restful.interfaces.ISubmitService;
import edu.nagojudge.msg.pojo.MetadataMessage;
import edu.nagojudge.msg.pojo.StringMessage;
import edu.nagojudge.msg.pojo.SubmitMessage;
import edu.nagojudge.msg.pojo.collections.ListMessage;
import java.io.File;
import java.io.IOException;
import javax.ejb.EJB;
import javax.naming.AuthenticationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
public class SubmitService implements ISubmitService {

    @EJB
    private AuthenticationDAOFacade authenticationFacade;

    @EJB
    private JudgeDAOFacade judgeFacade;

    @EJB
    private AttachmentsDAOFacade attachmentsFacade;

    private final Logger logger = Logger.getLogger(SubmitService.class);

    @Override
    public SubmitMessage generateJudgmentByTeam(
            String idChallenge,
            String idTeam,
            String idSubmit,
            MetadataMessage metadata) throws AuthenticationException, BusinessException {
        try {
            logger.debug("INICIA SERVICIO - generateJudgmentByTeam()");
            logger.debug(metadata);
            authenticationFacade.authorization(metadata.getToken());
            SubmitMessage startJudge = judgeFacade.startJudgeByTeam(
                    Long.parseLong(idChallenge),
                    Long.parseLong(idSubmit),
                    Long.parseLong(idTeam),
                    metadata);
            return startJudge;
        } catch (AuthenticationException ex) {
            logger.error(ex);
            throw ex;
        } catch (BusinessException ex) {
            logger.error(ex);
            throw ex;
        } finally {
            logger.debug("FINALIZA SERVICIO - generateJudgmentByTeam()");
        }
    }

    @Override
    public SubmitMessage generateJudgmentByUser(
            String email,
            String idSubmit,
            MetadataMessage metadata) throws AuthenticationException, BusinessException {
        try {
            logger.debug("INICIA SERVICIO - generateJudgmentByUser()");
            authenticationFacade.authorization(metadata.getToken());
            SubmitMessage startJudge = judgeFacade.startJudgeByUser(
                    Long.parseLong(idSubmit),
                    email);
            return startJudge;
        } catch (AuthenticationException ex) {
            logger.error(ex);
            throw ex;
        } catch (BusinessException ex) {
            logger.error(ex);
            throw ex;
        } finally {
            logger.debug("FINALIZA SERVICIO - generateJudgmentByUser()");
        }
    }

    @Override
    public StringMessage getCodeSourceBySubmit(
            String idSubmit,
            MetadataMessage metadata) throws IOException, AuthenticationException, BusinessException {
        StringMessage outcome = new StringMessage();
        try {
            logger.debug("INICIA SERVICIO - getCodeSourceBySubmit()");
            authenticationFacade.authorization(metadata.getToken());
            StringBuilder codeSource = attachmentsFacade.getCodeSourceFromUser(idSubmit);
            outcome.setString(codeSource.toString());
        } catch (IOException ex) {
            logger.error(ex);
            throw ex;
        } catch (BusinessException ex) {
            logger.error(ex);
            throw ex;
        } catch (AuthenticationException ex) {
            logger.error(ex);
            throw ex;
        } finally {
            logger.debug("FINALIZA SERVICIO - getCodeSourceBySubmit()");
        }
        return outcome;
    }

    @Override
    public Response downloadCodeSourceByTeam(
            String idChallenge,
            String idTeam,
            String idProblem,
            String idSubmit,
            String language,
            MetadataMessage metadata) throws IOException, AuthenticationException, BusinessException {
        try {
            logger.debug("INICIA SERVICIO - downloadCodeSourceByTeam()");
            authenticationFacade.authorization(metadata.getToken());
            final String fullPathFileCodeSourceFromTeam = judgeFacade.getPathFileCodeSourceFromTeam(new Long(idChallenge), new Long(idTeam), new Long(idProblem));
            final String fileName = judgeFacade.getNameFileSubmit(new Long(idSubmit), language);
            final String extensionFileName = fileName.substring(fileName.lastIndexOf("."));
            File file = new File(fullPathFileCodeSourceFromTeam + File.separatorChar + fileName);
            logger.debug("outcome [" + file.getAbsolutePath() + "]");
            if (file.exists()) {
                ResponseBuilder response = Response.ok((Object) file);
                response.header("Content-Disposition",
                        "attachment; filename=main" + extensionFileName);
                return response.build();
            }
            throw new BusinessException("Codigo fuente seleccionado no existe.");
        } finally {
            logger.debug("FINALIZA SERVICIO - downloadCodeSourceByTeam()");
        }
    }

    @Override
    public ListMessage<SubmitMessage> getAllJudgmentByTeam(
            String idChallenge,
            String idTeam,
            MetadataMessage metadata) throws AuthenticationException, BusinessException {
        try {
            logger.debug("INICIA SERVICIO - getAllJudgmentByTeam()");
            authenticationFacade.authorization(metadata.getToken());
            throw new BusinessException("Codigo fuente seleccionado no existe.");
        } finally {
            logger.debug("FINALIZA SERVICIO - getAllJudgmentByTeam()");
        }
    }

}
