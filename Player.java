import java.awt.event.KeyEvent;
import java.util.Set;

public class Player {
    private ActionStrategy actionStrategy;
    private MissStrategy missStrategy;

    private int x = 100, y = 100;
    private int animationFrame = 0;
    private int animationTick = 0;

    public Player(ActionStrategy action, MissStrategy miss) {
        this.actionStrategy = action;
        this.missStrategy = miss;
    }

    protected int getX() {
        return x;
    }

    protected int getY() {
        return y;
    }

    protected int getAnimationFrame() {
        return animationFrame % 3;
    } // Use % 3 for 3 bunny frames

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

        if (moving) {
            animationTick++;
            if (animationTick % 10 == 0) {
                animationFrame++;
            }
        } else {
            animationFrame = 0; // idle frame
        }
    }

    public void render(int[] pixels, int screenW, int screenH) {
        if (animationFrame == 0) {
            Sprite.drawSprite(pixels, screenW, screenH, x, y, Sprite.PLAYER_FRAME_1);
        } else {
            Sprite.drawSprite(pixels, screenW, screenH, x, y, Sprite.PLAYER_FRAME_2);
        }
    }
}
