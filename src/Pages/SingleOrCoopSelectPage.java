package Pages;

import Game.PageManager;
import com.jogamp.opengl.awt.GLCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SingleOrCoopSelectPage implements Page {

    private final GLCanvas canvas; // shared canvas
    private JFrame frame;
    private JButton singleButton, coopButton;
    private Runnable onSingle, onMulti;

    public SingleOrCoopSelectPage(GLCanvas sharedCanvas) {
        this.canvas = sharedCanvas;
    }

    @Override
    public void init() {
        setupFrame();
        addComponents();
        addListeners();

        SwingUtilities.invokeLater(canvas::display);
    }

    @Override
    public void setupFrame() {
        frame = new JFrame("Single or Coop Select");
        frame.setSize(800, 600);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);
        PageManager.registerFrameCloseHandler(this, frame);
    }

    @Override
    public void addComponents() {
        frame.add(canvas, BorderLayout.CENTER);

        JPanel glass = (JPanel) frame.getGlassPane();
        glass.setOpaque(false);
        glass.setLayout(null);
        glass.setVisible(true);

        singleButton = new JButton("Single");
        coopButton = new JButton("Coop");

        singleButton.setBounds(150, 200, 200, 120);
        coopButton.setBounds(450, 200, 200, 120);

        glass.add(singleButton);
        glass.add(coopButton);
    }

    @Override
    public void addListeners() {
        singleButton.addActionListener(e -> { if (onSingle != null) onSingle.run(); });
        coopButton.addActionListener(e -> { if (onMulti != null) onMulti.run(); });
    }

    @Override
    public void setupAnimator() { /* handled by shared canvas */ }

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

    public void setSinglePlayerButtonAction(Runnable r) { this.onSingle = r; }
    public void setCoopButtonAction(Runnable r) { this.onMulti = r; }
    public JFrame getFrame() { return frame; }
}
