package ulb.infof307.g01.server.database.dao;

import ulb.infof307.g01.model.Score;
import ulb.infof307.g01.server.database.DatabaseAccess;
import ulb.infof307.g01.server.database.exceptions.DatabaseException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import javafx.util.Pair;

public class ScoreDAO extends DAO {
    private final DatabaseAccess database;
    private UserDAO userDAO;

    public ScoreDAO(DatabaseAccess database) {
        this.database = database;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void addScore(Score score) throws DatabaseException {
        String sql = """
                INSERT INTO user_deck_score (user_id, timestamp, deck_id, score)
                VALUES (?, ?, ?, ?)
                """;

        String userId = userDAO.getUserId(score.getUsername());
        database.executeUpdate(sql,
                userId,
                Long.toString(score.getTimestamp().getTime()),
                score.getDeckId().toString(),
                score.getScore());
    }

    private Score extractScore(ResultSet res) throws DatabaseException {
        try {
            UUID userId = UUID.fromString(res.getString("user_id"));
            UUID deckId = UUID.fromString(res.getString("deck_id"));
            int score = res.getInt("score");
            Date date = new Date(res.getLong("timestamp"));
            String username = userDAO.getUsername(userId);
            return new Score(username, deckId, score, date);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public List<Score> getScoresForDeck(UUID deckId) throws DatabaseException {
        String sql = """
                SELECT *
                FROM user_deck_score
                WHERE deck_id = ?
                """;

        try {
            ResultSet res = database.executeQuery(sql, deckId.toString());
            List<Score> scores = new ArrayList<>();
            while (res.next()) {
                scores.add(extractScore(res));
            }
            return scores;
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public List<Score> getScoresForUser(UUID userId) throws DatabaseException {
        String sql = """
                SELECT *
                FROM user_deck_score
                WHERE user_id = ?
                """;

        try {
            ResultSet res = database.executeQuery(sql, userId.toString());
            List<Score> scores = new ArrayList<>();
            while (res.next()) {
                scores.add(extractScore(res));
            }
            return scores;
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public List<Pair<String, Integer>> getAllUserDeckScore() throws DatabaseException{
        String sql = """
                SELECT U.username, sum(score) as total_score
                FROM user_deck_score S, user U
                WHERE U.user_id = S.user_id
                GROUP BY U.username;
                """;

        try {
            ResultSet res = database.executeQuery(sql);
            List<Pair<String, Integer>> leaderboard = new ArrayList<>();
            while (res.next()) {
                leaderboard.add(new Pair<>(
                        res.getString("username"),
                        res.getInt("total_score")));
            }
            leaderboard.sort(Comparator.comparing(Pair<String, Integer>::getValue).reversed());
            System.out.println("leaderboard: " + leaderboard);
            return leaderboard;
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }

    }
}
