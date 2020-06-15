package gui;

import client.EventBusClass;
import client.Player;
import com.google.common.eventbus.Subscribe;
import model.Constants;
import packet.*;
import server.RoomServer;
import server.WaitingClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class RoomGUI extends JPanel implements ActionListener {
    private ManagerGUI managerGUI;
    private JButton startGameBtn;
    private JButton readyBtn;
    private ArrayList<SlotGUI> slotGUIS;
    private final int[] slotY = {280, 350, 420};
    private JComboBox<String> levelSelector;

    private final Vector<String> levels;

    private RoomServer roomServer;

    private Player player;
    public String playerName;

    public RoomGUI(int width, int height, HashMap<String, Object> args) {
        levels = new Vector<>();
        levels.add("Easy");
        levels.add("Medium");
        levels.add("Hard");
        levels.add("Super");
        setSize(width, height);
        setLayout(null);
        setBackground(Color.decode("#fee9e9"));
        initUI();
        setVisible(false);
        if (args != null) {
            boolean isHost = false;
            if (args.containsKey("isHost")) {
                if ((Boolean) args.get("isHost")) {
                    initRoomServer();
                    renderHostGUI();
                    isHost = (boolean) args.get("isHost");
                }
            }

            if (args.containsKey("playerName")) {
                this.playerName = args.get("playerName").toString();
                initPlayer(isHost);
            }
        }
    }

    private void renderHostGUI() {
        startGameBtn = new JButton("Start");
        startGameBtn.setBounds(50, 40, 70, 25);
        startGameBtn.setFont(new Font(Constants.NORMAL_FONT, Font.BOLD, 12));
        startGameBtn.setBackground(Color.decode("#E1FFFF"));
        startGameBtn.addActionListener(this);
        levelSelector.setEnabled(true);

        add(startGameBtn);
        remove(readyBtn);
    }

    private void initRoomServer() {
        roomServer = new RoomServer(Constants.HOST_PORT);
        roomServer.start();
    }

    private void initPlayer(boolean isHost) {
        player = new Player("localhost", Constants.HOST_PORT);
        player.isReady = isHost;
        player.playerName = this.playerName;
        player.connect();
        try {
            AddConnectionRequest addConnectionRequest = new AddConnectionRequest(playerName, isHost);
            player.sendObject(addConnectionRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestChangeLevel(String levelName) {
        ChangeLevel packet = new ChangeLevel(levels.indexOf(levelName));
        player.sendObject(packet);
    }

    private void initUI() {
        readyBtn = new JButton("Ready");
        JSeparator separator = new JSeparator();
        slotGUIS = new ArrayList<>();
        for (int i = 0; i < slotY.length; ++i) {
            slotGUIS.add(new SlotGUI(50, slotY[i]));
            add(slotGUIS.get(i));
        }

        levelSelector = new JComboBox<>(levels);
        levelSelector.setBounds(350, 40, 100, 25);
        levelSelector.setSelectedIndex(0);
        levelSelector.setEditable(false);
        levelSelector.setBackground(Color.decode("#E1FFFF"));
        levelSelector.setFont(new Font(Constants.NORMAL_FONT, Font.BOLD, 12));
        levelSelector.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String levelName = e.getItem().toString();
                requestChangeLevel(levelName);
            }
        });
        levelSelector.setEnabled(false);

        readyBtn.setBounds(20, 540, 110, 25);
        readyBtn.setFont(new Font(Constants.NORMAL_FONT, Font.PLAIN, 26));
        separator.setBounds(20, 525, 860, 10);

        readyBtn.addActionListener(this);

        add(levelSelector);
        add(readyBtn);
        add(separator);

        EventBusClass.getInstance().register(this);
    }

    public void renderPlayerList(ArrayList<WaitingClient> clients) {
        System.out.println("-----------------------------RENDER-----------------------------");
        for (int i = 0; i < Constants.MAX_ROOM_SIZE; i++) {
            if (i < clients.size()) {
                WaitingClient client = clients.get(i);
                if (client != null) {
                    SlotGUI holder = slotGUIS.get(i);
                    holder.getPlayerNameLb().setText(client.playerName);
                    holder.setFocusPlayer(client.playerName.equals(this.playerName));
                    holder.setReadyIcon(client.isReady);

                }
            } else {
                SlotGUI holder = slotGUIS.get(i);
                holder.getPlayerNameLb().setText("No Player");
                holder.setReadyIcon(false);
                holder.setFocusPlayer(false);
            }
        }
    }

    private void renderGameLevel(int level) {
        levelSelector.setSelectedIndex(level);
    }

    private void exitRoom() {
        exitScreen();
        roomServer.shutdown();
    }

    private void backToHome() {
        if (managerGUI == null)
            managerGUI = ManagerGUI.getInstance();
        managerGUI.navigate(Constants.HOME_SCREEN);
        if (roomServer != null)
            exitRoom();
        if (player != null)
            player.close();
        exitScreen();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startGameBtn) {
            player.sendStartGameRequest(1);
            startGame();
        } else if (e.getSource() == readyBtn) {
            player.notifyReadyState(!player.isReady);
        }
    }

    private void startGame() {
        HashMap<String, Object> args = new HashMap<>();
        args.put("player", player);
        ManagerGUI.getInstance().navigate(Constants.INGAME_SCREEN, args);
    }

    private void exitScreen() {
        EventBusClass.getInstance().unregister(this);
    }

    private void display() {
        setVisible(true);
    }

    @Subscribe
    public void onReceiveAddConnectionResponse(AddConnectionResponse packet) {
        if (packet.isConnectSuccess) {
            display();
        } else {
            JOptionPane.showMessageDialog(this, packet.message, "Server Message", JOptionPane.WARNING_MESSAGE);
            player.close();
            backToHome();
        }
    }


    @Subscribe
    public void onUpdateRoomInfoEvent(RoomUpdate roomUpdateInfoPacket) {
        renderPlayerList(roomUpdateInfoPacket.clients);
        renderGameLevel(roomUpdateInfoPacket.level);
    }

    @Subscribe
    public void onClosedServerEvent(ServerClosure serverClosure) {
        JOptionPane.showMessageDialog(this, "RoomGUI is closed by host!", "RoomGUI Closed", JOptionPane.WARNING_MESSAGE);
        backToHome();
    }

    @Subscribe
    public void onStartGameEvent(StartGameResponse startGameEvent) {
        startGame();
    }

}
