package ulb.infof307.g01.model.leaderboard;

public class GlobalLeaderboardEntry implements Comparable<GlobalLeaderboardEntry> {

    private final int totalScore;
    private final String username;
    private String rank = "Pas de rang";

    public GlobalLeaderboardEntry(int totalScore, String username) {
        this.totalScore = totalScore;
        this.username = username;
    }

    public String getTotalScore() {
        return totalScore + "";
    }

    public String getUsername() {
        return username;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    @Override
    public int compareTo(GlobalLeaderboardEntry o) {
        return this.totalScore - o.totalScore;
    }
}
