public class Sprite {
    public static final int TRANSPARENT = 0xFFFF00FF;

    public static final int[][] PLAYER_FRAME_1 = {
            { TRANSPARENT, 0x000000, 0xFFD800, 0xFFD800, 0x000000, TRANSPARENT },
            { 0x000000, 0xFFD800, 0xFFFFFF, 0xFFFFFF, 0xFFD800, 0x000000 },
            { 0x000000, 0xFFD800, 0x000000, 0x000000, 0xFFD800, 0x000000 },
            { TRANSPARENT, 0xFFD800, TRANSPARENT, TRANSPARENT, 0xFFD800, TRANSPARENT },
            { 0x000000, TRANSPARENT, 0x000000, 0x000000, TRANSPARENT, 0x000000 },
    };

    public static final int[][] PLAYER_FRAME_2 = {
            { TRANSPARENT, 0x000000, 0xFFD800, 0xFFD800, 0x000000, TRANSPARENT },
            { 0x000000, 0xFFD800, 0xFFFFFF, 0xFFFFFF, 0xFFD800, 0x000000 },
            { 0x000000, 0xFFD800, 0x000000, 0x000000, 0xFFD800, 0x000000 },
            { 0xFFD800, TRANSPARENT, 0xFFD800, 0xFFD800, TRANSPARENT, 0xFFD800 },
            { TRANSPARENT, 0x000000, TRANSPARENT, TRANSPARENT, 0x000000, TRANSPARENT },
    };

    public static void drawSprite(int[] pixels, int screenW, int screenH, int x, int y, int[][] sprite) {
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
}