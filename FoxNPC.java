import java.util.Set;
import java.awt.image.BufferedImage;

public class FoxNPC extends Entity {
    private enum Direction {
        LEFT, RIGHT
    }

    private enum State {
        PATROL, IDLE, ATTACK, TOPATROLPOSITION
    }

    private State state = State.PATROL;

    private Direction currentDirection = Direction.RIGHT;

    private int dir = 1, speed = 1;
    private int patrolMinX, patrolMaxX;
    private int targetPatrolX; // The patrol point we're moving toward
    private static final int PATROL_THRESHOLD = 5; // How close we need to be to consider position reached
    private int idleCounter = 0;
    private final int idleTime = 120; // ~1 second if 60 FPS
    private final int detectionRange = 100;

    private final BufferedImage[] walkRight;
    private final BufferedImage[] walkLeft;
    private final BufferedImage[] idleRight;
    private final BufferedImage[] idleLeft;

    private Player player;

    public FoxNPC(int startX, int startY, int minX, int maxX, Player player) {
        super(startX, startY, Assets.foxSheet);
        this.patrolMinX = minX;
        this.patrolMaxX = maxX;
        this.player = player;

        idleRight = sheet.getRow(0);
        walkRight = sheet.getRow(2);
        walkLeft = sheet.getRow(1);
        idleLeft = sheet.getRow(3);
    }

    @Override
    public void update(Set<Integer> keys) {
        int playerX = player.getX();
        int playerY = player.getY();
        int distToPlayer = Math.abs(playerX - x);

        // State transitions
        if (distToPlayer <= detectionRange) {
            state = State.ATTACK;
        } else if (state == State.ATTACK) {
            // When leaving attack mode, find nearest patrol point
            state = State.TOPATROLPOSITION;
            targetPatrolX = findNearestPatrolPoint();
        }

        switch (state) {
            case ATTACK:
                attackPlayer(playerX, playerY);
                break;

            case PATROL:
                patrol();
                break;

            case IDLE:
                idleCounter++;
                if (idleCounter >= idleTime) {
                    idleCounter = 0;
                    state = State.PATROL;
                    dir *= -1;
                }
                break;

            case TOPATROLPOSITION:
                moveToPatrolPosition();
                break;
        }

        animationTick++;
        if (animationTick % 10 == 0) {
            animationFrame = (animationFrame + 1) % 4;
        }
    }

    private int findNearestPatrolPoint() {
        // Find which patrol boundary is closer
        int distToMin = Math.abs(x - patrolMinX);
        int distToMax = Math.abs(x - patrolMaxX);

        return (distToMin < distToMax) ? patrolMinX : patrolMaxX;
    }

    private void moveToPatrolPosition() {
        // Move toward target position
        if (x < targetPatrolX) {
            x += speed;
            currentDirection = Direction.RIGHT;
        } else if (x > targetPatrolX) {
            x -= speed;
            currentDirection = Direction.LEFT;
        }

        // Check if we've reached the position
        if (Math.abs(x - targetPatrolX) <= PATROL_THRESHOLD) {
            x = targetPatrolX; // Snap to exact position
            state = State.IDLE; // Transition to idle before continuing patrol
            idleCounter = 0;
            animationFrame = 0;
            animationTick = 0;

            // Set patrol direction based on which boundary we're at
            dir = (targetPatrolX == patrolMinX) ? 1 : -1;
        }
    }

    private void patrol() {
        x += dir * speed;
        currentDirection = dir > 0 ? Direction.RIGHT : Direction.LEFT;

        if (x < patrolMinX || x > patrolMaxX) {
            // When hitting patrol boundary, go to idle
            x = Math.max(patrolMinX, Math.min(x, patrolMaxX));
            state = State.IDLE;
            animationFrame = 0;
            animationTick = 0;
        }
    }

    private void attackPlayer(int playerX, int playerY) {
        // Move toward the player horizontally
        if (x < playerX) {
            x += speed;
            currentDirection = Direction.RIGHT;
        } else if (x > playerX) {
            x -= speed;
            currentDirection = Direction.LEFT;
        }

        // // Optional: Move toward the player vertically (if needed)
        // if (y < playerY) {
        // y += speed;
        // } else if (y > playerY) {
        // y -= speed;
        // }

        // Check distance in both X and Y dimensions
        int xDistance = Math.abs(playerX - x);
        int yDistance = Math.abs(playerY - y);

        // Attack if close enough in both dimensions
        if (xDistance < 10 && yDistance < 20) { // Adjust thresholds as needed
            player.takeDamage(1, x); // Pass attacker's X position for knockback
        }
    }

    @Override
    public void render(int[] pixels, int screenW, int screenH) {
        BufferedImage img;

        switch (state) {
            case ATTACK:
                img = (currentDirection == Direction.RIGHT)
                        ? walkRight[animationFrame % walkRight.length]
                        : walkLeft[animationFrame % walkLeft.length];
                break;

            case IDLE:
                img = (currentDirection == Direction.RIGHT)
                        ? idleRight[animationFrame % idleRight.length]
                        : idleLeft[animationFrame % idleLeft.length];
                break;

            default: // PATROL
                img = (currentDirection == Direction.RIGHT)
                        ? walkRight[animationFrame % walkRight.length]
                        : walkLeft[animationFrame % walkLeft.length];
                break;
        }

        Sprite.drawImage(pixels, screenW, screenH, x, y, img);
    }
}
