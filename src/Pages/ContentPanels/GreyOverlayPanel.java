package Pages.ContentPanels;

import javax.swing.*;
import java.awt.*;

public class GreyOverlayPanel extends JPanel {
    private Color overlayColor;
    private float transparency;

    public GreyOverlayPanel() {
        this(new Color(50, 50, 50), 0.8f); // Default: dark grey, 70% opaque
        setOpaque(false);
        setLayout(new BorderLayout());
    }

    public GreyOverlayPanel(Color color, float transparency) {
        this.overlayColor = color;
        this.transparency = transparency;
        setOpaque(false);
        setLayout(new BorderLayout());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // Set transparency
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));

        // Draw overlay
        g2d.setColor(overlayColor);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Reset
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    // Factory methods for common overlays
    public static GreyOverlayPanel createDarkGreyOverlay() {
        return new GreyOverlayPanel(new Color(40, 40, 40), 0.8f);
    }

    public static GreyOverlayPanel createLightGreyOverlay() {
        return new GreyOverlayPanel(new Color(100, 100, 100), 0.6f);
    }

    public static GreyOverlayPanel createMenuOverlay() {
        return new GreyOverlayPanel(new Color(60, 60, 60), 0.75f);
    }
}
