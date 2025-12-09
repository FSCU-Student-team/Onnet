package Pages.ContentPanels;

import Game.SoundHandler;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.awt.GLJPanel;

import javax.swing.*;

public class MainMenuPanel extends JPanel {

    private final GLJPanel canvas;  // shared canvas

    private JButton playBtn, levelsBtn, muteBtn;
    private Runnable onPlay, onLevels;

    public MainMenuPanel(GLJPanel sharedCanvas) {
        this.canvas = sharedCanvas;
        setLayout(null);  // manual positioning like before
        setOpaque(false); // let canvas show through

        addButtons();
        addListeners();
    }

    private void addButtons() {
        playBtn = new JButton("Play");
        levelsBtn = new JButton("Levels");
        muteBtn = new JButton("Mute");

        playBtn.setBounds(150, 200, 200, 120);
        levelsBtn.setBounds(450, 200, 200, 120);
        muteBtn.setBounds(670, 10, 100, 50);

        add(playBtn);
        add(levelsBtn);
        add(muteBtn);
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
