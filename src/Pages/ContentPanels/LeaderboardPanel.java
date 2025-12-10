package Pages.ContentPanels;

import Game.LeaderboardHandler;
import Game.LeaderboardEntry;
import com.jogamp.opengl.awt.GLJPanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LeaderboardPanel extends JPanel {

    private final GLJPanel sharedCanvas;
    private final JPanel topButtonPanel; // Level buttons + back
    private final JPanel leaderboardDisplayPanel; // Shows leaderboard entries
    private final JButton backButton;
    private Runnable onBack;

    private int currentLevel = 1; // default level
    private static final int MAX_ENTRIES_DISPLAYED = 10; // top 10

    public LeaderboardPanel(GLJPanel canvas) {
        this.sharedCanvas = canvas;
        setLayout(new BorderLayout());
        setOpaque(false);

        // Top panel with back + level buttons using GridLayout (2 rows, 7 columns)
        topButtonPanel = new JPanel(new GridLayout(2, 7, 5, 5)); // 2 rows, 7 cols, 5px gaps
        topButtonPanel.setOpaque(false);
        add(topButtonPanel, BorderLayout.NORTH);

        // Back button first
        backButton = new JButton("Back");
        backButton.setBackground(new Color(200, 50, 50));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> {
            if (onBack != null) onBack.run();
        });
        topButtonPanel.add(backButton);

        // Add level buttons 1–12
        for (int i = 1; i <= 12; i++) {
            int level = i;
            JButton btn = new JButton("Level " + i);
            btn.setFocusPainted(false);
            btn.setBackground(new Color(50, 150, 250));
            btn.setForeground(Color.WHITE);
            btn.addActionListener(e -> loadLevel(level));
            topButtonPanel.add(btn);
        }

        // Leaderboard panel (fixed size to fit top 10)
        leaderboardDisplayPanel = new JPanel();
        leaderboardDisplayPanel.setLayout(new BoxLayout(leaderboardDisplayPanel, BoxLayout.Y_AXIS));
        leaderboardDisplayPanel.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(leaderboardDisplayPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER); // no scrollbar
        add(scrollPane, BorderLayout.CENTER);

        // Load default level
        loadLevel(currentLevel);
    }

    /** Sets what happens when Back button is pressed */
    public void setBackAction(Runnable r) {
        this.onBack = r;
    }

    /** Loads a specific level's leaderboard */
    private void loadLevel(int level) {
        this.currentLevel = level;
        leaderboardDisplayPanel.removeAll();

        List<LeaderboardEntry> entries = LeaderboardHandler.load(level);

        // Only take top 10
        int displayedCount = Math.min(MAX_ENTRIES_DISPLAYED, !entries.isEmpty() ? entries.size() : 1);

        // Calculate row height dynamically
        int availableHeight = 600 - topButtonPanel.getPreferredSize().height - 20; // frame height minus top panel
        int rowHeight = availableHeight / (displayedCount + 1); // +1 for header

        if (entries.isEmpty()) {
            JLabel emptyLabel = new JLabel("No entries yet for Level " + level);
            emptyLabel.setFont(new Font("Monospaced", Font.BOLD, 16));
            emptyLabel.setForeground(Color.darkGray);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            leaderboardDisplayPanel.add(Box.createVerticalStrut(rowHeight));
            leaderboardDisplayPanel.add(emptyLabel);
        } else {
            // Header row
            JPanel header = createRow("Name", "Score", true, rowHeight);
            leaderboardDisplayPanel.add(header);

            // Rows
            for (int i = 0; i < displayedCount; i++) {
                LeaderboardEntry entry = entries.get(i);
                JPanel row = createRow(entry.name(), String.valueOf((int) entry.score()), false, rowHeight);
                leaderboardDisplayPanel.add(row);
            }
        }

        leaderboardDisplayPanel.revalidate();
        leaderboardDisplayPanel.repaint();
    }

    /** Helper method to create a leaderboard row */
    private JPanel createRow(String name, String score, boolean isHeader, int height) {
        JPanel row = new JPanel(new GridLayout(1, 2));
        row.setPreferredSize(new Dimension(800, height));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.DARK_GRAY));
        row.setBackground(isHeader ? new Color(30, 30, 30) : new Color(50, 50, 50));

        JLabel nameLabel = new JLabel(name);
        nameLabel.setHorizontalAlignment(SwingConstants.LEFT);
        nameLabel.setForeground(isHeader ? Color.YELLOW : Color.WHITE);
        nameLabel.setFont(new Font("Monospaced", isHeader ? Font.BOLD : Font.PLAIN, 16));
        nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

        JLabel scoreLabel = new JLabel(score);
        scoreLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        scoreLabel.setForeground(isHeader ? Color.YELLOW : Color.CYAN);
        scoreLabel.setFont(new Font("Monospaced", isHeader ? Font.BOLD : Font.PLAIN, 16));
        scoreLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 50));

        row.add(nameLabel);
        row.add(scoreLabel);
        return row;
    }

    public GLJPanel getCanvas() {
        return sharedCanvas;
    }
}
