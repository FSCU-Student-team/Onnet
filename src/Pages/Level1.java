package Pages;

import Game.PageComponentAdapter;
import Renderers.Levels.Level1Renderer;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Level1 implements Page{
    private JFrame frame;
    private GLCanvas canvas;
    private Level1Renderer level1;
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
      animator=new FPSAnimator(canvas,60);
      animator.start();
    }

    @Override
    public void addComponents() {
        canvas = new GLCanvas();
        level1 = new Level1Renderer();
        canvas.addGLEventListener(level1);

        canvas.addKeyListener(level1.getInputManager());
        canvas.addMouseListener(level1.getInputManager());
        canvas.addMouseMotionListener(level1.getInputManager());

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
    public void setVisible(boolean b){
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
