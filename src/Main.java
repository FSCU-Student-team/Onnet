import Game.GlobalVariables;
import Game.InputManager;
import Pages.*;
import Pages.ContentPanels.*;
import Renderers.Levels.*;
import Renderers.MenuBackground;
import com.jogamp.opengl.awt.GLJPanel;

import java.lang.reflect.Field;


public class Main {

    private static boolean singlePlayer = true;

    // Shared canvas + SPA
    private static GLJPanel sharedCanvas;
    private static SinglePageApplication app;

    // Panels
    private static MainMenuPanel mainMenuPanel;
    private static LevelSelectPanel levelSelectPanel;
    private static LeaderboardPanel leaderboardPanel;
    private static Level levelPanel; // single level panel
    private static LoadingPanel loadingPanel;
    private static InputManager inputManager;
    private static InstructionsPanel instructionsPanel;
    private static CreditsPanel creditsPanel;

    public static void main(String[] args) {

        //exporting steps, please ignore

        exportOS();

        inputManager = new InputManager();
        // Create shared GLJPanel
        sharedCanvas = createSharedCanvas();

        // Single frame SPA
        app = new SinglePageApplication(sharedCanvas, "Onnet");

        // Child panels
        mainMenuPanel = new MainMenuPanel(sharedCanvas);
        levelSelectPanel = new LevelSelectPanel(sharedCanvas);
        levelPanel = new Level(sharedCanvas);
        leaderboardPanel = new LeaderboardPanel(sharedCanvas);
        loadingPanel = new LoadingPanel(sharedCanvas);
        instructionsPanel = new InstructionsPanel(sharedCanvas);
        creditsPanel = new CreditsPanel(sharedCanvas);

        instructionsPanel.setBackAction(() -> app.setContent(mainMenuPanel));
        creditsPanel.setBackAction(() -> app.setContent(mainMenuPanel));

        // Panel actions (with loading)
        mainMenuPanel.setLevelsButtonAction(() -> app.setContent(levelSelectPanel));
        mainMenuPanel.setPlayButtonAction(() -> app.setContent(leaderboardPanel));
        mainMenuPanel.setInstructionsButtonAction(() -> app.setContent(instructionsPanel));
        mainMenuPanel.setCreditsButtonAction(() -> app.setContent(creditsPanel));
        mainMenuPanel.setLevelsButtonAction(() -> {
            app.setContent(levelSelectPanel);
            GlobalVariables.playerName = app.askPlayerName();
        });

        levelSelectPanel.setBackButtonAction(() -> app.setContent(mainMenuPanel));
        for (int i = 0; i < 12; i++) {
            int idx = i;
            levelSelectPanel.setLevelAction(i, () -> openLevelWithLoading(idx));

        }

        levelPanel.setBackButtonAction(() -> {
            openLevel(-1);
            app.setContent(levelSelectPanel);
        });
        leaderboardPanel.setBackAction(() -> app.setContent(mainMenuPanel));
        // Show initial panel
        app.setContent(mainMenuPanel);

        // Start SPA
        app.init();
    }

    /**
     * Open level with loading screen
     */
    private static void openLevelWithLoading(int level) {
        // Show loading screen
        app.setContent(loadingPanel);

        // Start loading animation
        loadingPanel.startLoading(() -> {
            // After loading, open the actual level
            openLevel(level);
            app.setContent(levelPanel);
        });
    }

    /**
     * Open a level by swapping the renderer
     */
    private static void openLevel(int i) {
        switch (i) {
            case -1 -> app.setLevelRenderer(new MenuBackground(inputManager));
            case 0 -> app.setLevelRenderer(new Level1Renderer(inputManager));
            case 1 -> app.setLevelRenderer(new Level2Renderer(inputManager));
            case 2 -> app.setLevelRenderer(new Level3Renderer(inputManager));
            case 3 -> app.setLevelRenderer(new Level4Renderer(inputManager));
            case 4 -> app.setLevelRenderer(new Level5Renderer(inputManager));
            case 5 -> app.setLevelRenderer(new Level6Renderer(inputManager));
            case 6 -> app.setLevelRenderer(new Level7Renderer(inputManager));
            case 7 -> app.setLevelRenderer(new Level8Renderer(inputManager));
            case 8 -> app.setLevelRenderer(new Level9Renderer(inputManager));
            case 9 -> app.setLevelRenderer(new Level10Renderer(inputManager));
            case 10 -> app.setLevelRenderer(new Level11Renderer(inputManager));
            case 11 -> app.setLevelRenderer(new Level12Renderer(inputManager));
            default -> throw new IllegalArgumentException("No renderer for level " + i);
        }
        // Show the single shared level panel
        app.setContent(levelPanel);
    }

    private static GLJPanel createSharedCanvas() {
        GLJPanel canvas = new GLJPanel();
        MenuBackground renderer = new MenuBackground(inputManager);
        canvas.addGLEventListener(renderer);
        canvas.addKeyListener(inputManager);
        canvas.addMouseListener(inputManager);
        canvas.addMouseMotionListener(inputManager);
        return canvas;
    }

    private static void exportOS() {

        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            System.setProperty("java.library.path", "Libs/Natives/Windows");
        } else if (os.contains("linux")){
            System.setProperty("java.library.path", "Libs/Natives/Linux");
        } else{
            System.setProperty("java.library.path", "Libs/Natives/Mac");
        }

        // force JVM to reload the library path
        try {
            Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
            fieldSysPath.setAccessible(true);
            fieldSysPath.set(null, null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }
}
