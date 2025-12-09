import Pages.*;
import Pages.ContentPanels.LevelSelectPanel;
import Pages.ContentPanels.MainMenuPanel;
import Renderers.MenuBackground;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.awt.GLJPanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Objects;

public class Main {

    // Levels
    private static Level1 level1 = null;
    private static Level2 level2 = null;
    private static Level3 level3 = null;
    private static Level4 level4 = null;

    private static boolean singlePlayer = true;
    private static final ArrayList<Page> levels = new ArrayList<>();

    // Shared canvas + application
    private static GLJPanel sharedCanvas;
    private static SinglePageApplication app;

    // Panels
    private static MainMenuPanel mainMenuPanel;
    private static LevelSelectPanel levelSelectPanel;
//    private static SingleOrCoopSelectPage selectSingleOrCoopPanel;

    public static void main(String[] args) {

        // Create shared GLCanvas
        sharedCanvas = createSharedCanvas();

        // Create single frame application
        app = new SinglePageApplication(sharedCanvas, "Onnet");

        // Create child panels
        mainMenuPanel = new MainMenuPanel(sharedCanvas);
        levelSelectPanel = new LevelSelectPanel(sharedCanvas);
//        selectSingleOrCoopPanel = new SingleOrCoopSelectPanel(sharedCanvas);

        // Initialize level placeholders
        for (int i = 0; i < 4; i++) levels.add(null);

        // Setup panel actions
        mainMenuPanel.setPlayButtonAction(() -> openLevel(0));
        mainMenuPanel.setLevelsButtonAction(() -> app.setContent(levelSelectPanel));

//        selectSingleOrCoopPanel.setSinglePlayerButtonAction(() -> singlePlayer = true);
//        selectSingleOrCoopPanel.setCoopButtonAction(() -> singlePlayer = false);

        levelSelectPanel.setBackButtonAction(() -> app.setContent(mainMenuPanel));
        for (int i = 0; i < levels.size(); i++) {
            final int idx = i;
            levelSelectPanel.setLevelAction(i, () -> openLevel(idx));
        }

        // Show initial panel
        app.setContent(mainMenuPanel);

        // Start animator is handled by SinglePageApplication
        app.init();
    }

    /**
     * Swap the content panel to the requested level
     */
    private static void openLevel(int i) {
        if (i < 0 || i >= levels.size()) throw new IllegalArgumentException("Invalid level index: " + i);

        // Already loaded
        if (levels.get(i) != null) {
            if (levels.get(i) instanceof JPanel panel) app.setContent(panel);
            return;
        }

        // Create level lazily
        Page created = createLevel(i);
        levels.set(i, created);

        if (created instanceof JPanel panel) {
            app.setContent(panel);
        }
    }

    private static Page createLevel(int index) {
        return switch (index) {
            case 0 -> {
                if (level1 == null) level1 = new Level1();
                yield level1;
            }
            case 1 -> {
                if (level2 == null) level2 = new Level2();
                yield level2;
            }
            case 2 -> {
                if (level3 == null) level3 = new Level3();
                yield level3;
            }
            case 3 -> {
                if (level4 == null) level4 = new Level4();
                yield level4;
            }
            default -> throw new IllegalArgumentException("No level mapped for index " + index);
        };
    }

    private static GLJPanel createSharedCanvas() {
        GLJPanel canvas = new GLJPanel();
        MenuBackground renderer = new MenuBackground();
        canvas.addGLEventListener(renderer);
        canvas.addKeyListener(renderer.inputManager);
        canvas.addMouseListener(renderer.inputManager);
        canvas.addMouseMotionListener(renderer.inputManager);

        return canvas;
    }
}
