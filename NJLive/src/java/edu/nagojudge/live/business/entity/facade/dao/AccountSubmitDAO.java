/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.business.entity.facade.dao;

import edu.nagojudge.live.business.entity.AccountSubmit;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author andres.garcia
 */
@Stateless
public class AccountSubmitDAO extends AbstractDAO<AccountSubmit> {

    @PersistenceContext(unitName = "NJLivePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AccountSubmitDAO() {
        super(AccountSubmit.class);
    }
    
}
