package Pages;

import Game.PageComponentAdapter;
import Renderers.Levels.Level2Renderer;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Level2 implements Page {
    private JFrame frame;
    private GLCanvas canvas;
    private Level2Renderer level2;
    private FPSAnimator animator;

    @Override
    public void init() {
        setupFrame();
        addComponents();
        setupAnimator();
        addListeners();
        redraw();
    }

    @Override
    public void setupFrame() {
        frame = new JFrame("Sprite Testing");
        frame.setSize(800, 800);
        frame.setBackground(Color.black);
        frame.setResizable(false);
    }

    @Override
    public void setupAnimator() {
        animator = new FPSAnimator(canvas, 60);
        animator.start();
    }

    @Override
    public void addComponents() {
        canvas = new GLCanvas();
        level2 = new Level2Renderer();
        canvas.addGLEventListener(level2);

        canvas.addKeyListener(level2.getInputManager());
        canvas.addMouseListener(level2.getInputManager());
        canvas.addMouseMotionListener(level2.getInputManager());

        frame.add(canvas, BorderLayout.CENTER);

    }

    @Override
    public void addListeners() {
        frame.addComponentListener(new PageComponentAdapter(this));
    }

    @Override
    public void handleEvents(ActionEvent e) {
        String command = e.getActionCommand();

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
        frame.repaint();
    }

    @Override
    public JFrame getFrame() {
        return this.frame;
    }
}
