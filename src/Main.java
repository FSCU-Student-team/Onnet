import Game.GlobalVariables;
import Game.InputManager;
import Pages.*;
import Pages.ContentPanels.Level;
import Pages.ContentPanels.LevelSelectPanel;
import Pages.ContentPanels.MainMenuPanel;
import Renderers.Levels.*;
import Renderers.MenuBackground;
import com.jogamp.opengl.awt.GLJPanel;


public class Main {

    private static boolean singlePlayer = true;

    // Shared canvas + SPA
    private static GLJPanel sharedCanvas;
    private static SinglePageApplication app;

    // Panels
    private static MainMenuPanel mainMenuPanel;
    private static LevelSelectPanel levelSelectPanel;
    private static Level levelPanel; // single level panel
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

        // Panel actions
        mainMenuPanel.setPlayButtonAction(() -> openLevel(0));
        mainMenuPanel.setLevelsButtonAction(() -> {
            app.setContent(levelSelectPanel);
            GlobalVariables.playerName = app.askPlayerName();
        });

        levelSelectPanel.setBackButtonAction(() -> app.setContent(mainMenuPanel));
        levelSelectPanel.setLevelAction(0, () -> openLevel(0));
        levelSelectPanel.setLevelAction(1, () -> openLevel(1));
        levelSelectPanel.setLevelAction(2, () -> openLevel(2));
        levelSelectPanel.setLevelAction(3, () -> openLevel(3));
        levelSelectPanel.setLevelAction(4, () -> openLevel(4));
        levelSelectPanel.setLevelAction(5, () -> openLevel(5));
        levelSelectPanel.setLevelAction(6, () -> openLevel(6));
        levelSelectPanel.setLevelAction(7, () -> openLevel(7));
        levelSelectPanel.setLevelAction(8, () -> openLevel(8));
        levelSelectPanel.setLevelAction(9, () -> openLevel(9));

        levelPanel.setBackButtonAction(() -> {
            openLevel(-1);
            app.setContent(levelSelectPanel);
        });


        // Show initial panel
        app.setContent(mainMenuPanel);

        // Start SPA
        app.init();
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
