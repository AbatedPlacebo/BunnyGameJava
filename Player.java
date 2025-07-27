
// Player.java
import java.awt.event.KeyEvent;
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

    protected final SpriteSheet sheet; // <- храним здесь

    // Старый конструктор, если нужен «пустой» sheet:
    public Player(ActionStrategy action, MissStrategy miss) {
        this(action, miss, null);
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

        // Горизонтальное движение
        if (keys.contains(KeyEvent.VK_LEFT)) {
            x--;
            moving = true;
        }
        if (keys.contains(KeyEvent.VK_RIGHT)) {
            x++;
            moving = true;
        }

        // Прыжок, если не в воздухе
        if (keys.contains(KeyEvent.VK_UP) && !jumping && isOnGround()) {
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
}
