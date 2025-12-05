import Game.PageManager;
import Pages.DevTestScene;
import Pages.Page;
import com.jogamp.opengl.GLProfile;

public class Main {
    public static void main(String[] args) {
        GLProfile.initSingleton();
        PageManager.init();

        Page test = new DevTestScene();
        PageManager.showPage(test);
    }
}
