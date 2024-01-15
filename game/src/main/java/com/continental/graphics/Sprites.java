package com.continental.graphics;

import lombok.Data;
import lombok.val;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Data
public class Sprites {

    private final BufferedImage rightBluePlayerSprite;
    private final BufferedImage leftBluePlayerSprite;
    private final BufferedImage rightRedPlayerSprite;
    private final BufferedImage leftRedPlayerSprite;
    private final BufferedImage rightBulletSprite;
    private final BufferedImage leftBulletSprite;
    private final BufferedImage background;
    private final BufferedImage platformTile;

    public Sprites() {
        try {
            leftBluePlayerSprite = ImageIO.read(
                    new File("game/src/main/resources/blue-player.png")
            );
            rightBluePlayerSprite = getReflectedImage(leftBluePlayerSprite);

            leftRedPlayerSprite = ImageIO.read(
                    new File("game/src/main/resources/red-player.png"))
            ;
            rightRedPlayerSprite = getReflectedImage(leftRedPlayerSprite);

            leftBulletSprite = ImageIO.read(new File("game/src/main/resources/bullet.png"));
            rightBulletSprite = getReflectedImage(leftBulletSprite);

            background = ImageIO.read(new File("game/src/main/resources/background.png"));

            platformTile = ImageIO.read(new File("game/src/main/resources/platform.png"));
        } catch (IOException e) {
            System.out.println("[GRAPHICS] Error during loading sprites data");
            throw new RuntimeException();
        }
    }

    private BufferedImage getReflectedImage(BufferedImage originalImage) {
        val transform = AffineTransform.getScaleInstance(-1, 1);
        transform.translate(-originalImage.getWidth(), 0);

        val transformOp = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return transformOp.filter(originalImage, null);
    }
}
