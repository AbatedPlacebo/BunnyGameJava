import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet {
    private final BufferedImage sheet;
    private final int frameWidth, frameHeight;
    private final int cols, rows;

    public SpriteSheet(String resourcePath, int frameWidth, int frameHeight) {
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        try {
            sheet = ImageIO.read(getClass()
                    .getResourceAsStream(resourcePath));
        } catch (IOException | NullPointerException e) {
            throw new RuntimeException(
                    "Can't load sprite sheet: " + resourcePath, e);
        }
        this.cols = sheet.getWidth() / frameWidth;
        this.rows = sheet.getHeight() / frameHeight;
    }

    /** Возвращает один кадр по (row, col) */
    public BufferedImage getFrame(int row, int col) {
        return sheet.getSubimage(
                col * frameWidth,
                row * frameHeight,
                frameWidth, frameHeight);
    }

    /** Возвращает всю строку (например, направление) */
    public BufferedImage[] getRow(int row) {
        BufferedImage[] out = new BufferedImage[cols];
        for (int c = 0; c < cols; c++) {
            out[c] = getFrame(row, c);
        }
        return out;
    }
}
