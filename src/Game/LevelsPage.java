package Game;

import Pages.Page;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LevelsPage implements Page {
    private JPanel panel;
    private JButton homeButton;
    private JButton[] levelButtons = new JButton[10];
    private PageManager pageManager;
    private MainMenuPage mainMenu;

    public LevelsPage(MainMenuPage mainMenu) {
        panel = new JPanel(new BorderLayout());
        this.mainMenu = mainMenu;
    }

    public JPanel getPanel() { return panel; }

    @Override
    public void init() {
        addComponents();
        addListeners();
    }

    @Override
    public void setupFrame() {

    }

    @Override
    public void setupAnimator() {

    }

    @Override
    public void addComponents() {
        // Home button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        homeButton = new JButton("Home");
        topPanel.add(homeButton);
        panel.add(topPanel, BorderLayout.NORTH);

        // Levels
        JPanel centerPanel = new JPanel(new GridLayout(2, 5, 20, 20));
        for (int i = 0; i < 10; i++) {
            int level = i + 1;
            levelButtons[i] = new JButton("Level " + level);
            centerPanel.add(levelButtons[i]);
        }
        panel.add(centerPanel, BorderLayout.CENTER);
    }

    @Override
    public void addListeners() {
        homeButton.addActionListener(e -> PageManager.switchPage(this, mainMenu));
        for (int i = 0; i < 10; i++) {
            int level = i + 1;
            levelButtons[i].addActionListener(e -> {
                System.out.println("Starting Level " + level);
            });
        }
    }

    @Override
    public void handleEvents(ActionEvent e) {}

    @Override
    public void dispose() {}

    @Override
    public boolean isVisible() { return panel.isVisible(); }

    @Override
    public void setVisible(boolean b) { panel.setVisible(b); }

    @Override
    public void redraw() {

    }
}
