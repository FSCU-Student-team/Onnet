import Game.PageManager;
import Pages.DevTestScene;
import Pages.Page;

public class Main {
    public static void main(String[] args) {

        //IMPORTANT DEV NOTE:
        //SET VM OPTIONS IN RUN CONFIGURATION TO THIS TO RUN: -Djava.library.path=Libs\Natives\Windows --enable-preview --add-exports java.base/java.lang=ALL-UNNAMED --add-exports java.desktop/sun.awt=ALL-UNNAMED --add-exports java.desktop/sun.java2d=ALL-UNNAMED
        //IF USING LINUX CHANGE Libs\Natives\Windows WITH Libs\Natives\Linux
        PageManager.init();

        Page test = new DevTestScene();
        PageManager.showPage(test);
    }
}
