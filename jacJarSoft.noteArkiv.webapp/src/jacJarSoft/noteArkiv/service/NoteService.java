package jacJarSoft.noteArkiv.service;

import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import jacJarSoft.noteArkiv.api.SheetParam;
import jacJarSoft.noteArkiv.api.SheetSearchParam;
import jacJarSoft.noteArkiv.model.Tag;

@Path("/noteservice")
@WebService(name="noteService", targetNamespace="http://jacJarSoft/noteArkiv/noteService")
public interface NoteService {

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/note/{noteId}")
	public Response getNote(@PathParam("noteId") long noteId);

	@PUT
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/note")
	public Response updateNote(SheetParam param);

	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/note")
	public Response addNote(SheetParam param);

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/note/{sheetId}/file")
	public Response getSheetFiles(@PathParam("sheetId") long sheetId);

	@GET
	@Produces({MediaType.APPLICATION_OCTET_STREAM})
	@Path("/notefile/{fileId}/download")
	public Response downLoadFile(@PathParam("fileId") long fileId);
	@GET
	@Produces({MediaType.APPLICATION_OCTET_STREAM})
	@Path("/notefile/{fileId}")
	public Response getFileData(@PathParam("fileId") long fileId);

	@POST
	@Consumes({MediaType.MULTIPART_FORM_DATA})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/note/{sheetId}/file")
	public Response addSheetFile(@PathParam("sheetId") long sheetId,
			@Multipart("name") String name,
			@Multipart("description") String description,
			@Multipart("size") long size,
			@Multipart("file") Attachment file);

	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/note/search")
	public Response searchSheets(SheetSearchParam param);

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/voice/")
	public Response getVoices();

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/tags/")
	public Response getTags();

	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/tags/")
	public Response addTag(Tag tag);

	@DELETE
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/tags/{tagId}")
	public Response deleteTag(@PathParam("tagId") int tagId);

	
}
