package Game;

import Pages.Page;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PageManager {

    // all opened pages
    private static HashSet<Page> openedPages;

    // thread pool for async loading
    private static ExecutorService asyncLoader;

    private PageManager() {}

    // initializes the opened pages set
    public static void init() {
        openedPages = new HashSet<>();
        asyncLoader = Executors.newFixedThreadPool(2); // two background threads
    }

    // switches between pages (keeping cached)
    public static void switchPage(Page current, Page newPage) {
        switchPage(current, newPage, false);
    }

    // switches between pages optionally disposing current
    public static void switchPage(Page current, Page newPage, boolean dispose) {
        if (current != null) {
            Dimension currentSize = null;
            if (current.getFrame() != null) {
                currentSize = current.getFrame().getSize();
            }

            if (dispose) {
                disposePage(current);
            } else {
                hidePage(current);
            }

            showPage(newPage);

            if (currentSize != null && newPage.getFrame() != null) {
                newPage.getFrame().setSize(currentSize);
            }
        } else {
            showPage(newPage);
        }
    }

    // shows page; if first time, init it
    public static void showPage(Page page) {
        if (page == null) return;

        if (!openedPages.contains(page)) {
            page.init();           // BLOCKING IF CALLED DIRECTLY!!
            page.setVisible(true);
            openedPages.add(page);
        } else if (!page.isVisible()) {
            page.setVisible(true);
        }
    }

    // PRELOAD (blocking)
    public static void preLoadPage(Page page) {
        if (!openedPages.contains(page)) {
            page.init();
            openedPages.add(page);
        }
    }

    // 🔥 **ASYNC preload** — does NOT block main thread
    public static void preLoadPageAsync(Page page, Runnable callback) {
        if (openedPages.contains(page)) {
            if (callback != null) SwingUtilities.invokeLater(callback);
            return;
        }

        asyncLoader.submit(() -> {
            page.init();
            openedPages.add(page);

            if (callback != null)
                SwingUtilities.invokeLater(callback);
        });
    }

    // hides page but keeps cached
    public static void hidePage(Page page) {
        if (page != null) page.setVisible(false);
    }

    // disposes page and removes from cache
    public static void disposePage(Page page) {
        if (page != null) {
            page.dispose();
            openedPages.remove(page);
        }

        if (openedPages.isEmpty()) {
            System.exit(0);
        }

        for (Page p : openedPages) {
            if (p.getFrame().isVisible()) {
                return;
            }
        }

        System.exit(0);
    }

    // closing handler
    public static void registerFrameCloseHandler(Page page, JFrame frame) {
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                disposePage(page);
            }
        });
    }

    // get all opened pages
    public static HashSet<Page> getOpenedPages() {
        return openedPages;
    }
}
