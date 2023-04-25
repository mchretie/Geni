package ulb.infof307.g01.model.leaderboard;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GlobalLeaderboardTest {

    @Test
    void getUserScore() {
        GlobalLeaderboardEntry entry
                = new GlobalLeaderboardEntry(100, "cutie");

        GlobalLeaderboardEntry entry2
                = new GlobalLeaderboardEntry(200, "cutie2");

        GlobalLeaderboardEntry entry3
                = new GlobalLeaderboardEntry(300, "cutie3");

        List<GlobalLeaderboardEntry> entries = new ArrayList<>();
        entries.add(entry);
        entries.add(entry2);
        entries.add(entry3);

        GlobalLeaderboard leaderboard = new GlobalLeaderboard(entries);

        assertEquals("100", leaderboard.getUserScore("cutie"));
        assertEquals("200", leaderboard.getUserScore("cutie2"));
        assertEquals("300", leaderboard.getUserScore("cutie3"));
    }

    @Test
    void getUserRank() {
        GlobalLeaderboardEntry entry
                = new GlobalLeaderboardEntry(100, "cutie");

        GlobalLeaderboardEntry entry2
                = new GlobalLeaderboardEntry(200, "cutie2");

        GlobalLeaderboardEntry entry3
                = new GlobalLeaderboardEntry(300, "cutie3");

        List<GlobalLeaderboardEntry> entries = new ArrayList<>();
        entries.add(entry);
        entries.add(entry2);
        entries.add(entry3);

        GlobalLeaderboard leaderboard = new GlobalLeaderboard(entries);

        assertEquals("3", leaderboard.getUserRank("cutie"));
        assertEquals("2", leaderboard.getUserRank("cutie2"));
        assertEquals("1", leaderboard.getUserRank("cutie3"));
    }

    @Test
    void forEach() {
        GlobalLeaderboardEntry entry
                = new GlobalLeaderboardEntry(100, "cutie");

        GlobalLeaderboardEntry entry2
                = new GlobalLeaderboardEntry(200, "cutie2");

        GlobalLeaderboardEntry entry3
                = new GlobalLeaderboardEntry(300, "cutie3");

        List<GlobalLeaderboardEntry> entries = new ArrayList<>();
        entries.add(entry);
        entries.add(entry2);
        entries.add(entry3);

        GlobalLeaderboard leaderboard = new GlobalLeaderboard(entries);

        int i = 1;
        for (GlobalLeaderboardEntry e : leaderboard) {
            assertEquals(i + "", e.getRank());
            ++i;
        }
    }
}