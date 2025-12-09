package Pages;

import Game.PageManager;
import com.jogamp.opengl.awt.GLCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class LevelSelectPage implements Page {

    private final GLCanvas canvas; // shared canvas
    private JFrame frame;
    private JButton backBtn;
    private final ArrayList<JButton> levelBtns = new ArrayList<>();
    private final ArrayList<Runnable> onLevel = new ArrayList<>();
    private Runnable onBack;

    public LevelSelectPage(GLCanvas sharedCanvas) {
        this.canvas = sharedCanvas;
    }

    @Override
    public void init() {
        setupFrame();
        addComponents();
        addListeners();

        // Force first draw after frame is visible
        SwingUtilities.invokeLater(canvas::display);
    }

    @Override
    public void setupFrame() {
        frame = new JFrame("Level Select");
        frame.setSize(800, 600);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);
        PageManager.registerFrameCloseHandler(this, frame);
    }

    @Override
    public void addComponents() {
        frame.add(canvas, BorderLayout.CENTER);
        addButtons();
        frame.getGlassPane().setVisible(true);
    }

    private void addButtons() {
        JPanel glass = (JPanel) frame.getGlassPane();
        glass.setOpaque(false);
        glass.setLayout(null);

        // Level buttons
        Dimension btnSize = new Dimension(200, 120);
        for (int i = 0; i < 6; i++) {
            JButton btn = new JButton("Level " + (i + 1));
            int row = i % 3;
            int col = i / 3;
            btn.setBounds(150 + col * 300, 100 + row * 150, btnSize.width, btnSize.height);
            levelBtns.add(btn);
            onLevel.add(null);
            glass.add(btn);
        }

        backBtn = new JButton("Back");
        backBtn.setBounds(670, 10, 100, 50);
        glass.add(backBtn);
    }

    @Override
    public void addListeners() {
        backBtn.addActionListener(e -> { if (onBack != null) onBack.run(); });
        for (int i = 0; i < levelBtns.size(); i++) {
            final int idx = i;
            levelBtns.get(i).addActionListener(e -> {
                if (onLevel.get(idx) != null) onLevel.get(idx).run();
            });
        }
    }

    @Override
    public void setupAnimator() {
        // Animator is managed by shared canvas in MainMenuPage
    }

    @Override
    public void handleEvents(ActionEvent e) {}

    @Override
    public void dispose() { frame.dispose(); }

    @Override
    public boolean isVisible() { return frame.isVisible(); }

    @Override
    public void setVisible(boolean b) { frame.setVisible(b); }

    @Override
    public void redraw() { canvas.display(); }

    public void setBackButtonAction(Runnable r) { this.onBack = r; }
    public void setLevelAction(int index, Runnable r) { if (index >= 0 && index < 6) onLevel.set(index, r); }
    public JFrame getFrame() { return frame; }
}
