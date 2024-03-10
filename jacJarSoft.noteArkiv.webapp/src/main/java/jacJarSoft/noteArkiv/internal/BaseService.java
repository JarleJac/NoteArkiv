package jacJarSoft.noteArkiv.internal;

import java.util.function.BiFunction;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.apache.cxf.transport.http.AbstractHTTPDestination;

import freemarker.template.Configuration;
import jacJarSoft.noteArkiv.AppContextAware;
import jacJarSoft.noteArkiv.webapp.AppServletContextListner;
import jacJarSoft.util.DbUtil;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;

public class BaseService extends AppContextAware {
	private ServletContext servletContext;
	private UriInfo uriInfo;

	protected ServletContext getServletContext() {
		return servletContext;
	}

	protected String getNotearkivBaseUrl() {
		String baseNotearkivUrl = getAppSettings().getBaseNotearkivUrl();
		if (StringUtils.isEmpty(baseNotearkivUrl))
		{
			baseNotearkivUrl = uriInfo.getBaseUri().toString().replace("/rest/", "/#/");
		}
		return baseNotearkivUrl;
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

	@Context
	public void setUrisInfo(UriInfo uriInfo) {
		this.uriInfo = uriInfo;
	}

	public <T, R> R runWithTransaction(BiFunction<EntityManager, T, R> function, T param) {
		R result = null;
		result = DbUtil.runWithTransaction(getEntityManager(), function, param);
		return result;
	}

	protected NoteArkivSettings getAppSettings() {
		NoteArkivSettings appSettings = (NoteArkivSettings) servletContext
				.getAttribute(AppServletContextListner.APP_SETTINGS);
		return appSettings;
	}

	protected Configuration getFreemarkerConfig() {
		Configuration cfg = (Configuration) servletContext.getAttribute(AppServletContextListner.FREEMARKER_CONFIG);
		return cfg;
	}
}
