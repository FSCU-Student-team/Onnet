package Pages.ContentPanels;

import Game.GreyOverlayPanel;
import com.jogamp.opengl.awt.GLJPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class LevelSelectPanel extends JPanel {

    private final GLJPanel canvas;
    private final ArrayList<JButton> levelBtns = new ArrayList<>();
    private final ArrayList<Runnable> onLevel = new ArrayList<>();
    private Runnable onBack;
    private GreyOverlayPanel overlayPanel;

    public LevelSelectPanel(GLJPanel sharedCanvas) {
        this.canvas = sharedCanvas;
        setLayout(null);
        setOpaque(false);

        addButtons();
        addListeners();

        overlayPanel = GreyOverlayPanel.createMenuOverlay();
        overlayPanel.setBounds(0, 0, 800, 600);
        add(overlayPanel);
    }

    private void addButtons() {

        int panelWidth = 800;
        int btnWidth = 200;
        int btnHeight = 60;
        int gapBetweenColumns = 80;

        int col1X = (panelWidth / 2) - (gapBetweenColumns / 2) - btnWidth;
        int col2X = (panelWidth / 2) + (gapBetweenColumns / 2);

        int startY = 50;
        int verticalSpacing = 80;

        // Colors gradient: green → yellow → orange → red
        Color startColor = new Color(50, 200, 50);   // green
        Color mid1Color = new Color(250, 250, 50);   // yellow
        Color mid2Color = new Color(250, 150, 50);   // orange
        Color endColor = new Color(250, 50, 50);     // red

        for (int i = 0; i < 12; i++) {
            JButton btn = new JButton("Level " + (i + 1));
            int row = i % 6;
            int col = i / 6;
            int x = (col == 0 ? col1X : col2X);
            int y = startY + row * verticalSpacing;
            btn.setBounds(x, y, btnWidth, btnHeight);

            // Determine gradient color for difficulty
            float t = i / 11f; // 0 → 1
            Color levelColor;
            if (t <= 0.33f) { // green → yellow
                float subT = t / 0.33f;
                levelColor = blendColors(startColor, mid1Color, subT);
            } else if (t <= 0.66f) { // yellow → orange
                float subT = (t - 0.33f) / 0.33f;
                levelColor = blendColors(mid1Color, mid2Color, subT);
            } else { // orange → red
                float subT = (t - 0.66f) / 0.34f;
                levelColor = blendColors(mid2Color, endColor, subT);
            }

            btn.setBackground(levelColor);
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Arial", Font.BOLD, 18));
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

            // Hover effect: brighten
            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    btn.setBackground(levelColor.brighter());
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    btn.setBackground(levelColor);
                }
            });

            levelBtns.add(btn);
            onLevel.add(null);
            add(btn);
        }
    }

    private Color blendColors(Color c1, Color c2, float t) {
        int r = (int) (c1.getRed() + t * (c2.getRed() - c1.getRed()));
        int g = (int) (c1.getGreen() + t * (c2.getGreen() - c1.getGreen()));
        int b = (int) (c1.getBlue() + t * (c2.getBlue() - c1.getBlue()));
        return new Color(r, g, b);
    }

    private void addListeners() {

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

    public void setBackButtonAction(Runnable r) {
        this.onBack = r;
    }

    public void setLevelAction(int index, Runnable r) {
        if (index >= 0 && index < levelBtns.size()) {
            onLevel.set(index, r);
        }
    }

    public GLJPanel getCanvas() {
        return canvas;
    }
}
