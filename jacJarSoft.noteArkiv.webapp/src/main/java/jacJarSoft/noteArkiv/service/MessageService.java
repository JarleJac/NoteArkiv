package jacJarSoft.noteArkiv.service;

import jacJarSoft.noteArkiv.model.Message;
import jakarta.jws.WebService;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/messages")
@WebService(name="messageService", targetNamespace="http://jacJarSoft/noteArkiv/messageService")
public interface MessageService {

	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/message")
	public Response addMessage(Message message);

	@PUT
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/message")
	public Response updateMessage(Message message);

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/message/{messageId}")
	public Response getMessage(@PathParam("messageId")long messageId);
	
	@DELETE
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/message/{messageId}")
	public Response deleteMessage(@PathParam("messageId")long messageId);

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/message")
	public Response getMessages();

	
}
