package Pages.ContentPanels;

import Game.SoundHandler;
import Game.GreyOverlayPanel;
import com.jogamp.opengl.awt.GLJPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenuPanel extends JPanel {

    private final GLJPanel canvas;  // shared canvas

    private JButton playBtn, levelsBtn;
    private JButton instructionsBtn, creditsBtn;
    private Runnable onPlay, onLevels;
    private GreyOverlayPanel overlayPanel;

    public MainMenuPanel(GLJPanel sharedCanvas) {
        this.canvas = sharedCanvas;
        setLayout(null);  // manual positioning
        setOpaque(false); // canvas shows through



        addButtons();
        addListeners();
        overlayPanel = GreyOverlayPanel.createMenuOverlay();
        overlayPanel.setBounds(0, 0, 800, 600);
        add(overlayPanel);
    }

    private Runnable onInstructions, onCredits;

    private void addButtons() {


        playBtn = createButton("Leaderboard", new Color(20, 165, 224));
        levelsBtn = createButton("Levels", new Color(40, 173, 17));
        instructionsBtn = createButton("Instructions", new Color(255, 140, 0));
        creditsBtn = createButton("Credits", new Color(128, 0, 128));

        // Set positions (same Y as first row, adjust X for spacing)
        playBtn.setBounds(150, 200, 200, 120);
        levelsBtn.setBounds(450, 200, 200, 120);
        instructionsBtn.setBounds(150, 350, 200, 120); // second row left column
        creditsBtn.setBounds(450, 350, 200, 120);      // second row right column

        add(playBtn);
        add(levelsBtn);
        add(instructionsBtn);
        add(creditsBtn);

    }

    private JButton createButton(String text, Color baseColor) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 20));
        btn.setBackground(baseColor);
        btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        // Hover effect
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(baseColor.brighter());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(baseColor);
            }
        });

        return btn;
    }

    private void addListeners() {
        playBtn.addActionListener(e -> { if (onPlay != null) onPlay.run(); });
        levelsBtn.addActionListener(e -> { if (onLevels != null) onLevels.run(); });
        instructionsBtn.addActionListener(e -> { if (onInstructions != null) onInstructions.run(); });
        creditsBtn.addActionListener(e -> { if (onCredits != null) onCredits.run(); });
    }

    // Trigger a redraw of the canvas
    public void redraw() {
        if (canvas != null) canvas.display();
    }

    // Button actions
    public void setPlayButtonAction(Runnable r) { this.onPlay = r; }
    public void setLevelsButtonAction(Runnable r) { this.onLevels = r; }
    public void setInstructionsButtonAction(Runnable r) { this.onInstructions = r; }
    public void setCreditsButtonAction(Runnable r) { this.onCredits = r; }

    public GLJPanel getCanvas() { return canvas; }
}
