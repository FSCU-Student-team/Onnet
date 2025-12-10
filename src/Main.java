import Game.GlobalVariables;
import Game.InputManager;
import Pages.*;
import Pages.ContentPanels.LeaderboardPanel;
import Pages.ContentPanels.Level;
import Pages.ContentPanels.LevelSelectPanel;
import Pages.ContentPanels.MainMenuPanel;
import Renderers.Levels.*;
import Renderers.MenuBackground;
import com.jogamp.opengl.awt.GLJPanel;
import Pages.ContentPanels.LoadingPanel;


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

    public static void main(String[] args) {

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

        // Panel actions (with loading)
        mainMenuPanel.setPlayButtonAction(() -> openLevelWithLoading(0));
        mainMenuPanel.setLevelsButtonAction(() -> app.setContent(levelSelectPanel));
        mainMenuPanel.setPlayButtonAction(() -> app.setContent(leaderboardPanel));
        mainMenuPanel.setLevelsButtonAction(() -> {
            app.setContent(levelSelectPanel);
            GlobalVariables.playerName = app.askPlayerName();
        });

        levelSelectPanel.setBackButtonAction(() -> app.setContent(mainMenuPanel));
        levelSelectPanel.setLevelAction(0, () -> openLevelWithLoading(0));
        levelSelectPanel.setLevelAction(1, () -> openLevelWithLoading(1));
        levelSelectPanel.setLevelAction(2, () -> openLevelWithLoading(2));
        levelSelectPanel.setLevelAction(3, () -> openLevelWithLoading(3));
        levelSelectPanel.setLevelAction(4, () -> openLevelWithLoading(4));
        levelSelectPanel.setLevelAction(5, () -> openLevelWithLoading(5));
        levelSelectPanel.setLevelAction(6, () -> openLevelWithLoading(6));
        levelSelectPanel.setLevelAction(7, () -> openLevelWithLoading(7));
        levelSelectPanel.setLevelAction(8, () -> openLevelWithLoading(8));
        levelSelectPanel.setLevelAction(9, () -> openLevelWithLoading(9));
        levelSelectPanel.setLevelAction(10, () -> openLevelWithLoading(10));
        levelSelectPanel.setLevelAction(11, () -> openLevelWithLoading(11));

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
}
