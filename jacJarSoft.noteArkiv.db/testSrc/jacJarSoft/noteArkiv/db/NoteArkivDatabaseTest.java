package jacJarSoft.noteArkiv.db;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.Test;

import jacJarSoft.util.ExceptionUtil;

public class NoteArkivDatabaseTest {

	@Test
	public void TestCreateDb()
	{
		String dbFileName = ".\\tmp\\testDb\\theTestDb.db";
		File dbFile = new File(dbFileName);
		resetDbFile(dbFile);

		Properties addedOrOverridenProperties = new Properties();
		addedOrOverridenProperties.setProperty("javax.persistence.jdbc.driver", "org.sqlite.JDBC");
		addedOrOverridenProperties.setProperty("javax.persistence.jdbc.url", "jdbc:sqlite:" + dbFile.getAbsolutePath());
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("noteArkivDb", addedOrOverridenProperties);
		try {
			EntityManager entityManager = null;
			try {
				entityManager = entityManagerFactory.createEntityManager();
				NoteArkivDatabase db = new NoteArkivDatabase(entityManager);
				db.verifyAndUpgradeDb();
				assertTrue(dbFile.exists());
			}
			finally {
				if (entityManager != null)
					entityManager.close();
				entityManagerFactory.close();
			}
			assertTrue(Files.deleteIfExists(dbFile.getAbsoluteFile().toPath()));
		} catch (IOException e) {
			throw ExceptionUtil.getRuntimeException(e);
		}
	}

	private void resetDbFile(File dbFile) {
		try {
			Files.deleteIfExists(dbFile.getAbsoluteFile().toPath());
			Files.createDirectories(dbFile.getParentFile().toPath());
		} catch (IOException e) {
			throw ExceptionUtil.getRuntimeException(e);
		}
	}
}
