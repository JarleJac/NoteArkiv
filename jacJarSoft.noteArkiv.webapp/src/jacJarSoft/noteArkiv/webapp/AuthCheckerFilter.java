package jacJarSoft.noteArkiv.webapp;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(filterName = "AuthCheckerFilter",
urlPatterns = {"/rest/*"}
//,
//initParams = {
//    @WebInitParam(name = "mood", value = "awake")}
)
public class AuthCheckerFilter implements Filter 	 {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		boolean authOk = true;
		if (authOk)
			chain.doFilter(request, response);
		else {
			res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
