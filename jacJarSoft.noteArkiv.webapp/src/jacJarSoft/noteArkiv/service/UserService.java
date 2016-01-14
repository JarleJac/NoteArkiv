package jacJarSoft.noteArkiv.service;

import javax.jws.WebService;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import jacJarSoft.noteArkiv.api.ChangePassword;

@Path("/userservice")
@WebService(name="userService", targetNamespace="http://jacJarSoft/noteArkiv/userService")
public interface UserService {

	@PUT
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/user/{userNo}/changepw")
	public Response changePassword(@PathParam("userNo")String userNo, ChangePassword param);

}
