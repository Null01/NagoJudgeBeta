/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.app.business.dao.complex;

import edu.nagojudge.app.business.dao.beans.ChallengeFacadeDAO;
import edu.nagojudge.app.business.dao.entities.Challenge;
import edu.nagojudge.app.business.dao.pojo.ProblemPojo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
@Stateless
public class ChallengeFacadeComplex implements Serializable {

    private final Logger logger = Logger.getLogger(ChallengeFacadeComplex.class);

    @EJB
    private ChallengeFacadeDAO challengeFacade;

    public ChallengeFacadeComplex() {

    }

    public String createChallenge(Challenge challenge, List<ProblemPojo> problemPojos) {
        try {
            logger.debug("INICIA METODO - createChallenge()");
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (ProblemPojo p : problemPojos) {
                if (!first) {
                    sb.append(",");
                }
                sb.append(p.getIdProblem());
                first = false;
            }
            challenge.setIdsProblems(sb.toString());
            logger.debug("getIdsProblems()=" + challenge.getIdsProblems());
            logger.debug("getNameChallenge()=" + challenge.getNameChallenge());
            logger.debug("getDateChallenge()=" + challenge.getDateChallenge());
            logger.debug("getDurationMin()=" + challenge.getDurationMin());
            logger.debug("getIdAccountOrganizer()=" + challenge.getIdAccountOrganizer());
            logger.debug("getIdChallenge()=" + challenge.getIdChallenge());
            logger.debug("getIdsProblems()=" + challenge.getIdsProblems());
            logger.debug("getQuantityProblems()=" + challenge.getQuantityProblems());
            logger.debug("getTeamContestList()=" + challenge.getTeamContestList());
            logger.debug("getDescription()=" + challenge.getDescription());
            challengeFacade.create(challenge);
            return String.valueOf(challenge.getIdChallenge());
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

}
