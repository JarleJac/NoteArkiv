package jacJarSoft.noteArkiv.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.opencsv.CSVReader;

import jacJarSoft.noteArkiv.api.StatusResponce;
import jacJarSoft.noteArkiv.dao.NoteDao;
import jacJarSoft.noteArkiv.dao.TagsDao;
import jacJarSoft.noteArkiv.dao.UserDao;
import jacJarSoft.noteArkiv.dao.VoiceDao;
import jacJarSoft.noteArkiv.model.User;
import jacJarSoft.noteArkiv.service.AdminService;

public class AdminServiceImpl extends BaseService implements AdminService {
	private static Logger logger = Logger.getLogger(AdminServiceImpl.class.getName());
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private NoteDao noteDao;
	@Autowired
	private VoiceDao voiceDao;
	@Autowired
	private TagsDao tagsDao;

	@Override
	public Response importUserEMail() {
		File importFile = Paths.get(getFilesDirectory(), "users.csv").toFile();
		if (!importFile.exists())
			throw new ValidationErrorException("Finner ikke noe users.csv fil");
		try {
			try (CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(importFile), StandardCharsets.UTF_8))) {
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

	@Override
	public Response createCsvExport() {

		try {
			CsvExportHandler handler = new CsvExportHandler(getFilesDirectory(), noteDao, voiceDao, tagsDao);
			handler.runExport();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Exception during export", e);
			return Response.ok(new StatusResponce(100,"Error during export: " + e.getMessage())).build();
		}
		return Response.ok(new StatusResponce("sheetdata exported ok!")).build();
	}

}
