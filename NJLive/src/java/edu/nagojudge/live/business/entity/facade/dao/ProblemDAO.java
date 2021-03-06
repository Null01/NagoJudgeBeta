/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.business.entity.facade.dao;

import edu.nagojudge.live.business.entity.Problem;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.log4j.Logger;

/**
 *
 * @author andres.garcia
 */
@Stateless
public class ProblemDAO extends AbstractDAO<Problem> {

    private static final Logger logger = Logger.getLogger(ProblemDAO.class);

    @PersistenceContext(unitName = "NJLivePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProblemDAO() {
        super(Problem.class);
    }

    public List<Object[]> findProblemsByChallenge(Long idChallenge) {
        List<Object[]> idProblems = new ArrayList<Object[]>();
        try {
            logger.debug("INICIA METODO - findIdsProblemsByChallenge()");
            EntityManager manager = getEntityManager();
            idProblems = manager.createQuery("SELECT cp.idProblem.idProblem, cp.idProblem.nameProblem FROM ChallengeProblem cp WHERE cp.idChallenge.idChallenge = :id_challenge ORDER BY cp.idProblem.idProblem ")
                    .setParameter("id_challenge", idChallenge)
                    .getResultList();
        } finally {
            logger.debug("FINALIZA METODO - findIdsProblemsByChallenge()");
        }
        return idProblems;
    }

}
