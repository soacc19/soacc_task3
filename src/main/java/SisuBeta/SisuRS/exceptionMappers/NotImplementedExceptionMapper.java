package SisuBeta.SisuRS.exceptionMappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import SisuBeta.SisuRS.errors.ErrorMessage;
import SisuBeta.SisuRS.exceptions.NotImplementedException;

@Provider       // the annotation preregisters our Mapper for JAX-RS to be used
public class NotImplementedExceptionMapper implements ExceptionMapper<NotImplementedException> {

    @Override
    public Response toResponse(NotImplementedException exception) {
        ErrorMessage errorMessage = new ErrorMessage(exception.getMessage(),
                                                      Status.NOT_IMPLEMENTED.getStatusCode(),
                                                      "http://myDocs.org");
        return Response.status(Status.NOT_IMPLEMENTED).entity(errorMessage).build();
    }

}