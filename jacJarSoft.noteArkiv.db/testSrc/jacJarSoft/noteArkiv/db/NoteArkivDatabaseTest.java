package jacJarSoft.noteArkiv.db;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.Test;

import jacJarSoft.util.ExceptionUtil;

public class NoteArkivDatabaseTest {

	@Test
	public void TestCreateDb()
	{
		String dbFile = ".\\tmp\\testDb\\theTestDb.db";

		try {
			Files.deleteIfExists(new File(dbFile).getAbsoluteFile().toPath());
	
			NoteArkivDatabase db = new NoteArkivDatabase(dbFile);
			db.verifyAndUpgradeDb();
			assertTrue(new File(dbFile).exists());
		
			assertTrue(Files.deleteIfExists(new File(dbFile).getAbsoluteFile().toPath()));
		} catch (IOException e) {
			throw ExceptionUtil.getRuntimeException(e);
		}
	}
}
