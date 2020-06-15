package gui;

import model.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class HomeGUI extends JPanel implements ActionListener {

    private ManagerGUI managerGUI;
    private JButton createGameBtn;
    private JButton joinGameBtn;
    private JButton quitGameBtn;

    public HomeGUI(int width, int height) {
        setSize(width, height);
        setLayout(null);
        initUI();
        setVisible(true);
    }

    private void initUI() {
        setBackground(Color.decode("#fee9e9"));
        createGameBtn = new JButton("Create");
        joinGameBtn = new JButton("Join");
        quitGameBtn = new JButton("Quit");

        JLabel titleLb = new JLabel("FLYSHOT", SwingConstants.CENTER);

        createGameBtn.setBounds(200, 320, 100, 50);
        createGameBtn.setFont(new Font(Constants.NORMAL_FONT, Font.PLAIN, 20));
        createGameBtn.setBackground(Color.decode("#E1FFFF"));
        joinGameBtn.setBounds(200, 396, 100, 50);
        joinGameBtn.setFont(new Font(Constants.NORMAL_FONT, Font.PLAIN, 20));
        joinGameBtn.setBackground(Color.decode("#E1FFFF"));
        quitGameBtn.setBounds(200, 472, 100, 50);
        quitGameBtn.setFont(new Font(Constants.NORMAL_FONT, Font.PLAIN, 20));
        quitGameBtn.setBackground(Color.decode("#E1FFFF"));

        titleLb.setBounds(50, 100, 390, 100);
        titleLb.setFont(new Font(Constants.NORMAL_FONT, Font.BOLD, 46));

        quitGameBtn.addActionListener(this);
        createGameBtn.addActionListener(this);
        joinGameBtn.addActionListener(this);

        add(createGameBtn);
        add(joinGameBtn);
        add(quitGameBtn);
        add(titleLb);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == quitGameBtn) {
            System.exit(0);
        } else if (e.getSource() == createGameBtn) {
            joinRoom(true);
        } else if (e.getSource() == joinGameBtn) {
            joinRoom(false);
        }
    }

    private String enterPlayerName() {
        String name = JOptionPane.showInputDialog(this, "Enter player name:", "PlayerInGame Name", JOptionPane.QUESTION_MESSAGE);
        if (name == null) {
            JOptionPane.showMessageDialog(this, "Please enter a nickname before starting game!");
        } else if (name.length() > 16) {
            JOptionPane.showMessageDialog(this, "Your nickname is too long(must be shorter than 16!");
        } else {
            return name;
        }
        return null;
    }

    private void joinRoom(Boolean isHost) {
        String playerName = enterPlayerName();
        if (playerName != null) {
            HashMap<String, Object> args = new HashMap<>();
            args.put("playerName", playerName);
            args.put("isHost", isHost);
            if (managerGUI == null)
                managerGUI = ManagerGUI.getInstance();
            managerGUI.navigate(Constants.NEW_ROOM_SCREEN, args);
        }
    }
}
