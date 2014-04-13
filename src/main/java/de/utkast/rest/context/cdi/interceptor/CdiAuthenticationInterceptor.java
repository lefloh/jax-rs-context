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
package de.utkast.rest.context.cdi.interceptor;

import java.security.Principal;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.ws.rs.core.Response.Status;

import org.jboss.resteasy.spi.NoLogWebApplicationException;

import de.utkast.rest.context.auth.Authentication;

/**
 * @author Florian Hirsch
 */
@CdiAuthenticated
@Interceptor
public class CdiAuthenticationInterceptor {

	@Inject
	private Authentication auth;
	
	/**
	 * Authenticates and updates the ServletRequest if necessary.
	 * (A HttpServletRequest must be one of the method-parameters)
	 */
	@AroundInvoke
	public Object authenticate(InvocationContext ctx)  throws Exception {
		for (int i = 0; i < ctx.getParameters().length; i++) {
			if (ctx.getParameters()[i] instanceof HttpServletRequest) {
				ctx.getParameters()[i] = updateRequest((HttpServletRequest) ctx.getParameters()[i]); 
			}
		}
		return ctx.proceed();
	}

	private HttpServletRequest updateRequest(HttpServletRequest request) {
		final Principal principal = auth.authenticate(request);
		if (principal == null) {
			throw new NoLogWebApplicationException(Status.FORBIDDEN);
		}
		HttpServletRequestWrapper servletRequestWrapper = new HttpServletRequestWrapper((HttpServletRequest) request) {
			@Override
			public Principal getUserPrincipal() {
				return principal;
			}
		};
		return servletRequestWrapper;
	}
	
}
