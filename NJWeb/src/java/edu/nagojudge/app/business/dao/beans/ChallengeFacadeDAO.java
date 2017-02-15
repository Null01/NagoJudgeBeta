/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.app.business.dao.beans;

import edu.nagojudge.app.business.dao.entities.Challenge;
import edu.nagojudge.app.business.dao.entities.Problem;
import edu.nagojudge.app.business.dao.entities.TeamContest;
import edu.nagojudge.msg.pojo.ChallengeMessage;
import edu.nagojudge.msg.pojo.ProblemMessage;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
@Stateless
public class ChallengeFacadeDAO extends AbstractFacade<Challenge> implements Serializable {

    private final Logger logger = Logger.getLogger(ChallengeFacadeDAO.class);

    @PersistenceContext(unitName = "NJWebPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ChallengeFacadeDAO() {
        super(Challenge.class);
    }

    public String createChallenge(Challenge challenge, List<ProblemMessage> problemsMessage) throws Exception {
        try {
            logger.debug("INICIA METODO - createChallenge()");
            boolean first = true;
            String sql = "SELECT p FROM Problem p WHERE p.idProblem IN (";
            for (ProblemMessage problemMessage : problemsMessage) {
                if (!first) {
                    sql += ",";
                }
                sql += problemMessage.getIdProblem();
                first = false;
            }
            sql += ")";
            logger.debug("QUERY-ATTACH-PROBLEMS: " + sql);

            Query query = em.createQuery(sql, Problem.class);
            challenge.setProblemList(query.getResultList());

            challenge.setDateCreated(Calendar.getInstance().getTime());
            challenge.setDateEnd(challenge.getDateStart()); // Revisar

            logger.debug("getIdAccountOrganizer()=" + challenge.getIdAccountOrganizer());
            logger.debug("getNameChallenge()=" + challenge.getNameChallenge());
            logger.debug("getDescription()=" + challenge.getDescription());
            logger.debug("getTeamContestList()=" + challenge.getTeamContestList());
            logger.debug("getProblemList()=" + challenge.getProblemList());
            logger.debug("getDateCreated()=" + challenge.getDateCreated());
            logger.debug("getDateStart()=" + challenge.getDateStart());
            logger.debug("getDateEnd()=" + challenge.getDateEnd());

            create(challenge);
            return String.valueOf(challenge.getIdChallenge());
        } catch (Exception ex) {
            logger.error(ex);
            throw ex;
        } finally {
            logger.debug("FINALIZA METODO - createChallenge()");
        }
    }

    public List<String> obtenerListaHorasInicioCompetencias() {
        List<String> outcome = new ArrayList<String>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm a");
        for (int i = 0; i < 24; i++) {
            Date time = Calendar.getInstance().getTime();
            time.setHours(i);
            time.setMinutes(0);
            time.setSeconds(0);
            outcome.add(dateFormat.format(time));
        }
        return outcome;
    }

    public List<String> obtenerListaDuracionCompetencias() {
        List<String> outcome = new ArrayList<String>();
        outcome.add("3 Horas");
        outcome.add("4 Horas");
        outcome.add("5 Horas");
        outcome.add("6 Horas");
        outcome.add("7 Horas");
        outcome.add("1 Dia");
        outcome.add("2 Dias");
        outcome.add("3 Dias");
        outcome.add("4 Dias");
        outcome.add("1 Semana");
        outcome.add("2 Semanas");
        return outcome;
    }

    public List<String> obtenerListaNumeroEnunciados() {
        List<String> outcome = new ArrayList<String>();
        for (int i = 1; i <= 12; i++) {
            outcome.add(String.valueOf(i));
        }
        return outcome;
    }

    public List<ChallengeMessage> getAllChallengeMessage() {
        List<ChallengeMessage> outcome = new ArrayList<ChallengeMessage>();
        List<Challenge> challenges = findAll();
        for (Challenge challenge : challenges) {
            ChallengeMessage challengeMessage = new ChallengeMessage();
            challengeMessage.setDateCreated(challenge.getDateCreated());
            challengeMessage.setDateEnd(challenge.getDateEnd());
            challengeMessage.setDateStart(challenge.getDateStart());
            challengeMessage.setDescription(challenge.getDescription());
            challengeMessage.setIdAccountOrganizer(challenge.getIdAccountOrganizer());
            challengeMessage.setIdChallenge(challenge.getIdChallenge());
            challengeMessage.setNameChallenge(challenge.getNameChallenge());

            List<Problem> problemList = challenge.getProblemList();
            List<ProblemMessage> problemMessages = new ArrayList<ProblemMessage>();
            for (Problem problem : problemList) {
                ProblemMessage problemMessage = new ProblemMessage();
                problemMessage.setIdProblem(problem.getIdProblem());
                problemMessage.setNameCategory(problem.getIdCategory().getNameCategory());
                problemMessage.setNameDifficulty(problem.getIdDifficulty().getNameDifficulty());
                problemMessage.setAuthor(problem.getAuthor());
                problemMessage.setNameProblem(problem.getNameProblem());
                problemMessages.add(problemMessage);
            }
            
            challengeMessage.setProblemMessages(problemMessages);
            outcome.add(challengeMessage);
        }
        return outcome;
    }

}
