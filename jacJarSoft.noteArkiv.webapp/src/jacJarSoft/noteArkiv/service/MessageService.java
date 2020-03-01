package jacJarSoft.noteArkiv.service;

import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import jacJarSoft.noteArkiv.model.Message;

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
	public Response getMessage(long messageId);

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/message")
	public Response getMessages();

	
}
