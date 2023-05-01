package ulb.infof307.g01.server.database;

class DatabaseSchema {

    private DatabaseSchema() {
        throw new IllegalStateException("Utility class");
    }

    protected static final String[] SERVER = new String[]{
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
            name TEXT NOT NULL,
            color TEXT NOT NULL,
            image TEXT NOT NULL,
            color_name TEXT NOT NULL,
            FOREIGN KEY (user_id)
                REFERENCES user(user_id)
                ON DELETE CASCADE
        );
            """,
            """
        CREATE TABLE IF NOT EXISTS card (
            card_id TEXT PRIMARY KEY,
            deck_id TEXT NOT NULL,
            front TEXT NOT NULL,
            FOREIGN KEY (deck_id)
                REFERENCES deck(deck_id)
                ON DELETE CASCADE
        );
            """,
            """
        CREATE TABLE IF NOT EXISTS flash_card (
            card_id TEXT PRIMARY KEY,
            back TEXT NOT NULL,
            FOREIGN KEY (card_id) REFERENCES card(card_id) ON DELETE CASCADE
        );
            """,
            """
        CREATE TABLE IF NOT EXISTS mcq_card (
            card_id TEXT PRIMARY KEY,
            correct_answer_index INTEGER NOT NULL,
            countdown_time INTEGER,
            FOREIGN KEY (card_id) REFERENCES card(card_id) ON DELETE CASCADE
        );
            """,
            """
        CREATE TABLE IF NOT EXISTS mcq_answer (
            card_id TEXT,
            answer_index INTEGER NOT NULL,
            answer TEXT NOT NULL,
            FOREIGN KEY (card_id) REFERENCES card(card_id) ON DELETE CASCADE,
            PRIMARY KEY (card_id, answer_index)
        );
            """,
            """
        CREATE TABLE IF NOT EXISTS input_card (
            card_id TEXT PRIMARY KEY,
            answer TEXT NOT NULL,
            countdown_time INTEGER,
            FOREIGN KEY (card_id) REFERENCES card(card_id) ON DELETE CASCADE
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
            downloads INTEGER NOT NULL,
            FOREIGN KEY (deck_id)
                REFERENCES deck(deck_id)
                ON DELETE CASCADE
        );
            """,
            """
        CREATE TABLE IF NOT EXISTS user_deck_score (
            user_id TEXT,
            timestamp INTEGER NOT NULL,
            deck_id TEXT NOT NULL,
            score INTEGER NOT NULL,
            PRIMARY KEY (user_id, timestamp),
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

