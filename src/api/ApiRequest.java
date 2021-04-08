package api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiRequest {

    private static final String API_KEY = "";
    private static final String ROOT_URL = "https://fit3077.com/api/v1";
    private static HttpClient client;
    private static HttpRequest request;
    private static HttpResponse<String> response;

    public static HttpResponse<String> get(String url) throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        request = HttpRequest
                .newBuilder(URI.create(ROOT_URL + url))
                .setHeader("Authorization", API_KEY)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }

    public static HttpResponse<String> post(String url, String jsonObj) throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        request = HttpRequest.newBuilder(URI.create(ROOT_URL + url))
                .setHeader("Authorization", API_KEY)
                .header("Content-Type","application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonObj))
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }
}