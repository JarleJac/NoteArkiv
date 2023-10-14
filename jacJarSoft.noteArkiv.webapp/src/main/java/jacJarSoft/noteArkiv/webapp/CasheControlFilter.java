package jacJarSoft.noteArkiv.webapp;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter(filterName = "CasheControlFilter",
urlPatterns = {"/*"}
)
public class CasheControlFilter extends AbstractFilter {
	private static Logger logger = Logger.getLogger(CasheControlFilter.class.getName());
	private int chacheControlMaxAge;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		super.init(filterConfig);
		Integer chacheControlMaxAgeSetting = getAppSettings().getChacheControlMaxAge();
		if (null == chacheControlMaxAgeSetting)
			chacheControlMaxAge = 3600;
		else
			chacheControlMaxAge = chacheControlMaxAgeSetting;
	}
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpServletResponse httpRes = (HttpServletResponse) response;
		String servletPath = httpReq.getServletPath();
		if (!servletPath.startsWith("/rest")) {
			String cacheControl = "max-age=" + chacheControlMaxAge +  ", must-revalidate";
			
			if (logger.isLoggable(Level.FINE))
				logger.fine("Adding cache headers ["+cacheControl+"] to path " + servletPath);
			
			httpRes.setHeader("Cache-Control", cacheControl); // HTTP 1.1.
		}
		chain.doFilter(request, response);
	}
}
