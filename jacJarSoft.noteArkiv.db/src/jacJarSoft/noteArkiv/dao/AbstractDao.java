package jacJarSoft.noteArkiv.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class AbstractDao {
	
    private EntityManager entityManager;

	protected EntityManager getEntityManager() {
		return entityManager;
	}

	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.entityManager = em;
	}

}
