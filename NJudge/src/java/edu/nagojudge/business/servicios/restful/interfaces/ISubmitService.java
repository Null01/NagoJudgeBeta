/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.business.servicios.restful.interfaces;

import edu.nagojudge.business.servicios.restful.exceptions.BusinessException;
import edu.nagojudge.msg.pojo.SubmitMessage;
import java.io.IOException;
import javax.naming.AuthenticationException;

/**
 *
 * @author andresfelipegarciaduran
 */
public interface ISubmitService {

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
    public SubmitMessage generateJudgmentByTeam(
            String idTeam,
            String idSubmit,
            String idChallenge,
            String token)
            throws AuthenticationException, BusinessException;

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
    public SubmitMessage generateJudgmentByUser(
            String email,
            String idSubmit,
            String token) throws AuthenticationException, BusinessException;

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
    public String getCodeSourceBySubmit(
            String idSubmit,
            String token) throws IOException, AuthenticationException, BusinessException;

}
