/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.business.entity.facade.dao;

import edu.nagojudge.live.business.entity.ChallengeProblem;
import edu.nagojudge.live.business.entity.Problem;
import edu.nagojudge.live.business.entity.ProblemCategory;
import edu.nagojudge.live.web.utils.constants.IResourcesPath;
import edu.nagojudge.msg.pojo.CategoryMessage;
import edu.nagojudge.msg.pojo.ProblemMessage;
import edu.nagojudge.msg.pojo.collections.ListMessage;
import edu.nagojudge.msg.pojo.constants.TypeFilesEnum;
import edu.nagojudge.tools.utils.FileUtil;
import edu.nagojudge.tools.utils.FormatUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
        List<Problem> problems = em.createQuery("SELECT a.idProblem FROM ChallengeProblem a WHERE a.idChallenge.idChallenge = :id_challenge ", Problem.class)
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
        problemMessage.setTimeLimitSeg(problem.getTimeLimit());
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

    public String getFullPathProblem(Long idProblem) throws IOException {
        String fullPath = "";
        try {
            logger.debug("INICIA METODO - getFullPathProblem()");
            String nameFile = FormatUtil.getInstance().buildZerosToLeft(idProblem, 7) + TypeFilesEnum.PDF.getExtension();
            String tempFullPath = IResourcesPath.PATH_ROOT_SAVE_PROBLEMS_WEB + java.io.File.separatorChar + nameFile;
            boolean existFile = FileUtil.getInstance().existFile(tempFullPath);
            if (!existFile) {
                logger.debug("FILE NOT EXIST FULL_PATH [" + tempFullPath + "]");
                String fullPathLocal = IResourcesPath.PATH_SAVE_PROBLEMS_LOCAL + java.io.File.separatorChar + nameFile;
                FileUtil.getInstance().copyFile(fullPathLocal, tempFullPath);
            }
            fullPath = IResourcesPath.PATH_VIEW_PROBLEMS + java.io.File.separatorChar + nameFile;
            fullPath = fullPath.replace("\\", "/");
            logger.debug("PATH_VIEW_FILE_PUBLIC [" + fullPath + "]");
            return fullPath;
        } catch (IOException ex) {
            logger.error(ex);
            throw ex;
        } finally {
            logger.debug("FINALIZA METODO - getFullPathProblem()");
        }
    }

}
