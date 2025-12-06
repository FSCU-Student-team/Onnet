package Pages;

import Game.PageManager;

import javax.swing.*;
import java.awt.*;

public class MainMenuPage implements Page {

    private JPanel panel;
    private JButton playButton, levelsButton;
    private Runnable playAction, levelsAction; // مهم جداً

    public MainMenuPage() {
        panel = new JPanel(new BorderLayout());
    }

    public JPanel getPanel() { return panel; }

    public void setPlayButtonAction(Runnable action) { this.playAction = action; }
    public void setLevelsButtonAction(Runnable action) { this.levelsAction = action; }

    @Override
    public void init() {
        addComponents();
        addListeners();
    }

    @Override
    public void addComponents() {
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(20, 0, 20, 0);
        c.gridy = 0;

        playButton = new JButton("Play");
        playButton.setPreferredSize(new Dimension(200, 60));
        levelsButton = new JButton("Levels");
        levelsButton.setPreferredSize(new Dimension(200, 60));

        centerPanel.add(playButton, c);
        c.gridy = 1;
        centerPanel.add(levelsButton, c);

        panel.add(centerPanel, BorderLayout.CENTER);
    }

    @Override
    public void addListeners() {
        playButton.addActionListener(e -> {
            if (playAction != null) playAction.run();
        });
        levelsButton.addActionListener(e -> {
            if (levelsAction != null) levelsAction.run();
        });
    }

    @Override public void setupFrame() {}
    @Override public void setupAnimator() {}
    @Override public void handleEvents(java.awt.event.ActionEvent e) {}
    @Override public void dispose() {}
    @Override public boolean isVisible() { return panel.isVisible(); }
    @Override public void setVisible(boolean b) { panel.setVisible(b); }
    @Override public void redraw() { panel.repaint(); }
}
