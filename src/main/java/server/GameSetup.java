package server;

import model.PlayerInGame;
import packet.InGameAction;
import packet.InGameUpdate;

import java.util.Map;

public class GameSetup implements Runnable {
    int numberOfPlayers;
    private Thread thread;
    private boolean flag;
    private GameManager gameManager;

    public GameSetup(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    @Override
    public void run() {
        gameManager = new GameManager(numberOfPlayers);
        gameManager.init();
        int fps = 144;
        double timePerTick = 1e9 / fps;
        double delta = 0;
        long current = System.nanoTime();
        while (flag) {
            delta = delta + (System.nanoTime() - current) / timePerTick;
            current = System.nanoTime();
            if (delta >= 1) {
                tick();
                delta--;
            }
        }
    }

    public synchronized void start() {
        if (!flag) {
            flag = true;
            if (thread == null) {
                thread = new Thread(this);
                thread.start();
            }
        }

    }

    public synchronized void stop() {
        if (flag) {
            flag = false;
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void tick() {
        gameManager.tick();
        sendNewInGameStateToClients();
    }

    private void sendNewInGameStateToClients() {
        InGameUpdate inGameUpdate = new InGameUpdate(gameManager.playerInGames, GameManager.bullets, GameManager.enemies);
        for (Map.Entry<Integer, Connection> entry : ConnectionList.connections.entrySet()) {
            Connection connection = entry.getValue();
            connection.sendObject(inGameUpdate);
        }
    }

    public void controlPlayerAction(InGameAction inGameAction) {
        PlayerInGame player = gameManager.playerInGames.stream().filter(e -> e.id == inGameAction.playerId).findAny().orElse(null);
        if (player != null) {
            switch (inGameAction.action) {
                case FIRE_PRESSED -> player.fire = true;
                case FIRE_RELEASED -> player.fire = false;
                case LEFT_PRESSED -> player.left = true;
                case LEFT_RELEASED -> player.left = false;
                case RIGHT_PRESSED -> player.right = true;
                case RIGHT_RELEASED -> player.right = false;
                case UP_PRESSED -> player.up = true;
                case UP_RELEASED -> player.up = false;
                case DOWN_PRESSED -> player.down = true;
                case DOWN_RELEASED -> player.down = false;
                default -> System.out.println("Unknown key press");
            }
        }
    }
}
