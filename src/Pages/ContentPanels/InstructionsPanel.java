package Pages.ContentPanels;

import com.jogamp.opengl.awt.GLJPanel;

import javax.swing.*;
import java.awt.*;

public class InstructionsPanel extends JPanel {

    private final GLJPanel sharedCanvas;
    private final JPanel topButtonPanel;
    private final JPanel instructionsDisplayPanel;
    private final JButton backButton;
    private Runnable onBack;

    public InstructionsPanel(GLJPanel canvas) {
        this.sharedCanvas = canvas;
        setLayout(new BorderLayout());
        setBackground(new Color(20, 20, 20)); // Dark background
        setOpaque(true);

        // Top panel with back button
        topButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topButtonPanel.setOpaque(false);
        add(topButtonPanel, BorderLayout.NORTH);

        // Back button
        backButton = new JButton("Back");
        backButton.setBackground(new Color(200, 50, 50));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.addActionListener(e -> {
            if (onBack != null) onBack.run();
        });
        topButtonPanel.add(backButton);

        // Instructions panel (no scrolling)
        instructionsDisplayPanel = new JPanel();
        instructionsDisplayPanel.setLayout(new BoxLayout(instructionsDisplayPanel, BoxLayout.Y_AXIS));
        instructionsDisplayPanel.setOpaque(false);
        add(instructionsDisplayPanel, BorderLayout.CENTER);

        // Load instructions
        loadInstructions();
    }

    public void setBackAction(Runnable r) {
        this.onBack = r;
    }

    private void loadInstructions() {
        String[] instructions = new String[]{
                "General instructions:",
                "You control the player ball",
                "The line is the aim line which the ball will go to",
                "A/D to move the line up and down",
                "W/S to adjust the launching force",
                "Spacebar to launch",
                "R to reset the level",
                "Escape to pause",
                "Avoid hitting red objects",
                "Win by reaching the Yellow target (hollow rectangle shaped)",
                "Some objects bounce",
                "More power is not always good, be strategic",
                "Notes:",
                "Score starts at 100000, reduced by 1 every millisecond"
        };

        instructionsDisplayPanel.removeAll();

        // Calculate height for each row
        int totalLines = instructions.length;
        int topPanelHeight = topButtonPanel.getPreferredSize().height;
        int availableHeight = 600 - topPanelHeight - 20; // assume panel height ~600
        int rowHeight = availableHeight / totalLines;

        for (String line : instructions) {
            JLabel label = new JLabel(line);
            label.setFont(new Font("Monospaced", Font.PLAIN, 16));
            label.setAlignmentX(Component.LEFT_ALIGNMENT);

            // Color headers differently
            if (line.endsWith(":")) {
                label.setForeground(new Color(255, 215, 0)); // gold
                label.setFont(new Font("Monospaced", Font.BOLD, 18));
            } else {
                label.setForeground(new Color(173, 216, 230)); // light cyan
            }

            // Panel for each line with padding
            JPanel linePanel = new JPanel(new BorderLayout());
            linePanel.setBackground(new Color(40, 40, 40));
            linePanel.setPreferredSize(new Dimension(800, rowHeight));
            linePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, rowHeight));
            linePanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 10, 0)); // 5px left, 10px bottom
            linePanel.add(label, BorderLayout.CENTER);
            linePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            instructionsDisplayPanel.add(linePanel);
        }

        instructionsDisplayPanel.revalidate();
        instructionsDisplayPanel.repaint();
    }


    public GLJPanel getCanvas() {
        return sharedCanvas;
    }
}
