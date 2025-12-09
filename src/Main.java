import Game.PageManager;
import Pages.*;
import java.util.ArrayList;

public class Main {

    private static final SingleOrCoopSelectPage selectSingleOrCoop = new SingleOrCoopSelectPage();
    private static final MainMenuPage mainMenu = new MainMenuPage();
    private static final LevelSelectPage levelSelectPage = new LevelSelectPage();
    private static final DevTestScene devTestScene = new DevTestScene();
    private static final level1Frame level1 = new level1Frame();

    private static boolean singlePlayer = true;
    private static ArrayList<Page> levels = new ArrayList<>();

    public static void main(String[] args) {
        PageManager.init();

        // Preload pages
        PageManager.preLoadPage(devTestScene);
        PageManager.preLoadPage(level1);
        PageManager.preLoadPage(levelSelectPage);
        PageManager.preLoadPage(selectSingleOrCoop);
        PageManager.preLoadPage(mainMenu);

        // Show main menu
        PageManager.showPage(mainMenu);

        // Load levels into memory
        levels.add(devTestScene);
        levels.add(level1);

        // Set button actions
        mainMenu.setLevelsButtonAction(() -> PageManager.switchPage(mainMenu, selectSingleOrCoop));
        selectSingleOrCoop.setSinglePlayerButtonAction(() -> singlePlayer = true);
        selectSingleOrCoop.setCoopButtonAction(() -> singlePlayer = false);

        setupLevels();
    }

    private static void setupLevels() {
        mainMenu.setPlayButtonAction(() -> {
            // TODO: switch to level 1
        });

        mainMenu.setLevelsButtonAction(() -> {
            // Return to menu
            levelSelectPage.setBackButtonAction(() -> PageManager.switchPage(levelSelectPage, mainMenu));

            for (int i = 0; i < levels.size(); i++) {
                final int idx = i; // To bypass lambda final constraint
                levelSelectPage.setLevelAction(idx, () -> openLevel(idx));
            }

            PageManager.switchPage(mainMenu, levelSelectPage);
        });
    }

    private static void openLevel(int i) {
        PageManager.switchPage(levelSelectPage, levels.get(i));
    }
}
