package Pages;

import Game.PageManager;

import javax.swing.*;
import java.awt.*;

public class LevelsPage implements Page {

    private JPanel panel;
    private JButton homeButton, level1Button;
    private Runnable homeAction, level1Action;

    public LevelsPage() {
        panel = new JPanel(new BorderLayout());
    }

    public JPanel getPanel() { return panel; }

    public void setHomeButtonAction(Runnable action) { this.homeAction = action; }
    public void setLevel1ButtonAction(Runnable action) { this.level1Action = action; }

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

        level1Button = new JButton("Level 1");
        level1Button.setPreferredSize(new Dimension(200, 60));
        homeButton = new JButton("Home");
        homeButton.setPreferredSize(new Dimension(150, 50));

        centerPanel.add(level1Button, c);
        c.gridy = 1;
        centerPanel.add(homeButton, c);

        panel.add(centerPanel, BorderLayout.CENTER);
    }

    @Override
    public void addListeners() {
        homeButton.addActionListener(e -> {
            if (homeAction != null) homeAction.run();
        });
        level1Button.addActionListener(e -> {
            if (level1Action != null) level1Action.run();
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
