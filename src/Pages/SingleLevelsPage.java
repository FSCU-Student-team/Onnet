package Pages;

import Game.PageManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SingleLevelsPage implements Page {

    private JFrame frame;
    private JButton backBtn;
    private JButton[] levelBtns = new JButton[6]; // 6 مستويات
    private Runnable onBack;
    private Runnable[] onLevel = new Runnable[6]; // لكل مستوى Runnable

    @Override
    public void init() {
        setupFrame();
        addComponents();
        addListeners();
    }

    @Override
    public void setupFrame() {
        frame = new JFrame("Single Player Levels");
        frame.setSize(800, 600);
        frame.setLayout(new GridBagLayout());
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        frame.setResizable(true);
        frame.setMinimumSize(new Dimension(800, 600));

        PageManager.registerFrameCloseHandler(this, frame);
    }

    @Override
    public void setupAnimator() {}

    @Override
    public void addComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;

        // زرار Back فوق على اليمين
        backBtn = new JButton("Back");
        backBtn.setPreferredSize(new Dimension(200, 120));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.insets = new Insets(10, 400, 800, 100); // نفس مكان MainMenu
        frame.add(backBtn, gbc);

        // إنشاء 6 مستويات
        for (int i = 0; i < 6; i++) {
            levelBtns[i] = new JButton("Level " + (i + 1));
            levelBtns[i].setPreferredSize(new Dimension(200, 120));}

        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;

            gbc.gridx = 0;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.insets = new Insets(20, 50, 0, 0);
            frame.add(levelBtns[0], gbc);
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(80, 50, 0, 0);
        frame.add(levelBtns[1], gbc);
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(140, 50, 0, 0);
        frame.add(levelBtns[2], gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 50, 0, 0);
        frame.add(levelBtns[3], gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(80, 50, 0, 0);
        frame.add(levelBtns[4], gbc);

        gbc.gridx = 1;
           // الصفوف تبدأ من 1 لأن الصف 0 فيه Back
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.insets = new Insets(140, 50, 0, 0);
            frame.add(levelBtns[5], gbc);
        }


    @Override
    public void addListeners() {
        // زرار Back
        backBtn.addActionListener(e -> {
            if (onBack != null) onBack.run();
        });

        // زرارات Levels
        for (int i = 0; i < 6; i++) {
            final int idx = i;
            levelBtns[i].addActionListener(e -> {
                if (onLevel[idx] != null) onLevel[idx].run();
            });
        }
    }

    @Override
    public void handleEvents(ActionEvent e) {}

    @Override
    public void dispose() { frame.dispose(); }

    @Override
    public boolean isVisible() { return frame.isVisible(); }

    @Override
    public void setVisible(boolean b) { frame.setVisible(b); }

    @Override
    public void redraw() {}

    // ربط زرار Back
    public void setBackButtonAction(Runnable r) { this.onBack = r; }

    // ربط كل زرار مستوى
    public void setLevelAction(int levelIndex, Runnable r) {
        if (levelIndex >= 0 && levelIndex < 6) {
            onLevel[levelIndex] = r;
        }
    }
    public JFrame getFrame() {
        return frame;
    }

}
