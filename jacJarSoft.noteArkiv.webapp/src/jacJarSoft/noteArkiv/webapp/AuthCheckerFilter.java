package jacJarSoft.noteArkiv.webapp;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jacJarSoft.noteArkiv.AppContext;
import jacJarSoft.util.Auth.AuthException;
import jacJarSoft.util.Auth.AuthTokenInfo;
import jacJarSoft.util.Auth.AuthTokenUtil;

@WebFilter(filterName = "AuthCheckerFilter",
urlPatterns = {"/rest/*"}
//,
//initParams = {
//    @WebInitParam(name = "mood", value = "awake")}
)
public class AuthCheckerFilter  extends AbstractFilter {

	private static Logger logger = Logger.getLogger(AuthCheckerFilter.class.getName());
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpServletResponse httpRes = (HttpServletResponse) response;

		boolean authOk = false;
		AuthTokenInfo tokenInfo = null;
		String pathInfo = httpReq.getPathInfo();
		if (pathInfo.startsWith("/appservice") || 
			pathInfo.startsWith("/services") || 
			(pathInfo.equals("/") && (httpReq.getParameterValues("_wadl") != null)))
			authOk = true;
		else {
			logger.fine("Checking auth for: " + pathInfo );
			String authHeader = httpReq.getHeader("Authorization");
			if (authHeader == null) {
				authHeader = httpReq.getParameter("authInfo");
			}
			if (authHeader != null) {
				logger.fine("got header: " + authHeader );
				try {
					tokenInfo = AuthTokenUtil.getTokenInfo(authHeader);
					//TODO check timeout using tokenInfo.getCreationDateTime();
					logger.fine("Authenticated: " + tokenInfo.getUser());
					authOk = true;
				} catch (AuthException e) {
					logger.log(Level.SEVERE, "not auth, exception: " + e.getMessage(), e);
				}
			}
			else
			{
				logger.warning("Not Auth, no header");
			}
		}
		if (authOk)
		{
			AppContext appContext = AppContext.get();
			if (tokenInfo != null)
				appContext.setCurrentUser(tokenInfo.getUser());
			appContext.setFilesDirectory(getAppSettings().getFilesDirectory());

			httpRes.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
			httpRes.setHeader("Pragma", "no-cache"); // HTTP 1.0.
			httpRes.setDateHeader("Expires", 0); // Proxies.
			
			chain.doFilter(request, response);


			appContext.close();
			AppContext.remove();
		}
		else {
			httpRes.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}
}
