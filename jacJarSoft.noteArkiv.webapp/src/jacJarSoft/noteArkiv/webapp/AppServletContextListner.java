package jacJarSoft.noteArkiv.webapp;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import jacJarSoft.noteArkiv.db.NoteArkivDatabase;

public class AppServletContextListner implements ServletContextListener {

	NoteArkivDatabase db;
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		String dbFileName = "test.db";
		db = new NoteArkivDatabase(dbFileName);
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}

}
