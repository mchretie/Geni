package ulb.infof307.g01.gui.httpclient.dao;

import ulb.infof307.g01.gui.httpclient.HttpClientAPI;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.UUID;

public class UserDAO extends HttpClientAPI {

    private final UUID guestUUID
            = UUID.fromString("00000000-0000-0000-0000-000000000000");


    private String token;


    /* ====================================================================== */
    /*                               DAO methods                              */
    /* ====================================================================== */

    public UUID getGuestUUID() {
        return guestUUID;
    }

    public void login(String username, String password)
            throws IOException, InterruptedException {

        HttpResponse<String> response = post("/api/user/login",
                "username=" + username + "&password=" + password);

        checkResponseCode(response.statusCode());

    }

    public void registerGuest() throws IOException, InterruptedException {

        String query = "?guest_id=" + guestUUID;
        HttpResponse<String> response = get("/api/guest/register" + query);

        checkResponseCode(response.statusCode());
    }
}
