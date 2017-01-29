package jacJarSoft.noteArkiv.internal;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.opencsv.CSVReader;

import jacJarSoft.noteArkiv.dao.UserDao;
import jacJarSoft.noteArkiv.model.User;
import jacJarSoft.noteArkiv.service.AdminService;

public class AdminServiceImpl extends BaseService implements AdminService {
	@Autowired
	private UserDao userDao;

	@Override
	public Response importUserEMail() {
		File importFile = Paths.get(getFilesDirectory(), "users.csv").toFile();
		if (!importFile.exists())
			throw new ValidationErrorException("Finner ikke noe users.csv fil");
		try {
			try (CSVReader reader = new CSVReader(new FileReader(importFile))) {
				String[] nextLine;
				reader.readNext(); //Skip headers
				while ((nextLine = reader.readNext()) != null) {
					final String[] line = nextLine;
					runWithTransaction((em,p) -> {
						User user = userDao.getUser(line[0]);
						if (user == null)
							return null;
						user.seteMail(line[2]);
						userDao.updateUser(user);
						return null;
					}, null);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return Response.ok().build();
	}

}
