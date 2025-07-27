
// BrownRabbitPlayer.java
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Set;

public class BrownRabbitPlayer extends Player {
    private enum Direction {
        NONE, LEFT, RIGHT, BACK, UP
    }

    private Direction currentDirection = Direction.NONE;

    private final BufferedImage[] walkDown;
    private final BufferedImage[] walkUp;
    private final BufferedImage[] walkLeft;
    private final BufferedImage[] walkRight;
    private final BufferedImage[] idle;

    public BrownRabbitPlayer(ActionStrategy a, MissStrategy m) {
        // ПЕРЕДАЁМ sheet в super!
        super(a, m, Assets.brownRabbitSheet);

        idle = sheet.getRow(0);
        walkUp = sheet.getRow(1);
        walkLeft = sheet.getRow(2);
        walkRight = sheet.getRow(3);
        walkDown = sheet.getRow(4);
    }

    @Override
    public void update(Set<Integer> keys) {
        super.update(keys);
        if (keys.contains(KeyEvent.VK_LEFT))
            currentDirection = Direction.LEFT;
        else if (keys.contains(KeyEvent.VK_RIGHT))
            currentDirection = Direction.RIGHT;
        else if (keys.contains(KeyEvent.VK_DOWN))
            currentDirection = Direction.BACK;
        else if (keys.contains(KeyEvent.VK_UP))
            currentDirection = Direction.UP;
        else
            currentDirection = Direction.NONE;
    }

    @Override
    public void render(int[] pixels, int w, int h) {
        int f = getAnimationFrame();
        BufferedImage img;
        switch (currentDirection) {
            case RIGHT:
                img = walkRight[f];
                break;
            case LEFT:
                img = walkLeft[f];
                break;
            case BACK:
                img = walkDown[f];
                break;
            case UP:
                img = walkUp[f];
                break;
            default:
                img = idle[f];
                break;
        }
        Sprite.drawImage(pixels, w, h, getX(), getY(), img);
    }
}
