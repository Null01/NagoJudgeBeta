/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.app.business.dao.beans;

import edu.nagojudge.app.business.dao.entities.Account;
import edu.nagojudge.app.exceptions.NagoJudgeException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author andresfelipegarciaduran
 */
@Stateless
public class AccountFacade extends AbstractFacade<Account> {

    @PersistenceContext(unitName = "NJWebPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AccountFacade() {
        super(Account.class);
    }

    public void validateFieldsUnique(String nickname) throws NagoJudgeException {
        Long count = (Long) em.createQuery("SELECT COUNT(0) FROM Account a WHERE LOWER(a.nickname)")
                .getSingleResult();
        if (count >= 1) {
            throw new NagoJudgeException(" EL NICKNAME " + nickname + " YA ESTA REGISTRADO. ");
        }
    }

}
