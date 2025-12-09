package Pages;

import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SinglePageApplication implements Page {

    private final JFrame frame;
    private final GLJPanel canvas;
    private FPSAnimator animator;

    private final JPanel contentPanel;   // swap child pages here
    private final JPanel glassPane;      // overlay buttons
    private final JLayeredPane layeredPane;

    public SinglePageApplication(GLJPanel sharedCanvas, String title) {
        this.canvas = sharedCanvas;

        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        // Content panel
        contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BorderLayout());

        // Glass pane for buttons
        glassPane = new JPanel();
        glassPane.setOpaque(false);
        glassPane.setLayout(null);

        // Layered pane
        layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);
        layeredPane.setPreferredSize(new Dimension(800, 600));

        contentPanel.setBounds(0, 0, 800, 600);
        glassPane.setBounds(0, 0, 800, 600);

        layeredPane.add(contentPanel, Integer.valueOf(1));
        layeredPane.add(glassPane, Integer.valueOf(2));

        frame.setContentPane(layeredPane);
    }

    /** Swap the current content JPanel with a new one */
    public void setContent(JPanel newPanel) {
        contentPanel.removeAll();
        contentPanel.add(newPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    /** Add a button (or any component) to the overlay */
    public void addOverlayComponent(JComponent comp, int x, int y, int width, int height) {
        comp.setBounds(x, y, width, height);
        glassPane.add(comp);
        glassPane.revalidate();
        glassPane.repaint();
    }

    @Override
    public void init() {
        setupFrame();
        addComponents();
        addListeners();
        setupAnimator();
    }

    @Override
    public void setupFrame() {
        // Show frame and buttons first
        frame.setVisible(true);

        // Add GLJPanel asynchronously to avoid blocking EDT
        SwingUtilities.invokeLater(() -> {
            canvas.setBounds(0, 0, frame.getWidth(), frame.getHeight());
            layeredPane.add(canvas, Integer.valueOf(0));
            layeredPane.revalidate();
            layeredPane.repaint();
        });
    }

    @Override
    public void setupAnimator() {
        if (canvas != null && (animator == null || !animator.isStarted())) {
            animator = new FPSAnimator(canvas, 60);

            // Start animator asynchronously
            SwingUtilities.invokeLater(() -> {
                animator.start();
                canvas.display(); // force first GL context creation
            });
        }
    }

    @Override
    public void addComponents() {
        // content and overlay already added in constructor
    }

    @Override
    public void addListeners() {
        // override in child pages or add listeners to components
    }

    @Override
    public void handleEvents(ActionEvent e) {
        // override in child pages
    }

    @Override
    public void dispose() {
        if (animator != null && animator.isStarted()) animator.stop();
        frame.dispose();
    }

    @Override
    public boolean isVisible() { return frame.isVisible(); }

    @Override
    public void setVisible(boolean b) { frame.setVisible(b); }

    @Override
    public void redraw() { if (canvas != null) canvas.display(); }

    @Override
    public JFrame getFrame() { return frame; }

    public JPanel getContentPanel() { return contentPanel; }
    public JPanel getGlassPanePanel() { return glassPane; }

    //sets new level renderer
    public void setLevelRenderer(GLEventListener renderer) {
        // Remove all previous listeners
        for (int i = 0; i < canvas.getGLEventListenerCount(); i++) {
            canvas.removeGLEventListener(canvas.getGLEventListener(i));
        }

        // Add the new level renderer
        canvas.addGLEventListener(renderer);

        // Force a redraw
        canvas.display();
    }

}
