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
package de.utkast.rest.context.jaxrs.interceptor;

import java.io.IOException;
import java.security.Principal;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.spi.NoLogWebApplicationException;

import de.utkast.rest.context.auth.Authentication;

/**
 * @author Florian Hirsch
 */
@Provider
@JaxRsAuthenticated
public class JaxRsAuthenticationInterceptor implements ContainerRequestFilter {

	@Inject
	private Authentication auth;
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		final Principal principal = auth.authenticate(requestContext.getHeaderString("Authorization"));
		if (principal == null) {
			throw new NoLogWebApplicationException(Status.FORBIDDEN);
		}
		requestContext.setSecurityContext(new SecurityContext() {
			@Override
			public boolean isUserInRole(String role) {
				return false;
			}
			
			@Override
			public boolean isSecure() {
				return false;
			}
			
			@Override
			public Principal getUserPrincipal() {
				return principal;
			}
			
			@Override
			public String getAuthenticationScheme() {
				return null;
			}
		});
	}

}
