package SisuBeta.SisuRS.exceptionMappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import SisuBeta.SisuRS.errors.ErrorMessageDbQuery;
import SisuBeta.SisuRS.exceptions.DatabaseQueryException;

@Provider       // the annotation preregisters our Mapper for JAX-RS to be used
public class DatabaseQueryExceptionMapper implements ExceptionMapper<DatabaseQueryException> {

    @Override
    public Response toResponse(DatabaseQueryException exception) {
        ErrorMessageDbQuery errorMessageDbQuery = new ErrorMessageDbQuery(exception.getMessage(),
                 Status.BAD_REQUEST.getStatusCode(),
                 "http://myDocs.org",
                 exception.getSqlErrorMessage(),
                 exception.getQuery());
        return Response.status(Status.BAD_REQUEST).entity(errorMessageDbQuery).build();
    }
}
