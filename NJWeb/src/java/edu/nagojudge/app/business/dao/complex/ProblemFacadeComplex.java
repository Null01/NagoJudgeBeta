/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.app.business.dao.complex;

import edu.nagojudge.app.business.dao.beans.AttachmentsFacadeDAO;
import edu.nagojudge.app.business.dao.beans.ProblemFacadeDAO;
import edu.nagojudge.app.business.dao.entities.Attachments;
import edu.nagojudge.app.business.dao.entities.CategoryProblem;
import edu.nagojudge.app.business.dao.entities.DifficultyLevel;
import edu.nagojudge.app.business.dao.entities.Problem;
import edu.nagojudge.app.exceptions.UtilNagoJudgeException;
import edu.nagojudge.app.utils.ValidatorUtil;
import edu.nagojudge.app.utils.constants.IResourcesPaths;
import edu.nagojudge.msg.pojo.constants.TypeFilesEnum;
import edu.nagojudge.tools.security.constants.TypeSHAEnum;
import edu.nagojudge.tools.utils.FileUtil;
import edu.nagojudge.tools.utils.FormatUtil;
import edu.nagojudge.web.utils.dtos.FilePart;
import java.io.IOException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
@Stateless
public class ProblemFacadeComplex implements Serializable {

    @EJB
    private ProblemFacadeDAO problemFacade;

    @EJB
    private AttachmentsFacadeDAO attachmentsFacade;

    private final Logger logger = Logger.getLogger(ProblemFacadeComplex.class);

    public String createProblem(Problem problemView, CategoryProblem categoryProblemView, DifficultyLevel difficultyLevel, FilePart problem, FilePart input, FilePart output) throws IOException, NoSuchAlgorithmException, UtilNagoJudgeException {
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

            problemFacade.create(problemView);
            logger.debug("CREACION ENTIDAD PROBLEMA_ID=" + problemView.getIdProblem() + " @ECHO");

            String pathProblem = IResourcesPaths.PATH_ROOT_SAVE_PROBLEMS_WEB;
            String nameFileProblem = FormatUtil.getInstance().buildZerosToLeft(problemView.getIdProblem(), 7) + TypeFilesEnum.PDF.getExtension();
            logger.debug("PATH_PROBLEM=" + pathProblem);
            logger.debug("NAME_FILE_PROBLEM=" + nameFileProblem);
            FileUtil.getInstance().createFile(problem.getContent(), pathProblem, nameFileProblem);

            String pathProblemLocal = IResourcesPaths.PATH_SAVE_PROBLEMS_LOCAL;
            String nameFileProblemLocal = FormatUtil.getInstance().buildZerosToLeft(problemView.getIdProblem(), 7) + TypeFilesEnum.PDF.getExtension();
            logger.debug("PATH_PROBLEM_LOCAL=" + pathProblemLocal);
            logger.debug("NAME_FILE_PROBLEM_LOCAL=" + nameFileProblemLocal);
            FileUtil.getInstance().createFile(problem.getContent(), pathProblemLocal, nameFileProblemLocal);

            String pathInput = IResourcesPaths.PATH_SAVE_INPUT_LOCAL;
            String nameFileInput = FormatUtil.getInstance().buildZerosToLeft(problemView.getIdProblem(), 7);
            logger.debug("PATH_INPUT=" + pathInput);
            logger.debug("NAME_FILE_INPUT=" + nameFileInput);
            FileUtil.getInstance().createFile(input.getContent(), pathInput, nameFileInput);

            String pathOutput = IResourcesPaths.PATH_SAVE_OUTPUT_LOCAL;
            String nameFileOutput = FormatUtil.getInstance().buildZerosToLeft(problemView.getIdProblem(), 7);
            logger.debug("PATH_OUTPUT=" + pathOutput);
            logger.debug("NAME_FILE_OUTPUT=" + nameFileOutput);
            FileUtil.getInstance().createFile(output.getContent(), pathOutput, nameFileOutput);

            logger.debug("INIT - CREACION ATTACHMENTS @ECHO");
            List<Attachments> attachmentses = new ArrayList<Attachments>();
            Attachments attachment = new Attachments();
            attachment.setChecksum(FileUtil.getInstance().generateChechSum(problem.getContent(), TypeSHAEnum.SHA256));
            attachment.setDateLoad(Calendar.getInstance().getTime());
            attachment.setIdProblem(problemView);
            attachment.setTypeFileServer(TypeFilesEnum.TYPE_FILE_PROBLEM.getExtension());
            attachmentses.add(attachment);
            attachmentsFacade.create(attachment);
            logger.debug("CREACION ATTACHMENT_ID=" + attachment.getIdAttachment() + "  @ECHO");

            attachment = new Attachments();
            attachment.setChecksum(FileUtil.getInstance().generateChechSum(input.getContent(), TypeSHAEnum.SHA256));
            attachment.setDateLoad(Calendar.getInstance().getTime());
            attachment.setIdProblem(problemView);
            attachment.setTypeFileServer(TypeFilesEnum.TYPE_FILE_IN.getExtension());
            attachmentses.add(attachment);
            attachmentsFacade.create(attachment);
            logger.debug("CREACION ATTACHMENT_ID=" + attachment.getIdAttachment() + "  @ECHO");

            attachment = new Attachments();
            attachment.setChecksum(FileUtil.getInstance().generateChechSum(output.getContent(), TypeSHAEnum.SHA256));
            attachment.setDateLoad(Calendar.getInstance().getTime());
            attachment.setIdProblem(problemView);
            attachment.setTypeFileServer(TypeFilesEnum.TYPE_FILE_OUT.getExtension());
            attachmentses.add(attachment);
            attachmentsFacade.create(attachment);
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
        } finally {
            logger.debug("FINALIZA METODO - createProblem()");
        }
    }
}
