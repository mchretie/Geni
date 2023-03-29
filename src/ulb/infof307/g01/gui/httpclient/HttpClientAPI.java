package ulb.infof307.g01.gui.httpclient;

import com.google.gson.Gson;
import ulb.infof307.g01.gui.httpclient.exceptions.ServerRequestFailed;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is the base class for all the DAO classes. It contains the
 *  methods to send HTTP requests to the server.
 *
 */
public abstract class HttpClientAPI {

    private String token;

    protected final HttpClient httpClient = HttpClient.newBuilder().build();
    protected final String baseUrl = "http://localhost:8080";

    /* ====================================================================== */
    /*                            HTTP methods                                */
    /* ====================================================================== */

    protected HttpResponse<String> get(String path)
            throws IOException, InterruptedException {

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(java.net.URI.create(baseUrl + path))
                .GET();

        HttpRequest request
                = token == null ?   requestBuilder.build()
                                    : requestBuilder
                                        .header("Authorization", token)
                                        .build();


        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    protected HttpResponse<String> post(String path, String body)
            throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(baseUrl + path))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .header("Authorization", token)
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    protected HttpResponse<String> put(String path, String body)
            throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(baseUrl + path))
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .header("Authorization", token)
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    }

    protected HttpResponse<String> delete(String path)
            throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(baseUrl + path))
                .DELETE()
                .header("Authorization", token)
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }


    /* ====================================================================== */
    /*                          Response code reaction                        */
    /* ====================================================================== */

    protected void checkResponseCode(int responseCode) throws ServerRequestFailed {
        if (responseCode != 200)
            throw new ServerRequestFailed("Server request failed: "
                    + responseCode);
    }


    /* ====================================================================== */
    /*                        JSON string interpreters                        */
    /* ====================================================================== */

    protected <T> List<T> stringToArray(String s, Class<T[]> elementClass) {
        T[] arr = new Gson().fromJson(s, elementClass);
        return arr == null ? new ArrayList<>() : Arrays.asList(arr);
    }

    /* ====================================================================== */
    /*                                  Setter                                */
    /* ====================================================================== */

    public void setToken(String token) {
        this.token = token;
    }

    /* ====================================================================== */
    /*                                  Getter                                */
    /* ====================================================================== */

    public String getToken() {
        return token;
    }
}
