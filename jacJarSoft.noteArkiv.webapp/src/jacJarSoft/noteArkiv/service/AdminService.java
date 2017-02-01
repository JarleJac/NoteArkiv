package jacJarSoft.noteArkiv.service;

import javax.jws.WebService;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
