package gui;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageLoader {
    public static BufferedImage image, entities, player, enemy, myPlane;

    public static void init() {
        image = imageLoader("/cyan.png");
        entities = imageLoader("/airplane.png");
        enemy = entities.getSubimage(0, 0,85, 90);
        player = entities.getSubimage(85, 0, 95, 90);
        myPlane = imageLoader("/airplane3.png").getSubimage(0,0,95,90);
    }

    public static BufferedImage imageLoader(String path) {
        try {
            System.out.println(path);
            System.out.println( ImageLoader.class.getResource(ImageLoader.class.getSimpleName() + ".class") );
            return ImageIO.read(ImageLoader.class.getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

}
