package jacJarSoft.noteArkiv.db;

import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.SessionEvent;
import org.eclipse.persistence.sessions.SessionEventAdapter;

public class MySessionEventAdapter extends SessionEventAdapter {

	@Override
	public void postAcquireClientSession(SessionEvent event) {
		Session session = event.getSession();
		session.executeNonSelectingSQL("PRAGMA foreign_keys=ON");
		session.executeSQL("pragma busy_timeout=30000"); //Busy timeout set to 30000 milliseconds
		super.postAcquireClientSession(event);
	}
}
