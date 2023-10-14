package jacJarSoft.noteArkiv.service;

import jakarta.jws.WebService;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/adminservice")
@WebService(name="noteService", targetNamespace="http://jacJarSoft/noteArkiv/adminService")
public interface AdminService {
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/importuseremail")
	public Response importUserEMail();

	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/createcsvexport")
	public Response createCsvExport();

}
