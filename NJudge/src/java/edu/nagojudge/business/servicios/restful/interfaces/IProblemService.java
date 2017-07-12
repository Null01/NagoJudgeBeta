/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.business.servicios.restful.interfaces;

import edu.nagojudge.business.servicios.restful.exceptions.BusinessException;
import edu.nagojudge.msg.pojo.StringMessage;
import javax.naming.AuthenticationException;

/**
 *
 * @author andresfelipegarciaduran
 */
public interface IProblemService {

    public StringMessage getExternalPathFromProblem(
            String idProblem,
            String token) throws AuthenticationException, BusinessException;

}
