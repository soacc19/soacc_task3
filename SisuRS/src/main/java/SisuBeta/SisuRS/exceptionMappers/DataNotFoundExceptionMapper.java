package SisuBeta.SisuRS.exceptionMappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import SisuBeta.SisuRS.errors.ErrorMessage;
import SisuBeta.SisuRS.exceptions.*;

@Provider       // the annotation preregisters our Mapper for JAX-RS to be used
public class DataNotFoundExceptionMapper implements ExceptionMapper<DataNotFoundException> {

    @Override
    public Response toResponse(DataNotFoundException exception) {
        ErrorMessage errorMessage = new ErrorMessage(exception.getMessage(),
                                                      Status.NOT_FOUND.getStatusCode(),
                                                      "http://myDocs.org");
        return Response.status(Status.NOT_FOUND).entity(errorMessage).build();
    }

}
