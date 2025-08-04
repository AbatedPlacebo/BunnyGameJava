import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;

public class FoxNPC extends Entity {
    private enum Direction {
        LEFT, RIGHT
    }

    enum CollisionResult {
        NONE,
        PLAYER_ABOVE,
        FOX_ABOVE,
        SIDE
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
    private int attackCooldown = 0; // в кадрах
    private boolean isAlive = true;

    private final BufferedImage[] walkRight;
    private final BufferedImage[] walkLeft;
    private final BufferedImage[] idleRight;
    private final BufferedImage[] idleLeft;

    private List<Particle> particles = new ArrayList<>();

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
        if (!isAlive)
            return;

        int playerX = player.getX();
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
                attackPlayer(player);
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
        if (attackCooldown > 0) {
            attackCooldown--;
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

    private void attackPlayer(Player player) {
        if (x < player.getX()) {
            x += speed;
            currentDirection = Direction.RIGHT;
        } else if (x > player.getX()) {
            x -= speed;
            currentDirection = Direction.LEFT;
        }

        if (attackCooldown > 0)
            return;

        if (x < player.getX() + player.getWidth() &&
                x + width > player.getX() &&
                y < player.getY() + player.getHeight() &&
                y + height > player.getY()) {

            boolean wasAbove = (player.getPrevY() + player.getHeight()) <= this.y;
            boolean falling = player.getJumpVelocity() > 0;

            if (wasAbove && falling) {
                // stomp
                player.triggerJump();
                die();
            } else {
                // side-hit
                player.takeDamage(1, x);
                attackCooldown = 60;
            }
        }
    }

    @Override
    public void render(int[] pixels, int screenW, int screenH) {

        BufferedImage img;
        if (!isAlive)
            return;

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
        Sprite.drawImage(pixels, screenW, screenH, x, y - height, img);
    }

    public boolean isAlive() {
        return isAlive;
    }

    @Override
    public BufferedImage getCurrentFrame() {
        throw new UnsupportedOperationException("Unimplemented method 'getCurrentFrame'");
    }

    public int getTop() {
        return y - height;
    }

    public int getBottom() {
        return y;
    }

    public void die() {
        isAlive = false;

        BufferedImage baseImage = (currentDirection == Direction.RIGHT)
                ? idleRight[0]
                : idleLeft[0];

        // Создание 50 частиц
        for (int i = 0; i < 50; i++) {
            particles.add(new Particle(x, getTop(), baseImage));
        }
    }

}
