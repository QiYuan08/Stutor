package API;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

public class User {
	
	String API_KEY = Config.API_KEY;
	String ROOT_URL = Config.ROOT_URL;
	HttpRequest request;
	HttpClient client;
	String userUrl;
	
	/**
	 * Method to to build HttpRequest to get user from API
	 * @param id ID to look for specific user
	 * @return the HttpRequest URL
	 */
	public HttpRequest getUser() {
		
		userUrl = ROOT_URL + "/user";
		
		client = HttpClient.newHttpClient();
		request = HttpRequest
				.newBuilder(URI.create(userUrl))
				.setHeader("Authorization", API_KEY)
				.GET()
				.build();
		
		
		return request;
		
	}

			
//    client = HttpClient.newHttpClient();
//    request = HttpRequest
//      .newBuilder(URI.create(usersUrl))
//      .setHeader("Authorization", API_KEY)
//      .GET()
//      .build();


}
