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
package de.utkast.rest.interception;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jboss.resteasy.util.Base64;
import org.junit.Test;

/**
 * @author Florian Hirsch
 */
public class TestClient {

	private static final String URL = "http://localhost:9090/jax-rs-context/user/%s";
	
	private static final String[] RESOURCES = { 
		"provider",
		"dispatcher",
		"filter", 
		"jax-rs-interceptor",
		"cdi-interceptor",
		"cdi-producer"
	};
	
	@Test
	public void testAuthentication() throws IOException {
		for (String resource : RESOURCES) {
			assertEquals(200, authenticate(resource, "John", "Doe"));
			assertEquals(403, authenticate(resource, "un", "known"));
		}
	}
	
	private int authenticate(String path, String username, String password) throws IOException {
		byte[] usernameAndPassword = String.format("%s:%s", username, password).getBytes("UTF-8");
		String encodedAuthentication = Base64.encodeBytes(usernameAndPassword);
		URL url = new URL(String.format(URL, path));
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		urlConnection.setRequestProperty("Authorization", "Basic " + encodedAuthentication);
		urlConnection.setRequestMethod("GET");
		urlConnection.connect();
		return urlConnection.getResponseCode();
	}
	
}
