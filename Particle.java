import java.awt.image.BufferedImage;

public class Particle {
    float x, y;
    float vx, vy;
    float alpha;
    int life;
    BufferedImage image;


    private static final float GRAVITY = 0.1f;  // сила гравитации

    public Particle(float x, float y, BufferedImage image) {
        this.x = x;
        this.y = y;
        this.vx = (float) (Math.random() * 4 - 2); // [-2, 2]
        this.vy = (float) (Math.random() * 4 - 2);
        this.alpha = 1.0f;
        this.life = (int) (60 * Math.random()); // 60 кадров жизни
        this.image = image;
    }

    public void update() {
        vx *= 0.98f; // легкое трение по горизонтали
        vy += GRAVITY;  // гравитация добавляет ускорение вниз

        x += vx;
        y += vy;

        life--;
        alpha = Math.max(0, alpha - 1f / 60);
    }

    public boolean isDead() {
        return life <= 0;
    }

    public void render(int[] pixels, int screenW, int screenH) {
        if (image == null || alpha <= 0)
            return;

        BufferedImage temp = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int rgb = image.getRGB(i, j);
                int a = (rgb >> 24) & 0xff;
                int r = (rgb >> 16) & 0xff;
                int g = (rgb >> 8) & 0xff;
                int b = rgb & 0xff;
                a *= alpha;
                temp.setRGB(i, j, (a << 24) | (r << 16) | (g << 8) | b);
            }
        }

        Sprite.drawImage(pixels, screenW, screenH, (int) x, (int) y, temp);
    }
}
