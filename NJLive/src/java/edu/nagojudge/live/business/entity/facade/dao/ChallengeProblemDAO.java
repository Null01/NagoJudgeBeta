/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.business.entity.facade.dao;

import edu.nagojudge.live.business.entity.ChallengeProblem;
import edu.nagojudge.msg.pojo.ProblemMessage;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author andres.garcia
 */
@Stateless
public class ChallengeProblemDAO extends AbstractDAO<ChallengeProblem> {

    @PersistenceContext(unitName = "NJLivePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ChallengeProblemDAO() {
        super(ChallengeProblem.class);
    }

    public List<ProblemMessage> findProblemsByChallenge(Long challengeId) {
        List<ProblemMessage> outome = new ArrayList<ProblemMessage>();
        return outome;
    }

}
