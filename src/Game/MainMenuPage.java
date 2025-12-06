package Game;

import Pages.Page;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainMenuPage implements Page {
    private JPanel panel;
    private JButton newGameButton, levelsButton, continueButton, muteButton;
    private boolean isMuted = false;

    private Runnable newGameAction, levelsAction;

    public MainMenuPage() {
        panel = new JPanel(new BorderLayout());
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setNewGameButtonAction(Runnable action) {
        this.newGameAction = action;
    }

    public void setLevelsButtonAction(Runnable action) {
        this.levelsAction = action;
    }

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
        // Top panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        muteButton = new JButton("Mute");
        topPanel.add(muteButton);
        panel.add(topPanel, BorderLayout.NORTH);

        // Center buttons
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        newGameButton = new JButton("New Game");
        newGameButton.setPreferredSize(new Dimension(200, 100));
        levelsButton = new JButton("Levels");
        levelsButton.setPreferredSize(new Dimension(200, 100));
        continueButton = new JButton("Continue");
        continueButton.setPreferredSize(new Dimension(200, 100));
        gbc.gridy = 0;
        centerPanel.add(newGameButton, gbc);

        gbc.gridy = 1;
        centerPanel.add(levelsButton, gbc);
        gbc.gridy = 2;
        centerPanel.add(continueButton, gbc);

        panel.add(centerPanel, BorderLayout.CENTER);
    }

    @Override
    public void addListeners() {
        newGameButton.addActionListener(e -> {
            if (newGameAction != null) newGameAction.run();
        });
        levelsButton.addActionListener(e -> {
            if (levelsAction != null) levelsAction.run();
        });
        muteButton.addActionListener(e -> {
            isMuted = !isMuted;
            System.out.println("Mute toggled: " + isMuted);
        });
    }

    @Override
    public void handleEvents(ActionEvent e) {
    }

    @Override
    public void dispose() {
    }

    @Override
    public boolean isVisible() {
        return panel.isVisible();
    }

    @Override
    public void setVisible(boolean b) {
        panel.setVisible(b);
    }

    @Override
    public void redraw() {

    }
}
