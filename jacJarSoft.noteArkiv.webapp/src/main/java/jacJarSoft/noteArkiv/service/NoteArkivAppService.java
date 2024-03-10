package jacJarSoft.noteArkiv.service;

import jacJarSoft.noteArkiv.webapi.LogonInfo;
import jacJarSoft.noteArkiv.webapi.SetPwInfo;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/appservice")
public interface NoteArkivAppService {
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/getsystemInfo")
	public Response getSystemInfo();

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/gethelpfilesinfo")
	public Response getHelpFilesInfo();

	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/logon")
	public Response logon(LogonInfo logonInfo);

	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/forgotpw")
	public Response forgotPw(String userOrEmail);

	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/tokenuser")
	public Response getUserFromToken(String token);

	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/setpw")
	public Response setPw(SetPwInfo setPwInfo);
}
