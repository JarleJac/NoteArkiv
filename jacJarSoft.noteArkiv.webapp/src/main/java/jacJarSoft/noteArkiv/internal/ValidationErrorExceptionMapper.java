package jacJarSoft.noteArkiv.internal;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ValidationErrorExceptionMapper implements ExceptionMapper<ValidationErrorException> {
 
	public Response toResponse(ValidationErrorException ex) {
		ExceptionMapperResponseBuilder respBuilder = new ExceptionMapperResponseBuilder("Validation", ex.getMessage());
		return respBuilder.build();
	}
 
}