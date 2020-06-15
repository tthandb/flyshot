package model;

public class Level {
    public static int speed = 1;

    public static void setSpeed(int level) {
        switch (level) {
            case 1 -> Level.speed = 1;
            case 2 -> Level.speed = 2;
            case 3 -> Level.speed = 4;
            case 4 -> Level.speed = 8;
        }
    }

}
