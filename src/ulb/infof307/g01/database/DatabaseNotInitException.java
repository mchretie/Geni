package ulb.infof307.g01.database;

class DatabaseNotInitException extends RuntimeException {
    public DatabaseNotInitException(String msg) {
        super(msg);
    }
}
