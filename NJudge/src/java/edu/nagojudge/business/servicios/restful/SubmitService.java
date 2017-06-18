/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.business.servicios.restful;

import edu.nagojudge.business.dao.beans.AttachmentsDAOFacade;
import edu.nagojudge.business.dao.beans.AuthenticationDAOFacade;
import edu.nagojudge.business.dao.beans.JudgeDAOFacade;
import edu.nagojudge.business.servicios.restful.exceptions.BusinessException;
import edu.nagojudge.business.servicios.restful.interfaces.ISubmitService;
import edu.nagojudge.msg.pojo.SubmitMessage;
import java.io.IOException;
import javax.ejb.EJB;
import javax.naming.AuthenticationException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
@Path("/submit")
@Produces({MediaType.APPLICATION_JSON + ";charset=UTF-8"})
public class SubmitService implements ISubmitService {

    @EJB
    private AuthenticationDAOFacade authenticationFacade;

    @EJB
    private JudgeDAOFacade judgeFacade;

    @EJB
    private AttachmentsDAOFacade attachmentsFacade;

    private final Logger logger = Logger.getLogger(SubmitService.class);

    @GET
    @Path("/verdict/team/{idTeam}/{idSubmit}")
    @Override
    public SubmitMessage generateJudgmentByTeam(
            @PathParam("idTeam") String idTeam,
            @PathParam("idSubmit") String idSubmit,
            @QueryParam("idChallenge") String idChallenge,
            @QueryParam("token") String token) throws AuthenticationException, BusinessException {
        try {
            logger.debug("INICIA SERVICIO - generateJudgmentByTeam()");
            authenticationFacade.authorization(token);
            /*
            SubmitMessage startJudge = judgeFacade.startJudgeByTeam(
                    Long.parseLong(idChallenge),
                    Long.parseLong(idSubmit),
                    Long.parseLong(idTeam));
            return startJudge;
            */
            return null;
        } finally {
            logger.debug("FINALIZA SERVICIO - generateJudgmentByTeam()");
        }
    }

    @GET
    @Path("/verdict/user/{email}/{idSubmit}")
    @Override
    public SubmitMessage generateJudgmentByUser(
            @PathParam("email") String email,
            @PathParam("idSubmit") String idSubmit,
            @QueryParam("token") String token) throws AuthenticationException, BusinessException {
        try {
            logger.debug("INICIA SERVICIO - generateJudgmentByUser()");
            authenticationFacade.authorization(token);
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

    @GET
    @Path("/codeSource/{idSubmit}")
    @Override
    public String getCodeSourceBySubmit(@PathParam("idSubmit") String idSubmit,
            @QueryParam("token") String token) throws IOException, AuthenticationException, BusinessException {
        try {
            logger.debug("INICIA SERVICIO - getCodeSourceBySubmit()");
            authenticationFacade.authorization(token);
            StringBuilder codeSource = attachmentsFacade.getCodeSource(idSubmit);
            return codeSource.toString();
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
    }

}
