package ulb.infof307.g01.model.rating;

public enum RatingValue {
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE;

    public int asInt() {
        return ordinal() + 1;
    }

    public static RatingValue fromInt(int value) {
        return switch (value) {
            case 1 -> ONE;
            case 2 -> TWO;
            case 3 -> THREE;
            case 4 -> FOUR;
            case 5 -> FIVE;
            default -> null;
        };
    }
}
