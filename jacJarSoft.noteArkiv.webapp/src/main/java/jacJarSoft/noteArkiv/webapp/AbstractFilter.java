package jacJarSoft.noteArkiv.webapp;

import jacJarSoft.noteArkiv.internal.NoteArkivSettings;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

public abstract class AbstractFilter implements Filter {

	private NoteArkivSettings appSettings;
	private ServletContext servletContext;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		servletContext = filterConfig.getServletContext();
		appSettings = (NoteArkivSettings) servletContext.getAttribute(AppServletContextListner.APP_SETTINGS);
	}

	@Override
	public void destroy() {
	}

	protected NoteArkivSettings getAppSettings() {
		return appSettings;
	}

	protected void setAppSettings(NoteArkivSettings appSettings) {
		this.appSettings = appSettings;
	}

	protected ServletContext getServletContext() {
		return servletContext;
	}

	protected void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

}