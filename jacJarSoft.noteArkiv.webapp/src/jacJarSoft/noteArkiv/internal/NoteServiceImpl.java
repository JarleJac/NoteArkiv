package jacJarSoft.noteArkiv.internal;

import javax.persistence.EntityManager;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import jacJarSoft.noteArkiv.api.SheetParam;
import jacJarSoft.noteArkiv.dao.NoteDao;
import jacJarSoft.noteArkiv.dao.VoiceDao;
import jacJarSoft.noteArkiv.model.Note;
import jacJarSoft.noteArkiv.service.NoteService;
import jacJarSoft.util.DbUtil;
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
		return Response.ok(noteDao.getNote(noteId)).build();
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
		if (StringUtils.isEmpty(param.getSheet().getTitle())) {
			throw new ValidationErrorException("Tittel kan ikke v�re blank.");
		}
		
		Note sheetResult = noteDao.insertNote(em, param.getSheet());
		param.setSheet(sheetResult);
		return param;
	}

}
