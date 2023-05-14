package ulb.infof307.g01.model.rating;

import java.util.UUID;
import com.google.gson.Gson;

public record UserRating(UUID deckId, UUID userId, RatingValue value) {
    public static RatingValue DEFAULT_VALUE = RatingValue.THREE;

    public static UserRating fromJson(String jsonString) {
        return new Gson().fromJson(jsonString, UserRating.class);
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}
