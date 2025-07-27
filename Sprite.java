import java.awt.image.BufferedImage;

public class Sprite {
    public static final int TRANSPARENT = 0xFFFF00FF;

    /**
     * Рисует спрайт, заданный матрицей цветов.
     */
    public static void drawSprite(int[] pixels, int screenW, int screenH,
            int x, int y, int[][] sprite) {
        int spriteH = sprite.length;
        int spriteW = sprite[0].length;

        for (int sy = 0; sy < spriteH; sy++) {
            for (int sx = 0; sx < spriteW; sx++) {
                int color = sprite[sy][sx];
                if (color == TRANSPARENT)
                    continue;

                int px = x + sx;
                int py = y + sy;
                if (px < 0 || px >= screenW || py < 0 || py >= screenH)
                    continue;

                pixels[px + py * screenW] = color;
            }
        }
    }

    /**
     * Рисует BufferedImage в ваш буфер пикселей.
     * Прозрачные пиксели (alpha = 0) не запишутся.
     */
    public static void drawImage(int[] pixels, int screenW, int screenH,
            int x, int y, BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        // Вытаскиваем все ARGB-пиксели из изображения
        int[] imgPixels = new int[w * h];
        img.getRGB(0, 0, w, h, imgPixels, 0, w);

        for (int sy = 0; sy < h; sy++) {
            for (int sx = 0; sx < w; sx++) {
                int argb = imgPixels[sx + sy * w];
                int alpha = (argb >>> 24) & 0xFF;
                if (alpha == 0)
                    continue; // полностью прозрачный

                int px = x + sx;
                int py = y + sy;
                if (px < 0 || px >= screenW || py < 0 || py >= screenH)
                    continue;

                // Можно учитывать альфа‑смешивание:
                // pixels[px + py*screenW] = blend(pixels[...], argb);
                // но пока просто копируем
                pixels[px + py * screenW] = argb;
            }
        }
    }

    /**
     * Пример функции альфа‑смешивания (если понадобится).
     * src и dst в формате ARGB.
     */
    @SuppressWarnings("unused")
    private static int blend(int dst, int src) {
        int srcA = (src >>> 24) & 0xFF;
        if (srcA == 0xFF)
            return src;
        int invA = 255 - srcA;

        int srcR = (src >> 16) & 0xFF;
        int srcG = (src >> 8) & 0xFF;
        int srcB = src & 0xFF;

        int dstR = (dst >> 16) & 0xFF;
        int dstG = (dst >> 8) & 0xFF;
        int dstB = dst & 0xFF;

        int outR = (srcR * srcA + dstR * invA) / 255;
        int outG = (srcG * srcA + dstG * invA) / 255;
        int outB = (srcB * srcA + dstB * invA) / 255;
        int outA = Math.min(255, srcA + ((dst >>> 24) & 0xFF) * invA / 255);

        return (outA << 24) | (outR << 16) | (outG << 8) | outB;
    }
}
