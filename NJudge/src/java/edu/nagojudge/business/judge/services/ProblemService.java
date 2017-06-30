/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.business.judge.services;

import edu.nagojudge.business.dao.beans.AuthenticationDAOFacade;
import edu.nagojudge.business.servicios.restful.exceptions.BusinessException;
import edu.nagojudge.business.servicios.restful.interfaces.IProblemService;
import edu.nagojudge.msg.pojo.CredentialsMessage;
import javax.ejb.EJB;
import javax.naming.AuthenticationException;
import javax.validation.constraints.NotNull;
import javax.ws.rs.ApplicationPath;
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
@Path("/problem")
@Produces({MediaType.APPLICATION_JSON + ";charset=UTF-8"})
public class ProblemService implements IProblemService {

    @EJB
    private AuthenticationDAOFacade authenticationFacade;

    private final Logger logger = Logger.getLogger(ProblemService.class);

    @GET
    @Path("/external/{idProblem}")
    @Override
    public String getExternalPathFromProblem(
            @PathParam("idProblem") String idProblem,
            @QueryParam("idChallenge") String idChallenge,
            @NotNull @QueryParam("token") String token) throws AuthenticationException, BusinessException {
        String outcome = "hola-mundo-3";
        try {
            logger.debug("INICIA SERVICIO - getExternalPathFromProblem()");
            authenticationFacade.authorization(token);
            logger.debug(outcome);
        } finally {
            logger.debug("FINALIZA SERVICIO - getExternalPathFromProblem()");
        }
        return outcome;
    }

}
