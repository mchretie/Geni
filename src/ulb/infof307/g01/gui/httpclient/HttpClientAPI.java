package ulb.infof307.g01.gui.httpclient;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;

public abstract class HttpClientAPI {

    protected final HttpClient httpClient = HttpClient.newBuilder().build();
    protected final String baseUrl = "http://localhost:8080";

    protected HttpResponse<String> get(String path)
            throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(baseUrl + path))
                .GET()
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    protected HttpResponse<String> post(String path, String body)
            throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(baseUrl + path))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    protected HttpResponse<String> put(String path, String body)
            throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(baseUrl + path))
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    }

    protected HttpResponse<String> delete(String path)
            throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(baseUrl + path))
                .DELETE()
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    protected <T> List<T> stringToArray(String s, Class<T[]> elementClass) {
        T[] arr = new Gson().fromJson(s, elementClass);
        return Arrays.asList(arr);
    }

    protected String reformatString(String s) {
        return s.substring(1, s.length() - 1).replaceAll("\\\\", "");
    }
}
