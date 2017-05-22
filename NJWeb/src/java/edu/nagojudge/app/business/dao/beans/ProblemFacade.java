/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.app.business.dao.beans;

import edu.nagojudge.app.business.dao.entities.AccountSubmit;
import edu.nagojudge.app.business.dao.entities.Attachments;
import edu.nagojudge.app.business.dao.entities.Category;
import edu.nagojudge.app.business.dao.entities.ComplexityAlgorithm;
import edu.nagojudge.app.business.dao.entities.DifficultyLevel;
import edu.nagojudge.app.business.dao.entities.Problem;
import edu.nagojudge.app.business.dao.entities.ProblemCategory;
import edu.nagojudge.app.exceptions.NagoJudgeException;
import edu.nagojudge.app.exceptions.UtilNagoJudgeException;
import edu.nagojudge.app.utils.constants.IResourcesPaths;
import edu.nagojudge.msg.pojo.CategoryMessage;
import edu.nagojudge.msg.pojo.ProblemMessage;
import edu.nagojudge.msg.pojo.collections.ListMessage;
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

    public Map<String, Long> findStatisticsStatus(long idProblem) {
        Map<String, Long> mapStatisticsStatus = new HashMap<String, Long>();
        List<Object[]> resultList = em.createQuery("SELECT a.idSubmit.idStatus.keyStatus, COUNT(0) FROM AccountSubmit a WHERE a.idSubmit.idProblem.idProblem = :id_problem GROUP BY a.idSubmit.idStatus.keyStatus")
                .setParameter("id_problem", idProblem)
                .getResultList();
        for (Object[] r : resultList) {
            mapStatisticsStatus.put(r[0].toString(), Long.parseLong(String.valueOf(r[1])));
        }
        return mapStatisticsStatus;
    }

    public Map<String, Long> findStatisticsStatusByAccount(long idProblem, long idAccount) {
        Map<String, Long> mapStatisticsStatus = new HashMap<String, Long>();
        List<Object[]> resultList = em.createQuery("SELECT a.idSubmit.idStatus.keyStatus, COUNT(0) FROM AccountSubmit a WHERE a.idSubmit.idProblem.idProblem = :id_problem AND a.idAccount.idAccount = :id_account GROUP BY a.idSubmit.idStatus.keyStatus")
                .setParameter("id_problem", idProblem)
                .setParameter("id_account", idAccount)
                .getResultList();
        for (Object[] r : resultList) {
            mapStatisticsStatus.put(r[0].toString(), Long.parseLong(String.valueOf(r[1])));
        }
        return mapStatisticsStatus;
    }

    public List<ProblemMessage> findStatisticsFromAllProblem() {
        Map<Long, Map<String, Long>> map = new HashMap<Long, Map<String, Long>>();
        Query query = em.createQuery("SELECT p.idSubmit.idProblem.idProblem, p.idSubmit.idStatus.keyStatus, COUNT(0) FROM AccountSubmit p GROUP BY p.idSubmit.idProblem.idProblem, p.idSubmit.idStatus.keyStatus ORDER BY p.idSubmit.idProblem.idProblem DESC", AccountSubmit.class);
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

        List<ProblemMessage> problemMessages = new ArrayList<ProblemMessage>();
        List<Problem> problems = findAll();
        for (Problem p : problems) {

            ListMessage<CategoryMessage> listMessage = new ListMessage<CategoryMessage>();
            List<ProblemCategory> problemCategoryList = p.getProblemCategoryList();
            for (ProblemCategory category : problemCategoryList) {
                CategoryMessage categoryMessage = new CategoryMessage();
                categoryMessage.setIdCategory(category.getIdCategory().getIdCategory());
                categoryMessage.setNameCategory(category.getIdCategory().getNameCategory());
                listMessage.add(categoryMessage);
            }

            ProblemMessage problemMessage = new ProblemMessage(p.getIdProblem(), p.getNameProblem(), p.getAuthor(),
                    listMessage, p.getIdDifficulty().getNameDifficulty(), p.getDescription(), p.getIdComplexityAlgorithm().getNameComplexityAlgorithm(),
                    p.getTimeLimit(), p.getMemoLimit(), 0, 0, 0, 0, 0, 0, 0, 0);

            Map<String, Long> mapValues = map.get(problemMessage.getIdProblem());
            if (mapValues != null) {
                int sumTotal = 0;
                for (Map.Entry<String, Long> row : mapValues.entrySet()) {
                    sumTotal += row.getValue();
                    if (row.getKey().compareTo(TypeStateJudgeEnum.AC.name()) == 0) {
                        problemMessage.setStatusAC(row.getValue().intValue());
                    }
                    if (row.getKey().compareTo(TypeStateJudgeEnum.CE.name()) == 0) {
                        problemMessage.setStatusCE(row.getValue().intValue());
                    }
                    if (row.getKey().compareTo(TypeStateJudgeEnum.CS.name()) == 0) {
                        problemMessage.setStatusCS(row.getValue().intValue());
                    }
                    if (row.getKey().compareTo(TypeStateJudgeEnum.IP.name()) == 0) {
                        problemMessage.setStatusIP(row.getValue().intValue());
                    }
                    if (row.getKey().compareTo(TypeStateJudgeEnum.RE.name()) == 0) {
                        problemMessage.setStatusRE(row.getValue().intValue());
                    }
                    if (row.getKey().compareTo(TypeStateJudgeEnum.TL.name()) == 0) {
                        problemMessage.setStatusTL(row.getValue().intValue());
                    }
                    if (row.getKey().compareTo(TypeStateJudgeEnum.WR.name()) == 0) {
                        problemMessage.setStatusWR(row.getValue().intValue());
                    }
                    problemMessage.setTotalStatus(sumTotal);
                }
            }
            problemMessages.add(problemMessage);
        }
        return problemMessages;
    }

    public List<ProblemMessage> findProblemTry(long idAccount) {
        List<ProblemMessage> problemMessages = new ArrayList<ProblemMessage>();
        List<Problem> resultList = em.createQuery("SELECT b FROM Problem b WHERE b.idProblem IN (SELECT a.idSubmit.idProblem.idProblem FROM AccountSubmit a WHERE a.idAccount.idAccount = :id_account GROUP BY a.idSubmit.idProblem.idProblem)", Problem.class)
                .setParameter("id_account", idAccount)
                .getResultList();
        if (resultList != null) {
            for (Problem problem : resultList) {
                problemMessages.add(parseEntityProblemToMessage(problem));
            }
        }
        return problemMessages;

    }

    public List<Problem> findProblemWithStatusFrom(long idAccount, TypeStateJudgeEnum status) {
        List<Problem> resultList = em.createQuery("SELECT a.idSubmit.idProblem FROM AccountSubmit a WHERE a.idAccount.idAccount = :id_account AND a.idSubmit.idStatus.keyStatus = :key_status", Problem.class)
                .setParameter("id_account", idAccount)
                .setParameter("key_status", status.name())
                .getResultList();
        return resultList;

    }

    public String createProblem(ProblemMessage problemView, List<Category> categoryProblemView,
            DifficultyLevel difficultyLevelView, ComplexityAlgorithm complexityAlgorithmView,
            byte[] info, byte[] input, byte[] output) throws IOException, NoSuchAlgorithmException, UtilNagoJudgeException, Exception {
        try {
            logger.debug("INICIA METODO - createProblem()");

            Problem problem = new Problem();
            problem.setDateCreated(Calendar.getInstance().getTime());
            problem.setIdComplexityAlgorithm(complexityAlgorithmView);
            problem.setIdDifficulty(difficultyLevelView);
            problem.setAuthor(problemView.getAuthor());
            problem.setNameProblem(problemView.getNameProblem());
            problem.setTimeLimit(problemView.getTimeLimit());
            problem.setMemoLimit(problemView.getMemoLimit());
            problem.setDescription(problemView.getDescription());
            List<ProblemCategory> problemCategorys = new ArrayList<ProblemCategory>();
            for (Category category : categoryProblemView) {
                ProblemCategory problemCategory = new ProblemCategory();
                problemCategory.setIdCategory(category);
                problemCategory.setIdProblem(problem);
                problemCategorys.add(problemCategory);
            }
            problem.setProblemCategoryList(problemCategorys);

            logger.debug(" getProblemCategoryList [" + problem.getProblemCategoryList() + "] @ECHO");
            logger.debug(" getIdComplexityAlgorithm [" + problem.getIdComplexityAlgorithm() + "] @ECHO");
            logger.debug(" getIdDifficulty [" + problem.getIdDifficulty() + "] @ECHO");
            logger.debug(" getAuthor [" + problem.getAuthor() + "] @ECHO");
            logger.debug(" getNameProblem [" + problem.getNameProblem() + "] @ECHO");
            logger.debug(" getTimeLimit [" + problem.getTimeLimit() + "] @ECHO");
            logger.debug(" getMemoLimit [" + problem.getMemoLimit() + "] @ECHO");
            logger.debug(" getDescription [" + problem.getDescription() + "] @ECHO");

            create(problem);
            logger.debug("CREACION ENTIDAD PROBLEMA_ID=" + problem.getIdProblem() + " @ECHO");

            String pathProblem = IResourcesPaths.PATH_ROOT_SAVE_PROBLEMS_WEB;
            String nameFileProblem = FormatUtil.getInstance().buildZerosToLeft(problem.getIdProblem(), 7) + TypeFilesEnum.PDF.getExtension();
            logger.debug("PATH_PROBLEM=" + pathProblem);
            logger.debug("NAME_FILE_PROBLEM=" + nameFileProblem);
            FileUtil.getInstance().createFile(info, pathProblem, nameFileProblem);

            String pathProblemLocal = IResourcesPaths.PATH_SAVE_PROBLEMS_LOCAL;
            String nameFileProblemLocal = FormatUtil.getInstance().buildZerosToLeft(problem.getIdProblem(), 7) + TypeFilesEnum.PDF.getExtension();
            logger.debug("PATH_PROBLEM_LOCAL=" + pathProblemLocal);
            logger.debug("NAME_FILE_PROBLEM_LOCAL=" + nameFileProblemLocal);
            FileUtil.getInstance().createFile(info, pathProblemLocal, nameFileProblemLocal);

            String pathInput = IResourcesPaths.PATH_SAVE_INPUT_LOCAL;
            String nameFileInput = FormatUtil.getInstance().buildZerosToLeft(problem.getIdProblem(), 7);
            logger.debug("PATH_INPUT=" + pathInput);
            logger.debug("NAME_FILE_INPUT=" + nameFileInput);
            FileUtil.getInstance().createFile(input, pathInput, nameFileInput);

            String pathOutput = IResourcesPaths.PATH_SAVE_OUTPUT_LOCAL;
            String nameFileOutput = FormatUtil.getInstance().buildZerosToLeft(problem.getIdProblem(), 7);
            logger.debug("PATH_OUTPUT=" + pathOutput);
            logger.debug("NAME_FILE_OUTPUT=" + nameFileOutput);
            FileUtil.getInstance().createFile(output, pathOutput, nameFileOutput);

            logger.debug("INIT - CREACION ATTACHMENTS @ECHO");
            List<Attachments> attachmentses = new ArrayList<Attachments>();
            Attachments attachment = new Attachments();
            attachment.setChecksum(FileUtil.getInstance().generateChechSum(info, TypeSHAEnum.SHA256));
            attachment.setDateCreated(Calendar.getInstance().getTime());
            attachment.setIdProblem(problem);
            attachment.setTypeFileServer(TypeFilesEnum.TYPE_FILE_PROBLEM.getExtension());
            attachmentses.add(attachment);
            attachmentsFacadeDAO.create(attachment);
            logger.debug("CREACION ATTACHMENT_ID=" + attachment.getIdAttachment() + "  @ECHO");

            attachment = new Attachments();
            attachment.setChecksum(FileUtil.getInstance().generateChechSum(input, TypeSHAEnum.SHA256));
            attachment.setDateCreated(Calendar.getInstance().getTime());
            attachment.setIdProblem(problem);
            attachment.setTypeFileServer(TypeFilesEnum.TYPE_FILE_IN.getExtension());
            attachmentses.add(attachment);
            attachmentsFacadeDAO.create(attachment);
            logger.debug("CREACION ATTACHMENT_ID=" + attachment.getIdAttachment() + "  @ECHO");

            attachment = new Attachments();
            attachment.setChecksum(FileUtil.getInstance().generateChechSum(output, TypeSHAEnum.SHA256));
            attachment.setDateCreated(Calendar.getInstance().getTime());
            attachment.setIdProblem(problem);
            attachment.setTypeFileServer(TypeFilesEnum.TYPE_FILE_OUT.getExtension());
            attachmentses.add(attachment);
            attachmentsFacadeDAO.create(attachment);
            logger.debug("CREACION ATTACHMENT_ID=" + attachment.getIdAttachment() + "  @ECHO");

            return String.valueOf(problemView.getIdProblem());
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error(ex);
            throw ex;
        } finally {
            logger.debug("FINALIZA METODO - createProblem()");
        }
    }

    public ProblemMessage findProblemById(Long idProblem) {
        ProblemMessage message = null;
        Problem problem = em.find(Problem.class, idProblem);
        if (problem != null) {
            message = parseEntityProblemToMessage(problem);
        }
        return message;
    }

    private ProblemMessage parseEntityProblemToMessage(Problem problem) {
        ProblemMessage problemMessage = new ProblemMessage();
        problemMessage.setIdProblem(problem.getIdProblem());
        problemMessage.setNameProblem(problem.getNameProblem());
        problemMessage.setBestComplexity(problem.getIdComplexityAlgorithm().getNameComplexityAlgorithm());
        problemMessage.setNameProblem(problem.getIdDifficulty().getNameDifficulty());
        problemMessage.setMemoLimit(problem.getMemoLimit());
        problemMessage.setTimeLimit(problem.getTimeLimit());
        problemMessage.setDescription(problem.getDescription());
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

}
