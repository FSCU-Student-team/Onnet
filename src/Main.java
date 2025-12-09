import Game.PageManager;
import Pages.*;

import java.util.ArrayList;
import java.util.Objects;

//IMPORTANT DEV NOTE:
//SET VM OPTIONS IN RUN CONFIGURATION TO THIS TO RUN: -Djava.library.path=Libs\Natives\Windows --enable-preview --add-exports java.base/java.lang=ALL-UNNAMED --add-exports java.desktop/sun.awt=ALL-UNNAMED --add-exports java.desktop/sun.java2d=ALL-UNNAMED
//IF USING LINUX CHANGE Libs\Natives\Windows WITH Libs\Natives\Linux

public class Main {

    // Lightweight pages (OK to instantiate immediately)
    private static final SingleOrCoopSelectPage selectSingleOrCoop = new SingleOrCoopSelectPage();
    private static final MainMenuPage mainMenu = new MainMenuPage();
    private static final LevelSelectPage levelSelectPage = new LevelSelectPage();

    // Heavy level pages — created lazily
    private static Level1 level1 = null;
    private static Level2 level2 = null;
    private static Level3 level3 = null;
    private static Level4 level4 = null;

    private static boolean singlePlayer = true;

    // stable index-based list for levels
    private static final ArrayList<Page> levels = new ArrayList<>();


    public static void main(String[] args) {

        PageManager.init();

        long t = System.nanoTime();

        // preload only lightweight menus
        PageManager.preLoadPage(mainMenu);
        PageManager.preLoadPage(selectSingleOrCoop);
        PageManager.preLoadPage(levelSelectPage);

        System.out.println("Preload time: " + (System.nanoTime() - t) / 1_000_000 + " ms");

        // init placeholders for levels
        int numberOfLevels = 4;
        for (int i = 0; i < numberOfLevels; i++) {
            levels.add(null);
        }

        // show main menu
        PageManager.showPage(mainMenu);

        // menu actions
        mainMenu.setLevelsButtonAction(() -> PageManager.switchPage(mainMenu, selectSingleOrCoop));
        selectSingleOrCoop.setSinglePlayerButtonAction(() -> singlePlayer = true);
        selectSingleOrCoop.setCoopButtonAction(() -> singlePlayer = false);

        setupLevels();
    }

    private static void setupLevels() {
        mainMenu.setPlayButtonAction(() -> openLevel(0));

        mainMenu.setLevelsButtonAction(() -> {
            levelSelectPage.setBackButtonAction(() -> PageManager.switchPage(levelSelectPage, mainMenu));

            for (int i = 0; i < levels.size(); i++) {
                final int idx = i;
                levelSelectPage.setLevelAction(idx, () -> openLevel(idx));
            }

            PageManager.switchPage(mainMenu, levelSelectPage);
        });
    }

    private static void openLevel(int i) {

        if (i < 0 || i >= levels.size()) {
            throw new IllegalArgumentException("Invalid level index: " + i);
        }

        // already loaded?
        if (levels.get(i) != null) {
            PageManager.switchPage(levelSelectPage, levels.get(i));
            return;
        }

        // show loading page

        // create level instance (cheap)
        Page created = createLevel(i);
        Objects.requireNonNull(created);

        levels.set(i, created);

        // async preload (no blocking)
        PageManager.preLoadPageAsync(created, () -> {
            // once loaded: switch to the level
        });
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
}
