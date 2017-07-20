/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.business.entity.facade.dao;

import edu.nagojudge.live.business.entity.Challenge;
import edu.nagojudge.msg.pojo.ChallengeMessage;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
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
public class ChallengeDAO extends AbstractDAO<Challenge> {

    private static final Logger logger = Logger.getLogger(ChallengeDAO.class);

    @PersistenceContext(unitName = "NJLivePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ChallengeDAO() {
        super(Challenge.class);
    }

    public List<ChallengeMessage> findChallengeAviableByTeam(Long idTeam) {
        List<ChallengeMessage> challengeMessages = new ArrayList<ChallengeMessage>();
        try {
            logger.debug("INICIA METODO - findChallengeAviableByTeam()");
            logger.debug("idTeam [" + idTeam + "]");
            logger.debug("currentTime [" + Calendar.getInstance().getTime() + "]");
            EntityManager manager = getEntityManager();
            List<Challenge> challenges
                    = manager.createQuery("SELECT a FROM Challenge a WHERE (a.dateEnd >= :currentDate AND a.dateStart <= :currentDate) "
                            + "AND (a.idChallenge IN (SELECT b.idChallenge.idChallenge FROM ChallengeTeam b WHERE b.idTeam.idTeam = :idTeam GROUP BY b.idTeam.idTeam)) ")
                            .setParameter("idTeam", idTeam)
                            .setParameter("currentDate", new Timestamp(Calendar.getInstance().getTimeInMillis()))
                            .getResultList();
            if (challenges == null || challenges.isEmpty()) {
                return null;
            }
            for (Challenge challenge : challenges) {
                challengeMessages.add(parseEntityToMessage(challenge));
            }
            logger.debug("outcome [" + challengeMessages.size() + "]");
        } finally {
            logger.debug("FINALIZA METODO - findChallengeAviableByTeam()");
        }
        return challengeMessages;
    }

    public ChallengeMessage findChallengeById(Long idChallenge) {
        try {
            logger.debug("INICIA METODO - findChallengeById()");
            Challenge find = find(idChallenge);
            ChallengeMessage challengeMessage = parseEntityToMessage(find);
            logger.debug("outcome [" + challengeMessage + "]");
            return challengeMessage;
        } finally {
            logger.debug("FINALIZA METODO - findChallengeById()");
        }
    }

    private ChallengeMessage parseEntityToMessage(Challenge challenge) {
        ChallengeMessage challengeMessage = new ChallengeMessage();
        challengeMessage.setIdChallenge(challenge.getIdChallenge());
        challengeMessage.setNameChallenge(challenge.getNameChallenge());
        challengeMessage.setDateStart(challenge.getDateStart() != null ? challenge.getDateStart().getTime() : 0);
        challengeMessage.setDateEnd(challenge.getDateEnd() != null ? challenge.getDateEnd().getTime() : 0);
        challengeMessage.setDateCreated(challenge.getDateCreated() != null ? challenge.getDateCreated().getTime() : 0);
        return challengeMessage;
    }

}
