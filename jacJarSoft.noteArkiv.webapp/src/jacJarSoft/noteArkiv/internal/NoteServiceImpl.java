package jacJarSoft.noteArkiv.internal;

import java.util.List;

import javax.persistence.EntityManager;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.springframework.beans.factory.annotation.Autowired;

import jacJarSoft.noteArkiv.api.SheetParam;
import jacJarSoft.noteArkiv.api.SheetSearchParam;
import jacJarSoft.noteArkiv.dao.NoteDao;
import jacJarSoft.noteArkiv.dao.SheetFileDao;
import jacJarSoft.noteArkiv.dao.TagsDao;
import jacJarSoft.noteArkiv.dao.VoiceDao;
import jacJarSoft.noteArkiv.model.Note;
import jacJarSoft.noteArkiv.model.NoteFile;
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
			
		return noteDao.getSheetData(sheetResult);
	}

	@Override
	public Response addSheetFile(long sheetId, String description, Attachment file) {
		// TODO Auto-generated method stub
		
		NoteFile sheetFile = new NoteFile(); 
		sheetFile.setNoteId(sheetId);
		sheetFile.setDescription(description);
		sheetFile.setName(file.getContentDisposition().getParameter("filename"));
		return Response.ok(sheetFile).build();
		
		
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