package jacJarSoft.noteArkiv.service;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import jacJarSoft.noteArkiv.webapi.LogonInfo;

@Path("/appservice")
public interface NoteArkivAppService {
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/getsystemInfo")
	public Response getSystemInfo();

	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/logon")
	public Response logon(LogonInfo logonInfo);

}
