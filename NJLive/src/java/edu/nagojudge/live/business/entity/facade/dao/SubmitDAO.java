/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.business.entity.facade.dao;

import edu.nagojudge.live.business.entity.Challenge;
import edu.nagojudge.live.business.entity.ChallengeSubmit;
import edu.nagojudge.live.business.entity.LanguageProgramming;
import edu.nagojudge.live.business.entity.Problem;
import edu.nagojudge.live.business.entity.Submit;
import edu.nagojudge.live.business.entity.SubmitStatus;
import edu.nagojudge.live.business.entity.Team;
import edu.nagojudge.live.web.exceptions.NagoJudgeLiveException;
import edu.nagojudge.live.web.utils.FacesUtil;
import edu.nagojudge.live.web.utils.clients.ClientService;
//import edu.nagojudge.live.web.utils.constants.IKeysChallenge;
import edu.nagojudge.live.web.utils.constants.IResourcesPath;
import edu.nagojudge.msg.pojo.MetadataMessage;
import edu.nagojudge.msg.pojo.SubmitMessage;
import edu.nagojudge.msg.pojo.constants.TypeFilesEnum;
import edu.nagojudge.msg.pojo.constants.TypeStateJudgeEnum;
import edu.nagojudge.tools.utils.FileUtil;
import edu.nagojudge.tools.utils.FormatUtil;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.log4j.Logger;

/**
 *
 * @author andres.garcia
 */
@Stateless
public class SubmitDAO extends AbstractDAO<Submit> {

    @EJB
    private ChallengeSubmitDAO challengeSubmitDAO;

    private final String TOKEN = "asd";

    private final Logger logger = Logger.getLogger(SubmitDAO.class);

    @PersistenceContext(unitName = "NJLivePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SubmitDAO() {
        super(Submit.class);
    }

    public void sendSubmit(final Long idChallenge, final Long idTeam, Long idProblem,
            Long idLanguage, byte[] contentCodeSource) throws NagoJudgeLiveException {
        try {
            logger.debug("INICIA METODO - createSubmitSolve()");

            Submit submit = new Submit();
            submit.setIdProblem(em.find(Problem.class, idProblem));
            submit.setIdLanguage(em.find(LanguageProgramming.class, idLanguage));
            submit.setIdStatus(em.createQuery("SELECT a FROM SubmitStatus a WHERE a.keyStatus = :id_status ", SubmitStatus.class)
                    .setParameter("id_status", TypeStateJudgeEnum.IP.name())
                    .getSingleResult());
            submit.setDateSubmit(Calendar.getInstance().getTime());
            submit.setDateJudge(Calendar.getInstance().getTime());

            this.create(submit);
            logger.debug("getMsgJudge [" + submit.getMsgJudge() + "]");
            logger.debug("getDateJudge [" + submit.getDateJudge() + "]");
            logger.debug("getDateSubmit [" + submit.getDateSubmit() + "]");
            logger.debug("getIdLanguage [" + submit.getIdLanguage() + "]");
            logger.debug("getIdProblem [" + submit.getIdProblem() + "]");
            logger.debug("getIdStatus [" + submit.getIdStatus() + "]");

            ChallengeSubmit challengeSubmit = new ChallengeSubmit();
            challengeSubmit.setIdSubmit(submit);
            challengeSubmit.setIdChallenge(em.find(Challenge.class, idChallenge));
            challengeSubmit.setIdTeam(em.find(Team.class, idTeam));
            challengeSubmitDAO.create(challengeSubmit);

            String pathFile = IResourcesPath.PATH_SAVE_CODE_SOURCE_LOCAL_TM + java.io.File.separatorChar
                    + FormatUtil.getInstance().buildZerosToLeft(idChallenge, 7) + java.io.File.separatorChar
                    + FormatUtil.getInstance().buildZerosToLeft(idTeam, 7) + java.io.File.separatorChar
                    + FormatUtil.getInstance().buildZerosToLeft(idProblem, 7);
            String nameFile = FormatUtil.getInstance().buildZerosToLeft(submit.getIdSubmit(), 7) + "." + submit.getIdLanguage().getExtension();
            FileUtil.getInstance().createFile(contentCodeSource, pathFile, nameFile);

            final Long idSubmit = submit.getIdSubmit();
            final String path = FacesUtil.getFacesUtil().getParameterWEBINF("init-config", "judge.path.submit.team.send");
            final String host = FacesUtil.getFacesUtil().getParameterWEBINF("init-config", "judge.nagojudge.url");
            final Map<String, Object> metadata = FacesUtil.getFacesUtil().getMetadataRest(String.valueOf(idTeam));
            final Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Object objects[] = {String.valueOf(idChallenge), String.valueOf(idTeam)};
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("idSubmit", idSubmit);
                    params.putAll(metadata);
                    SubmitMessage submitMessage = (SubmitMessage) ClientService.getInstance().callRestfulGet(host, path, objects, params, SubmitMessage.class);
                    logger.debug("outcome [" + submitMessage + "]");
                }
            });
            thread.start();
        } catch (IOException ex) {
            logger.error(ex);
            throw new NagoJudgeLiveException(ex);
        } catch (Exception ex) {
            logger.error(ex);
            throw new NagoJudgeLiveException(ex);
        } finally {
            logger.debug("FINALIZA METODO - createSubmitSolve()");
        }
    }

}
