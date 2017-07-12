/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.business.dao.beans;

import javax.ejb.Stateless;
import javax.naming.AuthenticationException;
import javax.ws.rs.core.Context;
import org.apache.cxf.rs.security.oauth2.client.OAuthClientUtils;
import org.apache.cxf.rs.security.oauth2.utils.OAuthUtils;

/**
 *
 * @author andresfelipegarciaduran
 */
@Stateless
public class AuthenticationDAOFacade {

    private final String TOKEN = "asd";

    public void authorization(String token) throws AuthenticationException {

        if (token == null || token.isEmpty()) {
            throw new AuthenticationException("Parametros denegados, comuniquese con el administrador. #TeamNJ");
        }
        if (token.compareTo(TOKEN) != 0) {
            throw new AuthenticationException("Autenticación denegada, comuniquese con el administrador. #TeamNJ");
        }
    }
}
