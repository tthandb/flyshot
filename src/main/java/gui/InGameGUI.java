package gui;

import client.EventBusClass;
import client.Player;
import com.google.common.eventbus.Subscribe;
import model.*;
import packet.InGameAction;
import packet.StartGameResponse;
import packet.UpdateInGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.HashMap;

public class InGameGUI extends JPanel implements ActionListener, KeyListener {
    private ArrayList<PlayerInGame> playerInGames;
    private ArrayList<Bullet> bullets;
    private ArrayList<Enemy> enemies;

    public static Canvas canvas;

    private Player player;

    public InGameGUI(int width, int height, HashMap<String, Object> args) {
        if (args != null && args.containsKey("player")) {
            player = (Player) args.get("player");
        } else {
            JOptionPane.showMessageDialog(this, "Player in InGameScreen is null", "No Connection", JOptionPane.WARNING_MESSAGE);
        }
        setBackground(Color.decode("#fee9e9"));
        setSize(width, height);
        setVisible(true);

        initObjectList();
        renderCanvas();

        EventBusClass.getInstance().register(this);

        ManagerGUI.getInstance().getWindow().addKeyListener(this);
        ManagerGUI.getInstance().getWindow().setFocusable(true);
    }

    private void initPlayer() {
        player = new Player(AppPreferences.HOST_IP, Constants.HOST_PORT);
        player.connect();
    }

    private void initObjectList() {
        playerInGames = new ArrayList<>();
        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
    }

    @Override
    protected void finalize() throws Throwable {
        ManagerGUI.getInstance().getWindow().removeKeyListener(this);
        super.finalize();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keycode = e.getKeyCode();
        if (keycode == KeyEvent.VK_SPACE) {
            player.sendObject(new InGameAction(InGameAction.Action.FIRE_PRESSED));
        } else if (keycode == KeyEvent.VK_LEFT) {
            player.sendObject(new InGameAction(InGameAction.Action.LEFT_PRESSED));
        } else if (keycode == KeyEvent.VK_RIGHT) {
            player.sendObject(new InGameAction(InGameAction.Action.RIGHT_PRESSED));
        } else if (keycode == KeyEvent.VK_UP) {
            player.sendObject(new InGameAction(InGameAction.Action.UP_PRESSED));
        } else if (keycode == KeyEvent.VK_DOWN) {
            player.sendObject(new InGameAction(InGameAction.Action.DOWN_PRESSED));
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keycode = e.getKeyCode();
        if (keycode == KeyEvent.VK_SPACE) {
            player.sendObject(new InGameAction(InGameAction.Action.FIRE_RELEASED));
        } else if (keycode == KeyEvent.VK_LEFT) {
            player.sendObject(new InGameAction(InGameAction.Action.LEFT_RELEASED));
        } else if (keycode == KeyEvent.VK_RIGHT) {
            player.sendObject(new InGameAction(InGameAction.Action.RIGHT_RELEASED));
        } else if (keycode == KeyEvent.VK_UP) {
            player.sendObject(new InGameAction(InGameAction.Action.UP_RELEASED));
        } else if (keycode == KeyEvent.VK_DOWN) {
            player.sendObject(new InGameAction(InGameAction.Action.DOWN_RELEASED));
        }
    }

    private void renderCanvas() {
        canvas = new Canvas();
        canvas.setFocusable(false);
        canvas.setPreferredSize(new Dimension(Constants.IN_GAME_SCREEN_WIDTH, Constants.IN_GAME_SCREEN_HEIGHT));
        add(canvas);
        canvas.setVisible(true);
        ImageLoader.init();
    }

    private void renderUI() {
        BufferStrategy buffer = canvas.getBufferStrategy();
        if (buffer == null) {
            canvas.createBufferStrategy(3);
            System.out.println("buffer is NULL");
            return;
        }
        Graphics graphics = buffer.getDrawGraphics();
        graphics.clearRect(0, 0, Constants.IN_GAME_SCREEN_WIDTH, Constants.IN_GAME_SCREEN_HEIGHT);

        // draw
        graphics.drawImage(ImageLoader.image, 50, 50, Constants.GAME_WIDTH, Constants.GAME_HEIGHT, null);
        renderObjects(graphics);
        // end of draw

        buffer.show();
        graphics.dispose();
    }

    private void renderObjects(Graphics g) {
        for (Enemy e : enemies) {
            if (e.getX() >= 50 && e.getX() <= 450 - 25 && e.getY() <= 450 - 25 && e.getY() >= 50) {
                e.render(g);
            }
        }

        for (PlayerInGame player : playerInGames) {
            player.render(g);
        }

        for (Bullet bullet : bullets) {
            bullet.render(g);
        }
        g.setFont(new Font(Constants.NORMAL_FONT, Font.BOLD, 12));
        g.setColor(Color.BLACK);
        g.drawString(getPlayersScore(), 200, 500);
    }

    private String getPlayersScore() {
        int totalScore = playerInGames.stream().mapToInt(PlayerInGame::getScore).sum();
        return String.format("Total score: %d", totalScore);
    }

    private String getLives() {
        int lives = PlayerInGame::getLives
    }

    @Subscribe
    public void onGameStartEvent(StartGameResponse startGameResponsePacket) {
        renderCanvas();
    }

    @Subscribe
    public void onUpdateInGameInfoEvent(UpdateInGame event) {
//        System.out.println(String.format("IngameScreen - receive update game info event: %d players - %d bullets - %d enemies", event.playerInGames.size(), event.bullets.size(), event.enemies.size()));

        this.playerInGames.clear();
        this.bullets.clear();
        this.enemies.clear();

        this.playerInGames.addAll(event.playerInGames);
        this.bullets.addAll(event.bullets);
        this.enemies.addAll(event.enemies);
        renderUI();
    }

    private void exitScreen() {
        EventBusClass.getInstance().unregister(this);
    }

}
