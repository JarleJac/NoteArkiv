package jacJarSoft.noteArkiv.internal;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import jacJarSoft.noteArkiv.api.SheetParam;
import jacJarSoft.noteArkiv.dao.NoteDao;
import jacJarSoft.noteArkiv.dao.VoiceDao;
import jacJarSoft.noteArkiv.service.NoteService;

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
		// TODO Auto-generated method stub
		return Response.ok(param).build();
	}

}
