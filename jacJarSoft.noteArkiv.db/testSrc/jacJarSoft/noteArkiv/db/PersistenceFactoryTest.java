package jacJarSoft.noteArkiv.db;

import static org.junit.Assert.assertNotNull;

import javax.persistence.EntityManager;

import org.junit.Test;

public class PersistenceFactoryTest {

	@Test
	public void Test()
	{
		EntityManager entityManager = PersistenceFactory.getEntityManager();
		assertNotNull(entityManager);
		entityManager.close();
	}
}
