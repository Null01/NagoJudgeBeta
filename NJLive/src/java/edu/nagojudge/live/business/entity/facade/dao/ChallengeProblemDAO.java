/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.business.entity.facade.dao;

import edu.nagojudge.live.business.entity.ChallengeProblem;
import edu.nagojudge.live.business.entity.Problem;
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
        List<Problem> problems = em.createQuery("SELECT a.idProblem FROM ChallengeProblem a WHERE a.idChallenge.idChallenge = :id_challenge ", Problem.class)
                .setParameter("id_challenge", challengeId).getResultList();
        for (Problem problem : problems) {
            outome.add(parseProblemEntityToMessage(problem));
        }
        return outome;
    }

    private ProblemMessage parseProblemEntityToMessage(Problem problem) {
        ProblemMessage problemMessage = new ProblemMessage();
        problemMessage.setIdProblem(problem.getIdProblem());
        problemMessage.setNameProblem(problem.getNameProblem());
        problemMessage.setNameCategory(problem.getIdCategory().getNameCategory());
        return problemMessage;
    }

}
