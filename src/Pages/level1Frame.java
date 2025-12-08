package Pages;

import Renderers.DevRendererTest;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class level1Frame implements Page{
    private JFrame frame;
    private GLCanvas canvas;
    private GLlevel1 level1;
    private FPSAnimator animator;
    @Override
    public void init() {
        frame = new JFrame("Sprite Testing");
        setupFrame();
        addComponents();
        addListeners();
        redraw();
        setupAnimator();

        Game.PageManager.registerFrameCloseHandler(this, frame);
    }

    @Override
    public void setupFrame() {
        frame.setSize(800, 600);
        frame.setBackground(Color.black);

        Game.PageManager.registerFrameCloseHandler(this, frame);
    }

    @Override
    public void setupAnimator() {
      animator=new FPSAnimator(canvas,60);
    }

    @Override
    public void addComponents() {

    }

    @Override
    public void addListeners() {
        canvas = new GLCanvas();
        level1 = new GLlevel1();
        canvas.addGLEventListener(level1);

        canvas.addKeyListener(level1.getInputManager());
        canvas.addMouseListener(level1.getInputManager());
        canvas.addMouseMotionListener(level1.getInputManager());
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
        return null;
    }
}
