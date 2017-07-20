/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.business.contest.services;

import edu.nagojudge.business.dao.beans.AuthenticationDAOFacade;
import edu.nagojudge.business.dao.beans.JudgeDAOFacade;
import edu.nagojudge.business.servicios.restful.exceptions.BusinessException;
import edu.nagojudge.business.servicios.restful.interfaces.IProblemService;
import edu.nagojudge.msg.pojo.MetadataMessage;
import edu.nagojudge.msg.pojo.StringMessage;
import edu.nagojudge.tools.utils.FileUtil;
import java.io.IOException;
import javax.ejb.EJB;
import javax.naming.AuthenticationException;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
public class ProblemService implements IProblemService {

    @EJB
    private AuthenticationDAOFacade authenticationFacade;

    @EJB
    private JudgeDAOFacade judgeFacade;

    private final Logger logger = Logger.getLogger(ProblemService.class);

    @Override
    public StringMessage getExternalPathFromProblem(
            String idProblem,
            MetadataMessage metadata) throws AuthenticationException, BusinessException {
        StringMessage outcome = new StringMessage();
        try {
            logger.debug("INICIA SERVICIO - getExternalPathFromProblem()");
            authenticationFacade.authorization(metadata.getToken());
            String fullPathProblemFile = judgeFacade.getFullPathProblemFile(new Long(idProblem));
            logger.debug("fullPathProblemFile [" + fullPathProblemFile + "]");

            String tempFullPath = judgeFacade.getFullPathProblemFileToWrite(new Long(idProblem));
            logger.debug("tempFullPath [" + tempFullPath + "]");
            boolean existFile = FileUtil.getInstance().existFile(tempFullPath);
            if (!existFile) {
                logger.debug("FILE NOT EXIST FULL_PATH [" + tempFullPath + "]");
                String fullPathLocal = judgeFacade.getFullPathProblemFile(new Long(idProblem));
                try {
                    FileUtil.getInstance().copyFile(fullPathLocal, tempFullPath);
                } catch (IOException ex) {
                    logger.error(ex);
                }
            }
            outcome.setString(judgeFacade.getFullPathProblemFileFromWEB(new Long(idProblem)));
            logger.debug("outcome [" + outcome + "]");
        } finally {
            logger.debug("FINALIZA SERVICIO - getExternalPathFromProblem()");
        }
        return outcome;
    }

}
