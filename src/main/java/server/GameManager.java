package server;

import model.Bullet;
import model.Constants;
import model.Enemy;
import model.PlayerInGame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class GameManager {
    private final int NUMBER_OF_PLAYERS;
    public ArrayList<PlayerInGame> playerInGames;
    public static ArrayList<Bullet> bullets;
    public static ArrayList<Enemy> enemies;

    private long current;
    private long delay;

    public GameManager(int numberOfPlayers) {
        this.NUMBER_OF_PLAYERS = numberOfPlayers;
    }

    public void init() {
        playerInGames = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_PLAYERS; ++i) {
            int distance = (Constants.GAME_WIDTH) / NUMBER_OF_PLAYERS;
            PlayerInGame playerInGame = new PlayerInGame(33 + distance / 2 + (i * distance), Constants.GAME_HEIGHT + 20, i, i);
            playerInGame.init();
            playerInGames.add(playerInGame);
        }

        bullets = new ArrayList<>();
        enemies = new ArrayList<>();

        current = System.nanoTime();
        delay = 800;
    }

    public void tick() {
        for (PlayerInGame playerInGame : playerInGames)
            playerInGame.tick();
        for (Bullet bullet : bullets)
            bullet.tick();
        double breaks = (System.nanoTime() - current) / 1e6;
        if (breaks > delay) {
            for (int i = 0; i < 2; ++i) {
                Random random = new Random();
                enemies.add(new Enemy(random.nextInt(450), -random.nextInt(450)));
            }
            current = System.nanoTime();
        }
        for (Enemy enemy : enemies)
            enemy.tick();
        removeObject();
    }

    private boolean isCollision(Enemy e, Bullet b) {
        return e.getX() < b.getX() + 6 &&
                e.getX() + 25 > b.getX() &&
                e.getY() < b.getY() + 6 &&
                e.getY() + 25 > b.getY();
    }

    private boolean isCollision(PlayerInGame p, Enemy e) {
        return p.getX() < e.getX() + 25 &&
                p.getX() + 30 > e.getX() &&
                p.getY() < e.getY() + 25 &&
                p.getY() + 30 > e.getY();
    }

    private void removeObject() {
        bullets = bullets.stream().filter(bullet -> bullet.getY() > 50).collect(Collectors.toCollection(ArrayList::new));

        Set<PlayerInGame> playerInGameSet = new HashSet<>();
        Set<Enemy> enemySet = new HashSet<>();
        Set<Bullet> bulletSet = new HashSet<>();

        for (Enemy e : enemies) {
            for (PlayerInGame playerInGame : playerInGames) {
                if (isCollision(playerInGame, e)) {
                    enemySet.add(e);
                    playerInGame.setLives(playerInGame.getLives() - 1);
                    System.out.println("P" + playerInGame.getPosition() + " : " + playerInGame.getLives());
                    if (playerInGame.getLives() <= 0) {
                        System.out.println("P" + playerInGame.getPosition() + " : " + "Died");
                        playerInGameSet.add(playerInGame);
                    }
                }
            }

            playerInGames.removeAll(playerInGameSet);
            if (playerInGames.isEmpty()) {
                break;
            }

            for (Bullet b : bullets) {
                if (isCollision(e, b)) {
                    enemySet.add(e);
                    bulletSet.add(b);
                    playerInGames.get(0).incScore();
                }
            }
        }

        if (playerInGames.isEmpty())
            System.out.println("Loss");


        enemies.removeAll(enemySet);
        bullets.removeAll(bulletSet);
    }
}
