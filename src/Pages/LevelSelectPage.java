package Pages;

import Game.PageManager;
import Renderers.MenuBackground;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LevelSelectPage implements Page {

    private JFrame frame;
    private JButton backBtn;
    private JButton[] levelBtns = new JButton[6];
    private Runnable onBack;
    private Runnable[] onLevel = new Runnable[6];

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
        frame = new JFrame("Level Select");
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
        // 1. Background canvas
        canvas = new GLJPanel();
        MenuBackground renderer = new MenuBackground();
        canvas.addGLEventListener(renderer);
        canvas.addKeyListener(renderer.inputManager);
        canvas.addMouseListener(renderer.inputManager);
        canvas.addMouseMotionListener(renderer.inputManager);
        canvas.setBounds(0, 0, 800, 600);

        // 2. Button panel
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false); // transparent
        buttonPanel.setBounds(0, 0, 800, 600);

        // Level buttons
        Dimension btnSize = new Dimension(200, 120);
        for (int i = 0; i < 6; i++) {
            levelBtns[i] = new JButton("Level " + (i + 1));
            levelBtns[i].setPreferredSize(btnSize);
        }

        // GridBag layout for level buttons
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 50, 20, 50);
        gbc.anchor = GridBagConstraints.CENTER;

        // Left column
        for (int i = 0; i < 3; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            buttonPanel.add(levelBtns[i], gbc);
        }

        // Right column
        for (int i = 3; i < 6; i++) {
            gbc.gridx = 1;
            gbc.gridy = i - 3;
            buttonPanel.add(levelBtns[i], gbc);
        }

        // Back button at top-right
        backBtn = new JButton("Back");
        backBtn.setPreferredSize(btnSize);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.insets = new Insets(10, 0, 0, 10);
        buttonPanel.add(backBtn, gbc);

        // 3. LayeredPane
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(800, 600));
        layeredPane.setLayout(null);

        layeredPane.add(canvas, Integer.valueOf(0));
        layeredPane.add(buttonPanel, Integer.valueOf(1));

        frame.setContentPane(layeredPane);
    }

    @Override
    public void addListeners() {
        backBtn.addActionListener(e -> { if (onBack != null) onBack.run(); });
        for (int i = 0; i < 6; i++) {
            final int idx = i;
            levelBtns[i].addActionListener(e -> { if (onLevel[idx] != null) onLevel[idx].run(); });
        }
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
    public void redraw() {
        if (canvas != null) canvas.repaint();
    }

    public void setBackButtonAction(Runnable r) { this.onBack = r; }

    public void playLevelAction(int levelIndex, Runnable r) {
        if (levelIndex >= 0 && levelIndex < 6) onLevel[levelIndex] = r;
    }

    public JFrame getFrame() { return frame; }
}
