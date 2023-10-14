package jacJarSoft.noteArkiv.service;


import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import jacJarSoft.noteArkiv.api.SheetParam;
import jacJarSoft.noteArkiv.api.SheetSearchParam;
import jacJarSoft.noteArkiv.model.NoteFile;
import jacJarSoft.noteArkiv.model.SheetList;
import jacJarSoft.noteArkiv.model.Tag;
import jakarta.jws.WebService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HEAD;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/noteservice")
@WebService(name="noteService", targetNamespace="http://jacJarSoft/noteArkiv/noteService")
public interface NoteService {

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/note/{noteId}")
	public Response getNote(@PathParam("noteId") long noteId);

	@DELETE
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/note/{noteId}")
	public Response deleteNote(@PathParam("noteId") long noteId);

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
    
	@HEAD
	@Produces({MediaType.APPLICATION_OCTET_STREAM})
	@Path("/notefile/{fileId}/media")
	public Response returnMediaHeader(@HeaderParam("Range") String range, @PathParam("fileId") long fileId);
    
	@GET
	@Produces({MediaType.APPLICATION_OCTET_STREAM})
	@Path("/notefile/{fileId}/media")
	public Response returnMedia(@HeaderParam("Range") String range, @PathParam("fileId") long fileId);

	@GET
	@Produces({MediaType.APPLICATION_OCTET_STREAM})
	@Path("/notefile/{fileId}")
	public Response getFileData(@PathParam("fileId") long fileId);

	@DELETE
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/notefile/{fileId}")
	public Response deleteFile(@PathParam("fileId") long fileId);

	@PUT
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/notefile/{fileId}")
	public Response updateFile(@PathParam("fileId") long fileId, NoteFile file);

	@POST
	@Consumes({MediaType.MULTIPART_FORM_DATA})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/note/{sheetId}/file")
	public Response addSheetFile(@PathParam("sheetId") long sheetId,
			@Multipart("name") String name,
			@Multipart("description") String description,
			@Multipart("sizeStr") String sizeStr,
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

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/list/")
	public Response getLists();

	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/list/")
	public Response addList(SheetList list);

	@PUT
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/list/")
	public Response updateList(SheetList list);
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/list/{listId}")
	public Response getList(@PathParam("listId") long listId);
	
	@DELETE
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/list/{listId}")
	public Response deleteList(@PathParam("listId") long listId);

	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/list/{listId}")
	public Response connectListSheet(@PathParam("listId") long listId, long sheetId);

	@DELETE
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/list/{listId}/{noteId}")
	public Response disconnectListSheet(@PathParam("listId") long listId, @PathParam("noteId") long sheetId);
}
