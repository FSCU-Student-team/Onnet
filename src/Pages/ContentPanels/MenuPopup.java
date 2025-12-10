package Pages.ContentPanels;

import Game.SoundHandler;
import javax.swing.*;
import java.awt.*;

public class MenuPopup extends JPanel {
    private JButton MainMenuBtn;
    private JButton muteBtn;
    private JButton levelsBtn;
    private JButton exitBtn;

    private Runnable onMainMenu;
    private Runnable onLevels;
    private Runnable onExit;

    public MenuPopup() {
        setLayout(new GridLayout(4, 1, 10, 10));
        setOpaque(true);
        setBackground(new Color(50, 50, 50, 200)); // Semi-transparent dark
        setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        initButtons();
        addListeners();
    }

    private void initButtons() {
        MainMenuBtn = createStyledButton("Main Menu");
        muteBtn = createStyledButton("Mute");
        levelsBtn = createStyledButton("Levels");
        exitBtn = createStyledButton("Exit");

        add(MainMenuBtn);
        add(muteBtn);
        add(levelsBtn);
        add(exitBtn);
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(70, 70, 70));
//        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        btn.setPreferredSize(new Dimension(150, 40));

        // Hover effects
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(90, 90, 90));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(70, 70, 70));
            }
        });

        return btn;
    }

    private void addListeners() {
        muteBtn.addActionListener(e -> {
            SoundHandler.toggleMute();
            updateMuteButtonText();
        });

        MainMenuBtn.addActionListener(e -> {
            if (onMainMenu != null) onMainMenu.run();
            hideMenu();
        });

        levelsBtn.addActionListener(e -> {
            if (onLevels != null) onLevels.run();
            hideMenu();
        });

        exitBtn.addActionListener(e -> {
            if (onExit != null) onExit.run();
        });
    }

    private void updateMuteButtonText() {
        muteBtn.setText(SoundHandler.isMuted() ? "Unmute" : "Mute");
    }

    public void showMenu(JPanel parent, int x, int y) {
        updateMuteButtonText();
        setBounds(x, y, 180, 250);
        parent.add(this);
        parent.revalidate();
        parent.repaint();
    }

    public void hideMenu() {
        Container parent = getParent();
        if (parent != null) {
            parent.remove(this);
            parent.revalidate();
            parent.repaint();
        }
    }
    // Setters for actions
    public void setOnMainMenu(Runnable r) { this.onMainMenu = r; }
    public void setOnLevels(Runnable r) { this.onLevels = r; }
    public void setOnExit(Runnable r) { this.onExit = r; }
}
