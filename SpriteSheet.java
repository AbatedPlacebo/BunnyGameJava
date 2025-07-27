
/*
 * SpriteSheet.java
 * Loads a spritesheet, converts it to an int-based buffer for fast pixel access,
 * and provides methods to extract individual frames or full rows as BufferedImages.
 */
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SpriteSheet {
    private final BufferedImage sheet;
    private final int frameWidth, frameHeight;
    private final int cols, rows;
    private final int[] sheetPixels;

    public SpriteSheet(String resourcePath, int frameWidth, int frameHeight) {
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        BufferedImage loaded;
        try {
            loaded = ImageIO.read(getClass().getResourceAsStream(resourcePath));
            if (loaded == null)
                throw new IOException("Resource not found: " + resourcePath);
        } catch (IOException | NullPointerException e) {
            throw new RuntimeException("Can't load sprite sheet: " + resourcePath, e);
        }
        // Ensure we have an INT_ARGB image for fast pixel access and transparency
        sheet = new BufferedImage(
                loaded.getWidth(), loaded.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = sheet.createGraphics();
        // Draw preserving alpha channel
        g.drawImage(loaded, 0, 0, null);
        g.dispose();

        this.cols = sheet.getWidth() / frameWidth;
        this.rows = sheet.getHeight() / frameHeight;
        // Cache all pixels for fast access
        this.sheetPixels = ((DataBufferInt) sheet.getRaster().getDataBuffer()).getData();
    }

    /** Width (and height if square) of each tile/frame */
    public int getTileSize() {
        return frameWidth;
    }

    /** Extract a single frame (row, col) as a BufferedImage */
    public BufferedImage getFrame(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new IllegalArgumentException(
                    "Frame index out of bounds: row=" + row + ", col=" + col);
        }
        return sheet.getSubimage(
                col * frameWidth,
                row * frameHeight,
                frameWidth,
                frameHeight);
    }

    /** Extract an entire row of frames as an array of BufferedImages */
    public BufferedImage[] getRow(int row) {
        if (row < 0 || row >= rows) {
            throw new IllegalArgumentException("Row index out of bounds: " + row);
        }
        BufferedImage[] out = new BufferedImage[cols];
        for (int c = 0; c < cols; c++) {
            out[c] = getFrame(row, c);
        }
        return out;
    }

    /**
     * Render a sub-rectangle of the sheet into the pixel buffer, skipping
     * transparent pixels.
     *
     * @param x       destination X on screen
     * @param y       destination Y on screen
     * @param srcX    source X (in pixels) on the sheet
     * @param srcY    source Y (in pixels) on the sheet
     * @param pixels  the game screen pixel buffer
     * @param screenW the screen width
     * @param screenH the screen height
     */
    public void render(int x, int y, int srcX, int srcY, int[] pixels, int screenW, int screenH) {
        for (int yy = 0; yy < frameHeight; yy++) {
            int py = y + yy;
            if (py < 0 || py >= screenH)
                continue;
            for (int xx = 0; xx < frameWidth; xx++) {
                int px = x + xx;
                if (px < 0 || px >= screenW)
                    continue;
                int sheetIndex = (srcX + xx) + (srcY + yy) * sheet.getWidth();
                int col = sheetPixels[sheetIndex];
                // Only draw non-transparent pixels
                if ((col >>> 24) != 0) {
                    pixels[px + py * screenW] = col;
                }
            }
        }
    }
}
