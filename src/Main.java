import Game.PageManager;
import Game.SoundHandler;
import Pages.*;

public class Main {
    public static void main(String[] args) {

        //IMPORTANT DEV NOTE:
        //SET VM OPTIONS IN RUN CONFIGURATION TO THIS TO RUN: -Djava.library.path=Libs\Natives\Windows --enable-preview --add-exports java.base/java.lang=ALL-UNNAMED --add-exports java.desktop/sun.awt=ALL-UNNAMED --add-exports java.desktop/sun.java2d=ALL-UNNAMED
        //IF USING LINUX CHANGE Libs\Natives\Windows WITH Libs\Natives\Linux
        PageManager.init();
      /*  Page test = new DevTestScene();
        PageManager.showPage(test);
        SoundHandler.play("Sounds/memphis-trap-wav-349366.wav");*/
        PageManager.init();
        SingleOrMultiPage startPage = new SingleOrMultiPage();
        startPage.init();
        startPage.setVisible(true);
        startPage.setPlayButtonAction(() -> {
            // Single
            MainMenuPage singleMenu = new MainMenuPage();
            singleMenu.setBackBtnAction(() -> PageManager.switchPage(singleMenu, startPage));
            // زرار Play في الـ MainMenuPage يفتح اللعبة مباشرة
            singleMenu.setPlayButtonAction(() -> {
                SingleGamePage gamePage = new SingleGamePage();
                PageManager.switchPage(singleMenu, gamePage);
            });

            // زرار Levels يفتح صفحة اللفلات
            singleMenu.setLevelsButtonAction(() -> {
                SingleLevelsPage levelsPage = new SingleLevelsPage();

                // زرار Back يرجع للـ Menu
                levelsPage.setBackButtonAction(() -> PageManager.switchPage(levelsPage, singleMenu));
                // زرار Level1 يفتح اللعبة
                levelsPage.setLevelAction(0, () -> {
                    SingleGamePage gamePage = new SingleGamePage();
                    PageManager.switchPage(levelsPage, gamePage);
                });
                levelsPage.setLevelAction(1, () -> {
                    SingleGamePage gamePage = new SingleGamePage();
                    PageManager.switchPage(levelsPage, gamePage);
                });

                levelsPage.setLevelAction(2, () -> {
                    SingleGamePage gamePage = new SingleGamePage();
                    PageManager.switchPage(levelsPage, gamePage);
                });

                levelsPage.setLevelAction(3, () -> {
                    SingleGamePage gamePage = new SingleGamePage();
                    PageManager.switchPage(levelsPage, gamePage);
                });

                levelsPage.setLevelAction(4, () -> {
                    SingleGamePage gamePage = new SingleGamePage();
                    PageManager.switchPage(levelsPage, gamePage);
                });

                levelsPage.setLevelAction(5, () -> {
                    SingleGamePage gamePage = new SingleGamePage();
                    PageManager.switchPage(levelsPage, gamePage);
                });


                PageManager.switchPage(singleMenu, levelsPage);
            });

            PageManager.switchPage(startPage, singleMenu);
        });

        startPage.setLevelsButtonAction(() -> {
            // Multi
            MainMenuPage multiMenu = new MainMenuPage();
            multiMenu.setBackBtnAction(() -> PageManager.switchPage(multiMenu, startPage));
            multiMenu.setPlayButtonAction(() -> {
                MultiGamePage gamePage = new MultiGamePage();
                PageManager.switchPage(multiMenu, gamePage);
            });

            multiMenu.setLevelsButtonAction(() -> {
                MultiLevelsPage levelsPage = new MultiLevelsPage();

                // زرار Back يرجع للـ Menu
                levelsPage.setBackButtonAction(() -> PageManager.switchPage(levelsPage, multiMenu));

                // زرار Level1 يفتح اللعبة
                levelsPage.setLevelAction(0,() -> {
                    MultiGamePage gamePage = new MultiGamePage();
                    PageManager.switchPage(levelsPage, gamePage);
                });
                levelsPage.setLevelAction(1,() -> {
                    MultiGamePage gamePage = new MultiGamePage();
                    PageManager.switchPage(levelsPage, gamePage);
                });
                levelsPage.setLevelAction(2,() -> {
                    MultiGamePage gamePage = new MultiGamePage();
                    PageManager.switchPage(levelsPage, gamePage);
                });
                levelsPage.setLevelAction(3,() -> {
                    MultiGamePage gamePage = new MultiGamePage();
                    PageManager.switchPage(levelsPage, gamePage);
                });
                levelsPage.setLevelAction(4,() -> {
                    MultiGamePage gamePage = new MultiGamePage();
                    PageManager.switchPage(levelsPage, gamePage);
                });
                levelsPage.setLevelAction(5,() -> {
                    MultiGamePage gamePage = new MultiGamePage();
                    PageManager.switchPage(levelsPage, gamePage);
                });
                PageManager.switchPage(multiMenu, levelsPage);
            });

            PageManager.switchPage(startPage, multiMenu);
        });
    }
}
