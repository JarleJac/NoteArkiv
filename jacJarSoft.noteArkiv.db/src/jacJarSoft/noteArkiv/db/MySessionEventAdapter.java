package jacJarSoft.noteArkiv.db;

import org.eclipse.persistence.sessions.SessionEvent;
import org.eclipse.persistence.sessions.SessionEventAdapter;

public class MySessionEventAdapter extends SessionEventAdapter {

	@Override
	public void postAcquireClientSession(SessionEvent event) {
		event.getSession().executeNonSelectingSQL("PRAGMA foreign_keys=ON");
		super.postAcquireClientSession(event);
	}
}
