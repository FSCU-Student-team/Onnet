package Pages.ContentPanels;

import Game.SoundHandler;

import javax.swing.*;
import java.awt.*;

public class InMenuPopup extends JPanel {
    private JButton leaderboardBtn;
    private JButton muteBtn;
    private JButton levelsBtn;
    private JButton exitBtn;
    private JButton resumeBtn;
    private JButton mainMenuBtn;

    private Runnable onLeaderboard;
    private Runnable onLevels;
    private Runnable onExit;
    private Runnable onResume;
    private Runnable onMainMenu;

    private boolean isVisible = false;

//    private Component previousFocusOwner; // Store who had focus before menu opened
    private Component gameCanvas; // Add this

    public InMenuPopup() {
        setLayout(new GridLayout(6, 1, 10, 10));
        setOpaque(true);
        setBackground(new Color(50, 50, 50, 230)); // Semi-transparent dark
        setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        setBounds(250, 150, 300, 400); // Center position

        initButtons();
        addListeners();
        setVisible(false);
    }

    private void initButtons() {
        leaderboardBtn = createStyledButton("Leaderboard");
        muteBtn = createStyledButton("Mute");
        levelsBtn = createStyledButton("Levels");
        mainMenuBtn = createStyledButton("Main Menu");
        resumeBtn = createStyledButton("Resume / Hide Menu");
        exitBtn = createStyledButton("Exit");

        add(leaderboardBtn);
        add(muteBtn);
        add(levelsBtn);
        add(mainMenuBtn);
        add(resumeBtn);
        add(exitBtn);
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(70, 70, 70));
        btn.setFocusPainted(false);
        btn.setFocusable(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        btn.setPreferredSize(new Dimension(280, 50));
        btn.setFocusTraversalKeysEnabled(false);
        btn.setRequestFocusEnabled(false);

        // Clear all keyboard mappings
        btn.getInputMap().clear();
        btn.getActionMap().clear();

        // Mouse hover effects only
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(90, 90, 90));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(70, 70, 70));
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(110, 110, 110));
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(90, 90, 90));
            }
        });

        return btn;
    }

    private void addListeners() {
        muteBtn.addActionListener(e -> {
            SoundHandler.toggleMute();
            updateMuteButtonText();
        });

        leaderboardBtn.addActionListener(e -> {
            if (onLeaderboard != null) onLeaderboard.run();
            hideMenu();
        });

        levelsBtn.addActionListener(e -> {
            if (onLevels != null) onLevels.run();
            hideMenu();
        });

        mainMenuBtn.addActionListener(e -> {
            if (onMainMenu != null) onMainMenu.run();
            hideMenu();
        });

        resumeBtn.addActionListener(e -> {
            if (onResume != null) onResume.run();
            hideMenu();
        });

        exitBtn.addActionListener(e -> {
            if (onExit != null) onExit.run();
        });
    }

    private void updateMuteButtonText() {
        muteBtn.setText(SoundHandler.isMuted() ? "Unmute" : "Mute");
    }

    public void showMenu(JPanel parent) {
//        previousFocusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
        updateMuteButtonText();
        parent.add(this, Integer.valueOf(999)); // Top layer
        setVisible(true);
        parent.revalidate();
        parent.repaint();
        isVisible = true;
        if (gameCanvas != null) gameCanvas.setFocusable(false);
    }

    public void hideMenu() {
        if (isVisible) {
            setVisible(false);
            Container parent = getParent();
            if (parent != null) {
                parent.remove(this);
                parent.revalidate();
                parent.repaint();
            }
            isVisible = false;
        }

        if (gameCanvas != null) {
            gameCanvas.setFocusable(true);
            gameCanvas.requestFocusInWindow();
        }
    }

    public void toggleMenu(JPanel parent) {
        if (isVisible) {
            hideMenu();
        } else {
            showMenu(parent);
        }
    }

    // Setters for actions
    public void setOnLeaderboard(Runnable r) { this.onLeaderboard = r; }
    public void setOnLevels(Runnable r) { this.onLevels = r; }
    public void setOnExit(Runnable r) { this.onExit = r; }
    public void setOnMainMenu(Runnable r) { this.onMainMenu = r; }
    public void setGameCanvas(Component canvas) { this.gameCanvas = canvas; }

    public boolean isMenuVisible() { return isVisible; }

    public void setOnResume(Runnable onResume) {
        this.onResume = onResume;
    }
}
