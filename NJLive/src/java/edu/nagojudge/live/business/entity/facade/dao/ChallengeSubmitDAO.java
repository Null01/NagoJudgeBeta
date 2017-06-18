/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.business.entity.facade.dao;

import edu.nagojudge.live.business.entity.ChallengeProblem;
import edu.nagojudge.live.business.entity.ChallengeSubmit;
import edu.nagojudge.live.business.entity.Submit;
import edu.nagojudge.live.web.utils.FacesUtil;
import edu.nagojudge.msg.pojo.InfoScoreMessage;
import edu.nagojudge.msg.pojo.LanguageProgrammingMessage;
import edu.nagojudge.msg.pojo.ProblemMessage;
import edu.nagojudge.msg.pojo.ScoreMessage;
import edu.nagojudge.msg.pojo.SubmitMessage;
import edu.nagojudge.msg.pojo.TeamMessage;
import edu.nagojudge.msg.pojo.collections.ListMessage;
import edu.nagojudge.msg.pojo.constants.TypeStateJudgeEnum;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    public List<ScoreMessage> calculateScoreByChallenge(Long challengeId) {
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

            Long penalityTime = Long.parseLong(FacesUtil.getFacesUtil().getParameterWEBINF("init-config", "judge.nagojudge.penality.time"));

            StringBuilder sql = new StringBuilder();
            sql.append("(select \n"
                    + " (select t.name_team from team t where t.id_team = cs.id_team) as team,\n"
                    + " s.id_problem as problem,\n"
                    + " (select ss.key_status from submit_status ss where ss.id_status = s.id_status) as status, \n"
                    + " min(timestampdiff(minute, c.date_start, s.date_submit)) as minutes,\n"
                    + " count(0) as n\n"
                    + "from challenge_submit cs left join submit s on (cs.id_submit = s.id_submit)\n"
                    + "left join challenge c on(c.id_challenge = cs.id_challenge)\n"
                    + "where cs.id_challenge = " + String.valueOf(challengeId) + "\n"
                    + "group by cs.id_team, s.id_problem, s.id_status\n"
                    + "order by s.id_status desc)\n"
                    + "union\n"
                    + "(select (select t.name_team from team t where t.id_team = ct.id_team) as team, null, null, null, null \n"
                    + "from challenge_team ct where ct.id_challenge = " + String.valueOf(challengeId) + " and ct.id_team not \n"
                    + "in (select id_team from challenge_submit where id_challenge = " + String.valueOf(challengeId) + " group by id_team)\n"
                    + "group by id_team)");
            List<Object[]> objects = manager.createNativeQuery(sql.toString())
                    .getResultList();
            logger.debug("objects [" + objects.size() + "]");
            try {
                Map<String, Map<Long, InfoScoreMessage>> map = new HashMap<String, Map<Long, InfoScoreMessage>>();
                for (Object[] row : objects) {
                    if (map.get((String) row[0]) == null) {
                        map.put((String) row[0], crashCopyStructAllProblemsToChallenge(infoScore));
                    }
                    if (row[1] != null) {
                        InfoScoreMessage infoScoreMessage = map.get((String) row[0]).get((Long) row[1]);
                        boolean solved = ((String) row[2]).compareTo(TypeStateJudgeEnum.AC.toString()) == 0;
                        if (solved && !infoScoreMessage.isSolved()) {
                            infoScoreMessage.setTime(Long.parseLong(String.valueOf(row[3])) + (infoScoreMessage.getTries() * penalityTime));
                            infoScoreMessage.setTries(infoScoreMessage.getTries() + 1);
                        } else {
                            if (!infoScoreMessage.isSolved()) {
                                infoScoreMessage.setTries(infoScoreMessage.getTries() + Integer.parseInt(String.valueOf(row[4])));
                            }
                        }
                        infoScoreMessage.setSolved(infoScoreMessage.isSolved() ? true : solved);
                        map.get((String) row[0]).put((Long) row[1], infoScoreMessage);
                    }
                }

                for (Map.Entry<String, Map<Long, InfoScoreMessage>> team : map.entrySet()) {
                    TeamMessage teamMessage = new TeamMessage();
                    teamMessage.setNameTeam(team.getKey());

                    ScoreMessage scoreMessage = new ScoreMessage();
                    scoreMessage.setTeam(teamMessage);

                    ListMessage<InfoScoreMessage> listTypeMessage = new ListMessage<InfoScoreMessage>();
                    for (Map.Entry<Long, InfoScoreMessage> problemScore : team.getValue().entrySet()) {
                        listTypeMessage.add(problemScore.getValue());
                        InfoScoreMessage infoScoreMessage = problemScore.getValue();
                        scoreMessage.setTime((scoreMessage.getTime() == null ? 0 : scoreMessage.getTime()) + infoScoreMessage.getTime());
                        scoreMessage.setSolved((scoreMessage.getSolved() == null ? 0 : scoreMessage.getSolved()) + (infoScoreMessage.isSolved() ? 1 : 0));
                    }
                    scoreMessage.setResumeScore(listTypeMessage);
                    outcome.add(scoreMessage);
                }

            } catch (NumberFormatException e) {
                logger.error(e);
            }
            Comparator<ScoreMessage> comparator = new Comparator<ScoreMessage>() {
                @Override
                public int compare(ScoreMessage o1, ScoreMessage o2) {
                    if (o1.getSolved().compareTo(o2.getSolved()) == 0) {
                        return (int) (o1.getTime() - o2.getTime());
                    }
                    return (int) (o2.getSolved() - o1.getSolved());
                }
            };
            Collections.sort(outcome, comparator);
            logger.debug("outcome [" + outcome.size() + "]");
        } finally {
            logger.debug("FIN METODO - findScoreAllTeamFromChallenge()");
        }
        return new ArrayList<ScoreMessage>(outcome);
    }

    public List<SubmitMessage> findAllSubmitByTeam(Long teamId, Long challengeId) {
        List<SubmitMessage> outcome = new ArrayList<SubmitMessage>();
        try {
            logger.debug("INICIO METODO - findAllSubmitByTeam()");
            logger.debug("teamId [" + teamId + "]");
            logger.debug("challengeId [" + challengeId + "]");
            EntityManager manager = getEntityManager();
            List<Submit> submits
                    = manager.createQuery("SELECT cs.idSubmit FROM ChallengeSubmit cs WHERE cs.idTeam.idTeam = :idTeam AND cs.idChallenge.idChallenge = :idChallenge ORDER BY cs.idSubmit.dateSubmit DESC", Submit.class)
                            .setParameter("idTeam", teamId)
                            .setParameter("idChallenge", challengeId)
                            .getResultList();
            for (Submit submit : submits) {
                SubmitMessage submitMessage = new SubmitMessage();
                submitMessage.setIdSubmit(submit.getIdSubmit());
                submitMessage.setIdStatus(submit.getIdStatus().getKeyStatus());
                submitMessage.setNameStatus(submit.getIdStatus().getNameStatus());
                submitMessage.setDescriptionStatus(submit.getIdStatus().getDescription());
                submitMessage.setTimeUsed(submit.getTimeUsed() == null ? 0 : submit.getTimeUsed().longValue());
                submitMessage.setDateSubmit(submit.getDateSubmit() != null ? submit.getDateSubmit().getTime() : 0);

                LanguageProgrammingMessage languageProgrammingMessage = new LanguageProgrammingMessage();
                languageProgrammingMessage.setNameProgramming(submit.getIdLanguage().getNameLanguage());
                submitMessage.setLanguageProgrammingMessage(languageProgrammingMessage);

                ProblemMessage problemMessage = new ProblemMessage();
                problemMessage.setIdProblem(submit.getIdProblem().getIdProblem());
                problemMessage.setNameProblem(submit.getIdProblem().getNameProblem());
                submitMessage.setProblemMessage(problemMessage);
                outcome.add(submitMessage);
            }
        } catch (Exception ex) {
            logger.error(ex);
        } finally {
            logger.debug("FINALIZA METODO - findAllSubmitByTeam()");
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
