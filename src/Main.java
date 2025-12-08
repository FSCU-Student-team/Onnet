import Game.PageManager;
import Game.SoundHandler;
import Pages.LevelSelectPage;
import Pages.Page;
import Pages.SingleOrCoopSelectPage;
import Pages.MainMenuPage;

SingleOrCoopSelectPage selectSingleOrCoop = new SingleOrCoopSelectPage();
MainMenuPage mainMenu = new MainMenuPage();
LevelSelectPage levelSelectPage = new LevelSelectPage();
boolean singlePlayer = true;

ArrayList<Page> levels = new ArrayList<>();

void main() {

    //IMPORTANT DEV NOTE:
    //SET VM OPTIONS IN RUN CONFIGURATION TO THIS TO RUN: -Djava.library.path=Libs\Natives\Windows --enable-preview --add-exports java.base/java.lang=ALL-UNNAMED --add-exports java.desktop/sun.awt=ALL-UNNAMED --add-exports java.desktop/sun.java2d=ALL-UNNAMED
    //IF USING LINUX CHANGE Libs\Natives\Windows WITH Libs\Natives\Linux
    PageManager.init();
    PageManager.showPage(mainMenu);

    //load into memory without showing
    PageManager.preLoadPage(levelSelectPage);
    PageManager.preLoadPage(selectSingleOrCoop);

    //Add new levels to levels arrayList here (DO NOT PRELOAD THEM FOR PERFORMANCE REASONS)

    mainMenu.setLevelsButtonAction(() -> PageManager.switchPage(mainMenu, selectSingleOrCoop)); //prompts you for singleplayer or coop before opening levels
    selectSingleOrCoop.setSinglePlayerButtonAction(() -> singlePlayer = true);
    selectSingleOrCoop.setCoopButtonAction(() -> singlePlayer = false);

    mainMenu.setMuteButtonAction(SoundHandler::toggleMute);

    setupLevels();
}

void setupLevels() {
    mainMenu.setBackBtnAction(() -> PageManager.switchPage(levelSelectPage, mainMenu));
    mainMenu.setPlayButtonAction(() -> {
        //TODO: switch to level 1
    });

    mainMenu.setLevelsButtonAction(() -> {
        //returns to menu
        levelSelectPage.setBackButtonAction(() -> PageManager.switchPage(levelSelectPage, mainMenu));

        for (int i = 0; i < 6; i++) {
            //TODO: make gamePage an array that takes in level number and opens the level accordingly (use singlePlayer boolean)
        }
        PageManager.switchPage(mainMenu, levelSelectPage);
    });
}
