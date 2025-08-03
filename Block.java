import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Set;

public class Block extends Entity {
    private int x, y, width, height;
    private final Color color = new Color(100, 100, 100);
    private final BufferedImage[] texture;

    public Block(int x, int y) {
        super(x, y, Assets.blockSheet);
        this.x = x;
        this.y = y;
        texture = sheet.getRow(0);
        this.width = sheet.getFrameWidth();
        this.height = sheet.getFrameHeight();
    }

    @Override
    public void render(int[] pixels, int screenWidth, int screenHeight) {
        BufferedImage img = texture[0];
        // for (int yy = y; yy < y + height; yy++) {
        // if (yy < 0 || yy >= screenHeight)
        // continue;
        // for (int xx = x; xx < x + width; xx++) {
        // if (xx < 0 || xx >= screenWidth)
        // continue;
        // pixels[xx + yy * screenWidth] = color.getRGB();
        // }
        // }
        Sprite.drawImage(pixels, screenWidth, screenHeight, x, y, img);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    @Override
    public void update(Set<Integer> keys) {
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public BufferedImage getCurrentFrame() {
        throw new UnsupportedOperationException("Unimplemented method 'getCurrentFrame'");
    }
}
