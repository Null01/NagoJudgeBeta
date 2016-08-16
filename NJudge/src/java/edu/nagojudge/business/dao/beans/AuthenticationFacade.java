/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.business.dao.beans;

import javax.ejb.Stateless;
import javax.naming.AuthenticationException;

/**
 *
 * @author andresfelipegarciaduran
 */
@Stateless
public class AuthenticationFacade {

    private final String TOKEN = "asd";

    public void authentication(String token) throws AuthenticationException {
        if (token.compareTo(TOKEN) != 0) {
            throw new AuthenticationException("Autenticación denegada, comuniquese con el administrador. #TeamNJ");
        }
    }
}