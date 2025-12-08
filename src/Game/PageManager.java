package Game;
import java.awt.Dimension;
import Pages.Page;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;

public class PageManager {
    //all opened pages
    private static HashSet<Page> openedPages;

    //private constructor to prevent instantiation
    private PageManager() {
    }

    //initializes the opened pages set
    public static void init() {
        openedPages = new HashSet<>();
    }

    //switches between pages (keeping the page cached in memory)
    public static void switchPage(Page current, Page newPage) {
        switchPage(current, newPage, false);
    }

    //switches between pages (disposing the current page if dispose is true)
    public static void switchPage(Page current, Page newPage, boolean dispose) {
        if (current != null) {
            // حفظ حجم النافذة الحالي
            Dimension currentSize = null;
            if (current.getFrame() != null) {
                currentSize = current.getFrame().getSize();
            }

            if (dispose) {
                disposePage(current);
            } else {
                hidePage(current);
            }

            // عرض الصفحة الجديدة
            showPage(newPage);

            // استعادة حجم النافذة لو كان محفوظ
            if (currentSize != null && newPage.getFrame() != null) {
                newPage.getFrame().setSize(currentSize);
            }
        } else {
            showPage(newPage);
        }
    }

    //shows cached page if it exists, otherwise creates a new one
    public static void showPage(Page page) {
        if (page == null) return;
        if (!openedPages.contains(page)) {
            page.init();
            page.setVisible(true);
            openedPages.add(page);
        } else if (!page.isVisible()) {
            page.setVisible(true);
        }
    }

    public static void preLoadPage(Page page){
        if (!openedPages.contains(page)){
            page.init();
            openedPages.add(page);
        }
    }

    //hides page if it exists, keeping it cached in memory
    public static void hidePage(Page page) {
        if (page != null) page.setVisible(false);
    }

    //closes the page and removes it from the opened pages set (and from memory)
    public static void disposePage(Page page) {
        if (page != null) {
            page.dispose();
            openedPages.remove(page);
        }
        if (openedPages.isEmpty()) {
            System.exit(0);
        }
    }

    //Ensures that when a page is disposed that only that page disposes, and not all pages.
    public static void registerFrameCloseHandler(Page page, JFrame frame) {
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                disposePage(page);
            }
        });
    }

    //returns all opened pages
    public static HashSet<Page> getOpenedPages() {
        return openedPages;
    }
}
