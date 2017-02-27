/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.business.entity.dao;

import edu.nagojudge.live.business.entity.ChallengeProblem;
import edu.nagojudge.live.business.entity.Problem;
import edu.nagojudge.live.web.mbeans.ClientContentBean;
import edu.nagojudge.msg.pojo.ProblemMessage;
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
public class ChallengeProblemDAO extends AbstractDAO<ChallengeProblem> {

    private final Logger logger = Logger.getLogger(ChallengeProblemDAO.class);

    @PersistenceContext(unitName = "NJLivePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ChallengeProblemDAO() {
        super(ChallengeProblem.class);
    }

    public List<ProblemMessage> findProblemsByChallenge(Long idChallenge) {
        List<ProblemMessage> problemMessages = new ArrayList<ProblemMessage>();
        try {
            logger.debug("INICIA METODO - findProblemsByChallenge()");
            logger.debug("idChallenge [" + idChallenge + "]");
            EntityManager manager = getEntityManager();
            List<ChallengeProblem> challengeProblems = manager.createQuery("SELECT p FROM ChallengeProblem p WHERE p.idChallenge.idChallenge = :id")
                    .setParameter("id", idChallenge).getResultList();
            for (ChallengeProblem challengeProblem : challengeProblems) {
                ProblemMessage problemMessage = parseEntityToMessage(challengeProblem.getIdProblem());
                problemMessages.add(problemMessage);
            }
            logger.debug("outcome [" + problemMessages.size() + "]");
        } finally {
            logger.debug("FINALIZA METODO - findProblemsByChallenge()");
        }
        return problemMessages;
    }

    public ProblemMessage parseEntityToMessage(Problem problem) {
        ProblemMessage outcome = new ProblemMessage();
        outcome.setIdProblem(problem.getIdProblem());
        outcome.setNameProblem(problem.getNameProblem());
        outcome.setNameCategory(problem.getIdCategory().getNameCategory());
        return outcome;
    }

}
