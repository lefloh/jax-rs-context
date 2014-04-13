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
package de.utkast.rest.context.dispatcher;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.jboss.resteasy.spi.ResteasyProviderFactory;

import de.utkast.rest.context.auth.Authentication;
import de.utkast.rest.context.auth.User;

/**
 * @author Florian Hirsch
 */
public class UserProvider {
	
	private Authentication auth = new Authentication();
	
	public User get() {
		HttpServletRequest request = ResteasyProviderFactory.getContextData(HttpServletRequest.class);
		Principal principal = auth.authenticate(request);
		return principal == null ? null : new User(principal.getName());
	}
	
}
