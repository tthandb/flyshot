package model;

import gui.ImageLoader;
import server.GameManager;

import java.awt.*;
import java.io.Serializable;

public class PlayerInGame implements Serializable {
    private static final long serialVersionUID = 1L;

    public final int id;
    private int x;
    private int y;
    private final int position;

    private long current;
    private long delay;
    private int lives;
    public boolean left, right, fire, up, down;
    private int score = 0;

    public PlayerInGame(int x, int y, int id, int position) {
        this.id = id;
        this.position = position;
        this.x = x;
        this.y = y;
    }

    public void init() {
        this.current = System.nanoTime();
        this.delay = 100;
        this.lives = 3;
        this.score = 0;
    }

    public void tick() {
        if (lives > 0) {
            if (left && x >= 50) x -= 3;
            if (right && x <= 450 - 30) x += 3;
            if (down && y <= 420) y += 3;
            if (up && y >= 100) y -= 3;
            if (fire) {
                double breaks = (System.nanoTime() - current) / 1e6;
                if (breaks > delay) {
                    GameManager.bullets.add(new Bullet(x + 5, y));
                    current = System.nanoTime();
                }
            }
        }
    }

    public void render(Graphics graphics) {
        if (this.lives > 0) {
            if (isMe()) {
                graphics.drawImage(ImageLoader.myPlane, x, y, 30, 30, null);
            } else {
                graphics.drawImage(ImageLoader.player, x, y, 30, 30, null);
            }
        }
    }

    private boolean isMe() {
        return id == AppPreferences.UID;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getLives() {
        return this.lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getScore() {
        return this.score;
    }

    public void incScore() {
        this.score = this.score + 1;
    }

    public int getPosition() {
        return position;
    }
}
