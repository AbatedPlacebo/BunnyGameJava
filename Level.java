
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

public void render(int[] pixels, int screenW, int screenH, int xScroll, int yScroll) {
        for (int y = 0; y < screenH; y++) {
            // Calculate the absolute y-coordinate in the level
            int yy = y + yScroll;

            // --- CHANGE FOR ENDLESS SCROLL ---
            // Instead of checking bounds, we use the modulo operator (%) to wrap the coordinate.
            // This makes the level texture repeat vertically.
            // We add 'height' and take the modulo again to handle negative scroll values correctly.
            int wrappedY = (yy % height + height) % height;

            for (int x = 0; x < screenW; x++) {
                // Calculate the absolute x-coordinate in the level
                int xx = x + xScroll;

                // --- CHANGE FOR ENDLESS SCROLL ---
                // We do the same for the x-coordinate to make the level repeat horizontally.
                int wrappedX = (xx % width + width) % width;

                // Use the wrapped coordinates to get the correct pixel from the level texture
                // and place it on the screen.
                pixels[x + y * screenW] = tiles[wrappedX + wrappedY * width];
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
