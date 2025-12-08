package Pages;

import Game.PageManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainMenuPage implements Page {

    private JFrame frame;
    private JButton playBtn, levelsBtn, muteBtn,backBtn;
    private Runnable onPlay, onLevels, onMute, onBack;

    @Override
    public void init() {
        setupFrame();
        addComponents();
        addListeners();
    }

    @Override
    public void setupFrame() {
        frame = new JFrame("Main Menu");
        frame.setSize(800, 600);
        frame.setLayout(new GridLayout(3, 1, 10, 10));
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(true);
        frame.setMinimumSize(new Dimension(800, 600));


        PageManager.registerFrameCloseHandler(this, frame);
    }

    @Override
    public void setupAnimator() {
    }

    @Override
    public void addComponents() {
        playBtn = new JButton("Play");
        playBtn.setPreferredSize(new Dimension(200, 120));

        levelsBtn = new JButton("Levels");
        levelsBtn.setPreferredSize(new Dimension(200, 120));
        backBtn = new JButton("Back");
        backBtn.setPreferredSize(new Dimension(200, 120));

        muteBtn = new JButton("Mute");
        muteBtn.setPreferredSize(new Dimension(200, 120));

        frame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        playBtn = new JButton("Play");
        playBtn.setPreferredSize(new Dimension(200, 120));
        playBtn.setMinimumSize(new Dimension(200, 120));
        playBtn.setMaximumSize(new Dimension(200, 120));

        levelsBtn = new JButton("Levels");
        levelsBtn.setPreferredSize(new Dimension(200, 120));
        levelsBtn.setMinimumSize(new Dimension(200, 120));
        levelsBtn.setMaximumSize(new Dimension(200, 120));

        frame.setLayout(new GridBagLayout());


        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;

// زرار Play
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 200, 0, 200);
        frame.add(playBtn, gbc);

// زرار Levels
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 200, 0, 200);
        frame.add(levelsBtn, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.insets = new Insets(10, 400, 800, 10); // top, left, bottom, right
        frame.add(muteBtn, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.insets = new Insets(10, 400, 800, 100); // top, left, bottom, right
        frame.add(backBtn, gbc);

    }

    @Override
    public void addListeners() {
        playBtn.addActionListener(e -> {
            if (onPlay != null) onPlay.run();
        });

        levelsBtn.addActionListener(e -> {
            if (onLevels != null) onLevels.run();
        });

        backBtn.addActionListener(e -> {
            if (onBack != null) onBack.run();
        });

    }

    @Override
    public void handleEvents(ActionEvent e) {
    }

    @Override
    public void dispose() {
        frame.dispose();
    }

    @Override
    public boolean isVisible() {
        return frame.isVisible();
    }

    @Override
    public void setVisible(boolean b) {
        frame.setVisible(b);
    }

    @Override
    public void redraw() {
    }

    public void setPlayButtonAction(Runnable r) {
        this.onPlay = r;
    }

    public void setLevelsButtonAction(Runnable r) {
        this.onLevels = r;

    }
   public void setBackBtnAction(Runnable r) {
        this.onBack = r;
   }
    public JFrame getFrame() {
        return frame;
    }

}
