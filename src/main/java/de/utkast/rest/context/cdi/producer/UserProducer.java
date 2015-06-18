/**
 * Copyright (C) 2014 Florian Hirsch
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
package de.utkast.rest.context.cdi.producer;

import java.security.Principal;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import de.utkast.rest.context.auth.Authentication;
import de.utkast.rest.context.auth.User;

/**
 * @author Florian Hirsch
 */
@Named
@RequestScoped
public class UserProducer {

	@Inject 
	private HttpServletRequest request;
	
	@Inject
	private Authentication auth;
	
	@Produces
	public User findUser() {
		Principal principal = auth.authenticate(request);
		if (principal == null) {
			return null;
		}
		return new User(principal.getName());
	}
	
}
