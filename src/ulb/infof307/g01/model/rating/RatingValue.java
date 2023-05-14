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
        switch(value) {
        case 1:
            return ONE;
        case 2:
            return TWO;
        case 3:
            return THREE;
        case 4:
            return FOUR;
        case 5:
            return FIVE;
        }
        return null;
    }
}
