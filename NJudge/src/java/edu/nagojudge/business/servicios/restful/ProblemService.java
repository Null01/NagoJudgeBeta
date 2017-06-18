/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.business.servicios.restful;

import edu.nagojudge.business.servicios.restful.exceptions.BusinessException;
import edu.nagojudge.business.servicios.restful.interfaces.IProblemService;
import edu.nagojudge.msg.pojo.CredentialsMessage;
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
@Path("/problem")
@Produces({MediaType.APPLICATION_JSON + ";charset=UTF-8"})
public class ProblemService implements IProblemService {

    private final Logger logger = Logger.getLogger(ProblemService.class);

    @GET
    @Path("/external/{idProblem}")
    @Override
    public String getExternalPathFromProblem(
            @PathParam("idProblem") String idProblem,
            @QueryParam("idChallenge") CredentialsMessage credentials) throws AuthenticationException, BusinessException {
        try {
            logger.debug("INICIA SERVICIO - getExternalPathFromProblem()");
        } finally {
            logger.debug("FINALIZA SERVICIO - getExternalPathFromProblem()");
        }
        return null;
    }

}
