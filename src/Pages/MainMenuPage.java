package Pages;

import Game.PageManager;
import Game.SoundHandler;
import Renderers.MenuBackground;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainMenuPage implements Page {

    private JFrame frame;
    private JButton playBtn, levelsBtn, muteBtn, backBtn;
    private Runnable onPlay, onLevels, onMute, onBack;

    private GLJPanel canvas;
    private FPSAnimator animator;

    @Override
    public void init() {
        setupFrame();
        addComponents();
        addListeners();
        setupAnimator();
    }

    @Override
    public void setupFrame() {
        frame = new JFrame("Single Player Menu");
        frame.setSize(800, 600);
        frame.setResizable(false); // disable resizing
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        PageManager.registerFrameCloseHandler(this, frame);
        frame.setLocationRelativeTo(null);
    }

    @Override
    public void setupAnimator() {
        if (canvas != null) {
            animator = new FPSAnimator(canvas, 60);
            animator.start();
        }
    }

    @Override
    public void addComponents() {
        // 1. GLJPanel for background
        canvas = new GLJPanel();
        MenuBackground renderer = new MenuBackground();
        canvas.addGLEventListener(renderer);
        canvas.addKeyListener(renderer.inputManager);
        canvas.addMouseListener(renderer.inputManager);
        canvas.addMouseMotionListener(renderer.inputManager);
        canvas.setBounds(0, 0, 800, 600);

        // 2. Buttons panel
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false); // transparent
        buttonPanel.setBounds(0, 0, 800, 600);

        // Buttons
        playBtn = new JButton("Play");
        levelsBtn = new JButton("Levels");
        muteBtn = new JButton("Mute");
        backBtn = new JButton("Back");

        Dimension btnSize = new Dimension(200, 120);
        playBtn.setPreferredSize(btnSize);
        levelsBtn.setPreferredSize(btnSize);
        muteBtn.setPreferredSize(btnSize);
        backBtn.setPreferredSize(btnSize);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 50, 0, 50);

        // Center row: Play and Levels
        gbc.gridy = 0;
        gbc.gridx = 0;
        buttonPanel.add(playBtn, gbc);
        gbc.gridx = 1;
        buttonPanel.add(levelsBtn, gbc);

        // Top-right corner buttons
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.insets = new Insets(10, 0, 0, 10);
        gbc.gridx = 1;
        gbc.gridy = 1;
        buttonPanel.add(muteBtn, gbc);

        gbc.gridy = 2;
        buttonPanel.add(backBtn, gbc);

        // 3. LayeredPane
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(800, 600));
        layeredPane.setLayout(null);

        layeredPane.add(canvas, Integer.valueOf(0));      // bottom
        layeredPane.add(buttonPanel, Integer.valueOf(1)); // top

        frame.setContentPane(layeredPane);
    }

    @Override
    public void addListeners() {
        playBtn.addActionListener(e -> { if (onPlay != null) onPlay.run(); });
        levelsBtn.addActionListener(e -> { if (onLevels != null) onLevels.run(); });
        muteBtn.addActionListener(e -> { if (onMute != null) onMute.run(); });
        backBtn.addActionListener(e -> { if (onBack != null) onBack.run(); });
    }

    @Override
    public void handleEvents(ActionEvent e) {}

    @Override
    public void dispose() {
        if (animator != null && animator.isStarted()) animator.stop();
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
        if (canvas != null) canvas.repaint();
    }

    // Button action setters
    public void setPlayButtonAction(Runnable r) { this.onPlay = r; }
    public void setLevelsButtonAction(Runnable r) { this.onLevels = r; }
    public void setMuteButtonAction(Runnable r) {
        if (SoundHandler.isMuted()) SoundHandler.unmute();
        else SoundHandler.mute();
    }
    public void setBackBtnAction(Runnable r) { this.onBack = r; }

    public JFrame getFrame() { return frame; }
}
