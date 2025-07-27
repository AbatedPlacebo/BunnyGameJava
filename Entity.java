import java.util.Set;

public abstract class Entity {
    protected int x, y;
    protected SpriteSheet sheet;
    protected int animationFrame = 0, animationTick = 0;

    public Entity(int startX, int startY, SpriteSheet sheet) {
        this.x = startX;
        this.y = startY;
        this.sheet = sheet;
    }

    // Called every tick to update position, AI, etc.
    public abstract void update(Set<Integer> keys);

    // Draw yourself to the pixel buffer
    public abstract void render(int[] pixels, int screenW, int screenH);
}
