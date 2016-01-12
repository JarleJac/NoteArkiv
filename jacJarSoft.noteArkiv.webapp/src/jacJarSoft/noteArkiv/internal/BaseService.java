package jacJarSoft.noteArkiv.internal;

import java.util.function.BiFunction;

import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.apache.cxf.transport.http.AbstractHTTPDestination;

import jacJarSoft.noteArkiv.AppContextAware;
import jacJarSoft.util.DbUtil;

public class BaseService extends AppContextAware {
    private ServletContext servletContext;

    
	protected ServletContext getServletContext() {
		return servletContext;
	}

//	private EntityManagerFactory getEntityManagerFactory() {
//		return (EntityManagerFactory) getServletContext().getAttribute(AppServletContextListner.ENTITY_MANAGER_FACTORY);
//	}
	
	@SuppressWarnings("unused")
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
		result = DbUtil.runWithTransaction(getEntityManager(), function, param);
		return result;
	}
	
}
