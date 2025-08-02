
// BrownRabbitPlayer.java
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Set;

public class BrownRabbitPlayer extends Player {
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
    public BufferedImage getCurrentFrame() {
        int f = getAnimationFrame();
        if (isMoving() || isJumping()) {
            return (currentDirection == Direction.LEFT) ? walkLeft[f] : walkRight[f];
        } else {
            return (lastDirection == Direction.LEFT) ? idleLeft[f] : idleRight[f];
        }
    }

}
