/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.business.entity.facade.dao;

import edu.nagojudge.live.business.entity.ChallengeSubmit;
import edu.nagojudge.live.business.entity.Team;
import edu.nagojudge.msg.pojo.ScoreMessage;
import edu.nagojudge.msg.pojo.TeamMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.log4j.Logger;

/**
 *
 * @author andres.garcia
 */
@Stateless
public class ChallengeSubmitDAO extends AbstractDAO<ChallengeSubmit> {

    private final Logger logger = Logger.getLogger(ChallengeSubmitDAO.class);

    @PersistenceContext(unitName = "NJLivePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ChallengeSubmitDAO() {
        super(ChallengeSubmit.class);
    }

    public List<ScoreMessage> findScoreAllTeamFromChallenge(Long challengeId) {
        List<ScoreMessage> outcome = new ArrayList<ScoreMessage>();
        try {
            logger.debug("INICIO METODO - findScoreByTeamFromChallenge()");
            logger.debug("challengeId [" + challengeId + "]");
            EntityManager manager = getEntityManager();
            List<Object[]> objects = manager.createNativeQuery("select c.id_team, s.id_problem, count(0)\n"
                    + " from challenge_submit c left join submit s on (c.id_submit = s.id_submit)\n"
                    + " where c.id_challenge = ?"
                    + " group by c.id_team, s.id_problem")
                    .setParameter(1, challengeId)
                    .getResultList();

            Map<String, Object[]> map = new HashMap<String, Object[]>();
            for (Object object : objects) {
                if(map.get(String.valueOf(object)) == null){
                 
                }
            }
            logger.debug("OUTCOME [" + outcome.size() + "]");
        } finally {
            logger.debug("FIN METODO - findScoreByTeamFromChallenge()");
        }
        return outcome;
    }

}
