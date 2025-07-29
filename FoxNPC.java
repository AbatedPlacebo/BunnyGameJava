import java.util.Set;
import java.awt.image.BufferedImage;

public class FoxNPC extends Entity {
    private enum Direction {
        LEFT, RIGHT
    }

    private enum State {
        PATROL, IDLE, ATTACK
    }

    private State state = State.PATROL;

    private Direction currentDirection = Direction.RIGHT;
    private boolean isIdle = false;

    private int dir = 1, speed = 1;
    private int patrolMinX, patrolMaxX;
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
        walkRight = sheet.getRow(1);
        walkLeft = sheet.getRow(2);
        idleLeft = sheet.getRow(3);
    }

    @Override
    public void update(Set<Integer> keys) {
        int playerX = player.getX();
        int distToPlayer = Math.abs(playerX - x);

        // Enter attack mode if close enough
        if (distToPlayer <= detectionRange) {
            state = State.ATTACK;
        } else if (state == State.ATTACK) {
            // Return to patrol when player escapes
            state = State.PATROL;
        }

        switch (state) {
            case ATTACK:
                attackPlayer(playerX);
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
        }

        animationTick++;
        if (animationTick % 10 == 0) {
            animationFrame = (animationFrame + 1) % 4;
        }
    }

    private void patrol() {
        x += dir * speed;
        currentDirection = dir > 0 ? Direction.RIGHT : Direction.LEFT;

        if (x < patrolMinX || x > patrolMaxX) {
            x = Math.max(patrolMinX, Math.min(x, patrolMaxX));
            state = State.IDLE;
            animationFrame = 0;
            animationTick = 0;
        }
    }

    private void attackPlayer(int playerX) {
        // Move toward the player
        if (x < playerX) {
            x += speed;
            currentDirection = Direction.RIGHT;
        } else if (x > playerX) {
            x -= speed;
            currentDirection = Direction.LEFT;
        }

        // Optional: implement damage logic if close enough
        int distance = Math.abs(playerX - x);
        if (distance < 10) {
            // player.takeDamage(1); // You must define takeDamage in Player
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
