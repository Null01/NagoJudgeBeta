/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.app.business.dao.beans;

import edu.nagojudge.app.business.dao.entities.Account;
import edu.nagojudge.app.exceptions.NagoJudgeException;
import java.io.Serializable;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author andresfelipegarciaduran
 */
@Stateless
public class AccountFacadeDAO extends AbstractFacade<Account> implements Serializable{

    @PersistenceContext(unitName = "NJWebPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AccountFacadeDAO() {
        super(Account.class);
    }

    public void validateFieldsUnique(String nickname) throws NagoJudgeException {
        EntityManager em = getEntityManager();

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT COUNT(0) FROM ACCOUNT WHERE LOWER(NICKNAME) = LOWER('").append(nickname).append("')");
        Query query = em.createNativeQuery(sb.toString());
        int count = Integer.parseInt(query.getSingleResult().toString());
        if (count >= 1) {
            throw new NagoJudgeException(" EL NICKNAME " + nickname + " YA ESTA REGISTRADO. ");
        }
    }

}
