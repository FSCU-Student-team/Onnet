package Pages;

import Game.PageComponentAdapter;
import Game.PageManager;
import Renderers.MenuBackground;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SingleOrCoopSelectPage implements Page {

    private JFrame frame;
    private JButton singleButton, multiButton;
    private Runnable onSingle, onMulti;

    private GLJPanel canvas;
    private FPSAnimator animator;

    @Override
    public void init() {
        setupFrame();
        addComponents();
        addListeners();
        setupAnimator();
    }

    @Override
    public void setupFrame() {
        frame = new JFrame("Main Menu");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(true);
        frame.setMinimumSize(new Dimension(800, 600));
        PageManager.registerFrameCloseHandler(this, frame);
        frame.pack();           // Ensure correct sizing
        frame.setLocationRelativeTo(null); // Center on screen
        frame.setResizable(false); // <-- disable resizing
    }

    @Override
    public void setupAnimator() {
        if (canvas != null) {
            animator = new FPSAnimator(canvas, 60);
            animator.start();
        }
    }

    @Override
    public void addComponents() {
        // 1. Create canvas and renderer
        canvas = new GLJPanel();
        MenuBackground renderer = new MenuBackground();
        canvas.addGLEventListener(renderer);
        canvas.addKeyListener(renderer.inputManager);
        canvas.addMouseListener(renderer.inputManager);
        canvas.addMouseMotionListener(renderer.inputManager);

        // 2. Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Add GLJPanel to center (fills window)
        mainPanel.add(canvas, BorderLayout.CENTER);

        // 3. Button panel (transparent) using GridBagLayout
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false); // allow background to show

        singleButton = new JButton("Single");
        multiButton = new JButton("Multi");
        Dimension buttonSize = new Dimension(200, 120);
        singleButton.setPreferredSize(buttonSize);
        multiButton.setPreferredSize(buttonSize);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 50, 0, 50);

        gbc.gridx = 0;
        buttonPanel.add(singleButton, gbc);

        gbc.gridx = 1;
        buttonPanel.add(multiButton, gbc);

        // 4. Wrap canvas + buttons in JLayeredPane to allow layering
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(null); // use absolute positioning

        // Set bounds to fill frame
        canvas.setBounds(0, 0, 800, 600);
        buttonPanel.setBounds(0, 0, 800, 600);

        layeredPane.add(canvas, Integer.valueOf(0));      // bottom
        layeredPane.add(buttonPanel, Integer.valueOf(1)); // top

        // Add layeredPane to frame
        frame.setContentPane(layeredPane);

        // Add a component listener to resize canvas and buttonPanel when window resizes
        frame.addComponentListener(new PageComponentAdapter(this));
    }




    @Override
    public void addListeners() {
        singleButton.addActionListener(e -> {
            if (onSingle != null) onSingle.run();
        });

        multiButton.addActionListener(e -> {
            if (onMulti != null) onMulti.run();
        });
    }

    @Override
    public void handleEvents(ActionEvent e) {}

    @Override
    public void dispose() {
        if (animator != null && animator.isStarted()) animator.stop();
        frame.dispose();
    }

    @Override
    public boolean isVisible() {
        return frame.isVisible();
    }

    @Override
    public void setVisible(boolean b) {
        frame.setVisible(b);
    }

    @Override
    public void redraw() {
        if (canvas != null) canvas.repaint();
    }

    public void setSinglePlayerButtonAction(Runnable r) {
        this.onSingle = r;
    }

    public void setCoopButtonAction(Runnable r) {
        this.onMulti = r;
    }

    public JFrame getFrame() {
        return frame;
    }
}