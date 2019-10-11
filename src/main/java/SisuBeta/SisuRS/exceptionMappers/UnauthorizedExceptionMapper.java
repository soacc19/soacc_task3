package SisuBeta.SisuRS.exceptionMappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import SisuBeta.SisuRS.errors.ErrorMessage;
import SisuBeta.SisuRS.exceptions.UnauthorizedException;

@Provider       // the annotation preregisters our Mapper for JAX-RS to be used
public class UnauthorizedExceptionMapper implements ExceptionMapper<UnauthorizedException> {
    @Override
    public Response toResponse(UnauthorizedException exception) {
        ErrorMessage errorMessage = new ErrorMessage(exception.getMessage(),
                                                      Status.UNAUTHORIZED.getStatusCode(),
                                                      "http://myDocs.org");
        return Response.status(Status.UNAUTHORIZED).entity(errorMessage).build();
    }


}
