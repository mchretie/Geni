package ulb.infof307.g01.server.database;

public class DatabaseScheme {
    public static final String[] CLIENT = new String[]{
            """
        CREATE TABLE IF NOT EXISTS deck (
            deck_id TEXT PRIMARY KEY,
            name TEXT UNIQUE NOT NULL
        );""",

            """
        CREATE TABLE IF NOT EXISTS card (
            card_id TEXT PRIMARY KEY,
            deck_id TEXT,
            front TEXT,
            back TEXT,
            FOREIGN KEY (deck_id)
                REFERENCES Deck(deck_id)
                ON DELETE CASCADE
        );""",

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
                REFERENCES Deck(deck_id)
                ON DELETE CASCADE
            FOREIGN KEY (tag_id)
                REFERENCES Tag(tag_id)
        );""",

            """
        CREATE TABLE IF NOT EXISTS user (
            user_id TEXT PRIMARY KEY,
            username TEXT NOT NULL,
            password TEXT NOT NULL
        );""",

            """
        CREATE TABLE IF NOT EXISTS card_knowledge (
            user_id TEXT,
            card_id TEXT,
            knowledge INTEGER,
            PRIMARY KEY (user_id, card_id),
            FOREIGN KEY (user_id)
                REFERENCES User(user_id)
                ON DELETE CASCADE
            FOREIGN KEY (card_id)
                REFERENCES Card(card_id)
                ON DELETE CASCADE
        );"""
    };
}

