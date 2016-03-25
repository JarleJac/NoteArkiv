package jacJarSoft.noteArkiv.db;

import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistenceFactory {
	private static Properties overrideProperties = null;
	public static EntityManagerFactory getEntityManagerFactory() {
		Properties props = getProperties();
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("noteArkivDb", props);
		return factory;
	}
	private static Properties getProperties() {
		Properties props = new Properties();
		if (overrideProperties != null)
			props.putAll(overrideProperties);
		props.setProperty("eclipselink.session-event-listener", "jacJarSoft.noteArkiv.db.MySessionEventAdapter");
		return props;
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
