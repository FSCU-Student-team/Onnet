package Pages;

import Game.PageManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SingleOrMultiPage implements Page {


    private JFrame frame;
    private JButton single, multi;

    private Runnable onSingle, onMulti;

    @Override
    public void init() {
        setupFrame();
        addComponents();
        addListeners();
    }

    @Override
    public void setupFrame() {
        frame = new JFrame("Main Menu");
        frame.setSize(800, 600);
        frame.setLayout(new GridLayout(3, 1, 10, 10));
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(true);
        frame.setMinimumSize(new Dimension(800, 600));


        PageManager.registerFrameCloseHandler(this, frame);
    }

    @Override
    public void setupAnimator() {
    }

    @Override
    public void addComponents() {
        single = new JButton("Single");
        single.setPreferredSize(new Dimension(200, 120));

        multi = new JButton("Multi");
        multi.setPreferredSize(new Dimension(200, 120));

        frame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        single = new JButton("Single");
        single.setPreferredSize(new Dimension(200, 120));
        single.setMinimumSize(new Dimension(200, 120));
        single.setMaximumSize(new Dimension(200, 120));

        multi = new JButton("Multi");
        multi.setPreferredSize(new Dimension(200, 120));
        multi.setMinimumSize(new Dimension(200, 120));
        multi.setMaximumSize(new Dimension(200, 120));

        frame.setLayout(new GridBagLayout());


        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;

// زرار Play
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 200, 0, 200);
        frame.add(single, gbc);

// زرار Levels
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 200, 0, 200);
        frame.add(multi, gbc);

    }

    @Override
        public void addListeners() {
            single.addActionListener(e -> {
                if (onSingle != null) onSingle.run();
            });

            multi.addActionListener(e -> {
                if (onMulti != null) onMulti.run();
            });
        }



    @Override
    public void handleEvents(ActionEvent e){

    }

    @Override
    public void dispose() {
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
    }

    public void setPlayButtonAction(Runnable r) {
        this.onSingle = r;
    }

    public void setLevelsButtonAction(Runnable r) {
        this.onMulti = r;
    }
    public JFrame getFrame() {
        return frame;
    }

}
