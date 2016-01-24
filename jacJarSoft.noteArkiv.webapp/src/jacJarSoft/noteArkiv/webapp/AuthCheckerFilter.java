package jacJarSoft.noteArkiv.webapp;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jacJarSoft.noteArkiv.AppContext;
import jacJarSoft.util.Auth.AuthTokenInfo;
import jacJarSoft.util.Auth.AuthTokenUtil;

@WebFilter(filterName = "AuthCheckerFilter",
urlPatterns = {"/rest/*"}
//,
//initParams = {
//    @WebInitParam(name = "mood", value = "awake")}
)
public class AuthCheckerFilter implements Filter 	 {

	private static Logger logger = Logger.getLogger(AuthCheckerFilter.class.getName());
	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		boolean authOk = false;
		AuthTokenInfo tokenInfo = null;
		String pathInfo = req.getPathInfo();
		if (pathInfo.equals("/appservice/logon") || pathInfo.startsWith("/services") || (pathInfo.equals("/") && (req.getParameterValues("_wadl") != null)))
			authOk = true;
		else {
			logger.fine("Checking auth for: " + pathInfo );
			String authHeader = req.getHeader("Authorization");
			if (authHeader != null) {
				logger.fine("got header: " + authHeader );
				try {
					tokenInfo = AuthTokenUtil.getTokenInfo(authHeader);
					//TODO check timeout using tokenInfo.getCreationDateTime();
					logger.fine("Authenticated: " + tokenInfo.getUser());
					authOk = true;
				} catch (Exception e) {
					logger.fine("not auth, ex.message: " + e.getMessage() );
				}
			}
		}
		if (authOk)
		{
			AppContext appContext = AppContext.get();
			if (tokenInfo != null)
				appContext.setCurrentUser(tokenInfo.getUser());
			
			chain.doFilter(request, response);
			
			appContext.close();
			AppContext.remove();
		}
		else {
			res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
