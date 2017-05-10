/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.business.dao.beans;

import edu.nagojudge.business.dao.entity.AccountSubmit;
import edu.nagojudge.business.dao.entity.Attachments;
import edu.nagojudge.business.dao.entity.Submit;
import edu.nagojudge.business.servicios.restful.exceptions.BusinessException;
import edu.nagojudge.msg.pojo.constants.TypeStateEnum;
import edu.nagojudge.tools.utils.FileUtil;
import edu.nagojudge.tools.utils.FormatUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
@Stateless
public class AttachmentsDAOFacade extends AbstractFacade<Attachments> {

    private final Logger logger = Logger.getLogger(AttachmentsDAOFacade.class);

    @PersistenceContext(unitName = "NJBusinessPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AttachmentsDAOFacade() {
        super(Attachments.class);
    }

    public StringBuilder getCodeSource(String idSubmit) throws IOException, BusinessException {
        StringBuilder outcome = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            logger.debug("INICIA METODO - getCodeSource()");
            AccountSubmit accountSubmit = em.createQuery("SELECT s FROM AccountSubmit s WHERE s.idSubmit = :id_submit", AccountSubmit.class).setParameter("id_submit", idSubmit).getSingleResult();
            Submit submit = accountSubmit.getIdSubmit();
            logger.debug("FIND_SUBMIT_ID=" + idSubmit);
            if (submit == null) {
                throw new BusinessException("ID_SUBMIT [" + idSubmit + "] NO EXISTE.");
            }
            logger.debug("FIND_VISIBLE_WEB=" + accountSubmit.getVisibleWeb());
            if (accountSubmit.getVisibleWeb().compareTo(TypeStateEnum.PRIVATE.getType()) == 0) {
                return new StringBuilder("Actualmente este código fuente, no es visible para todo el publico.");
            }
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ID_EMAIL FROM USER WHERE ID_ACCOUNT = ").append(accountSubmit.getIdAccount().getIdAccount());
            logger.debug("QUERY=" + sql.toString());
            String email = (String) em.createNativeQuery(sql.toString()).getSingleResult();

            String pathFileCodeSource = getPathFileCodeSource(email, submit.getIdProblem().getIdProblem());
            String nameFileCodeSource = getNameFileCodeSource(submit.getIdSubmit(), submit.getIdLanguage().getExtension());
            logger.debug("pathFileCodeSource=" + pathFileCodeSource);
            logger.debug("nameFileCodeSource=" + nameFileCodeSource);

            String line;
            bufferedReader = FileUtil.getInstance().readFile(pathFileCodeSource, nameFileCodeSource);
            while ((line = bufferedReader.readLine()) != null) {
                outcome.append(line).append("\n");
            }

            return outcome.toString().length() == 0 ? null : outcome;
        } catch (IOException ex) {
            logger.error(ex);
            throw ex;
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            logger.debug("FINALIZA METODO - getCodeSource()");
        }
    }

    private String getPathFileCodeSource(String email, long idProblem) {
        String root = System.getProperty("user.dir") + File.separatorChar + "workspace" + File.separatorChar + "users";
        root += java.io.File.separatorChar + String.valueOf(email);
        root += java.io.File.separatorChar + FormatUtil.getInstance().buildZerosToLeft(idProblem, 7);
        return root;
    }

    private String getNameFileCodeSource(long idSubmit, String extension) {
        return FormatUtil.getInstance().buildZerosToLeft(idSubmit, 7) + "." + extension;
    }

}
