package jacJarSoft.noteArkiv.webapp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import jacJarSoft.noteArkiv.AppContext;
import jacJarSoft.util.Auth.AuthException;
import jacJarSoft.util.Auth.AuthTokenInfo;
import jacJarSoft.util.Auth.AuthTokenUtil;
import jacJarSoft.util.web.MultiReadHttpServletRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.MediaType;

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
			pathInfo.startsWith("/adminservice/createcsvexport") || 
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
		if (logger.isLoggable(Level.FINE) && 
				httpReq.getContentLength() > 0 && 
				httpReq.getContentType().startsWith(MediaType.APPLICATION_JSON))
		{
			MultiReadHttpServletRequest multireadRq = new MultiReadHttpServletRequest(httpReq);
			try (ByteArrayOutputStream os = new ByteArrayOutputStream())
			{
				int data;
				ServletInputStream inputStream = multireadRq.getInputStream();
				while ((data = inputStream.read()) != -1)
					os.write(data);
				String strData = new String(os.toByteArray());
				logger.fine("Received the following data:\n" + strData);
			}
			httpReq = multireadRq;
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
			
			chain.doFilter(httpReq, response);


			appContext.close();
			AppContext.remove();
		}
		else {
			httpRes.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}
}
