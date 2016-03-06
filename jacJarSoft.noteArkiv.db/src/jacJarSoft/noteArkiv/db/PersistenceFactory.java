package jacJarSoft.noteArkiv.db;

import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistenceFactory {
	private static Properties overrideProperties = null;
	public static EntityManagerFactory getEntityManagerFactory() {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("noteArkivDb", overrideProperties);
		return factory;
	}
	public static EntityManager getEntityManager() {
		EntityManagerFactory factory = getEntityManagerFactory();
		EntityManager em = factory.createEntityManager();	
		return em;
	}
	public static Properties getOverrideProperties() {
		return overrideProperties;
	}
	public static void setOverrideProperties(Properties overrideProperties) {
		PersistenceFactory.overrideProperties = overrideProperties;
	}

}
