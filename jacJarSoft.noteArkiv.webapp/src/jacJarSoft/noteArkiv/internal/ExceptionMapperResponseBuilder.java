package jacJarSoft.noteArkiv.internal;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

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
