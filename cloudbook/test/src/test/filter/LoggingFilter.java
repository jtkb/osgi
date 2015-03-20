package test.filter;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class LoggingFilter implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
		String ip = req.getRemoteAddr();
		System.out.println("Incoming request from " + ip + " at " + new Date().toString());
		
		filterChain.doFilter(req, resp);
		
		System.out.println("Sending response to " + ip + " at " + new Date().toString());
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}
