package Pages;

import Game.PageComponentAdapter;
import Renderers.DevRendererTest;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class DevTestScene implements Page {

    private JFrame frame;
    private GLCanvas canvas;
    private DevRendererTest renderer;

    @Override
    public void init() {
        frame = new JFrame("Sprite Testing");
        setupFrame();
        addComponents();
        setupAnimator();
        addListeners();
        redraw();

        Game.PageManager.registerFrameCloseHandler(this, frame);
    }

    @Override
    public void setupFrame() {
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());
        frame.setBackground(Color.black);
    }

    @Override
    public void setupAnimator() {
        FPSAnimator animator = new FPSAnimator(canvas, 60);
        animator.start();
    }

    @Override
    public void addComponents() {
        canvas = new GLCanvas();
        renderer = new DevRendererTest();
        canvas.addGLEventListener(renderer);

        canvas.addKeyListener(renderer.inputManager);
        canvas.addMouseListener(renderer.inputManager);
        canvas.addMouseMotionListener(renderer.inputManager);

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
    public void redraw() {
        frame.repaint();
    }

    @Override
    public JFrame getFrame() {
        return frame;
    }


    @Override
    public boolean isVisible() {
        return frame.isVisible();
    }

    @Override
    public void setVisible(boolean b) {
        frame.setVisible(b);
    }
}
