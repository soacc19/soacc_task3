package SisuBeta.SisuRS.exceptionMappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import SisuBeta.SisuRS.errors.ErrorMessage;

@Provider       // the annotation preregisters our Mapper for JAX-RS to be used
public class DefaultExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        ErrorMessage errorMessage = new ErrorMessage(exception.getMessage(),
                                                     Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                                                     "http://myDocs.org");
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorMessage).build();
    }

}
