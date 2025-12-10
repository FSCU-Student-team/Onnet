package Game;

import java.io.*;
import java.util.*;

public class LeaderboardHandler {
    private static final String LEADERBOARD_DIR = "Leaderboards/";

    /**
     * Loads the leaderboard for a specific level.
     * Returns a sorted list (highest score first).
     */
    public static List<LeaderboardEntry> load(int level) {
        List<LeaderboardEntry> leaderboard = new ArrayList<>();
        String path = LEADERBOARD_DIR + "level" + level + ".txt";
        File file = new File(path);
        if (!file.exists()) return leaderboard; // return empty if file doesn't exist

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            if (line != null && !line.isEmpty()) {
                String[] entries = line.split(",");
                for (String entry : entries) {
                    String[] parts = entry.split(":");
                    if (parts.length == 2) {
                        String name = parts[0];
                        double score = Double.parseDouble(parts[1]);
                        leaderboard.add(new LeaderboardEntry(name, score));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        // Sort descending by score
        leaderboard.sort((a, b) -> Double.compare(b.score(), a.score()));

        return leaderboard;
    }

    /**
     * Saves a new entry to the leaderboard for a specific level.
     * Keeps the leaderboard sorted and overwrites the file.
     */
    public static void save(int level, LeaderboardEntry newEntry) {
        List<LeaderboardEntry> leaderboard = load(level); // load existing
        leaderboard.add(newEntry);                        // add new entry
        leaderboard.sort((a, b) -> Double.compare(b.score(), a.score())); // descending

        // Write back to file
        String path = LEADERBOARD_DIR + "level" + level + ".txt";
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            for (int i = 0; i < leaderboard.size(); i++) {
                pw.print(leaderboard.get(i).toString());
                if (i < leaderboard.size() - 1) pw.print(",");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());;
        }
    }
}
