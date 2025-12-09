package Pages.ContentPanels;

import Pages.SinglePageApplication;
import Renderers.Levels.Level1Renderer;
import com.jogamp.opengl.awt.GLJPanel;

import javax.swing.*;
import java.awt.*;

public class Level extends JPanel {

    private final GLJPanel canvas; // shared canvas from SPA
    private JButton backBtn;
    private Runnable onBack;

    public Level(GLJPanel sharedCanvas) {
        this.canvas = sharedCanvas;
        setLayout(null);    // manual positioning
        setOpaque(false);   // allow canvas to show through

        addBackButton();
        addBackListener();
    }

    private void addBackButton() {
        backBtn = new JButton("Back");
        backBtn.setBounds(670, 10, 100, 50);
        add(backBtn);
    }

    private void addBackListener() {
        backBtn.addActionListener(e -> { if (onBack != null) onBack.run(); });
    }

    public void setBackButtonAction(Runnable r) { this.onBack = r; }

    public void redraw() {
        if (canvas != null) canvas.display();
    }

    public GLJPanel getCanvas() { return canvas; }
}
