import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Set;

public class Player extends Entity {
    private ActionStrategy actionStrategy;
    private MissStrategy missStrategy;

    private int x = 100, y = 100;
    private int animationFrame = 0;
    private int animationTick = 0;
    private boolean wasMoving = false;
    private boolean moving = false;
    private boolean jumping = false;

    private float jumpVelocity = 0;
    private final double gravity = 0.2;
    private final int jumpStrength = -4;
    private final int groundY = 125; // можно передавать или вычислять в будущем
    private int speed = 2;
    private int health = 100; // Default health value
    private int maxHealth = 100;

    // Add these fields to your Player class
    private float knockbackVelocityX = 0;
    private float knockbackAccelerationX = 0;
    private boolean inKnockback = false;

    protected boolean isHit = false;
    protected int hitFlashFrames = 0;
    protected static final int HIT_FLASH_DURATION = 20; // Duration in frames (adjust as needed)

    protected final SpriteSheet sheet; // <- храним здесь

    protected enum Direction {
        NONE, LEFT, RIGHT
    }

    protected Direction currentDirection = Direction.RIGHT;
    protected Direction lastDirection = Direction.RIGHT;

    // Старый конструктор, если нужен «пустой» sheet:
    public Player(ActionStrategy action, MissStrategy miss) {
        this(action, miss, null);
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }

    protected boolean isMoving() {
        return moving;
    }

    protected boolean isJumping() {
        return jumping;
    }

    private boolean isOnGround() {
        return y >= groundY;
    }

    // Новый конструктор, принимающий SpriteSheet:
    public Player(ActionStrategy action, MissStrategy miss, SpriteSheet sheet) {
        super(100, 125, sheet);
        this.actionStrategy = action;
        this.missStrategy = miss;
        this.sheet = sheet;
    }

    protected int getX() {
        return x;
    }

    protected int getY() {
        return y;
    }

    protected int getAnimationFrame() {
        return animationFrame % 4;
    }

    public void act() {
        if (missStrategy.isMissed()) {
            System.out.println("The action missed!");
        } else {
            actionStrategy.perform();
        }
    }

    public void update(Set<Integer> keys) {
        wasMoving = moving;
        moving = false;

        // Handle knockback physics first
        if (inKnockback) {
            // Apply knockback movement
            x += knockbackVelocityX;
            knockbackVelocityX += knockbackAccelerationX;

            // Apply gravity during knockback
            y += jumpVelocity;
            jumpVelocity += gravity;

            // Check if knockback has ended
            if (Math.abs(knockbackVelocityX) < 0.5f) {
                knockbackVelocityX = 0;
                inKnockback = false;
            }

            // Ground check
            if (y >= groundY) {
                y = groundY;
                jumping = false;
                jumpVelocity = 0;
            }

            animationTick++;
            if (animationTick % 10 == 0)
                animationFrame++;
            return; // Skip normal movement during knockback
        }

        if (keys.contains(KeyEvent.VK_SHIFT)) {
            speed = 4;
        } else {
            speed = 2;
        }

        // Horizontal movement
        if (keys.contains(KeyEvent.VK_LEFT)) {
            x -= speed;
            moving = true;
            currentDirection = Direction.LEFT;
            lastDirection = Direction.LEFT;
        }
        if (keys.contains(KeyEvent.VK_RIGHT)) {
            x += speed;
            moving = true;
            currentDirection = Direction.RIGHT;
            lastDirection = Direction.RIGHT;
        }

        // Прыжок, если не в воздухе
        if ((keys.contains(KeyEvent.VK_UP) || keys.contains(KeyEvent.VK_SPACE)) && !jumping && isOnGround()) {
            jumping = true;
            jumpVelocity = jumpStrength;
            moving = true;
        }

        // Обработка прыжка и падения
        if (jumping || !isOnGround()) {
            y += jumpVelocity;
            jumpVelocity += gravity;

            // Приземление
            if (y >= groundY) {
                y = groundY;
                jumping = false;
                jumpVelocity = 0;
            }
        }

        // Сброс анимации, если только начали движение
        if (moving && !wasMoving) {
            animationFrame = 0;
            animationTick = 0;
        }

        animationTick++;
        if (animationTick % 10 == 0)
            animationFrame++;
    }

    // В этом базовом методе мы уже не рендерим никаких кадров:
    public void render(int[] pixels, int screenW, int screenH) {
        // Может остаться пустым, или общая «заглушка».

    }

    // Add getters for health
    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void takeDamage(int damage, int attackerX) {
        health -= damage;
        if (health < 0)
            health = 0;

        // Trigger hit effect
        isHit = true;
        hitFlashFrames = HIT_FLASH_DURATION;

        applyKnockback(attackerX); // Your existing knockback logic
    }

    private void applyKnockback(int attackerX) {
        // Determine knockback direction (away from attacker)
        int knockbackDirection = (x < attackerX) ? -1 : 1;

        // Set initial knockback velocities
        knockbackVelocityX = knockbackDirection * 8f; // Initial horizontal speed
        jumpVelocity = -5f; // Stronger vertical launch
        knockbackAccelerationX = -knockbackDirection * 0.5f; // Deceleration in opposite direction

        jumping = true;
        inKnockback = true;

    }

    // Optionally add a heal method
    public void heal(int amount) {
        health += amount;
        if (health > maxHealth) {
            health = maxHealth;
        }
        System.out.println("Player healed " + amount + " health! Health: " + health + "/" + maxHealth);
    }

    public boolean isAlive() {
        return health > 0;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public BufferedImage getCurrentFrame() {
        if (sheet == null)
            return null;

        int frameIndex = animationFrame % 4;
        return (lastDirection == Direction.RIGHT)
                ? sheet.getFrame(0, frameIndex)
                : sheet.getFrame(1, frameIndex); // допустим, 0 — вправо, 1 — влево
    }

    public void setJumping(boolean jumping) {
        jumpVelocity = -3;
        this.jumping = jumping;
    }

}