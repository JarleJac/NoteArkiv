package jacJarSoft.noteArkiv.internal;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class BaseService {
    private EntityManager entityManager;

	protected EntityManager getEntityManager() {
		return entityManager;
	}

	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.entityManager = em;
	}

}
