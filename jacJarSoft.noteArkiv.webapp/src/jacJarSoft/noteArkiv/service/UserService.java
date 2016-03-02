package jacJarSoft.noteArkiv.service;

import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import jacJarSoft.noteArkiv.model.User;
import jacJarSoft.noteArkiv.webapi.ChangePassword;

@Path("/userservice")
@WebService(name="userService", targetNamespace="http://jacJarSoft/noteArkiv/userService")
public interface UserService {

	@PUT
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/user/{userNo}/changepw")
	public Response changePassword(@PathParam("userNo")String userNo, ChangePassword param);

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/user/{userNo}")
	public Response getUser(@PathParam("userNo")String userNo);

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/user")
	public Response getUsers();

	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/user")
	public Response addUser(User user);

}
