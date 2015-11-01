
package jacJarSoft.noteArkiv.internal;

import javax.ws.rs.core.Response;

import jacJarSoft.noteArkiv.model.AppInfo;
import jacJarSoft.noteArkiv.service.NoteArkivAppService;

public class NoteArkivAppServiceImpl implements NoteArkivAppService {

	@Override
	public Response getSystemInfo() {
		AppInfo info = new AppInfo();
		return Response.ok(info).build();
	}

}
