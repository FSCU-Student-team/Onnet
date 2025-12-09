package Pages;

import Game.PageManager;
import Game.SoundHandler;
import Renderers.MenuBackground;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoadingPage implements Page {
    private JFrame frame;

    @Override
    public void init() {
        setupFrame();
        addComponents();
        setupAnimator();
        addListeners();
    }

    @Override
    public void setupFrame() {
        frame = new JFrame("Single Player Menu");
        frame.setSize(800, 600);
        frame.setResizable(false); // disable resizing
        frame.setBackground(Color.black);
        PageManager.registerFrameCloseHandler(this, frame);
        frame.setLocationRelativeTo(null);
    }

    @Override
    public void setupAnimator() {

    }

    @Override
    public void addComponents() {
    }

    @Override
    public void addListeners() {
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
    }

    public JFrame getFrame() {
        return frame;
    }
}
