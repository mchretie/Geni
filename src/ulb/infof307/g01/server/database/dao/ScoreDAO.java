package ulb.infof307.g01.server.database.dao;

import ulb.infof307.g01.model.deck.Score;
import ulb.infof307.g01.model.gamehistory.Game;
import ulb.infof307.g01.model.leaderboard.GlobalLeaderboardEntry;
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

    public void deleteScoresForDeck(UUID deckId) throws DatabaseException {
        String sql = """
                DELETE FROM user_deck_score
                WHERE deck_id = ?;
                """;

        database.executeUpdate(sql,
                deckId.toString());
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

    public List<GlobalLeaderboardEntry> getAllUserDeckScore() throws DatabaseException{
        String sql = """
                SELECT U.username, sum(score) as total_score
                FROM user_deck_score S, user U
                WHERE U.user_id = S.user_id
                GROUP BY U.username;
                """;

        try {
            ResultSet res = database.executeQuery(sql);
            List<GlobalLeaderboardEntry> leaderboard = new ArrayList<>();
            while (res.next()) {
                leaderboard.add(
                        new GlobalLeaderboardEntry(
                                res.getInt("total_score"),
                                res.getString("username")
                        ));
            }

            return leaderboard;

        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }

    }

    private List<Game> getGames(ResultSet res) throws SQLException {
        List<Game> games = new ArrayList<>();

        while (res.next()) {
            String deckName = res.getString("deck_name");
            int score = res.getInt("score");
            Date date = new Date(res.getLong("timestamp"));

            games.add(new Game(date, deckName, score + ""));
        }

        return games;
    }

    public List<Game> getGameHistory(UUID userId) {
        String sql = """
                SELECT d.name AS deck_name, s.score, s.timestamp
                FROM deck d
                INNER JOIN user_deck_score s ON d.deck_id = s.deck_id
                WHERE s.user_id = ?;
                """;

        try (ResultSet res = database.executeQuery(sql, userId.toString())) {
            return getGames(res);

        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public List<Game> getGameHistory(UUID userId, UUID deckId) {
        String sql = """
                SELECT d.name AS deck_name, s.score, s.timestamp
                FROM deck d
                INNER JOIN user_deck_score s ON d.deck_id = s.deck_id
                WHERE s.user_id = ? AND s.deck_id = ?;
                """;

        try (ResultSet res = database.executeQuery(sql, userId.toString(), deckId.toString())) {
            return getGames(res);

        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
