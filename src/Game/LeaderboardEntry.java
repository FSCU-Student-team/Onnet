package Game;

public record LeaderboardEntry(String name, double score) {
    @Override
    public String toString() {
        // if the score is a whole number, print as integer
        return name + ":" + (int) score;
    }

}
