/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.business.servicios.restful;

import edu.nagojudge.business.dao.beans.AttachmentsFacade;
import edu.nagojudge.business.dao.beans.AuthenticationFacade;
import edu.nagojudge.business.dao.beans.JudgeFacade;
import edu.nagojudge.business.servicios.restful.exceptions.BusinessException;
import edu.nagojudge.msg.pojo.ResponseMessage;
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
public class SubmitService {

    @EJB
    private AuthenticationFacade authenticationFacade;

    @EJB
    private JudgeFacade judgeFacade;

    @EJB
    private AttachmentsFacade attachmentsFacade;

    private final Logger logger = Logger.getLogger(SubmitService.class);

    /**
     *
     * @param email Correo electronico - Llave del usuario.
     * @param idSubmit Llave del envio.
     * @param token Identificador de seguridad.
     * @return proceso asincrono/sincrono no devuelve una respuesta
     * inmediatamente.
     * @throws AuthenticationException Se genera, si el identificador de
     * seguridad no es valido.
     * @throws BusinessException Se genera, si la información es incorrecta o no
     * existe, validaciones de negocio.
     */
    @GET
    @Path("/evaluate/{email}/{idSubmit}")
    public ResponseMessage judgmentOnLive(@PathParam("email") String email,
            @PathParam("idSubmit") String idSubmit,
            @QueryParam("token") String token) throws AuthenticationException, BusinessException {
        try {
            logger.debug("INICIA SERVICIO - judgmentLive()");
            ResponseMessage outcome = new ResponseMessage();
            authenticationFacade.authentication(token);
            judgeFacade.startJudge(idSubmit, email);
            return outcome;
        } catch (AuthenticationException ex) {
            logger.error(ex);
            throw ex;
        } catch (BusinessException ex) {
            logger.error(ex);
            throw ex;
        } finally {
            logger.debug("FINALIZA SERVICIO - judgmentLive()");
        }
    }

    /**
     * Servicio encargado de retornar el codigo fuente, que se encuentra
     * directamente relacionado con el envio hecho, el envio se indentifica
     * según su identificador.
     *
     * @param idSubmit Llave del envio.
     * @param token Identificador de seguridad.
     * @return Retorna codigo fuente.
     * @throws IOException Se genera, leyendo un archivo o copiandolo.
     * @throws BusinessException Se genera, si la información es incorrecta o no
     * existe, validaciones de negocio.
     * @throws AuthenticationException Se genera, si el identificador de
     * seguridad no es valido.
     */
    @GET
    @Path("/codeSource/{idSubmit}")
    public ResponseMessage getCodeSourceByIdSubmit(@PathParam("idSubmit") String idSubmit,
            @QueryParam("token") String token) throws IOException, BusinessException, AuthenticationException {
        try {
            logger.debug("INICIA SERVICIO - getCodeSourceByIdSubmit()");
            ResponseMessage outcome = new ResponseMessage();
            authenticationFacade.authentication(token);
            StringBuilder codeSource = attachmentsFacade.getCodeSource(idSubmit);
            outcome.setObject(codeSource.toString());
            return outcome;
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
            logger.debug("FINALIZA SERVICIO - getCodeSourceByIdSubmit()");
        }
    }

}
