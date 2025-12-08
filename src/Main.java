import Game.PageManager;
import Pages.*;

public class Main {

    static MainMenuPage mainMenu = new MainMenuPage();
    static SinglePlayerSelectPage singlePlayerMenu = new SinglePlayerSelectPage();
    static SingleGamePage gamePage = new SingleGamePage();
    static LevelSelectPage levelSelectPage = new LevelSelectPage();

    public static void main(String[] args) {

        //IMPORTANT DEV NOTE:
        //SET VM OPTIONS IN RUN CONFIGURATION TO THIS TO RUN: -Djava.library.path=Libs\Natives\Windows --enable-preview --add-exports java.base/java.lang=ALL-UNNAMED --add-exports java.desktop/sun.awt=ALL-UNNAMED --add-exports java.desktop/sun.java2d=ALL-UNNAMED
        //IF USING LINUX CHANGE Libs\Natives\Windows WITH Libs\Natives\Linux
        PageManager.init();
        PageManager.showPage(mainMenu);

        //load into memory without showing
        PageManager.preLoadPage(singlePlayerMenu);
        PageManager.preLoadPage(gamePage);
        PageManager.preLoadPage(levelSelectPage);

        mainMenu.setPlayButtonAction(() -> {
            // Singleplayer
            setupLevels(mainMenu, gamePage, singlePlayerMenu);
        });

        mainMenu.setLevelsButtonAction(() -> {
            // Multiplayer (coop)
            SinglePlayerSelectPage multiMenu = new SinglePlayerSelectPage();
            setupLevels(mainMenu, gamePage, multiMenu);
        });
    }

    private static void setupLevels(MainMenuPage startPage, SingleGamePage gamePage, SinglePlayerSelectPage multiMenu) {
        multiMenu.setBackBtnAction(() -> PageManager.switchPage(multiMenu, startPage));
        multiMenu.setPlayButtonAction(() -> {
            PageManager.switchPage(multiMenu, gamePage);
        });

        multiMenu.setLevelsButtonAction(() -> {
            // زرار Back يرجع للـ Menu
            levelSelectPage.setBackButtonAction(() -> PageManager.switchPage(levelSelectPage, multiMenu));

            for (int i = 0; i < 6; i++) {
                levelSelectPage.setLevelAction(i, () -> PageManager.switchPage(levelSelectPage, gamePage)); //0-based i is mapped to 1-based level numbers
            }
            PageManager.switchPage(multiMenu, levelSelectPage);
        });

        PageManager.switchPage(startPage, multiMenu);
    }
}
