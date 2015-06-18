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
package de.utkast.rest.context.auth;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.jboss.resteasy.util.Base64;


/**
 * Mocked Authention
 * @author Florian Hirsch
 */
@ApplicationScoped
public class Authentication {

	private static final Map<String, String> users;
	
	static {
		users = new HashMap<String, String>();
		users.put("john", "Doe");
	}
	
	public Principal authenticate(String authHeader) {
		if (authHeader == null || !authHeader.contains("Basic")) {
			return null;
		}
		try {
			byte[] decodedHeader = Base64.decode(authHeader.replace("Basic ", ""));
			String[] userAndPassword = new String(decodedHeader, "UTF-8").split(":");
			String password = users.get(userAndPassword[0].toLowerCase());
			if (password != null && password.equals(userAndPassword[1])) {
				return new UserPrincipal(userAndPassword[0]);
			}
		} catch (IOException ex) {
			throw new IllegalArgumentException(ex);
		}
		return null;
	}
	
	public Principal authenticate(ServletRequest request) {
		return authenticate(((HttpServletRequest)request).getHeader("Authorization"));
	}
	
}
