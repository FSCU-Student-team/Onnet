package Pages;


import com.jogamp.opengl.GL2;
import com.jogamp.opengl.awt.GLCanvas;

import Game.*;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class DevFrame implements Page, GameLoop {
    private JFrame frame;
    private GLCanvas canvas;
    private FPSAnimator animator;
    private LoopState loopState;
    private InputManager inputManager;
    private DevGlListener glListener;

    @Override
    public void physicsUpdate() {
//   uses as updating the game parts on physics
    }

    @Override
    public void renderUpdate(GL2 gl) {
//   uses to render the components of the draw to update it
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    @Override
    public void init() {
        loopState = new LoopState();
        inputManager = new InputManager();

        setupFrame();
        addComponents();
        setupAnimator();
        addListeners();
    }

    @Override
    public void setupFrame() {
        frame = new JFrame();
        frame.setSize(1000, 1000);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //PageManager.showPage(this);  may be use
        PageManager.registerFrameCloseHandler(this, frame);
    }

    @Override
    public void setupAnimator() {
        animator = new FPSAnimator(canvas, 60);
    }

    @Override
    public void addComponents() {
        canvas = new GLCanvas();
        canvas.addGLEventListener(glListener);
        canvas.setSize(1000, 1000);
        canvas.setFocusable(true);

        frame.getContentPane().add(canvas);
    }

    @Override
    public void addListeners() {
        canvas.addMouseListener(inputManager);
        canvas.addMouseMotionListener(inputManager);
        canvas.addKeyListener(inputManager);

        frame.addComponentListener(new PageComponentAdapter(this));
    }

    @Override
    public void handleEvents(ActionEvent e) {
        //when uses a JFrame Buttons
    }

    @Override
    public void dispose() {
        if (animator.isStarted() || animator != null) animator.stop();
        if (frame != null) frame.dispose();
    }

    @Override
    public boolean isVisible() {
        return frame != null && frame.isVisible();
    }

    @Override
    public void setVisible(boolean visible) {
        if (frame != null) frame.setVisible(visible);
        if (visible) {
            animator.start();
            canvas.requestFocus();
        } else {
            animator.stop();
        }
    }

    @Override
    public void redraw() {
        if (canvas != null) canvas.repaint();
    }

}