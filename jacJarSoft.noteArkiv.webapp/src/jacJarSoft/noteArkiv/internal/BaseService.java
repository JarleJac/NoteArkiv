package jacJarSoft.noteArkiv.internal;

import java.util.function.BiFunction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.apache.cxf.transport.http.AbstractHTTPDestination;

import jacJarSoft.noteArkiv.webapp.AppServletContextListner;
import jacJarSoft.util.DbUtil;

public class BaseService {
    private EntityManager entityManager;
    private ServletContext servletContext;

    
	protected EntityManager getEntityManager() {
		return entityManager;
	}

	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.entityManager = em;
	}

	protected ServletContext getServletContext() {
		return servletContext;
	}

	private EntityManagerFactory getEntityManagerFactory() {
		return (EntityManagerFactory) getServletContext().getAttribute(AppServletContextListner.ENTITY_MANAGER_FACTORY);
	}
	
	private HttpServletRequest getHttpServletRequest() {
		Message message = PhaseInterceptorChain.getCurrentMessage();
	    HttpServletRequest httpRequest = (HttpServletRequest) message.get(AbstractHTTPDestination.HTTP_REQUEST);
		return httpRequest;
	}
	
	@Context
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public <T,R> R runWithTransaction(BiFunction<EntityManager,T,R> function, T param) {
		R result = null;
		EntityManagerFactory entityManagerFactory = getEntityManagerFactory();
		EntityManager entityManager = null;
		try {
			entityManager = entityManagerFactory.createEntityManager();
			result = DbUtil.runWithTransaction(entityManager, function, param);
		}
		finally {
			if (entityManager != null)
			entityManager.close();
		}
		return result;
	}
	
}
