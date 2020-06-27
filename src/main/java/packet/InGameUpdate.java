package packet;

import model.Bullet;
import model.Enemy;
import model.PlayerInGame;

import java.io.Serializable;
import java.util.ArrayList;

public class InGameUpdate implements Serializable {
    private static final long serialVersionUID = 1L;
    public ArrayList<PlayerInGame> playerInGames;
    public ArrayList<Bullet> bullets;
    public ArrayList<Enemy> enemies;

    public InGameUpdate(ArrayList<PlayerInGame> playerInGames, ArrayList<Bullet> bullets, ArrayList<Enemy> enemies) {
        this.playerInGames = new ArrayList<>();
        this.bullets = new ArrayList<>();
        this.enemies = new ArrayList<>();

        this.playerInGames.addAll(playerInGames);
        this.bullets.addAll(bullets);
        this.enemies.addAll(enemies);
    }
}
