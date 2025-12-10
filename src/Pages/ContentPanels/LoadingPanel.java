package Pages.ContentPanels;

import com.jogamp.opengl.awt.GLJPanel;
import javax.swing.*;
import java.awt.*;

public class LoadingPanel extends JPanel {

    private final GLJPanel canvas;
    private JLabel loadingLabel;
    private JProgressBar progressBar;
    private Timer timer;
    private Timer animationTimer;
    private Runnable onLoadingComplete;
    private int dotCount = 0;

    public LoadingPanel(GLJPanel sharedCanvas) {
        this.canvas = sharedCanvas;
        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(new Color(0, 0, 0, 200)); // Semi-transparent black

        setupLoadingScreen();
    }

    private void setupLoadingScreen() {
        // Main panel with vertical layout
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));

        // Loading label with animated dots
        loadingLabel = new JLabel("LOADING", SwingConstants.CENTER);
        loadingLabel.setFont(new Font("Monospaced", Font.BOLD, 48));
        loadingLabel.setForeground(Color.YELLOW);
        loadingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Progress bar
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true); // Animated progress bar
        progressBar.setPreferredSize(new Dimension(400, 30));
        progressBar.setMaximumSize(new Dimension(400, 30));
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add components
        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(loadingLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        contentPanel.add(progressBar);
        contentPanel.add(Box.createVerticalGlue());

        add(contentPanel, BorderLayout.CENTER);

        // Setup half-second timer
        timer = new Timer(500, e -> {
            if (onLoadingComplete != null) {
                onLoadingComplete.run();
            }
        });
        timer.setRepeats(false);

        // Animation timer for loading dots
        animationTimer = new Timer(165, e -> {
            dotCount = (dotCount + 1) % 4;
            String dots = ".".repeat(dotCount);
            loadingLabel.setText("LOADING" + dots);
        });
    }

    public void startLoading(Runnable onComplete) {
        this.onLoadingComplete = onComplete;

        // Reset and show
        dotCount = 0;
        loadingLabel.setText("LOADING");
        progressBar.setIndeterminate(true);

        // Start animations
        animationTimer.start();
        timer.start();

        // Make sure it's visible
        setVisible(true);
    }

    public void stopLoading() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
        progressBar.setIndeterminate(false);
        setVisible(false);
    }

    public void redraw() {
        if (canvas != null) canvas.display();
    }
}
