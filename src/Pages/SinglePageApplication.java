package Pages;

import Pages.ContentPanels.InMenuPopup;
import Pages.ContentPanels.MainMenuPanel;
import Pages.ContentPanels.LevelSelectPanel;
import Pages.ContentPanels.LeaderboardPanel;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SinglePageApplication{

    private final JFrame frame;
    private final GLJPanel canvas;
    private FPSAnimator animator;

    private final JPanel contentPanel;   // swap child pages here
    private final JPanel glassPane;      // overlay buttons
    private final JLayeredPane layeredPane;
    // Reference to panels
    private MainMenuPanel mainMenuPanel;
    private LevelSelectPanel levelSelectPanel;
    private LeaderboardPanel leaderboardPanel;
    private InMenuPopup globalMenu;
    private JButton globalMenuBtn;
    private Runnable onExitLevel; // Callback to reset renderer when leaving a level

    public SinglePageApplication(GLJPanel sharedCanvas, String title) {
        this.canvas = sharedCanvas;

        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);


        // Content panel
        contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BorderLayout());

        // Glass pane for buttons
        glassPane = new JPanel();
        glassPane.setOpaque(false);
        glassPane.setLayout(null);

        // Layered pane
        layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);
        layeredPane.setPreferredSize(new Dimension(800, 600));

        contentPanel.setBounds(0, 0, 800, 600);
        glassPane.setBounds(0, 0, 800, 600);

        layeredPane.add(contentPanel, Integer.valueOf(1));
        layeredPane.add(glassPane, Integer.valueOf(2));

        frame.setContentPane(layeredPane);

        // global menu
        addGlobalMenu();
        makeMenuMouseOnly();
    }

    /**
     * Swap the current content JPanel with a new one
     */
    public void setContent(JPanel newPanel) {
        contentPanel.removeAll();
        contentPanel.add(newPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    /**
     * Add a button (or any component) to the overlay
     */
    public void addOverlayComponent(JComponent comp, int x, int y, int width, int height) {
        comp.setBounds(x, y, width, height);
        glassPane.add(comp);
        glassPane.revalidate();
        glassPane.repaint();
    }

    public void init() {
        setupFrame();
        addComponents();
        addListeners();
        setupAnimator();
    }

    public void setupFrame() {
        // Show frame and buttons first
        frame.setVisible(true);

        // Add GLJPanel asynchronously to avoid blocking EDT
        SwingUtilities.invokeLater(() -> {
            canvas.setBounds(0, 0, frame.getContentPane().getWidth(), frame.getContentPane().getHeight());
            layeredPane.add(canvas, Integer.valueOf(0));
            layeredPane.revalidate();
            layeredPane.repaint();
        });
    }

    public void setupAnimator() {
        if (canvas != null && (animator == null || !animator.isStarted())) {
            animator = new FPSAnimator(canvas, 60);
            animator.start();
            canvas.display();
        }
    }

    public void addComponents() {
        // content and overlay already added in constructor
    }

    public void addListeners() {
        // override in child pages or add listeners to components
    }

    public void handleEvents(ActionEvent e) {
        // override in child pages
    }

    public void dispose() {
        if (animator != null && animator.isStarted()) animator.stop();
        frame.dispose();
    }

    public JFrame getFrame() {
        return frame;
    }

    //sets new level renderer
    public void setLevelRenderer(GLEventListener renderer) {
        for (int i = 0; i < canvas.getGLEventListenerCount(); i++) {
            canvas.removeGLEventListener(canvas.getGLEventListener(i));
        }

        // Add the new level renderer
        canvas.addGLEventListener(renderer);
    }

    /**
     * Prompt the player for their name. Defaults to "Player" if left empty.
     */
    public String askPlayerName() {
        String name = JOptionPane.showInputDialog(frame, "Enter your name:", "Player Name",
                JOptionPane.PLAIN_MESSAGE);
        if (name == null || name.trim().isEmpty()) {
            name = "Player";
        }
        return name;
    }

    private void addGlobalMenu() {
        // Create global menu button
        globalMenuBtn = new JButton("Menu");
        globalMenuBtn.setBackground(new Color(200, 50, 50));
        globalMenuBtn.setForeground(Color.WHITE);
        globalMenuBtn.setFocusPainted(false);
        globalMenuBtn.setFont(new Font("Arial", Font.BOLD, 16));
        globalMenuBtn.setBounds(694, 30, 100, 50);
        glassPane.add(globalMenuBtn);

        // Create global menu
        globalMenu = new InMenuPopup();

        // Set up menu actions
        setupGlobalMenuActions();

        globalMenuBtn.addActionListener(e -> globalMenu.toggleMenu(glassPane));

        globalMenu.setGameCanvas(canvas); // Pass canvas reference
    }

    private void setupGlobalMenuActions() {
        globalMenu.setOnMainMenu(() -> {
            // Return to main menu
            if (mainMenuPanel != null) {
                if (onExitLevel != null) onExitLevel.run(); // Reset renderer
                setContent(mainMenuPanel);
                globalMenu.hideMenu();
            }
        });

        globalMenu.setOnLevels(() -> {
            // Go to level selection
            if (levelSelectPanel != null) {
                if (onExitLevel != null) onExitLevel.run(); // Reset renderer
                setContent(levelSelectPanel);
                globalMenu.hideMenu();
            }
        });

        globalMenu.setOnLeaderboard(() -> {
            // Go to leaderboard
            if (leaderboardPanel != null) {
                if (onExitLevel != null) onExitLevel.run(); // Reset renderer
                setContent(leaderboardPanel);
                globalMenu.hideMenu();
            }
        });

        globalMenu.setOnExit(() -> {
            int choice = JOptionPane.showConfirmDialog(
                    frame,
                    "Exit to desktop?",
                    "Exit Game",
                    JOptionPane.YES_NO_OPTION
            );
            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        // Resume just hides menu
        globalMenu.setOnResume(() -> globalMenu.hideMenu());
    }

    private void hideMenu() {
        if (globalMenu != null && globalMenu.isMenuVisible()) {
            globalMenu.hideMenu();
        }
    }

    public void setPanels(MainMenuPanel mainMenu, LevelSelectPanel levelSelect, LeaderboardPanel leaderboard) {
        this.mainMenuPanel = mainMenu;
        this.levelSelectPanel = levelSelect;
        this.leaderboardPanel = leaderboard;
    }

    public void setOnExitLevel(Runnable callback) {
        this.onExitLevel = callback;
    }

    private void makeMenuMouseOnly() {
        // Disable focus for all menu-related components
        if (globalMenuBtn != null) {
            globalMenuBtn.setFocusable(false);
            globalMenuBtn.setRequestFocusEnabled(false);
        }

        if (glassPane != null) {
            glassPane.setFocusable(false);
        }

        // Ensure canvas stays focusable for game controls
        if (canvas != null) {
            canvas.setFocusable(true);
            canvas.requestFocusInWindow();
        }
    }
}
