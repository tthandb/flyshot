package model;

import gui.ImageLoader;

import java.awt.*;
import java.io.Serializable;

public class Enemy implements Serializable {

    private static final long serialVersionUID = 1L;
    private final int x;
    private int y;

    public Enemy(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void tick() {
        y = y + Level.speed;
    }

    public void render(Graphics g) {
        g.drawImage(ImageLoader.enemy, x, y, 25, 25, null);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
