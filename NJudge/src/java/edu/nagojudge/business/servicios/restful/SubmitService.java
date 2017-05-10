/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.business.servicios.restful;

import edu.nagojudge.business.dao.beans.AttachmentsDAOFacade;
import edu.nagojudge.business.dao.beans.AuthenticationDAOFacade;
import edu.nagojudge.business.dao.beans.JudgeDAOFacade;
import edu.nagojudge.business.dao.entity.Submit;
import edu.nagojudge.business.servicios.restful.exceptions.BusinessException;
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
public class SubmitService {

    @EJB
    private AuthenticationDAOFacade authenticationFacade;

    @EJB
    private JudgeDAOFacade judgeFacade;

    @EJB
    private AttachmentsDAOFacade attachmentsFacade;

    private final Logger logger = Logger.getLogger(SubmitService.class);

    /**
     *
     * @param idTeam Correo electronico - Llave del usuario.
     * @param idSubmit Llave del envio.
     * @param idChallenge
     * @param token Identificador de seguridad.
     * @return proceso asincrono/sincrono no devuelve una respuesta
     * inmediatamente.
     * @throws AuthenticationException Se genera, si el identificador de
     * seguridad no es valido.
     * @throws BusinessException Se genera, si la información es incorrecta o no
     * existe, validaciones de negocio.
     */
    @GET
    @Path("/verdict/team/{idTeam}/{idSubmit}")
    public SubmitMessage judgmentLiveOnlyVerdict(@PathParam("idTeam") String idTeam,
            @PathParam("idSubmit") String idSubmit,
            @QueryParam("idChallenge") String idChallenge,
            @QueryParam("token") String token) throws AuthenticationException, BusinessException {
        try {
            logger.debug("INICIA SERVICIO - judgmentLiveOnlyVerdictTeam()");
            authenticationFacade.authentication(token);
            Submit startJudge = judgeFacade.startJudgeATeam(
                    Long.parseLong(idChallenge),
                    Long.parseLong(idSubmit),
                    Long.parseLong(idTeam));
            return parseSubmitEntityToMessage(startJudge);
        } finally {
            logger.debug("FINALIZA SERVICIO - judgmentLiveOnlyVerdictTeam()");
        }
    }

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
    @Path("/verdict/user/{email}/{idSubmit}")
    public SubmitMessage judgmentLiveOnlyVerdict(@PathParam("email") String email,
            @PathParam("idSubmit") String idSubmit,
            @QueryParam("token") String token) throws AuthenticationException, BusinessException {
        try {
            logger.debug("INICIA SERVICIO - judgmentLiveOnlyVerdict()");
            authenticationFacade.authentication(token);
            Submit startJudge = judgeFacade.startJudge(idSubmit, email);
            return parseSubmitEntityToMessage(startJudge);
        } catch (AuthenticationException ex) {
            logger.error(ex);
            throw ex;
        } catch (BusinessException ex) {
            logger.error(ex);
            throw ex;
        } finally {
            logger.debug("FINALIZA SERVICIO - judgmentLiveOnlyVerdict()");
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
    public String getCodeSourceByIdSubmit(@PathParam("idSubmit") String idSubmit,
            @QueryParam("token") String token) throws IOException, BusinessException, AuthenticationException {
        try {
            logger.debug("INICIA SERVICIO - getCodeSourceByIdSubmit()");
            authenticationFacade.authentication(token);
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
            logger.debug("FINALIZA SERVICIO - getCodeSourceByIdSubmit()");
        }
    }

    private SubmitMessage parseSubmitEntityToMessage(Submit submit) {
        SubmitMessage submitMessage = new SubmitMessage();
        submitMessage.setDateJudge(submit.getDateJudge() == null ? 0 : submit.getDateJudge().getTime());
        submitMessage.setDateSubmit(submit.getDateSubmit() == null ? 0 : submit.getDateSubmit().getTime());
        submitMessage.setIdProblem(submit.getIdProblem().getIdProblem());
        submitMessage.setIdSubmit(submit.getIdSubmit());
        submitMessage.setMsgJudge(submit.getMsgJudge());
        submitMessage.setNameLanguage(submit.getIdLanguage().getNameLanguage());
        submitMessage.setNameProblem(submit.getIdProblem().getNameProblem());
        return submitMessage;
    }

}
