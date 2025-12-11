package Pages.ContentPanels;

import com.jogamp.opengl.awt.GLJPanel;

import javax.swing.*;
import java.awt.*;

public class CreditsPanel extends JPanel {

    private final GLJPanel sharedCanvas;
    private final JPanel topButtonPanel;
    private final JPanel creditsDisplayPanel;
    private final JButton backButton;
    private Runnable onBack;

    public CreditsPanel(GLJPanel canvas) {
        this.sharedCanvas = canvas;
        setLayout(new BorderLayout());
        setBackground(new Color(20, 20, 20));
        setOpaque(true);

        // Top panel with back button
        topButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topButtonPanel.setOpaque(false);
        add(topButtonPanel, BorderLayout.NORTH);

        backButton = new JButton("Back");
        backButton.setBackground(new Color(200, 50, 50));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.addActionListener(e -> { if (onBack != null) onBack.run(); });
        topButtonPanel.add(backButton);

        // Credits display panel
        creditsDisplayPanel = new JPanel();
        creditsDisplayPanel.setLayout(new BoxLayout(creditsDisplayPanel, BoxLayout.Y_AXIS));
        creditsDisplayPanel.setOpaque(false);
        add(creditsDisplayPanel, BorderLayout.CENTER);

        loadCredits();
    }

    public void setBackAction(Runnable r) {
        this.onBack = r;
    }

    private void loadCredits() {
        String[][] credits = new String[][]{
                {"Abdallah Atef (2327220)", "Roles: Lead Developer - Software architect",
                        "Made collision systems and game engine basis. Ensured code quality and designed project structure."},
                {"Mohammed Helmy (2328158)", "Roles: Developer - Level designer - Sound designer",
                        "Handled sound system and multiple high-quality levels. Responsible for most of the sound effects."},
                {"Abdelrahman Osama (2327133)", "Roles: Developer - Level designer",
                        "Implemented gravity, launch and aiming physics. Created multiple high-quality levels."},
                {"Ahmed Reda (2327492)", "Roles: Developer - Levels designer & Logic Contributor",
                        "Responsible for loading page quality & pause menu, Created multiple high-quality levels. Contributed to project routing."},
                {"Yousef Atef (2327471)", "Roles: Developer - Level designer",
                        "Contributed to GUI foundations and produced several levels."}
        };

        creditsDisplayPanel.removeAll();

        int totalRows = credits.length * 3; // 3 rows per contributor
        int topPanelHeight = topButtonPanel.getPreferredSize().height;
        int availableHeight = 600 - topPanelHeight - 30; // total frame height minus top panel and padding
        int rowHeight = availableHeight / totalRows; // each row height

        for (String[] contributor : credits) {
            // Name
            JLabel nameLabel = new JLabel(contributor[0]);
            nameLabel.setFont(new Font("Monospaced", Font.BOLD, Math.max(rowHeight / 2, 12)));
            nameLabel.setForeground(new Color(255, 215, 0));
            nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JPanel namePanel = new JPanel(new BorderLayout());
            namePanel.setBackground(new Color(40, 40, 40));
            namePanel.setPreferredSize(new Dimension(800, rowHeight));
            namePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, rowHeight));
            namePanel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
            namePanel.add(nameLabel, BorderLayout.CENTER);
            namePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            creditsDisplayPanel.add(namePanel);

            // Roles
            JLabel roleLabel = new JLabel(contributor[1]);
            roleLabel.setFont(new Font("Monospaced", Font.PLAIN, Math.max(rowHeight / 3, 12)));
            roleLabel.setForeground(new Color(173, 216, 230));
            roleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JPanel rolePanel = new JPanel(new BorderLayout());
            rolePanel.setBackground(new Color(40, 40, 40));
            rolePanel.setPreferredSize(new Dimension(800, rowHeight));
            rolePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, rowHeight));
            rolePanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 2, 5));
            rolePanel.add(roleLabel, BorderLayout.CENTER);
            rolePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            creditsDisplayPanel.add(rolePanel);

            // Contributions
            JLabel contributionLabel = new JLabel(
                    "<html><body style='width:700px;margin:0;padding:0;'>" +
                            "<p style='margin:0;padding:0;'>" +
                            contributor[2].replaceAll("\\. ", ".<br>") +
                            "</p></body></html>"
            );
            contributionLabel.setFont(new Font("Monospaced", Font.PLAIN, Math.max(rowHeight / 4, 12)));
            contributionLabel.setForeground(new Color(173, 216, 230));
            contributionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JPanel contributionPanel = new JPanel(new BorderLayout());
            contributionPanel.setBackground(new Color(40, 40, 40));
            contributionPanel.setPreferredSize(new Dimension(800, rowHeight));
            contributionPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, rowHeight));
            contributionPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 4, 5));
            contributionPanel.add(contributionLabel, BorderLayout.CENTER);
            contributionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            creditsDisplayPanel.add(contributionPanel);
        }

        creditsDisplayPanel.revalidate();
        creditsDisplayPanel.repaint();
    }

    public GLJPanel getCanvas() {
        return sharedCanvas;
    }
}
