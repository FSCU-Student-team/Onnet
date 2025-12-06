import Pages.MainMenuPage;
import Pages.LevelsPage;
import Pages.GamePage;
import Game.PageManager;

import javax.swing.*;

public class Main1 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Init PageManager
            PageManager.init();

            // Pages
            final MainMenuPage mainMenu = new MainMenuPage();
            mainMenu.init();

            final LevelsPage levelsPage = new LevelsPage();
            levelsPage.init();

            final GamePage gamePage = new GamePage();
            gamePage.init();

            // Connect buttons
            mainMenu.setPlayButtonAction(() -> PageManager.switchPage(mainMenu, gamePage));
            mainMenu.setLevelsButtonAction(() -> PageManager.switchPage(mainMenu, levelsPage));

            levelsPage.setHomeButtonAction(() -> PageManager.switchPage(levelsPage, mainMenu));
            levelsPage.setLevel1ButtonAction(() -> PageManager.switchPage(levelsPage, gamePage));

            // Create main frame
            JFrame frame = new JFrame("Game Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setLayout(null);

            frame.add(mainMenu.getPanel());
            frame.add(levelsPage.getPanel());
            frame.add(gamePage.getPanel());

            // Show Main Menu initially
            PageManager.showPage(mainMenu);

            frame.setVisible(true);
        });
    }
}
