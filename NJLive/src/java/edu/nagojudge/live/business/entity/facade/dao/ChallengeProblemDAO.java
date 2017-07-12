/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.business.entity.facade.dao;

import edu.nagojudge.live.business.entity.ChallengeProblem;
import edu.nagojudge.live.business.entity.Problem;
import edu.nagojudge.live.business.entity.ProblemCategory;
import edu.nagojudge.live.web.utils.FacesUtil;
import edu.nagojudge.live.web.utils.clients.ClientService;
import edu.nagojudge.msg.pojo.CategoryMessage;
import edu.nagojudge.msg.pojo.ProblemMessage;
import edu.nagojudge.msg.pojo.StringMessage;
import edu.nagojudge.msg.pojo.collections.ListMessage;
import java.io.IOException;
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
public class ChallengeProblemDAO extends AbstractDAO<ChallengeProblem> {

    private final String TOKEN = "asd";

    private final Logger logger = Logger.getLogger(ChallengeProblemDAO.class);

    @PersistenceContext(unitName = "NJLivePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ChallengeProblemDAO() {
        super(ChallengeProblem.class);
    }

    public List<ProblemMessage> findProblemsByChallenge(Long challengeId) {
        List<ProblemMessage> outome = new ArrayList<ProblemMessage>();
        List<Problem> problems = em.createQuery("SELECT a.idProblem FROM ChallengeProblem a WHERE a.idChallenge.idChallenge = :id_challenge ORDER BY a.idProblem.idProblem ASC", Problem.class)
                .setParameter("id_challenge", challengeId).getResultList();
        for (Problem problem : problems) {
            outome.add(parseProblemEntityToMessage(problem));
        }
        return outome;
    }

    private ProblemMessage parseProblemEntityToMessage(Problem problem) {
        ProblemMessage problemMessage = new ProblemMessage();
        problemMessage.setIdProblem(problem.getIdProblem());
        problemMessage.setNameProblem(problem.getNameProblem());
        problemMessage.setBestComplexity(problem.getIdComplexityAlgorithm().getNameComplexityAlgorithm());
        problemMessage.setMemoLimit(problem.getMemoLimit());
        problemMessage.setTimeLimit(problem.getTimeLimit());
        List<ProblemCategory> problemCategorys = problem.getProblemCategoryList();
        ListMessage<CategoryMessage> listMessage = new ListMessage<CategoryMessage>();
        for (ProblemCategory category : problemCategorys) {
            CategoryMessage categoryMessage = new CategoryMessage();
            categoryMessage.setIdCategory(category.getIdCategory().getIdCategory());
            categoryMessage.setNameCategory(category.getIdCategory().getNameCategory());
            listMessage.add(categoryMessage);
        }
        problemMessage.setListCategoryMessage(listMessage);
        return problemMessage;
    }

    public String getFullPathProblem(final Long idProblem) throws IOException {
        try {
            final String path = FacesUtil.getFacesUtil().getParameterWEBINF("init-config", "judge.path.problem.view");
            final String host = FacesUtil.getFacesUtil().getParameterWEBINF("init-config", "judge.nagojudge.url");
            Object objects[] = {String.valueOf(idProblem)};
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("token", TOKEN);
            StringMessage objectMessage = (StringMessage) ClientService.getInstance().callRestfulGet(host, path, objects, params, StringMessage.class);
            return String.valueOf(objectMessage.getString());
        } finally {
            logger.debug("FINALIZA METODO - getFullPathProblem()");
        }
    }

}
