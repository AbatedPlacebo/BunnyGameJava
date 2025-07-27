
// Level.java
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Level {
    private final int width, height;
    private final int[] tiles;

    public Level(String path) {
        BufferedImage img;
        try {
            img = ImageIO.read(getClass().getResourceAsStream(path));
        } catch (IOException | NullPointerException e) {
            throw new RuntimeException("Can't load level: " + path, e);
        }

        width = img.getWidth();
        height = img.getHeight();
        tiles = new int[width * height];
        img.getRGB(0, 0, width, height, tiles, 0, width);
    }

    public void render(int[] pixels, int screenW, int screenH) {
        for (int y = 0; y < screenH; y++) {
            for (int x = 0; x < screenW; x++) {
                if (x >= width || y >= height)
                    continue;
                pixels[x + y * screenW] = tiles[x + y * width];
            }
        }
    }

    public int getTile(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height)
            return 0;
        return tiles[x + y * width];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
