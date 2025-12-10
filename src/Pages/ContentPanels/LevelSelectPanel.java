package Pages.ContentPanels;

import com.jogamp.opengl.awt.GLJPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class LevelSelectPanel extends JPanel {

    private final GLJPanel canvas;
    private JButton backBtn;
    private final ArrayList<JButton> levelBtns = new ArrayList<>();
    private final ArrayList<Runnable> onLevel = new ArrayList<>();
    private Runnable onBack;

    public LevelSelectPanel(GLJPanel sharedCanvas) {
        this.canvas = sharedCanvas;
        setLayout(null);
        setOpaque(false);

        addButtons();
        addListeners();
    }

    private void addButtons() {

        int panelWidth = 800;   // adjust if your window differs
        int btnWidth = 200;
        int btnHeight = 60;
        int gapBetweenColumns = 80;

        int col1X = (panelWidth / 2) - (gapBetweenColumns / 2) - btnWidth;
        int col2X = (panelWidth / 2) + (gapBetweenColumns / 2);

        int startY = 50;
        int verticalSpacing = 80;

        for (int i = 0; i < 12; i++) {
            JButton btn = new JButton("Level " + (i + 1));

            int row = i % 6;
            int col = i / 6;

            int x = (col == 0 ? col1X : col2X);
            int y = startY + row * verticalSpacing;

            btn.setBounds(x, y, btnWidth, btnHeight);

            levelBtns.add(btn);
            onLevel.add(null);
            add(btn);
        }

        // back button
        backBtn = new JButton("Back");
        backBtn.setBounds(670, 10, 100, 50);
        add(backBtn);
    }

    private void addListeners() {
        backBtn.addActionListener(e -> { if (onBack != null) onBack.run(); });

        for (int i = 0; i < levelBtns.size(); i++) {
            final int idx = i;
            levelBtns.get(idx).addActionListener(e -> {
                Runnable r = onLevel.get(idx);
                if (r != null) r.run();
            });
        }
    }

    public void redraw() {
        if (canvas != null) canvas.display();
    }

    public void setBackButtonAction(Runnable r) { this.onBack = r; }
    public void setLevelAction(int index, Runnable r) {
        if (index >= 0 && index < levelBtns.size()) {
            onLevel.set(index, r);
        }
    }

    public GLJPanel getCanvas() { return canvas; }
}
