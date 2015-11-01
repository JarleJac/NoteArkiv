package jacJarSoft.noteArkiv.service;

import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/noteservice")
@WebService(name="noteService", targetNamespace="http://jacJarSoft/noteArkiv/noteService")
public interface NoteService {

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/getnote/{noteId}")
	public Response getNote(@PathParam("noteId") long noteId);
	
}
