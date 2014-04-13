/**
 * Copyright (C) 2014 Florian Hirsch fhi@adorsys.de
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.utkast.rest.context.filter;

import java.io.IOException;
import java.security.Principal;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import de.utkast.rest.context.auth.Authentication;

/**
 * @author Florian Hirsch
 */
@WebFilter("/user/filter")
public class AuthenticationFilter implements Filter {

	@Inject
	private Authentication auth;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		final Principal principal = auth.authenticate(request);
		if (principal == null) {
			((HttpServletResponse)response).sendError(403);
			return;
		}
		HttpServletRequestWrapper servletRequestWrapper = new HttpServletRequestWrapper((HttpServletRequest) request) {
			@Override
			public Principal getUserPrincipal() {
				return principal;
			}
		};
		chain.doFilter(servletRequestWrapper, response);
	}

	@Override
	public void destroy() {}

}
