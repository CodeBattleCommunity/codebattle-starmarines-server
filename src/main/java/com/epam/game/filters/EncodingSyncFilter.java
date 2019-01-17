package com.epam.game.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Servlet Filter implementation class UTF8ConvertFilter
 */
public class EncodingSyncFilter implements Filter {

    /**
     * Default constructor. 
     */
    public EncodingSyncFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	    if(request.getCharacterEncoding() == null){
	        chain.doFilter(new EncodingRequestWrapper((HttpServletRequest) request), response);
	    } else {
	        response.setCharacterEncoding("UTF8");
	        chain.doFilter(request, response);
	    }
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
