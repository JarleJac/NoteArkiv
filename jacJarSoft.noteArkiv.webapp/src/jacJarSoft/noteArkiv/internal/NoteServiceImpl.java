package jacJarSoft.noteArkiv.internal;

import java.io.IOException;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.MimetypesFileTypeMap;
import javax.persistence.EntityManager;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.springframework.beans.factory.annotation.Autowired;

import jacJarSoft.noteArkiv.api.SheetParam;
import jacJarSoft.noteArkiv.api.SheetSearchParam;
import jacJarSoft.noteArkiv.base.NoteArkivAppInfo;
import jacJarSoft.noteArkiv.dao.NoteDao;
import jacJarSoft.noteArkiv.dao.SheetFileDao;
import jacJarSoft.noteArkiv.dao.TagsDao;
import jacJarSoft.noteArkiv.dao.VoiceDao;
import jacJarSoft.noteArkiv.model.Note;
import jacJarSoft.noteArkiv.model.NoteFile;
import jacJarSoft.noteArkiv.model.NoteFileData;
import jacJarSoft.noteArkiv.model.Tag;
import jacJarSoft.noteArkiv.service.NoteService;
import jacJarSoft.util.StringUtils;

public class NoteServiceImpl extends BaseService implements NoteService {

	@Autowired
	private NoteDao noteDao;
	@Autowired
	private SheetFileDao sheetFileDao;
	@Autowired
	private VoiceDao voiceDao;
	@Autowired
	private TagsDao tagsDao;

	@Override
	public Response getNote(long noteId) {
		if (noteId <= 0) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		Note note = noteDao.getNote(noteId);
		if (note == null)
			throw new ValidationErrorException("Finner ikke note " + noteId);
	
		SheetParam result = noteDao.getSheetData(note);
		return Response.ok(result).build();
	}
	@Override
	public Response deleteNote(long noteId) {
		Note note = noteDao.getNote(noteId);
		if (note == null)
			throw new ValidationErrorException("Finner ikke note " + noteId);
		return runWithTransaction((ec, p)-> {
			noteDao.deleteNote(note);
			return Response.ok().build();
		}, null);
	}
	@Override
	public Response getSheetFiles(long sheetId) {
		List<NoteFile> list = sheetFileDao.getSheetFiles(sheetId);
		return Response.ok(list).build();
	}

	@Override
	public Response getVoices() {
		return Response.ok(voiceDao.getVoices()).build();
	}

	@Override
	public Response addNote(SheetParam param) {
		SheetParam result = runWithTransaction(this::internalAddOrUpdateNote, param);
		return Response.ok(result).build();
	}
	private SheetParam internalAddOrUpdateNote(EntityManager em, SheetParam param)
	{
		if (param == null || param.getSheet() == null || StringUtils.isEmpty(param.getSheet().getTitle())) {
			throw new ValidationErrorException("Tittel kan ikke v�re blank.");
		}
		if (param.getSheet().getTitle().equals("FUCK"))
			throw new RuntimeException("Bad language!");
		Note sheetResult;
		if (param.getSheet().getNoteId() != 0) {
			sheetResult = noteDao.updateNote(param.getSheet());
		}
		else {
			sheetResult = noteDao.insertNote(param.getSheet());
		}
			
		return noteDao.getSheetData(sheetResult);
	}

	@Override
	public Response addSheetFile(long sheetId, String name, String description, long size, Attachment file) {
		
		if (size > NoteArkivAppInfo.MAX_FILE_SIZE)
			throw new ValidationErrorException("Filen " + name + " er for stor. 100MB er maks.");
		
		NoteFile sheetFile = new NoteFile(); 
		sheetFile.setNoteId(sheetId);
		sheetFile.setFileSize(size);
		sheetFile.setName(name);
		sheetFile.setDescription(description);
		//sheetFile.setName(file.getContentDisposition().getParameter("filename"));
		
		

		@SuppressWarnings("unused")
		Void dummy = runWithTransaction((em, param)-> {
			NoteFile insertedFile = sheetFileDao.insertNoteFile(sheetFile);
			
			NoteFileData fileData = new NoteFileData();
			fileData.setFileId(insertedFile.getFileId());
			DataHandler dataHandler = file.getDataHandler();
			try {
				fileData.setData(IOUtils.readBytesFromStream(dataHandler.getInputStream()));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			
			sheetFileDao.insertNoteFileData(fileData);
			return null;
		}, null);
		return Response.ok(sheetFile).build();
		
		
	}
	@Override
	public Response getFileData(long fileId) {
		return getFileData(fileId, false);
	}
	@Override
	public Response downLoadFile(long fileId) {
		return getFileData(fileId, true);
	}
	private Response getFileData(long fileId, boolean download) {
		NoteFile noteFile = sheetFileDao.getNoteFile(fileId);
		if (null == noteFile)
			throw new ValidationErrorException("Finner ikke fil med id " + fileId);
		NoteFileData fileData = sheetFileDao.getFileData(fileId);
		if (null == fileData)
			throw new ValidationErrorException("Finner ikke data for fil " + noteFile.getName());
//        context.Response.ContentType = "application/octet-stream";
//        context.Response.AddHeader("Content-Disposition", "attachment; filename=\"" + fileData.FileName+"\"");
	    String mediaType = getMimeType(noteFile);
	    String contentDisposition = getContentDisposition(noteFile, download);
	    return Response.ok(fileData.getData(), mediaType)
	            .header("Content-Disposition", contentDisposition)
	            .header("Content-Length", fileData.getData().length)
	            .build();
	
	}

	private static String getMimeType(NoteFile noteFile) {
		String mediaType = MediaType.APPLICATION_OCTET_STREAM;
//		MimetypesFileTypeMap map = new MimetypesFileTypeMap();
//		map.addMimeTypes("application/pdf					pdf");
//		String mediaTypeFromExtension = map.getContentType(noteFile.getName());
		String mediaTypeFromExtension = MimetypesFileTypeMap.getDefaultFileTypeMap()
				.getContentType(noteFile.getName());
		if (mediaTypeFromExtension != null) {
			mediaType = mediaTypeFromExtension;
		}
		return mediaType;
	}

	private static String getContentDisposition(NoteFile noteFile, boolean download) {
		String contentDisposition = "inline";
		if (download) {
			contentDisposition = "attachment";
		}
		return String.format("%s; filename=\"%s\"", contentDisposition, noteFile.getName());
	}
	
	@Override
	public Response deleteFile(long fileId) {
		Void result = runWithTransaction(this::internalDeleteFile, fileId);
		return Response.ok(result).build();
	}
	private Void internalDeleteFile(EntityManager em, long fileId) {
		NoteFile noteFile = sheetFileDao.getNoteFile(fileId);
		if (null == noteFile)
			throw new ValidationErrorException("Finner ikke fil med id " + fileId);
		sheetFileDao.deleteFile(noteFile);
		return null;	
	}
	@Override
	public Response updateFile(long fileId, NoteFile file) {
		if (fileId != file.getFileId())
			throw new RuntimeException("Mismatch between url pram and file id!");
		Response resp = runWithTransaction((ec, p)-> {
			return Response.ok(sheetFileDao.updateFile(file)).build();
		}, null);
		return resp;
	}
	@Override
	public Response searchSheets(SheetSearchParam param) {
		return Response.ok(noteDao.sheetSearch(param)).build();
	}

	@Override
	public Response updateNote(SheetParam param) {
		SheetParam result = runWithTransaction(this::internalAddOrUpdateNote, param);
		return Response.ok(result).build();
	}

	@Override
	public Response getTags() {
		return Response.ok(tagsDao.getTags()).build();
	}

	@Override
	public Response addTag(Tag tag) {
		Tag result = runWithTransaction(this::internalAddTag, tag);
		return Response.ok(result).build();
	}
	private Tag internalAddTag(EntityManager em, Tag tag) {
		if (tagsDao.doesTagWithNameExist(tag.getName()))
			throw new ValidationErrorException("Sjanger med det navnet finnes allerede");
		return tagsDao.insertTag(tag);
	}

	@Override
	public Response deleteTag(int tagId) {
		Void result = runWithTransaction(this::internalDeleteTag, tagId);
		return Response.ok(result).build();
	}


	private Void internalDeleteTag(EntityManager em, int tagId) {
		Tag tag = tagsDao.getTag(tagId);
		tagsDao.deleteTag(tag);
		return null;	
	}
}