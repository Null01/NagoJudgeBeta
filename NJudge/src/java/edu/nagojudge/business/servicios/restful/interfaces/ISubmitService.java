/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.business.servicios.restful.interfaces;

import edu.nagojudge.business.servicios.restful.exceptions.BusinessException;
import edu.nagojudge.msg.pojo.MetadataMessage;
import edu.nagojudge.msg.pojo.StringMessage;
import edu.nagojudge.msg.pojo.SubmitMessage;
import edu.nagojudge.msg.pojo.collections.ListMessage;
import java.io.IOException;
import javax.naming.AuthenticationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author andresfelipegarciaduran
 */
@Path("/submit")
@Produces({MediaType.APPLICATION_JSON + ";charset=UTF-8"})
@Consumes({MediaType.APPLICATION_JSON + ";charset=UTF-8"})
public interface ISubmitService {

    /**
     *
     * @param idTeam Correo electronico - Llave del usuario.
     * @param idSubmit Llave del envio.
     * @param idChallenge
     * @param metadata
     * @return proceso asincrono/sincrono no devuelve una respuesta
     * inmediatamente.
     * @throws AuthenticationException Se genera, si el identificador de
     * seguridad no es valido.
     * @throws BusinessException Se genera, si la información es incorrecta o no
     * existe, validaciones de negocio.
     */
    @GET
    @Path("/verdict/team/{idChallenge}/{idTeam}")
    public SubmitMessage generateJudgmentByTeam(
            @PathParam("idChallenge") String idChallenge,
            @PathParam("idTeam") String idTeam,
            @QueryParam("idSubmit") String idSubmit,
            @QueryParam("") MetadataMessage metadata)
            throws AuthenticationException, BusinessException;

    /**
     *
     * @param email Correo electronico - Llave del usuario.
     * @param idSubmit Llave del envio.
     * @param metadata
     * @return proceso asincrono/sincrono no devuelve una respuesta
     * inmediatamente.
     * @throws AuthenticationException Se genera, si el identificador de
     * seguridad no es valido.
     * @throws BusinessException Se genera, si la información es incorrecta o no
     * existe, validaciones de negocio.
     */
    @GET
    @Path("/verdict/user/{email}")
    public SubmitMessage generateJudgmentByUser(
            @PathParam("email") String email,
            @QueryParam("idSubmit") String idSubmit,
            @QueryParam("") MetadataMessage metadata)
            throws AuthenticationException, BusinessException;

    /**
     *
     * @param idChallenge
     * @param idTeam
     * @param metadata
     * @return
     * @throws AuthenticationException
     * @throws BusinessException
     */
    @GET
    @Path("/search/team/{idChallenge}/{idTeam}")
    public ListMessage<SubmitMessage> getAllJudgmentByTeam(
            @PathParam("idChallenge") String idChallenge,
            @PathParam("idTeam") String idTeam,
            @QueryParam("") MetadataMessage metadata)
            throws AuthenticationException, BusinessException;

    /**
     * Servicio encargado de retornar el codigo fuente, que se encuentra
     * directamente relacionado con el envio hecho, el envio se indentifica
     * según su identificador.
     *
     * @param idSubmit Llave del envio.
     * @param metadata
     * @return Retorna codigo fuente.
     * @throws IOException Se genera, leyendo un archivo o copiandolo.
     * @throws BusinessException Se genera, si la información es incorrecta o no
     * existe, validaciones de negocio.
     * @throws AuthenticationException Se genera, si el identificador de
     * seguridad no es valido.
     */
    @GET
    @Path("/code/{idSubmit}")
    public StringMessage getCodeSourceBySubmit(
            @PathParam("idSubmit") String idSubmit,
            @QueryParam("") MetadataMessage metadata)
            throws IOException, AuthenticationException, BusinessException;

    /**
     *
     * @param idTeam
     * @param idSubmit Llave del envio.
     * @param idChallenge
     * @param idProblem
     * @param language
     * @param metadata
     * @return
     * @throws IOException
     * @throws AuthenticationException
     * @throws BusinessException
     */
    @GET
    @Path("/code/download/{idChallenge}/{idTeam}/{idProblem}")
    @Produces({MediaType.APPLICATION_OCTET_STREAM + ";charset=UTF-8"})
    public Response downloadCodeSourceByTeam(
            @PathParam("idChallenge") String idChallenge,
            @PathParam("idTeam") String idTeam,
            @PathParam("idProblem") String idProblem,
            @QueryParam("idSubmit") String idSubmit,
            @QueryParam("language") String language,
            @QueryParam("") MetadataMessage metadata) throws IOException, AuthenticationException, BusinessException;

}
