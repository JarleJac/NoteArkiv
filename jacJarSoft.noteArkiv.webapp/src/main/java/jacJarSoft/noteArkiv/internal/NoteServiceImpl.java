package jacJarSoft.noteArkiv.internal;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.springframework.beans.factory.annotation.Autowired;

import jacJarSoft.noteArkiv.api.SheetFile;
import jacJarSoft.noteArkiv.api.SheetParam;
import jacJarSoft.noteArkiv.api.SheetSearchList;
import jacJarSoft.noteArkiv.api.SheetSearchParam;
import jacJarSoft.noteArkiv.base.NoteArkivAppInfo;
import jacJarSoft.noteArkiv.dao.NoteDao;
import jacJarSoft.noteArkiv.dao.SheetFileDao;
import jacJarSoft.noteArkiv.dao.SheetListDao;
import jacJarSoft.noteArkiv.dao.TagsDao;
import jacJarSoft.noteArkiv.dao.VoiceDao;
import jacJarSoft.noteArkiv.model.Note;
import jacJarSoft.noteArkiv.model.NoteFile;
import jacJarSoft.noteArkiv.model.NoteFileData;
import jacJarSoft.noteArkiv.model.SheetList;
import jacJarSoft.noteArkiv.model.SheetListNote;
import jacJarSoft.noteArkiv.model.Tag;
import jacJarSoft.noteArkiv.service.NoteService;
import jacJarSoft.util.StringUtils;
import jakarta.activation.DataHandler;
import jakarta.activation.MimetypesFileTypeMap;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class NoteServiceImpl extends BaseService implements NoteService {

	@Autowired
	private NoteDao noteDao;
	@Autowired
	private SheetFileDao sheetFileDao;
	@Autowired
	private VoiceDao voiceDao;
	@Autowired
	private TagsDao tagsDao;
	@Autowired
	private SheetListDao sheetListDao;

	private static Logger logger = Logger.getLogger(NoteServiceImpl.class.getName());
	
	@Override
	public Response getNote(long noteId) {
		if (noteId <= 0) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		Note note = getVerifiedNote(noteId);

		SheetParam result = getSheetData(getSheetsInCurrentList(), note);
		return Response.ok(result).build();
	}
	@Override
	public Response deleteNote(long noteId) {
		Note note = getVerifiedNote(noteId);
		return runWithTransaction((ec, p)-> {
			List<NoteFile> sheetFiles = sheetFileDao.getSheetFiles(noteId);
			sheetFiles.forEach(sheetFile-> {
				sheetFileDao.deleteFile(sheetFile);
			});
			noteDao.deleteNote(note);
			return Response.ok().build();
		}, null);
	}
	private Note getVerifiedNote(long noteId) {
		Note note = noteDao.getNote(noteId);
		if (note == null)
			throw new ValidationErrorException("Finner ikke note " + noteId);
		return note;
	}
	private SheetList getVerifiedList(long listId) {
		SheetList list = sheetListDao.getList(listId);
		if (list == null)
			throw new ValidationErrorException("Finner ikke liste " + listId);
		return list;
	}
	@Override
	public Response getSheetFiles(long sheetId) {
		List<NoteFile> list = sheetFileDao.getSheetFiles(sheetId);
		List<SheetFile> retList = list
				.stream()
				.map((file)-> {
					SheetFile sheetFile = new SheetFile(file);
					sheetFile.setMimeType(getMimeType(file));
					return sheetFile;
				})
				.collect(Collectors.toList()); 
		return Response.ok(retList).build();
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
			throw new ValidationErrorException("Tittel kan ikke være blank.");
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
			
		return  getSheetData(getSheetsInCurrentList(), sheetResult);
	}

	@Override
	public Response addSheetFile(long sheetId, String name, String description, String sizeStr, Attachment file) {
		long size = Long.valueOf(sizeStr);
			
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
				sheetFileDao.insertSheetFileData(insertedFile,IOUtils.readBytesFromStream(dataHandler.getInputStream()));
				//fileData.setData(IOUtils.readBytesFromStream(dataHandler.getInputStream()));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			
			//sheetFileDao.insertNoteFileData(fileData);
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
	@Override
	public Response returnMediaHeader(String range, long fileId) {
		NoteFile noteFile = sheetFileDao.getNoteFile(fileId);
		return Response.ok().status(206).header(HttpHeaders.CONTENT_LENGTH, noteFile.getFileSize()).build();
	}

	@Override
	public Response returnMedia(String range, long fileId) {
		NoteFile noteFile = sheetFileDao.getNoteFile(fileId);
		File sheetFileDataAsFile = sheetFileDao.getSheetFileDataAsFile(noteFile);

		if (range == null)
			return streamMedia(noteFile, sheetFileDataAsFile);
		else
			return streamRangeBasedMedia(range, noteFile, sheetFileDataAsFile);
	}

	private Response streamRangeBasedMedia(String range, NoteFile noteFile, File sheetFileDataAsFile) {
		RangeBasedSheetFileStreamer streamer = new RangeBasedSheetFileStreamer(range, noteFile, sheetFileDataAsFile);
        Response.ResponseBuilder res = Response.ok(streamer)
        		.status(206)
        		.type(getMimeType(noteFile))
                .header("Accept-Ranges", "bytes")
                .header("Content-Range", streamer.getResponseRange())
                .header(HttpHeaders.CONTENT_LENGTH, streamer.getLenth())
	            .header(HttpHeaders.CONTENT_DISPOSITION, getContentDisposition(noteFile, false))
                .header(HttpHeaders.LAST_MODIFIED, new Date(sheetFileDataAsFile.lastModified()));
        return res.build();		
	}

	private Response streamMedia(NoteFile noteFile, File sheetFileDataAsFile) {
		SheetFileStreamer streamer = new SheetFileStreamer(noteFile, sheetFileDataAsFile);
        Response.ResponseBuilder res = Response.ok(streamer)
        		.type(getMimeType(noteFile))
                .header(HttpHeaders.CONTENT_LENGTH, streamer.getLenth())
	            .header(HttpHeaders.CONTENT_DISPOSITION, getContentDisposition(noteFile, false))
                .header(HttpHeaders.LAST_MODIFIED, new Date(sheetFileDataAsFile.lastModified()));
        return res.build();		
	}

	private Response getFileData(long fileId, boolean download) {
		NoteFile noteFile = sheetFileDao.getNoteFile(fileId);
		if (null == noteFile)
			throw new ValidationErrorException("Finner ikke fil med id " + fileId);
		byte[] bytes;
		try {
			bytes = sheetFileDao.getSheetFileData(noteFile);
		} catch (IOException e) {
			String msg = "Unable to read fileData for sheet: " + noteFile.getNoteId() + ", name: " +noteFile.getName();
			logger.log(Level.SEVERE, msg, e);
			throw new RuntimeException(msg, e);
		}
	    String mediaType = getMimeType(noteFile);
	    return Response.ok(bytes, mediaType)
	            .header("Content-Disposition", getContentDisposition(noteFile, download))
	            .header("Content-Length", bytes.length)
	            .build();
	
	}

	private static String getMimeType(NoteFile noteFile) {
		String mediaType = MediaType.APPLICATION_OCTET_STREAM;
//		MimetypesFileTypeMap map = new MimetypesFileTypeMap();
//		map.addMimeTypes("application/pdf					pdf");
//		String mediaTypeFromExtension = map.getContentType(noteFile.getName());
		String mediaTypeFromExtension = MimetypesFileTypeMap.getDefaultFileTypeMap()
				.getContentType(noteFile.getName().toLowerCase());
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
		List<Note> sheets = noteDao.sheetSearch(param);

		SheetSearchList result = new SheetSearchList();
		List<SheetParam> sheetList = result.getSheetList();
		
		Set<Long> currentSheets = getSheetsInCurrentList();
		
		for (Note sheet : sheets) {
			SheetParam sheetData = getSheetData(currentSheets, sheet);
			sheetList.add(sheetData);
		}
		
		return Response.ok(result).build();
	}
	private SheetParam getSheetData(Set<Long> currentSheets, Note sheet) {
		return noteDao.getSheetData(sheet, currentSheets.contains(Long.valueOf(sheet.getNoteId())));
	}
	private Set<Long> getSheetsInCurrentList() {
		Set<Long> currentSheets = sheetListDao.getLinkedSheets(1);
		return currentSheets;
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
	@Override
	public Response getLists() {
		return Response.ok(sheetListDao.getLists()).build();
	}
	@Override
	public Response addList(SheetList list) {
		SheetList result = runWithTransaction(this::internalAddOrUpdateNote, list);
		return Response.ok(result).build();
	}
	@Override
	public Response updateList(SheetList list) {
		SheetList result = runWithTransaction(this::internalAddOrUpdateNote, list);
		return Response.ok(result).build();
	}
	private SheetList internalAddOrUpdateNote(EntityManager em, SheetList list)
	{
		if (list == null || StringUtils.isEmpty(list.getName())) {
			throw new ValidationErrorException("Navn kan ikke være blank.");
		}
		if (list == null || list.getListDate() == null) {
			throw new ValidationErrorException("Dato må angis.");
		}
		SheetList result;
		if (list.getListId() != 0) {
			result = sheetListDao.updateList(list);
		}
		else {
			result = sheetListDao.insertList(list);
		}
			
		return  result;
	}
	@Override
	public Response deleteList(long listId) {
		return runWithTransaction((ec, p)-> {
			SheetList list = getVerifiedList(listId);
			if (list != null)
				sheetListDao.deleteList(list);
			return Response.ok().build();	
		}, null);
	}
	@Override
	public Response connectListSheet(long listId, long sheetId) {
		Note note = getVerifiedNote(sheetId);
		SheetList list = getVerifiedList(listId);
		return runWithTransaction((ec, p)-> {
			SheetListNote link = new SheetListNote(list.getListId(), note.getNoteId());
			sheetListDao.insertLink(link);
			return Response.ok().build();
		}, null);
	}
	@Override
	public Response disconnectListSheet(long listId, long sheetId) {
		Note note = getVerifiedNote(sheetId);
		SheetList list = getVerifiedList(listId);
		return runWithTransaction((ec, p)-> {
			SheetListNote link = sheetListDao.getLink(list.getListId(), note.getNoteId());
			if (link != null)
				sheetListDao.deleteLink(link);
			return Response.ok().build();
		}, null);
	}
	@Override
	public Response getList(long listId) {
		return Response.ok(sheetListDao.getList(listId)).build();
	}
}