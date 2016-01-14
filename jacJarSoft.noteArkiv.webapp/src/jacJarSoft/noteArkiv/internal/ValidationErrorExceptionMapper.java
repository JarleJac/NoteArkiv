package jacJarSoft.noteArkiv.internal;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ValidationErrorExceptionMapper implements ExceptionMapper<ValidationErrorException> {
 
	public Response toResponse(ValidationErrorException ex) {
		return Response.status(Status.BAD_REQUEST)
				.entity("{\"msg\": \"" + ex.getMessage() +"\"}")
				.build();
	}
 
}