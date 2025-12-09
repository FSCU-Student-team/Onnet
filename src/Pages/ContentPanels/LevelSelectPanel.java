package Pages.ContentPanels;

import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.awt.GLJPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class LevelSelectPanel extends JPanel {

    private final GLJPanel canvas; // shared canvas
    private JButton backBtn;
    private final ArrayList<JButton> levelBtns = new ArrayList<>();
    private final ArrayList<Runnable> onLevel = new ArrayList<>();
    private Runnable onBack;

    public LevelSelectPanel(GLJPanel sharedCanvas) {
        this.canvas = sharedCanvas;
        setLayout(null);    // manual positioning
        setOpaque(false);   // let canvas show through

        addButtons();
        addListeners();
    }

    private void addButtons() {
        // Level buttons
        Dimension btnSize = new Dimension(200, 120);
        for (int i = 0; i < 6; i++) {
            JButton btn = new JButton("Level " + (i + 1));
            int row = i % 3;
            int col = i / 3;
            btn.setBounds(150 + col * 300, 100 + row * 150, btnSize.width, btnSize.height);
            levelBtns.add(btn);
            onLevel.add(null);
            add(btn);
        }

        // Back button
        backBtn = new JButton("Back");
        backBtn.setBounds(670, 10, 100, 50);
        add(backBtn);
    }

    private void addListeners() {
        backBtn.addActionListener(e -> { if (onBack != null) onBack.run(); });
        for (int i = 0; i < levelBtns.size(); i++) {
            final int idx = i;
            levelBtns.get(idx).addActionListener(e -> {
                if (onLevel.get(idx) != null) onLevel.get(idx).run();
            });
        }
    }

    /** Trigger redraw of the shared canvas */
    public void redraw() {
        if (canvas != null) canvas.display();
    }

    // Action setters
    public void setBackButtonAction(Runnable r) { this.onBack = r; }
    public void setLevelAction(int index, Runnable r) { if (index >= 0 && index < 6) onLevel.set(index, r); }

    public GLJPanel getCanvas() { return canvas; }
}
