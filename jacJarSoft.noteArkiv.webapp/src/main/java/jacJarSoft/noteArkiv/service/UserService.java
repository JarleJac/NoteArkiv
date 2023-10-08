package jacJarSoft.noteArkiv.service;

import jacJarSoft.noteArkiv.model.User;
import jacJarSoft.noteArkiv.webapi.ChangePassword;
import jakarta.jws.WebService;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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

	@DELETE
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/user/{userNo}")
	public Response deleteUser(@PathParam("userNo")String userNo);

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/user")
	public Response getUsers();

	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/user")
	public Response addUser(User user);

	@PUT
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/user")
	public Response updateUser(User user);

}
