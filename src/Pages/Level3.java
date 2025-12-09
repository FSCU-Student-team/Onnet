package Pages;

import Game.PageComponentAdapter;
import Renderers.Levels.Level2Renderer;
import Renderers.Levels.Level3Renderer;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Level3 implements Page {
    private JFrame frame;
    private GLCanvas canvas;
    private Level3Renderer level3;
    private FPSAnimator animator;

    @Override
    public void init() {
        setupFrame();
        addComponents();
        setupAnimator();
        addListeners();
        redraw();

        Game.PageManager.registerFrameCloseHandler(this, frame);
    }

    @Override
    public void setupFrame() {
        frame = new JFrame("Sprite Testing");
        frame.setSize(800, 800);
        frame.setBackground(Color.black);
        frame.setResizable(false);

        Game.PageManager.registerFrameCloseHandler(this, frame);
    }

    @Override
    public void setupAnimator() {
        animator = new FPSAnimator(canvas, 60);
        animator.start();
    }

    @Override
    public void addComponents() {
        canvas = new GLCanvas();
        level3 = new Level3Renderer();
        canvas.addGLEventListener(level3);

        canvas.addKeyListener(level3.getInputManager());
        canvas.addMouseListener(level3.getInputManager());
        canvas.addMouseMotionListener(level3.getInputManager());

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
