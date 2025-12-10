package Pages.ContentPanels;

import Game.SoundHandler;
import com.jogamp.opengl.awt.GLJPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenuPanel extends JPanel {

    private final GLJPanel canvas;  // shared canvas

    private JButton playBtn, levelsBtn, muteBtn;
    private Runnable onPlay, onLevels;

    public MainMenuPanel(GLJPanel sharedCanvas) {
        this.canvas = sharedCanvas;
        setLayout(null);  // manual positioning
        setOpaque(false); // canvas shows through

        addButtons();
        addListeners();
    }

    private void addButtons() {
        playBtn = createButton("Leaderboard", new Color(20, 165, 224));
        levelsBtn = createButton("Levels", new Color(40, 173, 17));
        muteBtn = createButton("Mute", new Color(236, 179, 13));

        playBtn.setBounds(150, 200, 200, 120);
        levelsBtn.setBounds(450, 200, 200, 120);
        muteBtn.setBounds(670, 10, 100, 50);

        add(playBtn);
        add(levelsBtn);
        add(muteBtn);
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
        muteBtn.addActionListener(e -> SoundHandler.toggleMute());
    }

    // Trigger a redraw of the canvas
    public void redraw() {
        if (canvas != null) canvas.display();
    }

    // Button actions
    public void setPlayButtonAction(Runnable r) { this.onPlay = r; }
    public void setLevelsButtonAction(Runnable r) { this.onLevels = r; }

    public GLJPanel getCanvas() { return canvas; }
}
