import java.awt.image.BufferedImage;
import java.util.Set;

public abstract class Entity {
    protected int x, y;
    protected SpriteSheet sheet;
    protected int animationFrame = 0, animationTick = 0;
    protected int width, height;

    public Entity(int startX, int startY, SpriteSheet sheet) {
        this.x = startX;
        this.y = startY;
        this.sheet = sheet;
        if (sheet != null) {
            this.width = sheet.getFrameWidth(); // предполагается метод, возвращающий ширину спрайта
            this.height = sheet.getFrameHeight(); // предполагается метод, возвращающий высоту спрайта
        }
    }

    // Called every tick to update position, AI, etc.
    public abstract void update(Set<Integer> keys);

    // Draw yourself to the pixel buffer
    public abstract void render(int[] pixels, int screenW, int screenH);

    public abstract BufferedImage getCurrentFrame();
}
