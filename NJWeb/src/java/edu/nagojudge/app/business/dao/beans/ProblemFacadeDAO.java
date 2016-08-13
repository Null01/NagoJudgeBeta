/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.app.business.dao.beans;

import edu.nagojudge.app.business.dao.entities.Problem;
import edu.nagojudge.app.business.dao.pojo.ProblemPojo;
import edu.nagojudge.msg.pojo.constants.TypeStateJudgeEnum;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

/**
 *
 * @author andresfelipegarciaduran
 */
@Stateless
public class ProblemFacadeDAO extends AbstractFacade<Problem> implements Serializable{

    @PersistenceContext(unitName = "NJWebPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProblemFacadeDAO() {
        super(Problem.class);
    }

    private List<Problem> findProblemEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Problem.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();

    }

    public Map<String, Long> findStatisticsStatus(long idProblem) {
        EntityManager em = getEntityManager();
        Map<String, Long> mapStatisticsStatus = new HashMap<String, Long>();
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT STATUS_SUBMIT, COUNT(0) FROM SUBMIT WHERE ID_PROBLEM = ? GROUP BY STATUS_SUBMIT ");
        Query query = em.createNativeQuery(sb.toString()).setParameter(1, idProblem);
        List<Object[]> resultList = query.getResultList();
        for (Object[] r : resultList) {
            mapStatisticsStatus.put(r[0].toString(), Long.parseLong(String.valueOf(r[1])));
        }
        return mapStatisticsStatus;
    }

    public Map<String, Long> findStatisticsStatusByAccount(long idProblem, long idAccount) {
        EntityManager em = getEntityManager();
        Map<String, Long> mapStatisticsStatus = new HashMap<String, Long>();
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT STATUS_SUBMIT, COUNT(0) FROM SUBMIT WHERE ID_PROBLEM = ? AND ID_ACCOUNT = ? GROUP BY STATUS_SUBMIT ");
        Query query = em.createNativeQuery(sb.toString()).setParameter(1, idProblem).setParameter(2, idAccount);
        List<Object[]> resultList = query.getResultList();
        for (Object[] r : resultList) {
            mapStatisticsStatus.put(r[0].toString(), Long.parseLong(String.valueOf(r[1])));
        }
        return mapStatisticsStatus;
    }

    public List<ProblemPojo> findProblemEntitiesPojo() {
        Map<Long, Map<String, Long>> map = new HashMap<Long, Map<String, Long>>();
        EntityManager em = getEntityManager();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.ID_PROBLEM, S.STATUS_SUBMIT, COUNT(0) FROM SUBMIT S GROUP BY S.ID_PROBLEM, S.STATUS_SUBMIT ORDER BY S.ID_PROBLEM DESC");
        List<Object[]> resultList = em.createNativeQuery(sql.toString()).getResultList();
        for (Object[] objects : resultList) {
            long idProblem = Long.parseLong(String.valueOf(objects[0]));
            if (map.get(idProblem) == null) {
                map.put(idProblem, new HashMap<String, Long>());
            }
            String status = String.valueOf(objects[1]);
            long value = Long.parseLong(String.valueOf(objects[2]));
            map.get(idProblem).put(status, value);
        }

        List<ProblemPojo> problemPojos = new ArrayList<ProblemPojo>();
        List<Problem> problems = findProblemEntities(true, -1, -1);
        for (Problem p : problems) {
            ProblemPojo problemPojo = new ProblemPojo(p.getIdProblem(), p.getNameProblem(), p.getAuthor(), p.getIdCategory().getNameCategory(), p.getIdDifficulty().getNameDifficulty(),
                    p.getDescription(), p.getTimeLimitSeg(), 0, 0, 0, 0, 0, 0, 0, 0);
            Map<String, Long> mapValues = map.get(problemPojo.getIdProblem());
            if (mapValues != null) {
                int sumTotal = 0;
                for (Map.Entry<String, Long> row : mapValues.entrySet()) {
                    sumTotal += row.getValue();
                    if (row.getKey().compareTo(TypeStateJudgeEnum.AC.getValue()) == 0) {
                        problemPojo.setStatusAC(row.getValue().intValue());
                    }
                    if (row.getKey().compareTo(TypeStateJudgeEnum.CE.getValue()) == 0) {
                        problemPojo.setStatusCE(row.getValue().intValue());
                    }
                    if (row.getKey().compareTo(TypeStateJudgeEnum.CS.getValue()) == 0) {
                        problemPojo.setStatusCS(row.getValue().intValue());
                    }
                    if (row.getKey().compareTo(TypeStateJudgeEnum.IP.getValue()) == 0) {
                        problemPojo.setStatusIP(row.getValue().intValue());
                    }
                    if (row.getKey().compareTo(TypeStateJudgeEnum.RE.getValue()) == 0) {
                        problemPojo.setStatusRE(row.getValue().intValue());
                    }
                    if (row.getKey().compareTo(TypeStateJudgeEnum.TL.getValue()) == 0) {
                        problemPojo.setStatusTL(row.getValue().intValue());
                    }
                    if (row.getKey().compareTo(TypeStateJudgeEnum.WR.getValue()) == 0) {
                        problemPojo.setStatusWR(row.getValue().intValue());
                    }
                    problemPojo.setTotalStatus(sumTotal);
                }
            }
            problemPojos.add(problemPojo);
        }
        return problemPojos;
    }

    public List<Problem> findProblemTryEntities(long idAccount) {
        EntityManager em = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM PROBLEM WHERE ID_PROBLEM IN (SELECT ID_PROBLEM FROM SUBMIT WHERE  ID_ACCOUNT = ").append(idAccount).append(")");
        List<Problem> resultList = em.createNativeQuery(sql.toString(), Problem.class).getResultList();
        return resultList;

    }

    public List<Problem> findProblemTrySolveEntities(long idAccount) {
        EntityManager em = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM PROBLEM WHERE ID_PROBLEM IN (SELECT ID_PROBLEM FROM SUBMIT ");
        sql.append(" WHERE STATUS_SUBMIT = '").append(TypeStateJudgeEnum.AC.getValue()).append("' AND ID_ACCOUNT = ").append(idAccount).append(")");
        List<Problem> resultList = em.createNativeQuery(sql.toString(), Problem.class).getResultList();
        return resultList;

    }

}
