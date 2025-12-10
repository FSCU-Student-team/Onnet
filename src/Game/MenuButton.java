package Game;

import javax.swing.*;
import java.awt.*;

public class MenuButton extends JButton {
    private InMenuPopup menu;
    private JPanel parentPanel;

    public MenuButton(JPanel parent) {
        super("Menu");
        this.parentPanel = parent;
        this.menu = new InMenuPopup();

        setFont(new Font("Arial", Font.BOLD, 14));
        setBounds(694, 30, 100, 50); // Standard position

        setFocusable(false); // Don't steal focus

        // Store focus before opening menu
        addActionListener(e -> {
            menu.toggleMenu(parentPanel);
        });
    }

    private Component findCanvas(Container parent) {
        // Find the OpenGL canvas in the hierarchy
        for (Component comp : parent.getComponents()) {
            if (comp.getClass().getName().contains("GLJPanel") ||
                    comp.getClass().getName().contains("GLCanvas")) {
                return comp;
            }
            if (comp instanceof Container) {
                Component found = findCanvas((Container) comp);
                if (found != null) return found;
            }
        }
        return null;
    }

    public InMenuPopup getMenu() {
        return menu;
    }


    public void setMenuActions(Runnable onMainMenu, Runnable onLevels,
                               Runnable onLeaderboard, Runnable onExit) {

        menu.setOnMainMenu(() -> {
            if (onMainMenu != null) {
                onMainMenu.run();
                menu.hideMenu();
            }
        });

        menu.setOnLevels(() -> {
            if (onLevels != null) {
                onLevels.run();
                menu.hideMenu();
            }
        });

        menu.setOnLeaderboard(() -> {
            if (onLeaderboard != null) {
                onLeaderboard.run();
                menu.hideMenu();
            }
        });

        menu.setOnExit(() -> {
            if (onExit != null) {
                onExit.run();
            }
        });

        // Resume just hides the menu
        menu.setOnResume(() -> menu.hideMenu());
    }
}
