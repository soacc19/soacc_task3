package SisuBeta.SisuRS.exceptionMappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import SisuBeta.SisuRS.errors.ErrorMessage;
import SisuBeta.SisuRS.errors.ErrorMessageBadRequest;
import SisuBeta.SisuRS.exceptions.BadInputException;

@Provider       // the annotation preregisters our Mapper for JAX-RS to be used
public class BadInputExceptionMapper implements ExceptionMapper<BadInputException> {

	@Override
	public Response toResponse(BadInputException exception) {
		ErrorMessageBadRequest errorMessageBadReq = new ErrorMessageBadRequest(exception.getMessage(),
				 Status.BAD_REQUEST.getStatusCode(),
			     "http://myDocs.org",
			     exception.getBadInputName(),
			     exception.getBadInputValue());
		return Response.status(Status.BAD_REQUEST).entity(errorMessageBadReq).build();
	}
}
