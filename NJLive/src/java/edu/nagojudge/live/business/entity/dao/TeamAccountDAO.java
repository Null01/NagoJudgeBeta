/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.business.entity.dao;

import edu.nagojudge.live.business.entity.Team;
import edu.nagojudge.live.business.entity.TeamAccount;
import edu.nagojudge.live.web.mbeans.ClientScoreBean;
import edu.nagojudge.msg.pojo.ScoreMessage;
import edu.nagojudge.msg.pojo.TeamMessage;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;

/**
 *
 * @author andres.garcia
 */
@Stateless
public class TeamAccountDAO extends AbstractDAO<TeamAccount> {

    private final Logger logger = Logger.getLogger(TeamAccountDAO.class);

    @PersistenceContext(unitName = "NJLivePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TeamAccountDAO() {
        super(TeamAccount.class);
    }

    public List<ScoreMessage> findTeamsScoreByChallenge(Long challengeId) {
        List<ScoreMessage> outcome = new ArrayList<ScoreMessage>();
        try {
            logger.debug("INICIO METODO - findTeamsByChallenge()");
            logger.debug("challengeId [" + challengeId + "]");
            EntityManager manager = getEntityManager();
            List<TeamAccount> teamAccounts = manager.createQuery("SELECT a FROM TeamAccount a WHERE a.idChallenge.idChallenge = :idChallenge ", TeamAccount.class)
                    .setParameter("idChallenge", challengeId)
                    .getResultList();
            for (TeamAccount teamAccount : teamAccounts) {
                Team team = teamAccount.getIdTeam();
                TeamMessage teamMessage = new TeamMessage();
                teamMessage.setIdTeam(team.getIdTeam());
                teamMessage.setNameTeam(team.getIdNameTeam());

                ScoreMessage scoreMessage = new ScoreMessage();
                scoreMessage.setTeam(teamMessage);
                scoreMessage.setListResult(null);
                outcome.add(scoreMessage);
            }
            logger.debug("OUTCOME [" + outcome.size() + "]");
        } finally {
            logger.debug("FIN METODO - findTeamsByChallenge()");
        }
        return outcome;
    }

}
