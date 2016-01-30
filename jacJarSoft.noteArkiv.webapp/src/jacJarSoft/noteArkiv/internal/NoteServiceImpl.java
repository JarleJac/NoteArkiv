package jacJarSoft.noteArkiv.internal;

import javax.persistence.EntityManager;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import jacJarSoft.noteArkiv.api.SheetParam;
import jacJarSoft.noteArkiv.api.SheetSearchParam;
import jacJarSoft.noteArkiv.dao.NoteDao;
import jacJarSoft.noteArkiv.dao.VoiceDao;
import jacJarSoft.noteArkiv.model.Note;
import jacJarSoft.noteArkiv.service.NoteService;
import jacJarSoft.util.StringUtils;

public class NoteServiceImpl extends BaseService implements NoteService {

	@Autowired
	private NoteDao noteDao;
	@Autowired
	private VoiceDao voiceDao;

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
	public Response getVoices() {
		return Response.ok(voiceDao.getVoices()).build();
	}

	@Override
	public Response addNote(SheetParam param) {
		SheetParam result = runWithTransaction(this::internalAddNote, param);
		return Response.ok(result).build();
	}
	private SheetParam internalAddNote(EntityManager em, SheetParam param)
	{
		if (param == null || param.getSheet() == null || StringUtils.isEmpty(param.getSheet().getTitle())) {
			throw new ValidationErrorException("Tittel kan ikke være blank.");
		}
		if (param.getSheet().getTitle().equals("FUCK"))
			throw new RuntimeException("Bad language!");
		
		Note sheetResult = noteDao.insertNote(param.getSheet());
		return noteDao.getSheetData(sheetResult);
	}

	@Override
	public Response searchSheets(SheetSearchParam param) {
		return Response.ok(noteDao.sheetSearch(param)).build();
	}

	@Override
	public Response updateNote(SheetParam param) {
		throw new ValidationErrorException("updateNote er ikke implementert!");
//		return null;
	}

}
