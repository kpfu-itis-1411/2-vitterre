package com.continental.gui;

import com.continental.game.ClientGame;
import com.continental.game.ClientInputHandler;
import com.continental.game.entity.BulletEntity;
import com.continental.game.entity.PlatformEntity;
import com.continental.game.entity.PlayerEntity;
import com.continental.graphics.Sprites;
import lombok.val;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ClientGamePanel extends JPanel {

    private final int[][] gameViewRanges = new int[][]{ {0, 10}, {0, 10 }};
    private final ClientGame game;
    private final Sprites sprites = new Sprites();

    public ClientGamePanel(ClientGame game) {
        this.game = game;

        setMinimumSize(new Dimension(ClientMainFrame.WIDTH * ClientMainFrame.SCALE_FACTOR,
                                     ClientMainFrame.HEIGHT * ClientMainFrame.SCALE_FACTOR
        ));

        setMaximumSize(new Dimension(ClientMainFrame.WIDTH * ClientMainFrame.SCALE_FACTOR,
                                     ClientMainFrame.HEIGHT * ClientMainFrame.SCALE_FACTOR
        ));

        setPreferredSize(new Dimension(ClientMainFrame.WIDTH * ClientMainFrame.SCALE_FACTOR,
                                       ClientMainFrame.HEIGHT * ClientMainFrame.SCALE_FACTOR
        ));

        addKeyListener(new ClientInputHandler(game));

        setFocusable(true);
    }


    @Override
    public void paint(Graphics g) {
        val graphics2D = (Graphics2D) g;

        val backgroundImage = sprites.getBackground();
        graphics2D.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);

        for (val entity : game.getEntities().values()) {
            val x1 = remapXCoords(entity.getX());
            val y1 = remapYCoords(entity.getY());
            val x2 = x1 + rescaleWidth(entity.getWidth());
            val y2 = y1 + rescaleHeight(entity.getHeight());

            val minX = Math.min(x1, x2);
            val minY = Math.min(y1, y2);
            val maxX = Math.max(x1, x2);
            val maxY = Math.max(y1, y2);

            val width = maxX - minX;
            val height = maxY - minY;

            BufferedImage sprite;

            switch (entity) {
                case PlatformEntity pfe -> {
                    sprite = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
                    val spriteGraphics2D = sprite.getGraphics();
                    spriteGraphics2D.setColor(new Color(8, 19, 28));
                    spriteGraphics2D.drawRect(0, 0, 1, 1);
                    spriteGraphics2D.dispose();
                }
                case PlayerEntity pe -> sprite = switch (pe.getTeam()) {
                    case RED -> switch (pe.getDirection()) {
                        case LEFT -> sprites.getLeftRedPlayerSprite();
                        case RIGHT -> sprites.getRightRedPlayerSprite();
                    };
                    case BLUE -> switch (pe.getDirection()) {
                        case LEFT -> sprites.getLeftBluePlayerSprite();
                        case RIGHT -> sprites.getRightBluePlayerSprite();
                    };
                };
                case BulletEntity be -> sprite = switch (be.getDirection()) {
                    case LEFT -> sprites.getLeftBulletSprite();
                    case RIGHT -> sprites.getRightBulletSprite();
                };
                default -> {
                    sprite = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
                    val spriteGraphics2D = sprite.getGraphics();
                    spriteGraphics2D.setColor(Color.DARK_GRAY);
                    spriteGraphics2D.drawRect(0, 0, 1, 1);
                    spriteGraphics2D.dispose();
                    System.out.println("Entity of unknown type");
                }
            }

            graphics2D.drawImage(sprite, minX, minY, width, height, null);
        }
    }

    private int remapXCoords(final double gameX) {
        return remap(gameX, gameViewRanges[0][0], gameViewRanges[0][1], 0, getWidth());
    }

    private int remapYCoords(final double gameY) {
        return remap(gameY, gameViewRanges[1][0], gameViewRanges[1][1], getHeight(), 0);
    }

    private int remap(final double initialPoint,
                      final double initialBottom,
                      final double initialTop,
                      final double newBottom,
                      final double newTop
    ) {

        final double ratio = (initialPoint - initialBottom) / (initialTop - initialBottom);

        final double newDist = (newTop - newBottom) * ratio;

        final double newPoint = newBottom + newDist;
        return (int) Math.round(newPoint);
    }

    private int rescaleWidth(final double gameWidth) {
        return rescale(gameWidth, gameViewRanges[0][1] - gameViewRanges[0][0], getWidth());
    }

    private int rescaleHeight(final double gameHeight) {
        return rescale(gameHeight, gameViewRanges[1][1] - gameViewRanges[1][0], -getHeight());
    }

    private int rescale(final double initialLength, final double initialRange, final double newRange) {
        final double ratio = initialLength / initialRange;

        final double newLength = newRange * ratio;
        return (int) Math.round(newLength);
    }
}
