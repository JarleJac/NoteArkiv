package jacJarSoft.noteArkiv.webapp;

import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import jacJarSoft.noteArkiv.db.NoteArkivDatabase;

public class AppServletContextListner implements ServletContextListener {

	NoteArkivDatabase db;
	private ServletContext servletContext;
	private Logger logger;
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		servletContext = sce.getServletContext();
		initLogging(servletContext.getInitParameter("logging.props"));
		String dbFileName = "test.db";
		db = new NoteArkivDatabase(dbFileName);
		
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
	}

}
