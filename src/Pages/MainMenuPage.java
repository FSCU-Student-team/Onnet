package Pages;

import Game.PageManager;
import Game.SoundHandler;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainMenuPage implements Page {

    private final GLCanvas canvas;  // shared canvas
    private FPSAnimator animator;

    private JFrame frame;
    private JButton playBtn, levelsBtn, muteBtn;
    private Runnable onPlay, onLevels;

    public MainMenuPage(GLCanvas sharedCanvas) {
        this.canvas = sharedCanvas;
    }

    @Override
    public void init() {
        setupFrame();
        addComponents();
        addListeners();
        setupAnimator();

        // Force first draw after frame is visible
        SwingUtilities.invokeLater(canvas::display);
    }

    @Override
    public void setupFrame() {
        frame = new JFrame("Main Menu");
        frame.setSize(800, 600);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);
        PageManager.registerFrameCloseHandler(this, frame);
    }

    @Override
    public void setupAnimator() {
        if (animator == null && canvas != null) {
            animator = new FPSAnimator(canvas, 60);
            animator.start();
        }
    }

    @Override
    public void addComponents() {
        frame.add(canvas, BorderLayout.CENTER);
        addButtons();

        // Ensure buttons appear
        frame.getGlassPane().setVisible(true);
    }

    private void addButtons() {
        JPanel glass = (JPanel) frame.getGlassPane();
        glass.setLayout(null);
        glass.setOpaque(false);

        playBtn = new JButton("Play");
        levelsBtn = new JButton("Levels");
        muteBtn = new JButton("Mute");

        playBtn.setBounds(150, 200, 200, 120);
        levelsBtn.setBounds(450, 200, 200, 120);
        muteBtn.setBounds(670, 10, 100, 50);

        glass.add(playBtn);
        glass.add(levelsBtn);
        glass.add(muteBtn);
    }

    @Override
    public void addListeners() {
        playBtn.addActionListener(e -> { if (onPlay != null) onPlay.run(); });
        levelsBtn.addActionListener(e -> { if (onLevels != null) onLevels.run(); });
        muteBtn.addActionListener(e -> SoundHandler.toggleMute());
    }

    @Override
    public void handleEvents(ActionEvent e) {}

    @Override
    public void dispose() {
        if (animator != null && animator.isStarted()) animator.stop();
        frame.dispose();
    }

    @Override
    public boolean isVisible() { return frame.isVisible(); }

    @Override
    public void setVisible(boolean b) { frame.setVisible(b); }

    @Override
    public void redraw() { canvas.display(); }

    public void setPlayButtonAction(Runnable r) { this.onPlay = r; }
    public void setLevelsButtonAction(Runnable r) { this.onLevels = r; }
    public JFrame getFrame() { return frame; }
}