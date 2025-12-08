package Pages;

import Game.PageManager;
import Renderers.MenuBackground;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class LevelSelectPage implements Page {

    private JFrame frame;
    private JButton backBtn;

    private ArrayList<JButton> levelBtns = new ArrayList<>();
    private ArrayList<Runnable> onLevel = new ArrayList<>();

    private Runnable onBack;

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
        frame.setResizable(false);
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

        // -----------------------------
        // 1. Background GLJPanel
        // -----------------------------
        canvas = new GLJPanel();
        MenuBackground renderer = new MenuBackground();

        canvas.addGLEventListener(renderer);
        canvas.addKeyListener(renderer.inputManager);
        canvas.addMouseListener(renderer.inputManager);
        canvas.addMouseMotionListener(renderer.inputManager);

        canvas.setBounds(0, 0, 800, 600);

        // -----------------------------
        // 2. Buttons Panel
        // -----------------------------
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.setBounds(0, 0, 800, 600);

        // Create 6 level buttons + listeners array
        Dimension btnSize = new Dimension(200, 120);

        for (int i = 0; i < 6; i++) {
            JButton btn = new JButton("Level " + (i + 1));
            btn.setPreferredSize(btnSize);

            levelBtns.add(btn);
            onLevel.add(null); // placeholder
        }

        // -----------------------------
        // GridBag placement for levels
        // -----------------------------
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 50, 20, 50);
        gbc.anchor = GridBagConstraints.CENTER;

        // Left column (0,1,2)
        for (int i = 0; i < 3; i++) {
            gbc.gridx = 0;
            gbc.gridy = i + 1;  // start at row 1 (row 0 = back button)
            buttonPanel.add(levelBtns.get(i), gbc);
        }

        // Right column (3,4,5)
        for (int i = 3; i < 6; i++) {
            gbc.gridx = 1;
            gbc.gridy = (i - 3) + 1;
            buttonPanel.add(levelBtns.get(i), gbc);
        }

        // -----------------------------
        // Back button (Top-Right)
        // -----------------------------
        backBtn = new JButton("Back");
        backBtn.setPreferredSize(btnSize);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.insets = new Insets(10, 0, 0, 10);

        buttonPanel.add(backBtn, gbc);

        // -----------------------------
        // 3. LayeredPane
        // -----------------------------
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(800, 600));
        layeredPane.setLayout(null);

        layeredPane.add(canvas, Integer.valueOf(0));
        layeredPane.add(buttonPanel, Integer.valueOf(1));

        frame.setContentPane(layeredPane);
    }

    @Override
    public void addListeners() {

        backBtn.addActionListener(e -> {
            if (onBack != null) onBack.run();
        });

        for (int i = 0; i < levelBtns.size(); i++) {
            final int idx = i;
            levelBtns.get(i).addActionListener(e -> {
                if (onLevel.get(idx) != null) onLevel.get(idx).run();
            });
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

    // -----------------------------
    // Action setters
    // -----------------------------
    public void setBackButtonAction(Runnable r) { this.onBack = r; }

    public void setLevelAction(int index, Runnable r) {
        if (index >= 0 && index < 6) {
            onLevel.set(index, r);
        }
    }

    public JFrame getFrame() { return frame; }
}
