package jacJarSoft.noteArkiv;

import javax.persistence.EntityManager;

import jacJarSoft.noteArkiv.db.PersistenceFactory;

public class AppContext {
	private static final ThreadLocal<AppContext> appContext =
        new ThreadLocal<AppContext>() {
            @Override protected AppContext initialValue() {
            	AppContext ctx = new AppContext();
                return ctx;
        }
    };
    
    public static AppContext get() {
        return appContext.get();
    }
    public static void remove() {
        appContext.remove();
    }
    private boolean closed = false;
	private EntityManager entityManager;
	private String currentUser;
	private String filesDirectory;

	private AppContext() {
    	entityManager = PersistenceFactory.getEntityManager();
    }
    private void verifyNotClosed() {
    	if (closed)
    		throw new IllegalStateException("AppContext used after it has been closed");
    }
	public void close() {
		if (null != entityManager)
			entityManager.close();
		closed = true;
	}
    public EntityManager getEntityManager() {
    	verifyNotClosed();
		return entityManager;
	}
	public String getCurrentUser() {
    	verifyNotClosed();
		return currentUser;
	}
	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}
	public void setFilesDirectory(String filesDirectory) {
		this.filesDirectory = filesDirectory;
	}
	public String getFilesDirectory() {
		return filesDirectory;
	}
}
