/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.business.entity.facade.dao;

import edu.nagojudge.live.business.entity.ChallengeProblem;
import edu.nagojudge.live.business.entity.ChallengeSubmit;
import edu.nagojudge.msg.pojo.InfoScoreMessage;
import edu.nagojudge.msg.pojo.ScoreMessage;
import edu.nagojudge.msg.pojo.TeamMessage;
import edu.nagojudge.msg.pojo.collections.ListTypeMessage;
import edu.nagojudge.msg.pojo.constants.TypeStateJudgeEnum;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

            List<ChallengeProblem> challengeProblems = manager.createQuery("SELECT cp FROM ChallengeProblem cp WHERE cp.idChallenge.idChallenge = :id_challenge", ChallengeProblem.class)
                    .setParameter("id_challenge", challengeId)
                    .getResultList();

            final HashMap<Long, InfoScoreMessage> infoScore = new HashMap<Long, InfoScoreMessage>();
            for (ChallengeProblem challengeProblem : challengeProblems) {
                InfoScoreMessage infoScoreMessage = new InfoScoreMessage();
                infoScoreMessage.setIdProblem(challengeProblem.getIdProblem().getIdProblem());
                infoScoreMessage.setSolved(false);
                infoScoreMessage.setTime(0);
                infoScoreMessage.setTries(0);
                infoScore.put(infoScoreMessage.getIdProblem(), infoScoreMessage);
            }

            StringBuilder sql = new StringBuilder();
            sql.append("select \n"
                    + "(select t.id_name_team from team t where t.id_team = cs.id_team) as team,\n"
                    + "s.id_problem as problem,\n"
                    + "(select ss.key_status from submit_status ss where ss.id_status = s.id_status) as status, \n"
                    + "sum(s.date_submit - c.date_start) as minutes,\n"
                    + "count(0) as n\n"
                    + "from challenge_submit cs left join submit s on (cs.id_submit = s.id_submit)\n"
                    + "left join challenge c on(c.id_challenge = cs.id_challenge)\n"
                    + "where cs.id_challenge = " + String.valueOf(challengeId) + "\n"
                    + "group by cs.id_team, s.id_problem, s.id_status\n");
            List<Object[]> objects = manager.createNativeQuery(sql.toString())
                    .getResultList();

            Map<String, Map<Long, InfoScoreMessage>> map = new HashMap<String, Map<Long, InfoScoreMessage>>();
            for (Object[] row : objects) {
                if (map.get((String) row[0]) == null) {
                    map.put((String) row[0], crashCopyStructAllProblemsToChallenge(infoScore));
                }

                InfoScoreMessage infoScoreMessage = map.get((String) row[0]).get((long) row[1]);
                boolean solved = ((String) row[2]).compareTo(TypeStateJudgeEnum.AC.toString()) == 0;
                infoScoreMessage.setSolved(infoScoreMessage.isSolved() ? true : solved);
                if (solved) {
                    infoScoreMessage.setTime(infoScoreMessage.getTime() + Long.parseLong(String.valueOf(row[3])));
                    infoScoreMessage.setTries(infoScoreMessage.getTries() + Integer.parseInt(String.valueOf(row[4])));
                } else {
                    infoScoreMessage.setTime(infoScoreMessage.getTime() + Long.parseLong(String.valueOf(row[3])));
                    infoScoreMessage.setTries(infoScoreMessage.getTries() + Integer.parseInt(String.valueOf(row[4])));
                }
                map.get((String) row[0]).put((long) row[1], infoScoreMessage);
            }

            for (Map.Entry<String, Map<Long, InfoScoreMessage>> team : map.entrySet()) {
                TeamMessage teamMessage = new TeamMessage();
                teamMessage.setNameTeam(team.getKey());

                ScoreMessage scoreMessage = new ScoreMessage();
                scoreMessage.setTeam(teamMessage);

                ListTypeMessage<InfoScoreMessage> listTypeMessage = new ListTypeMessage<InfoScoreMessage>();
                for (Map.Entry<Long, InfoScoreMessage> problemScore : team.getValue().entrySet()) {
                    listTypeMessage.add(problemScore.getValue());
                    InfoScoreMessage infoScoreMessage = problemScore.getValue();
                    scoreMessage.setTime((scoreMessage.getTime() == null ? 0 : scoreMessage.getTime()) + infoScoreMessage.getTime());
                }
                scoreMessage.setResumeScore(listTypeMessage);
                outcome.add(scoreMessage);
            }

            /*
            Map<Long, HashMap<Long, InfoScoreMessage>> map = new HashMap<Long, HashMap<Long, InfoScoreMessage>>();
            Map<Long, String> mapTeams = new HashMap<Long, String>();
            for (ChallengeSubmit challengeSubmit : challengeSubmits) {
                Team team = challengeSubmit.getIdTeam();
                Problem problem = challengeSubmit.getIdSubmit().getIdProblem();
                boolean solved = challengeSubmit.getIdSubmit().getIdStatus().getKeyStatus().compareTo(TypeStateJudgeEnum.AC.toString()) == 0;
                
                if (map.get(team.getIdTeam()) == null) {
                    map.put(team.getIdTeam(), new HashMap<Long, InfoScoreMessage>());
                    mapTeams.put(team.getIdTeam(), team.getIdNameTeam());
                }
                if (map.get(team.getIdTeam()).get(problem.getIdProblem()) == null) {
                    InfoScoreMessage infoScoreMessage = new InfoScoreMessage();
                    infoScoreMessage.setIdProblem(problem.getIdProblem());
                    infoScoreMessage.setSolved(solved);
                    map.get(team.getIdTeam()).put(problem.getIdProblem(), infoScoreMessage);
                }
                map.get(team.getIdTeam()).get(problem.getIdProblem()).addTry();
                map.get(team.getIdTeam()).get(problem.getIdProblem()).isSolve(solved);
            }
            
            int iterator = 0;
            for (Map.Entry<Long, HashMap<Long, InfoScoreMessage>> entry : map.entrySet()) {
                TeamMessage teamMessage = new TeamMessage();
                teamMessage.setIdTeam(entry.getKey());
                teamMessage.setNameTeam(mapTeams.get(entry.getKey()));

                ListTypeMessage<InfoScoreMessage> listTypeMessage = new ListTypeMessage<InfoScoreMessage>();
                for (Map.Entry<Long, InfoScoreMessage> e : entry.getValue().entrySet()) {
                    listTypeMessage.add(e.getValue());
                }
                ScoreMessage scoreMessage = new ScoreMessage();
                scoreMessage.setAliasScore(String.valueOf((char) 'A' + iterator));
                scoreMessage.setTeam(teamMessage);
                scoreMessage.setScoreTeam(listTypeMessage);
                outcome.add(scoreMessage);
                ++iterator;
            }
             */
            logger.debug("OUTCOME [" + outcome.size() + "]");
        } finally {
            logger.debug("FIN METODO - findScoreAllTeamFromChallenge()");
        }
        return outcome;
    }

    private Map<Long, InfoScoreMessage> crashCopyStructAllProblemsToChallenge(HashMap<Long, InfoScoreMessage> temp) {
        Map<Long, InfoScoreMessage> outcome = new HashMap<Long, InfoScoreMessage>();
        for (Map.Entry<Long, InfoScoreMessage> entry : temp.entrySet()) {
            InfoScoreMessage infoScoreMessage = new InfoScoreMessage();
            infoScoreMessage.setIdProblem(entry.getValue().getIdProblem());
            infoScoreMessage.setSolved(entry.getValue().isSolved());
            infoScoreMessage.setTime(entry.getValue().getTime());
            infoScoreMessage.setTries(entry.getValue().getTries());
            outcome.put(entry.getKey(), infoScoreMessage);
        }
        return outcome;
    }

}
