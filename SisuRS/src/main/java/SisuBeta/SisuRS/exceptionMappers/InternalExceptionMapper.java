package SisuBeta.SisuRS.exceptionMappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import SisuBeta.SisuRS.errors.ErrorMessageInternal;
import SisuBeta.SisuRS.exceptions.InternalException;

@Provider       // the annotation preregisters our Mapper for JAX-RS to be used
public class InternalExceptionMapper implements ExceptionMapper<InternalException>{
    @Override
    public Response toResponse(InternalException exception) {
        ErrorMessageInternal errorMessage = new ErrorMessageInternal(exception.getMessage(),
                                                     Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                                                     "http://myDocs.org",
                                                     exception.getAdditionalMessage());
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorMessage).build();
    }
}
