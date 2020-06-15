package gui;

import model.Constants;

import javax.swing.*;
import java.util.HashMap;

public class ManagerGUI {
    private static ManagerGUI instance;
    private final JFrame window;
    private HomeGUI homeGUI;
    private RoomGUI roomGUI;

    public static ManagerGUI getInstance() {
        if (instance == null) {
            synchronized (ManagerGUI.class) {
                if (null == instance) {
                    instance = new ManagerGUI();
                }
            }
        }
        return instance;
    }

    public void display() {
        window.setVisible(true);
    }

    private ManagerGUI() {
        window = new JFrame("Flyshot");
        window.setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(false);
        navigate(Constants.HOME_SCREEN);

    }

    public void navigate(String screenName) {
        navigate(screenName, null);
    }

    public void navigate(String screen, HashMap<String, Object> args) {
        switch (screen) {
            case Constants.HOME_SCREEN -> {
                window.getContentPane().removeAll();
                window.getContentPane().add(getHomeGUI());
                window.revalidate();
                window.repaint();
            }
            case Constants.NEW_ROOM_SCREEN -> {
                String playerName;
                Boolean isHost = false;
                if (args != null) {
                    if (args.containsKey("playerName")) playerName = args.get("playerName").toString();
                    else playerName = "PlayerNameDefault";
                    if (args.containsKey("isHost")) isHost = (Boolean) args.get("isHost");
                } else playerName = "PlayerNameDefault";
                window.getContentPane().removeAll();
                window.getContentPane().add(getNewRoomScreen(playerName, isHost));
                window.revalidate();
                window.repaint();
            }
            case Constants.EXISTED_ROOM_SCREEN -> {
                window.getContentPane().removeAll();
                window.getContentPane().add(getRoomGUI());
                window.revalidate();
                window.repaint();
            }
            case Constants.INGAME_SCREEN -> openNewScreen(getInGameScreen(args));
        }

    }

    private void openNewScreen(JPanel screen) {
        window.getContentPane().removeAll();
        window.getContentPane().add(screen);
        window.revalidate();
        window.repaint();
    }

    private synchronized HomeGUI getHomeGUI() {
        if (homeGUI == null) {
            homeGUI = new HomeGUI(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        }
        return homeGUI;
    }

    private synchronized RoomGUI getRoomGUI() {
        if (roomGUI == null)
            roomGUI = new RoomGUI(Constants.GAME_WIDTH, Constants.GAME_HEIGHT, null);
        return roomGUI;
    }

    private synchronized RoomGUI getNewRoomScreen(String playerName, Boolean isHost) {
        HashMap<String, Object> args = new HashMap<>();
        args.put("playerName", playerName);
        args.put("isHost", isHost);
        roomGUI = new RoomGUI(Constants.GAME_WIDTH, Constants.GAME_HEIGHT, args);
        return roomGUI;
    }

    private synchronized JPanel getInGameScreen(HashMap<String, Object> args) {
        return new InGameGUI(Constants.IN_GAME_SCREEN_WIDTH, Constants.IN_GAME_SCREEN_HEIGHT, args);
    }

    public JFrame getWindow() {
        return this.window;
    }
}
