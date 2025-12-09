package Pages;

import Game.PageManager;
import Game.SoundHandler;
import Renderers.MenuBackground;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainMenuPage implements Page {

    private JFrame frame;
    private JButton playBtn, levelsBtn, muteBtn;
    private Runnable onPlay, onLevels;

    private GLCanvas canvas;
    private FPSAnimator animator;

    @Override
    public void init() {
        setupFrame();
        addComponents();    // adds canvas + buttons
        addListeners();

        // Lazy GL initialization in background
        new Thread(() -> {
            SwingUtilities.invokeLater(() -> {
                // forces GL context creation without blocking UI
                canvas.display();
            });
        }).start();

        setupAnimator();    // animator starts after display
    }

    @Override
    public void setupAnimator() {
        animator = new FPSAnimator(canvas, 60);
        // Start later, after first display to ensure context is ready
        SwingUtilities.invokeLater(() -> {
            if (!animator.isStarted()) animator.start();
        });
    }

    @Override
    public void setupFrame() {
        frame = new JFrame("Single Player Menu");
        frame.setSize(800, 600);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        PageManager.registerFrameCloseHandler(this, frame);
    }

    private void addCanvas() {
        // Create GLCanvas with hardware acceleration
        GLProfile.initSingleton();
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities caps = new GLCapabilities(profile);
        caps.setDoubleBuffered(true);
        caps.setHardwareAccelerated(true);

        canvas = new GLCanvas(caps);
        MenuBackground renderer = new MenuBackground();
        canvas.addGLEventListener(renderer);
        canvas.addKeyListener(renderer.inputManager);
        canvas.addMouseListener(renderer.inputManager);
        canvas.addMouseMotionListener(renderer.inputManager);

        frame.add(canvas, BorderLayout.CENTER);
    }

    private void addButtons() {
        // Use the GlassPane for button overlay
        JPanel glass = (JPanel) frame.getGlassPane();
        glass.setVisible(true);
        glass.setOpaque(false);
        glass.setLayout(null);

        // Buttons
        playBtn = new JButton("Play");
        levelsBtn = new JButton("Levels");
        muteBtn = new JButton("Mute");
        Dimension btnSize = new Dimension(200, 120);
        playBtn.setSize(btnSize);
        levelsBtn.setSize(btnSize);
        btnSize.height = 50;
        btnSize.width = 100;
        muteBtn.setSize(btnSize);

        // Position buttons manually
        playBtn.setLocation(150, 200);
        levelsBtn.setLocation(450, 200);
        muteBtn.setLocation(670, 10);

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
    public void addComponents() {
        addCanvas();
        addButtons();
    }

    @Override
    public void handleEvents(ActionEvent e) { }

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
        if (canvas != null) canvas.display();
    }

    // Button action setters
    public void setPlayButtonAction(Runnable r) { this.onPlay = r; }
    public void setLevelsButtonAction(Runnable r) { this.onLevels = r; }
    public JFrame getFrame() { return frame; }
}
