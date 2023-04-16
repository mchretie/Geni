package ulb.infof307.g01.gui.httpdao.dao;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import ulb.infof307.g01.gui.httpdao.exceptions.ServerRequestFailedException;
import ulb.infof307.g01.model.DeckMetadata;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is the base class for all the DAO classes. It contains the
 *  methods to send HTTP requests to the server.
 *
 */
public abstract class HttpDAO {

    private String token = "";

    private final HttpClient httpClient = HttpClient.newBuilder().build();

    protected final String BASE_URL    = "http://localhost:8080";
    private final String AUTH_HEADER = "Authorization";

    /* ====================================================================== */
    /*                            HTTP methods                                */
    /* ====================================================================== */

    protected HttpResponse<String> get(String path)
            throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(BASE_URL + path))
                .GET()
                .header(AUTH_HEADER, token)
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    protected HttpResponse<String> post(String path, String body)
            throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(BASE_URL + path))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .header(AUTH_HEADER, token)
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    protected HttpResponse<String> delete(String path)
            throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(BASE_URL + path))
                .DELETE()
                .header(AUTH_HEADER, token)
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    protected HttpResponse<String> upload(String path, File file, String filename)
            throws IOException, InterruptedException {

        HttpRequest uploadFileRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Content-Type", "application/octet-stream")
                .header(AUTH_HEADER, token)
                .header("File-Name", filename)
                .POST(HttpRequest.BodyPublishers.ofFile(file.toPath()))
                .build();

        return httpClient.send(uploadFileRequest, HttpResponse.BodyHandlers.ofString());
    }


    /* ====================================================================== */
    /*                          Response code reaction                        */
    /* ====================================================================== */

    protected void checkResponseCode(int responseCode) throws ServerRequestFailedException {
        if (responseCode != 200)
            throw new ServerRequestFailedException("Server request failed: "
                    + responseCode);
    }


    /* ====================================================================== */
    /*                        JSON string interpreters                        */
    /* ====================================================================== */

    protected List<DeckMetadata> stringToDeckArray(String json) {
            List<DeckMetadata> deckList = new ArrayList<>();
            JsonArray jsonArray = new Gson().fromJson(json, JsonArray.class);
            for (int i = 0; i < jsonArray.size(); i++) {
                deckList.add(DeckMetadata.fromJson(jsonArray.get(i).getAsJsonObject()));
            }
            return deckList;
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
