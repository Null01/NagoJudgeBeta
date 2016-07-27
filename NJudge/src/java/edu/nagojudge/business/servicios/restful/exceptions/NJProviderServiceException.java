/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.business.servicios.restful.exceptions;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.naming.AuthenticationException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
@Provider
public class NJProviderServiceException implements ExceptionMapper {

    private final Logger logger = Logger.getLogger(NJProviderServiceException.class);

    @Context
    private ServletContext context;
    @Context
    private HttpServletRequest request;
    @Context
    private HttpServletResponse response;

    private final String pageError = "/error.html";

    @Override
    public Response toResponse(Throwable throwable) {
        Response.Status status = Response.Status.BAD_REQUEST;
        ServiceException serviceException = new ServiceException();
        serviceException.setMessage(throwable.getMessage());
        StringWriter errorStackTrace = new StringWriter();
        throwable.printStackTrace(new PrintWriter(errorStackTrace));
        serviceException.setStack(errorStackTrace.toString());

        if (throwable instanceof WebApplicationException) {
            WebApplicationException applicationException = (WebApplicationException) throwable;
            status = Response.Status.fromStatusCode(applicationException.getResponse().getStatus());
            try {
                context.getRequestDispatcher(pageError).forward(request, response);
            } catch (ServletException ex) {
                logger.error(ex);
            } catch (IOException ex) {
                logger.error(ex);
            }
        }

        if (throwable instanceof BusinessException || throwable instanceof AuthenticationException) {
            serviceException.setStack(null);
        }

        serviceException.setCode(String.valueOf(status.getStatusCode()) + " - " + status.getReasonPhrase());
        return Response.status(status)
                .entity(serviceException)
                .type(MediaType.APPLICATION_JSON + ";charset=UTF-8").build();
    }

}
