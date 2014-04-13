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
package de.utkast.rest.context.provider;

import java.security.Principal;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import de.utkast.rest.context.auth.Authentication;
import de.utkast.rest.context.auth.User;

/**
 * @author Florian Hirsch
 */
@Provider
@Produces(MediaType.WILDCARD)
public class UserProvider implements ContextResolver<User> {

	@Inject
	private Authentication auth;
	
	@Context
	private HttpServletRequest request;
	
	@Override
	public User getContext(Class<?> type) {
		Principal principal = auth.authenticate(request);
		return principal == null ? null : new User(principal.getName());
	}

}
