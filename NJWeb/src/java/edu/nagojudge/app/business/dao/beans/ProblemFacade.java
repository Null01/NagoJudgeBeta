/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.app.business.dao.beans;

import edu.nagojudge.app.business.dao.entities.AccountSubmit;
import edu.nagojudge.app.business.dao.entities.Attachments;
import edu.nagojudge.app.business.dao.entities.CategoryProblem;
import edu.nagojudge.app.business.dao.entities.DifficultyLevel;
import edu.nagojudge.app.business.dao.entities.Problem;
import edu.nagojudge.app.exceptions.UtilNagoJudgeException;
import edu.nagojudge.app.utils.ValidatorUtil;
import edu.nagojudge.app.utils.constants.IResourcesPaths;
import edu.nagojudge.msg.pojo.ProblemMessage;
import edu.nagojudge.msg.pojo.constants.TypeFilesEnum;
import edu.nagojudge.msg.pojo.constants.TypeStateJudgeEnum;
import edu.nagojudge.tools.security.constants.TypeSHAEnum;
import edu.nagojudge.tools.utils.FileUtil;
import edu.nagojudge.tools.utils.FormatUtil;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
@Stateless
public class ProblemFacade extends AbstractFacade<Problem> {

    @EJB
    private AttachmentsFacade attachmentsFacadeDAO;

    private final Logger logger = Logger.getLogger(ProblemFacade.class);

    @PersistenceContext(unitName = "NJWebPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProblemFacade() {
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

    public List<ProblemMessage> findProblemEntitiesPojo() {
        Map<Long, Map<String, Long>> map = new HashMap<Long, Map<String, Long>>();
        EntityManager em = getEntityManager();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p.idSubmit.idProblem.idProblem, p.idSubmit.idStatus.idStatus, COUNT(0) ");
        sql.append("FROM AccountSubmit p GROUP BY p.idSubmit.idProblem.idProblem, p.idSubmit.idStatus.idStatus ");
        sql.append("ORDER BY p.idSubmit.idProblem.idProblem DESC");
        Query query = em.createQuery(sql.toString(), AccountSubmit.class);
        List<Object[]> resultList = query.getResultList();

        for (Object[] objects : resultList) {
            long idProblem = Long.parseLong(String.valueOf(objects[0]));
            if (map.get(idProblem) == null) {
                map.put(idProblem, new HashMap<String, Long>());
            }
            String status = String.valueOf(objects[1]);
            long value = Long.parseLong(String.valueOf(objects[2]));
            map.get(idProblem).put(status, value);
        }

        List<ProblemMessage> problemPojos = new ArrayList<ProblemMessage>();
        List<Problem> problems = findProblemEntities(true, -1, -1);
        for (Problem p : problems) {
            ProblemMessage problemPojo = new ProblemMessage(p.getIdProblem(), p.getNameProblem(), p.getAuthor(), p.getIdCategory().getNameCategory(), p.getIdDifficulty().getNameDifficulty(),
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
        if (resultList != null && resultList.size() >= 1) {
            logger.debug(resultList.get(0).getIdCategory());
        }
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

    public String createProblem(Problem problemView, CategoryProblem categoryProblemView, DifficultyLevel difficultyLevel, byte[] problem, byte[] input, byte[] output) throws IOException, NoSuchAlgorithmException, UtilNagoJudgeException, Exception {
        try {
            logger.debug("INICIA METODO - createProblem()");
            problemView.setIdCategory(categoryProblemView);
            logger.debug(" CAMPO getIdCategory=" + problemView.getIdCategory() + " @ECHO");
            problemView.setIdDifficulty(difficultyLevel);
            logger.debug(" CAMPO getIdDifficulty=" + problemView.getIdDifficulty() + " @ECHO");
            ValidatorUtil.getUtilValidator().onlyLetterNumberSpace(problemView.getAuthor());
            problemView.setAuthor(problemView.getAuthor());
            logger.debug("VALACION CAMPO getAuthor=" + problemView.getAuthor() + " @ECHO");
            ValidatorUtil.getUtilValidator().onlyLetterNumberSpace(problemView.getNameProblem());
            problemView.setNameProblem(problemView.getNameProblem());
            logger.debug("VALACION CAMPO getNameProblem=" + problemView.getNameProblem() + " @ECHO");
            logger.debug(" CAMPO getDescription=" + problemView.getDescription() + " @ECHO");
            logger.debug(" CAMPO getTimeLimitSeg=" + problemView.getTimeLimitSeg() + " @ECHO");

            create(problemView);
            logger.debug("CREACION ENTIDAD PROBLEMA_ID=" + problemView.getIdProblem() + " @ECHO");

            String pathProblem = IResourcesPaths.PATH_ROOT_SAVE_PROBLEMS_WEB;
            String nameFileProblem = FormatUtil.getInstance().buildZerosToLeft(problemView.getIdProblem(), 7) + TypeFilesEnum.PDF.getExtension();
            logger.debug("PATH_PROBLEM=" + pathProblem);
            logger.debug("NAME_FILE_PROBLEM=" + nameFileProblem);
            FileUtil.getInstance().createFile(problem, pathProblem, nameFileProblem);

            String pathProblemLocal = IResourcesPaths.PATH_SAVE_PROBLEMS_LOCAL;
            String nameFileProblemLocal = FormatUtil.getInstance().buildZerosToLeft(problemView.getIdProblem(), 7) + TypeFilesEnum.PDF.getExtension();
            logger.debug("PATH_PROBLEM_LOCAL=" + pathProblemLocal);
            logger.debug("NAME_FILE_PROBLEM_LOCAL=" + nameFileProblemLocal);
            FileUtil.getInstance().createFile(problem, pathProblemLocal, nameFileProblemLocal);

            String pathInput = IResourcesPaths.PATH_SAVE_INPUT_LOCAL;
            String nameFileInput = FormatUtil.getInstance().buildZerosToLeft(problemView.getIdProblem(), 7);
            logger.debug("PATH_INPUT=" + pathInput);
            logger.debug("NAME_FILE_INPUT=" + nameFileInput);
            FileUtil.getInstance().createFile(input, pathInput, nameFileInput);

            String pathOutput = IResourcesPaths.PATH_SAVE_OUTPUT_LOCAL;
            String nameFileOutput = FormatUtil.getInstance().buildZerosToLeft(problemView.getIdProblem(), 7);
            logger.debug("PATH_OUTPUT=" + pathOutput);
            logger.debug("NAME_FILE_OUTPUT=" + nameFileOutput);
            FileUtil.getInstance().createFile(output, pathOutput, nameFileOutput);

            logger.debug("INIT - CREACION ATTACHMENTS @ECHO");

            List<Attachments> attachmentses = new ArrayList<Attachments>();
            Attachments attachment = new Attachments();
            attachment.setChecksum(FileUtil.getInstance().generateChechSum(problem, TypeSHAEnum.SHA256));
            attachment.setDateCreated(Calendar.getInstance().getTime());
            attachment.setIdProblem(problemView);
            attachment.setTypeFileServer(TypeFilesEnum.TYPE_FILE_PROBLEM.getExtension());
            attachmentses.add(attachment);
            attachmentsFacadeDAO.create(attachment);
            logger.debug("CREACION ATTACHMENT_ID=" + attachment.getIdAttachment() + "  @ECHO");

            attachment = new Attachments();
            attachment.setChecksum(FileUtil.getInstance().generateChechSum(input, TypeSHAEnum.SHA256));
            attachment.setDateCreated(Calendar.getInstance().getTime());
            attachment.setIdProblem(problemView);
            attachment.setTypeFileServer(TypeFilesEnum.TYPE_FILE_IN.getExtension());
            attachmentses.add(attachment);
            attachmentsFacadeDAO.create(attachment);
            logger.debug("CREACION ATTACHMENT_ID=" + attachment.getIdAttachment() + "  @ECHO");

            attachment = new Attachments();
            attachment.setChecksum(FileUtil.getInstance().generateChechSum(output, TypeSHAEnum.SHA256));
            attachment.setDateCreated(Calendar.getInstance().getTime());
            attachment.setIdProblem(problemView);
            attachment.setTypeFileServer(TypeFilesEnum.TYPE_FILE_OUT.getExtension());
            attachmentses.add(attachment);
            attachmentsFacadeDAO.create(attachment);
            logger.debug("CREACION ATTACHMENT_ID=" + attachment.getIdAttachment() + "  @ECHO");

            return String.valueOf(problemView.getIdProblem());
        } catch (IOException ex) {
            logger.error(ex);
            throw ex;
        } catch (NoSuchAlgorithmException ex) {
            logger.error(ex);
            throw ex;
        } catch (UtilNagoJudgeException ex) {
            logger.error(ex);
            throw ex;
        } catch (Exception ex) {
            logger.error(ex);
            throw ex;
        } finally {
            logger.debug("FINALIZA METODO - createProblem()");
        }
    }

}
