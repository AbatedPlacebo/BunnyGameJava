
// BrownRabbitPlayer.java
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Set;

public class BrownRabbitPlayer extends Player {
    private final BufferedImage[] walkLeft;
    private final BufferedImage[] walkRight;
    private final BufferedImage[] idleRight;
    private final BufferedImage[] idleLeft;

    public BrownRabbitPlayer(ActionStrategy a, MissStrategy m) {
        // ПЕРЕДАЁМ sheet в super!
        super(a, m, Assets.brownRabbitSheet);

        idleRight = sheet.getRow(0);
        walkRight = sheet.getRow(1);
        walkLeft = sheet.getRow(2);
        idleLeft = sheet.getRow(3);
    }

    @Override
    public void update(Set<Integer> keys) {
        super.update(keys);

        if (keys.contains(KeyEvent.VK_LEFT)) {
            currentDirection = Direction.LEFT;
            lastDirection = Direction.LEFT;
        } else if (keys.contains(KeyEvent.VK_RIGHT)) {
            currentDirection = Direction.RIGHT;
            lastDirection = Direction.RIGHT;
        }
    }

    @Override
    public void render(int[] pixels, int w, int h) {
        int f = getAnimationFrame();
        BufferedImage img;

        if (isMoving() || isJumping()) {
            switch (currentDirection) {
                case RIGHT:
                    img = walkRight[f];
                    break;
                case LEFT:
                    img = walkLeft[f];
                    break;
                default:
                    img = idleRight[f];
                    break;
            }
        } else {
            switch (lastDirection) {
                case RIGHT:
                    img = idleRight[f];
                    break;
                case LEFT:
                    img = idleLeft[f];
                    break;
                default:
                    img = idleRight[f];
                    break;
            }
        }

        // First draw the normal sprite
        Sprite.drawImage(pixels, w, h, getX(), getY(), img);

        // Then apply hit effect if needed
        if (isHit && hitFlashFrames > 0) {
            applyHitEffect(pixels, w, h);
            hitFlashFrames--;
            if (hitFlashFrames <= 0)
                isHit = false;
        }
    }

    private void applyHitEffect(int[] pixels, int w, int h) {
        int f = getAnimationFrame();
        BufferedImage frameToDraw;

        // Get the appropriate frame based on direction/movement
        if (isMoving() || isJumping()) {
            frameToDraw = (currentDirection == Direction.LEFT) ? walkLeft[f] : walkRight[f];
        } else {
            frameToDraw = (lastDirection == Direction.LEFT) ? idleLeft[f] : idleRight[f];
        }

        // Create a red-tinted version of the frame
        BufferedImage redFrame = createRedTintFrame(frameToDraw);

        // Draw the tinted frame
        Sprite.drawImage(pixels, w, h, getX(), getY(), redFrame);
    }

    private BufferedImage createRedTintFrame(BufferedImage original) {
        BufferedImage tinted = new BufferedImage(original.getWidth(), original.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        float intensity = (float) hitFlashFrames / HIT_FLASH_DURATION; // 1.0 to 0.0

        for (int y = 0; y < original.getHeight(); y++) {
            for (int x = 0; x < original.getWidth(); x++) {
                int pixel = original.getRGB(x, y);
                if ((pixel >> 24) != 0x00) {
                    int a = (pixel >> 24) & 0xff;
                    int r = (pixel >> 16) & 0xff;
                    int g = (pixel >> 8) & 0xff;
                    int b = pixel & 0xff;

                    // Dynamic intensity based on remaining flash frames
                    r = Math.min(255, r + (int) (100 * intensity));
                    g = (int) (g * (1 - intensity * 0.7f));
                    b = (int) (b * (1 - intensity * 0.7f));

                    tinted.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
                } else {
                    tinted.setRGB(x, y, pixel);
                }
            }
        }
        return tinted;
    }
}
