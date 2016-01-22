package jacJarSoft.noteArkiv.internal;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ValidationErrorExceptionMapper implements ExceptionMapper<ValidationErrorException> {
 
	public Response toResponse(ValidationErrorException ex) {
		ExceptionMapperResponseBuilder respBuilder = new ExceptionMapperResponseBuilder("Validation", ex.getMessage());
		return respBuilder.build();
	}
 
}