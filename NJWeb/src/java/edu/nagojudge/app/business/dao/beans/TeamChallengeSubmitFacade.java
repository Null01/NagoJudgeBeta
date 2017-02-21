/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.app.business.dao.beans;

import edu.nagojudge.app.business.dao.entities.TeamChallengeSubmit;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author andresfelipegarciaduran
 */
@Stateless
public class TeamChallengeSubmitFacade extends AbstractFacade<TeamChallengeSubmit> {

    @PersistenceContext(unitName = "NJWebPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TeamChallengeSubmitFacade() {
        super(TeamChallengeSubmit.class);
    }
    
}
