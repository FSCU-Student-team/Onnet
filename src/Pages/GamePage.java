package Pages;

import Renderers.GameRenderer;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.*;

public class GamePage implements Page {

    private JPanel panel;
    private GLCanvas canvas;
    private FPSAnimator animator;
    private GameRenderer renderer;

    public GamePage() {
        panel = new JPanel(new BorderLayout());
    }

    public JPanel getPanel() { return panel; }

    @Override
    public void init() {
        addComponents();
        setupAnimator();
        addListeners();
    }

    @Override
    public void addComponents() {
        renderer = new GameRenderer();
        canvas = new GLCanvas();
        canvas.addGLEventListener(renderer);
        panel.add(canvas, BorderLayout.CENTER);
    }

    @Override
    public void setupAnimator() {
        animator = new FPSAnimator(canvas, 60, true);
        animator.start();
    }

    @Override
    public void addListeners() {}

    @Override
    public void handleEvents(java.awt.event.ActionEvent e) {}
    @Override public void setupFrame() {}
    @Override public void dispose() { if(animator != null) animator.stop(); }
    @Override public boolean isVisible() { return panel.isVisible(); }
    @Override public void setVisible(boolean b) { panel.setVisible(b); }
    @Override public void redraw() { canvas.display(); }
}
