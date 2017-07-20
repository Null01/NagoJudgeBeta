/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.business.entity.facade.dao;

import edu.nagojudge.live.business.entity.Team;
import edu.nagojudge.live.web.exceptions.NagoJudgeLiveException;
import edu.nagojudge.live.web.utils.FacesUtil;
import edu.nagojudge.live.web.utils.constants.IKeysApplication;
import edu.nagojudge.msg.pojo.ChallengeMessage;
import edu.nagojudge.tools.security.constants.TypeSHAEnum;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author andres.garcia
 */
@Stateless
public class TeamDAO extends AbstractDAO<Team> {

    @EJB
    private ChallengeDAO challengeDAO;

    @EJB
    private SecurityDAO securityDAO;

    private static final Logger logger = Logger.getLogger(TeamDAO.class);

    @PersistenceContext(unitName = "NJLivePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TeamDAO() {
        super(Team.class);
    }

    public Long findTeamAvailable(String username, String password) throws NagoJudgeLiveException {
        Team idTeam = existTeamRegistered(username, password);
        if (idTeam == null) {
            throw new NagoJudgeLiveException("Equipo no registrado");
        }
        List<ChallengeMessage> challengeMessages = challengeDAO.findChallengeAviableByTeam(idTeam.getIdTeam());
        if (challengeMessages == null) {
            throw new NagoJudgeLiveException("No esta inscrito a ninguna competencia.");
        }
        logger.debug("getIdTeam [" + idTeam.getIdTeam() + "]");
        logger.debug("getNameTeam [" + idTeam.getNameTeam() + "]");
        HttpSession session = FacesUtil.getFacesUtil().getSession(true);
        session.setAttribute(IKeysApplication.KEY_SESSION_TEAM_ID, idTeam.getIdTeam());
        session.setAttribute(IKeysApplication.KEY_SESSION_TEAM_NAME, idTeam.getNameTeam());
        return idTeam.getIdTeam();
    }

    private Team existTeamRegistered(String username, String password) {
        String nameTeam = username.trim().toLowerCase();
        Team outcome = null;
        String passwordEncrypted = securityDAO.encodeSHA2(password, TypeSHAEnum.SHA256_NUM);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p FROM Team p WHERE p.nameTeam = :nameTeam AND p.passwordTeam = :passwordTeam");
        List<Team> resultList = em.createQuery(sql.toString(), Team.class).setParameter("nameTeam", nameTeam).setParameter("passwordTeam", passwordEncrypted).getResultList();
        if (resultList != null && resultList.size() == 1) {
            outcome = resultList.get(0);
        }
        return outcome;

    }

}
