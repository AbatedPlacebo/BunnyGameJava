
// BrownRabbitPlayer.java
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Set;

public class BrownRabbitPlayer extends Player {
    private enum Direction {
        NONE, LEFT, RIGHT
    }

    private Direction currentDirection = Direction.RIGHT;
    private Direction lastDirection = Direction.RIGHT; // по умолчанию смотрит вправо

    private final BufferedImage[] walkLeft;
    private final BufferedImage[] walkRight;
    private final BufferedImage[] idleRight;
    private final BufferedImage[] idleLeft;

    public BrownRabbitPlayer(ActionStrategy a, MissStrategy m) {
        // ПЕРЕДАЁМ sheet в super!
        super(a, m, Assets.brownRabbitSheet);

        idleRight = sheet.getRow(0);
        walkRight = sheet.getRow(1);
        walkLeft = sheet.getRow(2);
        idleLeft = sheet.getRow(3);
    }

    @Override
    public void update(Set<Integer> keys) {
        super.update(keys);

        if (keys.contains(KeyEvent.VK_LEFT)) {
            currentDirection = Direction.LEFT;
            lastDirection = Direction.LEFT;
        } else if (keys.contains(KeyEvent.VK_RIGHT)) {
            currentDirection = Direction.RIGHT;
            lastDirection = Direction.RIGHT;
        }
    }

    @Override
    public void render(int[] pixels, int w, int h) {
        int f = getAnimationFrame();
        BufferedImage img;

        if (isMoving() || isJumping()) {
            switch (currentDirection) {
                case RIGHT:
                    img = walkRight[f];
                    break;
                case LEFT:
                    img = walkLeft[f];
                    break;
                default:
                    img = idleRight[f]; // запасной случай, не должен вызываться
                    break;
            }
        } else {
            switch (lastDirection) {
                case RIGHT:
                    img = idleRight[f];
                    break;
                case LEFT:
                    img = idleLeft[f];
                    break;
                default:
                    img = idleRight[f]; // по умолчанию
                    break;
            }
        }

        Sprite.drawImage(pixels, w, h, getX(), getY(), img);
    }
}
