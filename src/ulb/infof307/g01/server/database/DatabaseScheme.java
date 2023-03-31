package ulb.infof307.g01.server.database;

class DatabaseScheme {

    public static final String[] SERVER = new String[]{
            """
        CREATE TABLE IF NOT EXISTS user (
            user_id TEXT PRIMARY KEY,
            username TEXT NOT NULL,
            password TEXT NOT NULL,
            salt TEXT NOT NULL
        );
            """,
            """
        CREATE TABLE IF NOT EXISTS deck (
            deck_id TEXT PRIMARY KEY,
            user_id TEXT,
            name TEXT UNIQUE NOT NULL,
            color TEXT NOT NULL,
            FOREIGN KEY (user_id)
                REFERENCES user(user_id)
                ON DELETE CASCADE
        );
            """,
            // TODO: 29/03/23 replace with content instead of front and back
            """
        CREATE TABLE IF NOT EXISTS card (
            card_id TEXT PRIMARY KEY,
            deck_id TEXT NOT NULL,
            front TEXT NOT NULL,
            back TEXT NOT NULL,
            FOREIGN KEY (deck_id)
                REFERENCES deck(deck_id)
                ON DELETE CASCADE
        );
            """,
            """
        CREATE TABLE IF NOT EXISTS tag (
            tag_id TEXT PRIMARY KEY,
            name TEXT UNIQUE NOT NULL,
            color TEXT NOT NULL
        );""",
            """
        CREATE TABLE IF NOT EXISTS deck_tag (
            deck_id TEXT,
            tag_id TEXT,
            PRIMARY KEY (deck_id, tag_id),
            FOREIGN KEY (deck_id)
                REFERENCES deck(deck_id)
                ON DELETE CASCADE
            FOREIGN KEY (tag_id)
                REFERENCES Tag(tag_id)
        );
            """,
            """
        CREATE TABLE IF NOT EXISTS marketplace (
            deck_id TEXT PRIMARY KEY,
            rating INTEGER,
            download INTEGER NOT NULL,
            FOREIGN KEY (deck_id)
                REFERENCES deck(deck_id)
                ON DELETE CASCADE
        );
            """,
            """
        CREATE TABLE IF NOT EXISTS user_deck_score (
            user_id TEXT,
            deck_id TEXT,
            timestamp TEXT NOT NULL,
            score INTEGER NOT NULL,
            PRIMARY KEY (user_id, deck_id),
            FOREIGN KEY (user_id)
                REFERENCES user(user_id)
                ON DELETE CASCADE,
            FOREIGN KEY (deck_id)
                REFERENCES deck(deck_id)
                ON DELETE CASCADE
        );
            """,
            """
        CREATE TABLE IF NOT EXISTS user_deck_collection (
            user_id TEXT,
            deck_id TEXT,
            PRIMARY KEY (user_id, deck_id),
            FOREIGN KEY (user_id)
                REFERENCES user(user_id)
                ON DELETE CASCADE,
            FOREIGN KEY (deck_id)
                REFERENCES deck(deck_id)
                ON DELETE CASCADE
        );
            """
    };
}

