package jacJarSoft.noteArkiv.internal;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;

public class ExceptionMapperResponseBuilder {
	private String type;
	private String message;

	ExceptionMapperResponseBuilder(String type, String message) {
		this.type = type;
		this.message = message;
	}

	public Response build() {
		return Response.status(Status.BAD_REQUEST)
		.entity(buildReponseEntity())
		.build();
	}
	private String buildReponseEntity() {
		JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
		buildJsonResponse(jsonObjectBuilder);
		return jsonObjectBuilder.build().toString();
	}

	protected void buildJsonResponse(JsonObjectBuilder jsonObjectBuilder) {
		jsonObjectBuilder
				.add("msgType", type)
				.add("msg", message);
	}
	
}
