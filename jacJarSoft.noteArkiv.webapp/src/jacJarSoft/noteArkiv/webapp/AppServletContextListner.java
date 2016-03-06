package jacJarSoft.noteArkiv.webapp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import jacJarSoft.noteArkiv.db.NoteArkivDatabase;
import jacJarSoft.noteArkiv.db.PersistenceFactory;

public class AppServletContextListner implements ServletContextListener {

	public static final String ENTITY_MANAGER_FACTORY = "jacJarSoft.noteArkiv.webapp.EntityManagerFactory";
	NoteArkivDatabase db;
	private ServletContext servletContext;
	private Logger logger;
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		servletContext = sce.getServletContext();
		initLogging(servletContext.getInitParameter("logging.props"));
		initPeristenceFactory(servletContext.getInitParameter("persistence.props"));
		
		EntityManagerFactory entityManagerFactory = PersistenceFactory.getEntityManagerFactory();
		servletContext.setAttribute(ENTITY_MANAGER_FACTORY, entityManagerFactory);		
		EntityManager entityManager = null;
		try {
			entityManager = entityManagerFactory.createEntityManager();
			NoteArkivDatabase db = new NoteArkivDatabase(entityManager);
			db.verifyAndUpgradeDb();
		}
		finally {
			if (entityManager != null)
			entityManager.close();
		}
		
	}

	private void initPeristenceFactory(String persistencePropsFile) {
		Properties persistenceProps = new Properties();
		try (InputStream inputStream = servletContext.getResourceAsStream(persistencePropsFile)) {
			persistenceProps.load(inputStream);
			PersistenceFactory.setOverrideProperties(persistenceProps);
		} catch (IOException e) {
			throw new RuntimeException("error loading persistence properties", e);
		}
		
		
	}

	private void initLogging(String resourceName) {
		try
		{
			final InputStream inputStream = servletContext.getResourceAsStream(resourceName);
		    LogManager.getLogManager().readConfiguration(inputStream);
		    logger = Logger.getLogger(AppServletContextListner.class.getName());
		    logger.info("Application logging initiated");
		}
		catch (final Exception e)
		{
		    Logger.getAnonymousLogger().severe("Could not load default logging.properties file");
		    Logger.getAnonymousLogger().severe(e.getMessage());
		}	
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		servletContext = sce.getServletContext();
		EntityManagerFactory entityManagerFactory = (EntityManagerFactory) servletContext.getAttribute(ENTITY_MANAGER_FACTORY);
		if (null != entityManagerFactory)
			entityManagerFactory.close();
	}

}
