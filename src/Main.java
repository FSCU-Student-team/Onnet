import Game.PageManager;
import Pages.*;

SingleOrCoopSelectPage selectSingleOrCoop = new SingleOrCoopSelectPage();
MainMenuPage mainMenu = new MainMenuPage();
LevelSelectPage levelSelectPage = new LevelSelectPage();
DevTestScene devTestScene = new DevTestScene();
level1Frame level1 = new level1Frame();

boolean singlePlayer = true;

ArrayList<Page> levels = new ArrayList<>();

void main() {
    PageManager.init();

    PageManager.preLoadPage(devTestScene);
    PageManager.preLoadPage(level1);
    PageManager.preLoadPage(levelSelectPage);
    PageManager.preLoadPage(selectSingleOrCoop);
    PageManager.preLoadPage(mainMenu);
    //IMPORTANT DEV NOTE:
    //SET VM OPTIONS IN RUN CONFIGURATION TO THIS TO RUN: -Djava.library.path=Libs\Natives\Windows --enable-preview --add-exports java.base/java.lang=ALL-UNNAMED --add-exports java.desktop/sun.awt=ALL-UNNAMED --add-exports java.desktop/sun.java2d=ALL-UNNAMED
    //IF USING LINUX CHANGE Libs\Natives\Windows WITH Libs\Natives\Linux
    PageManager.showPage(mainMenu);

    //load into memory without showing
    levels.add(devTestScene);
    levels.add(level1);

    mainMenu.setLevelsButtonAction(() -> PageManager.switchPage(mainMenu, selectSingleOrCoop)); //prompts you for singleplayer or coop before opening levels
    selectSingleOrCoop.setSinglePlayerButtonAction(() -> singlePlayer = true);
    selectSingleOrCoop.setCoopButtonAction(() -> singlePlayer = false);

    setupLevels();
}

void setupLevels() {
    mainMenu.setPlayButtonAction(() -> {
        //TODO: switch to level 1

    });

    mainMenu.setLevelsButtonAction(() -> {
        //returns to menu
        levelSelectPage.setBackButtonAction(() -> PageManager.switchPage(levelSelectPage, mainMenu));

        for (int i = 0; i < levels.size(); i++) {
            final int idx = i; //to bypass the lambda final constraint
            levelSelectPage.setLevelAction(idx, () -> openLevel(idx));
        }
        PageManager.switchPage(mainMenu, levelSelectPage);
    });
}

void openLevel(int i) {
    PageManager.switchPage(levelSelectPage, levels.get(i));
}

