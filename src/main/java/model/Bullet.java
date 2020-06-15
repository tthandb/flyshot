package model;

import java.awt.*;
import java.io.Serializable;

public class Bullet implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int x;
    private int y;
    private final int speed;

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
        this.speed = 5;
    }

    public void tick() {
        this.y -= speed;

    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public void render(Graphics graphics) {
        graphics.setColor(Color.PINK);
        graphics.fillRect(x,y,6,10);
        graphics.setColor(Color.cyan);
    }
}
