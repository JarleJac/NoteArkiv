package jacJarSoft.noteArkiv;

import javax.persistence.EntityManager;

public class AppContextAware {
	protected EntityManager getEntityManager() {
		return AppContext.get().getEntityManager();
	}
	protected String getCurrentUser() {
		return AppContext.get().getCurrentUser();
	}

	protected String getFilesDirectory() {
		return AppContext.get().getFilesDirectory();
	}
}
