import java.util.Set;
import java.awt.image.BufferedImage;

public class FoxNPC extends Entity {
    private enum Direction {
        LEFT, RIGHT
    }

    private Direction currentDirection = Direction.RIGHT;
    private boolean isIdle = false;

    private int dir = 1, speed = 1;
    private int patrolMinX, patrolMaxX;

    private int idleCounter = 0;
    private final int idleTime = 120; // ~1 second if 60 FPS

    private final BufferedImage[] walkRight;
    private final BufferedImage[] walkLeft;
    private final BufferedImage[] idleRight;
    private final BufferedImage[] idleLeft;

    public FoxNPC(int startX, int startY, int minX, int maxX) {
        super(startX, startY, Assets.foxSheet);
        this.patrolMinX = minX;
        this.patrolMaxX = maxX;

        idleRight = sheet.getRow(0);
        walkRight = sheet.getRow(1);
        walkLeft = sheet.getRow(2);
        idleLeft = sheet.getRow(3);
    }

    @Override
    public void update(Set<Integer> keys) {
        // Animate
        animationTick++;
        if (animationTick % 10 == 0) {
            animationFrame = (animationFrame + 1) % walkRight.length;
        }

        if (isIdle) {
            idleCounter++;
            if (idleCounter >= idleTime) {
                isIdle = false;
                idleCounter = 0;
                dir *= -1; // change direction after idling
            }
            return;
        }

        x += dir * speed;

        // Update direction
        currentDirection = dir > 0 ? Direction.RIGHT : Direction.LEFT;

        // Check patrol boundaries
        if (x < patrolMinX || x > patrolMaxX) {
            x = Math.max(patrolMinX, Math.min(x, patrolMaxX)); // clamp position
            isIdle = true; // start idle
            animationFrame = 0;
            animationTick = 0;
            return;
        }

    }

    @Override
    public void render(int[] pixels, int screenW, int screenH) {
        BufferedImage img;

        if (isIdle) {
            img = (currentDirection == Direction.RIGHT) ? idleRight[animationFrame % idleRight.length]
                    : idleLeft[animationFrame % idleLeft.length];
        } else {
            img = (currentDirection == Direction.RIGHT) ? walkRight[animationFrame % walkRight.length]
                    : walkLeft[animationFrame % walkLeft.length];
        }

        Sprite.drawImage(pixels, screenW, screenH, x, y, img);
    }
}
