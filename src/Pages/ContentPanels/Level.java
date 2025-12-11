package Pages.ContentPanels;

import com.jogamp.opengl.awt.GLJPanel;

import javax.swing.*;
import java.awt.*;

public class Level extends JPanel {

    private final GLJPanel canvas; // shared canvas from SPA
    private Runnable onMenu;
    private MenuButton menuBtn; // Using our custom MenuButton


    public Level(GLJPanel sharedCanvas) {
        this.canvas = sharedCanvas;
        setLayout(null);    // manual positioning
        setOpaque(false);   // allow canvas to show through
        addMenuButton();
        addMenuListener();
    }

    private void addMenuButton() {
        menuBtn = new MenuButton(this);
        menuBtn.setBounds(694, 30, 100, 50);
        menuBtn.setBackground(new Color(200, 50, 50));
        menuBtn.setForeground(Color.WHITE);
        menuBtn.setFocusPainted(false);
        menuBtn.setFont(new Font("Arial", Font.BOLD, 16));
        add(menuBtn);

        // Add listener for the menu button itself
        menuBtn.addActionListener(e -> {
            if (onMenu != null) onMenu.run();
        });
    }

    private void addMenuListener() {
        menuBtn.addActionListener(e -> {
            if (onMenu != null) onMenu.run();
        });
    }


    // set all menu actions
    public void setMenuActions(Runnable onMainMenu, Runnable onLevels,
                               Runnable onLeaderboard, Runnable onExit) {
        menuBtn.setMenuActions(onMainMenu, onLevels, onLeaderboard, onExit);
    }

    // backward compatibility
    public void setMenuButtonAction(Runnable r) {
        // This now sets the "Levels" action (to go back to level select)
        menuBtn.setMenuActions(null, r, null, null);
    }

    public void redraw() {
        if (canvas != null) canvas.display();
    }

    public GLJPanel getCanvas() {
        return canvas;
    }
}
