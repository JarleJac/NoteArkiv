package jacJarSoft.noteArkiv.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistenceFactory {
	public static EntityManagerFactory getEntityManagerFactory() {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("noteArkivDb");
		return factory;
	}
	public static EntityManager getEntityManager() {
		EntityManagerFactory factory = getEntityManagerFactory();
		EntityManager em = factory.createEntityManager();	
		return em;
	}

}
