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
package de.utkast.rest.context;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Providers;

import de.utkast.rest.context.auth.User;
import de.utkast.rest.context.cdi.interceptor.CdiAuthenticated;
import de.utkast.rest.context.cdi.interceptor.CdiAuthenticationInterceptor;
import de.utkast.rest.context.dispatcher.UserProvider;
import de.utkast.rest.context.filter.AuthenticationFilter;
import de.utkast.rest.context.jaxrs.interceptor.JaxRsAuthenticated;
import de.utkast.rest.context.jaxrs.interceptor.JaxRsAuthenticationInterceptor;

/**
 * @author Florian Hirsch
 */
@Path("/user")
public class UserResource {

	@Context
	private Providers providers;

	/**
	 * Resolves the User via the Injected providers.
	 * Work is done by {@link de.utkast.rest.context.provider.UserProvider}
	 */
	@GET
	@Path("/provider")
	public Response context() {
		ContextResolver<User> contextResolver = providers.getContextResolver(User.class, MediaType.WILDCARD_TYPE);
		User user = contextResolver.getContext(User.class);
		if (user == null) {
			return Response.status(Status.FORBIDDEN).build();
		}
		return Response.ok(String.format("Hello, %s!", user.getName())).build();
	}
	
	/**
	 * Gets the UserProvider registered in {@link RestApplication}
	 */
	@GET
	@Path("/dispatcher")
	public Response dispatcher(@Context UserProvider userProvider) {
		User user = userProvider.get();
		if (user == null) {
			return Response.status(Status.FORBIDDEN).build();
		}
		return Response.ok(String.format("Hello, %s!", user.getName())).build();
	}
	
	/**
	 * Is filtered by {@link AuthenticationFilter}
	 */
	@GET
	@Path("/filter")
	public Response filtered(@Context HttpServletRequest request) {
		String name = request.getUserPrincipal().getName(); // filter would have sent an 403 if principal is null
		return Response.ok(String.format("Hello, %s!", name)).build();
	}
	
	/**
	 * Is intercepted by {@link JaxRsAuthenticationInterceptor}
	 */
	@GET
	@Path("/jax-rs-interceptor")
	@JaxRsAuthenticated
	public Response jaxRsIntercepted(@Context SecurityContext ctx) {
		String name = ctx.getUserPrincipal().getName();
		return Response.ok(String.format("Hello, %s!", name)).build();
	}
	
	/**
	 * Is intercepted by {@link CdiAuthenticationInterceptor}
	 */
	@GET
	@Path("/cdi-interceptor")
	@CdiAuthenticated
	public Response cdiIntercepted(@Context HttpServletRequest request) {
		String name = request.getUserPrincipal().getName();
		return Response.ok(String.format("Hello, %s!", name)).build();
	}
	
	@Inject
	private User user;
	
	/**
	 * Uses the User produced by {@link UserProducer)
	 */
	@GET
	@Path("/cdi-producer")
	public Response cdiProduced() {
		if (user == null) {
			return Response.status(Status.FORBIDDEN).build();
		}
		return Response.ok(String.format("Hello, %s!", user.getName())).build();
	}
	
}
