package jacJarSoft.noteArkiv.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistenceFactory {
	public static EntityManager getEntityManager() {
		EntityManagerFactory factory =   Persistence.createEntityManagerFactory("noteArkivDb");
		EntityManager em = factory.createEntityManager();	
		return em;
	}

}
