
// Player.java
import java.awt.event.KeyEvent;
import java.util.Set;

public class Player {
    private ActionStrategy actionStrategy;
    private MissStrategy missStrategy;

    private int x = 100, y = 100;
    private int animationFrame = 0;
    private int animationTick = 0;

    protected final SpriteSheet sheet; // <- храним здесь

    // Старый конструктор, если нужен «пустой» sheet:
    public Player(ActionStrategy action, MissStrategy miss) {
        this(action, miss, null);
    }

    // Новый конструктор, принимающий SpriteSheet:
    public Player(ActionStrategy action, MissStrategy miss, SpriteSheet sheet) {
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
        boolean moving = false;
        if (keys.contains(KeyEvent.VK_LEFT)) {
            x--;
            moving = true;
        }
        if (keys.contains(KeyEvent.VK_RIGHT)) {
            x++;
            moving = true;
        }
        if (keys.contains(KeyEvent.VK_UP)) {
            y--;
            moving = true;
        }
        if (keys.contains(KeyEvent.VK_DOWN)) {
            y++;
            moving = true;
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
