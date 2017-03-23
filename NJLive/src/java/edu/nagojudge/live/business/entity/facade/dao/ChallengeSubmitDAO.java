/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.business.entity.facade.dao;

import edu.nagojudge.live.business.entity.ChallengeSubmit;
import edu.nagojudge.live.business.entity.SubmitStatus;
import edu.nagojudge.live.business.entity.Team;
import edu.nagojudge.msg.pojo.InfoScoreMessage;
import edu.nagojudge.msg.pojo.ScoreMessage;
import edu.nagojudge.msg.pojo.TeamMessage;
import edu.nagojudge.msg.pojo.collections.ListTypeMessage;
import edu.nagojudge.msg.pojo.constants.TypeStateJudgeEnum;
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
            logger.debug("INICIO METODO - findScoreAllTeamFromChallenge()");
            logger.debug("challengeId [" + challengeId + "]");
            EntityManager manager = getEntityManager();
            List<ChallengeSubmit> challengeSubmits = manager.createQuery("SELECT cs FROM ChallengeSubmit cs LEFT JOIN cs.idSubmit s WHERE (cs.idChallenge.idChallenge = :id_challenge) ", ChallengeSubmit.class)
                    .setParameter("id_challenge", challengeId)
                    .getResultList();
            
            Map<String, ListTypeMessage<InfoScoreMessage>> map = new HashMap<String, ListTypeMessage<InfoScoreMessage>>();
            for (ChallengeSubmit challengeSubmit : challengeSubmits) {
                SubmitStatus idStatus = challengeSubmit.getIdSubmit().getIdStatus();
                Team idTeam = challengeSubmit.getIdTeam();
                
                InfoScoreMessage infoScoreMessage = new InfoScoreMessage();
                infoScoreMessage.setSolved(infoScoreMessage.isSolved() ? true : (idStatus.getKeyStatus().compareTo(TypeStateJudgeEnum.AC.toString()) == 0));
                infoScoreMessage.setTries((idStatus.getKeyStatus().compareTo(TypeStateJudgeEnum.AC.toString()) == 0) ? infoScoreMessage.getTries() : (infoScoreMessage.getTries() + 1));
                if (map.get(idTeam.getIdNameTeam()) == null) {
                    map.put(idTeam.getIdNameTeam(), new ListTypeMessage<InfoScoreMessage>());
                }
                map.get(idTeam.getIdNameTeam()).add(infoScoreMessage);
            }         
            
            for (Map.Entry<String, ListTypeMessage<InfoScoreMessage>> entry : map.entrySet()) {
                TeamMessage teamMessage = new TeamMessage();
                teamMessage.setNameTeam(entry.getKey());
                
                ScoreMessage scoreMessage = new ScoreMessage();
                scoreMessage.setTeam(teamMessage);
                scoreMessage.setScoreTeam(entry.getValue());
                
                outcome.add(scoreMessage);
            }
            
            logger.debug("OUTCOME [" + outcome.size() + "]");
        } finally {
            logger.debug("FIN METODO - findScoreAllTeamFromChallenge()");
        }
        return outcome;
    }
    
}
