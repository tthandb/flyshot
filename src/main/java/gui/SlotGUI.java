package gui;

import model.Constants;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class SlotGUI extends JPanel {
    private JLabel playerNameLb;
    private JPanel readyIcon;

    public SlotGUI(int x, int y) {
        setLayout(null);
        setSize(200, 300);
        setBorder(new LineBorder(Color.GRAY));
        setBounds(x, y, Constants.SLOT_WIDTH, Constants.SLOT_HEIGHT);
        setBackground(Color.decode("#E1FFFF"));
        initUI();
    }

    private void initUI() {
        playerNameLb = new JLabel("No PlayerInGame", SwingConstants.CENTER);
        playerNameLb.setBounds(20, 100, 160, 30);
        playerNameLb.setFont(new Font(Constants.NORMAL_FONT, Font.PLAIN, 14));

        readyIcon = new JPanel();
        readyIcon.setBounds(370, 0, 30, 50);
        readyIcon.setBorder(new LineBorder(Color.GRAY, 3));
        readyIcon.setBackground(Color.CYAN);
        readyIcon.setOpaque(false);

        add(playerNameLb);
        add(readyIcon);
    }

    public void setFocusPlayer(boolean isFocused) {
        if (isFocused) {
            setBorder(new LineBorder(Color.PINK, 3));
        } else {
            setBorder(new LineBorder(Color.GRAY));
        }

    }

    public void setReadyIcon(boolean isReady) {
        readyIcon.setOpaque(isReady);
        repaint();
    }

    public JLabel getPlayerNameLb() {
        return playerNameLb;
    }
}
