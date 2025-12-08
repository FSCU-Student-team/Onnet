package Pages;

import Game.PageComponentAdapter;
import Game.PageManager;
import Renderers.GameRenderer;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SingleGamePage implements Page {

    private JFrame frame;
    private GLCanvas canvas;
    private FPSAnimator animator;

    @Override
    public void init() {
        setupFrame();
        setupAnimator();
        addComponents();
        addListeners();
    }

    @Override
    public void setupFrame() {
        frame = new JFrame("Single Player Game");
        frame.setSize(800,600);
        frame.setLayout(new BorderLayout());
        frame.setResizable(true);
        frame.setMinimumSize(new Dimension(800, 600));

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        PageManager.registerFrameCloseHandler(this, frame);
    }

    @Override
    public void setupAnimator() {
        canvas = new GLCanvas();
        canvas.addGLEventListener(new GameRenderer());
        animator = new FPSAnimator(canvas, 60);
    }

    @Override
    public void addComponents() {
        frame.add(canvas, BorderLayout.CENTER);
        animator.start();
    }

    @Override
    public void addListeners() {
        frame.addComponentListener(new PageComponentAdapter(this));
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
        canvas.display();
    }
    public JFrame getFrame() {
        return frame;
    }

}
