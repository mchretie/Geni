package ulb.infof307.g01.server.database.dao;

import ulb.infof307.g01.model.Score;
import ulb.infof307.g01.server.database.DatabaseAccess;
import ulb.infof307.g01.server.database.exceptions.DatabaseException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ScoreDAO extends DAO {
    private final DatabaseAccess database;

    public ScoreDAO(DatabaseAccess database) {
        this.database = database;
    }

    public void addScore(Score score) {
        String sql = """
                INSERT INTO user_deck_score (user_id, timestamp, deck_id, score)
                VALUES (?, ?, ?, ?)
                """;

        database.executeUpdate(sql,
                score.getUserId(),
                score.getTimestamp().getTime(),
                score.getDeckId(),
                score.getScore());
    }

    private Score extractScore(ResultSet res) {
        try {
            UUID userId = UUID.fromString(res.getString("user_id"));
            UUID deckId = UUID.fromString(res.getString("deck_id"));
            int score = res.getInt("score");
            Date date = new Date(res.getLong("timestamp"));
            return new Score(userId, deckId, score, date);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public List<Score> getScoreFromDeckId(String deckId) throws DatabaseException {
        String sql = """
                SELECT *
                FROM user_deck_score
                WHERE deckId = ?
                """;

        ResultSet res = database.executeQuery(sql, deckId);
        try {
            List<Score> scores = new ArrayList<>();
            while (res.next()) {
                scores.add(extractScore(res));
            }
            return scores;
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public List<Score> getScoreFromUserId(String userId) throws DatabaseException {
        String sql = """
                SELECT *
                FROM user_deck_score
                WHERE userId = ?
                """;

        ResultSet res = database.executeQuery(sql, userId);
        try {
            List<Score> scores = new ArrayList<>();
            while (res.next()) {
                scores.add(extractScore(res));
            }
            return scores;
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
