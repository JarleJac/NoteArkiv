package jacJarSoft.noteArkiv.service;

import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import jacJarSoft.noteArkiv.api.SheetParam;
import jacJarSoft.noteArkiv.api.SheetSearchParam;

@Path("/noteservice")
@WebService(name="noteService", targetNamespace="http://jacJarSoft/noteArkiv/noteService")
public interface NoteService {

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/note/{noteId}")
	public Response getNote(@PathParam("noteId") long noteId);

	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/note")
	public Response addNote(SheetParam param);

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/voice/")
	public Response getVoices();

	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/note/search")
	public Response searchSheets(SheetSearchParam param);
	
}
