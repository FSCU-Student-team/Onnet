import Game.PageManager;
import Pages.*;

import java.util.ArrayList;

//IMPORTANT DEV NOTE:
//SET VM OPTIONS IN RUN CONFIGURATION TO THIS TO RUN: -Djava.library.path=Libs\Natives\Windows --enable-preview --add-exports java.base/java.lang=ALL-UNNAMED --add-exports java.desktop/sun.awt=ALL-UNNAMED --add-exports java.desktop/sun.java2d=ALL-UNNAMED
//IF USING LINUX CHANGE Libs\Natives\Windows WITH Libs\Natives\Linux

public class Main {

    private static final SingleOrCoopSelectPage selectSingleOrCoop = new SingleOrCoopSelectPage();
    private static final MainMenuPage mainMenu = new MainMenuPage();
    private static final LevelSelectPage levelSelectPage = new LevelSelectPage();
    private static final level1Frame level1 = new level1Frame();
    private static final Level2 level2 = new Level2();
    private static final Level3 level3 = new Level3();
    private static final Level4 level4 = new Level4();

    private static boolean singlePlayer = true;
    private static ArrayList<Page> levels = new ArrayList<>();

    public static void main(String[] args) {
        PageManager.init();

        // Preload pages
        PageManager.preLoadPage(level1);
        PageManager.preLoadPage(level2);
        PageManager.preLoadPage(level3);
        PageManager.preLoadPage(level4);
        PageManager.preLoadPage(levelSelectPage);
        PageManager.preLoadPage(selectSingleOrCoop);
        PageManager.preLoadPage(mainMenu);

        // Show main menu
        PageManager.showPage(mainMenu);

        // Load levels into memory
        levels.add(level1);
        levels.add(level2);
        levels.add(level3);
        levels.add(level4);

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
