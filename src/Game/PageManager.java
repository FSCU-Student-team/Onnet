package Game;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PageManager {

    private static JFrame mainFrame;
    private static JPanel currentPanel;
    private static ExecutorService asyncLoader;

    public static void init(JFrame frame) {
        mainFrame = frame;
        asyncLoader = Executors.newFixedThreadPool(2);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void switchPage(JPanel newPanel) {
        if (currentPanel != null) mainFrame.remove(currentPanel);
        currentPanel = newPanel;
        mainFrame.add(currentPanel, BorderLayout.CENTER);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    public static void preLoadPageAsync(JPanel panel, Runnable callback) {
        asyncLoader.submit(() -> {
            // optional heavy init
            if (callback != null) SwingUtilities.invokeLater(callback);
        });
    }
}
