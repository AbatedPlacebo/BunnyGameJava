public class BrownRabbitSprite {
    public static final int TRANSPARENT = 0xFFFF00FF;
    public static final int BLACK = 0x000000;
    public static final int BROWN = 0x8B4513;
    public static final int DARK_BROWN = 0xA0522D;
    public static final int PINK = 0xFFC0CB;
    public static final int WHITE = 0xFFFFFF;

    // ----------------
    // Стандартный спрайт лицом
    // ----------------
    public static final int[][] FRAME_FACE_1 = {
            { TRANSPARENT, PINK, TRANSPARENT, TRANSPARENT, PINK, TRANSPARENT },
            { TRANSPARENT, BROWN, BROWN, BROWN, BROWN, TRANSPARENT },
            { BROWN, DARK_BROWN, BROWN, BROWN, DARK_BROWN, BROWN },
            { BROWN, BROWN, WHITE, WHITE, BROWN, BROWN },
            { TRANSPARENT, BROWN, BROWN, BROWN, BROWN, TRANSPARENT },
            { TRANSPARENT, TRANSPARENT, BROWN, BROWN, TRANSPARENT, TRANSPARENT }
    };
    public static final int[][] FRAME_FACE_2 = {
            { TRANSPARENT, PINK, TRANSPARENT, TRANSPARENT, PINK, TRANSPARENT },
            { TRANSPARENT, BROWN, BROWN, BROWN, BROWN, TRANSPARENT },
            { BROWN, DARK_BROWN, BROWN, BROWN, DARK_BROWN, BROWN },
            { BROWN, BROWN, WHITE, WHITE, BROWN, BROWN },
            { TRANSPARENT, BROWN, BROWN, BROWN, BROWN, TRANSPARENT },
            { TRANSPARENT, BROWN, TRANSPARENT, TRANSPARENT, BROWN, TRANSPARENT }
    };
    public static final int[][] FRAME_FACE_3 = {
            { TRANSPARENT, PINK, TRANSPARENT, TRANSPARENT, PINK, TRANSPARENT },
            { TRANSPARENT, BROWN, BROWN, BROWN, BROWN, TRANSPARENT },
            { BROWN, DARK_BROWN, BROWN, BROWN, DARK_BROWN, BROWN },
            { BROWN, BROWN, WHITE, WHITE, BROWN, BROWN },
            { TRANSPARENT, TRANSPARENT, BROWN, BROWN, TRANSPARENT, TRANSPARENT },
            { TRANSPARENT, TRANSPARENT, BROWN, BROWN, TRANSPARENT, TRANSPARENT }
    };
    public static final int[][][] RABBIT_FACE = {
            FRAME_FACE_1, FRAME_FACE_2, FRAME_FACE_3
    };

    // ----------------
    // Прыжок вправо
    // ----------------
    public static final int[][] JUMP_RIGHT_1 = {
            { TRANSPARENT, TRANSPARENT, PINK, TRANSPARENT, TRANSPARENT, TRANSPARENT },
            { TRANSPARENT, BROWN, BROWN, BROWN, TRANSPARENT, TRANSPARENT },
            { TRANSPARENT, DARK_BROWN, BROWN, DARK_BROWN, BROWN, TRANSPARENT },
            { TRANSPARENT, BROWN, WHITE, WHITE, BROWN, TRANSPARENT },
            { TRANSPARENT, TRANSPARENT, BROWN, BROWN, TRANSPARENT, TRANSPARENT },
            { TRANSPARENT, TRANSPARENT, TRANSPARENT, BROWN, TRANSPARENT, TRANSPARENT }
    };
    public static final int[][] JUMP_RIGHT_2 = {
            { TRANSPARENT, TRANSPARENT, PINK, TRANSPARENT, TRANSPARENT, TRANSPARENT },
            { TRANSPARENT, BROWN, BROWN, BROWN, TRANSPARENT, TRANSPARENT },
            { TRANSPARENT, DARK_BROWN, BROWN, DARK_BROWN, BROWN, TRANSPARENT },
            { TRANSPARENT, BROWN, WHITE, WHITE, BROWN, TRANSPARENT },
            { TRANSPARENT, TRANSPARENT, TRANSPARENT, BROWN, TRANSPARENT, TRANSPARENT },
            { TRANSPARENT, TRANSPARENT, TRANSPARENT, TRANSPARENT, TRANSPARENT, TRANSPARENT }
    };
    public static final int[][] JUMP_RIGHT_3 = {
            { TRANSPARENT, TRANSPARENT, PINK, TRANSPARENT, TRANSPARENT, TRANSPARENT },
            { TRANSPARENT, BROWN, BROWN, BROWN, TRANSPARENT, TRANSPARENT },
            { TRANSPARENT, DARK_BROWN, BROWN, DARK_BROWN, BROWN, TRANSPARENT },
            { TRANSPARENT, TRANSPARENT, WHITE, WHITE, TRANSPARENT, TRANSPARENT },
            { TRANSPARENT, TRANSPARENT, BROWN, BROWN, TRANSPARENT, TRANSPARENT },
            { TRANSPARENT, TRANSPARENT, TRANSPARENT, TRANSPARENT, TRANSPARENT, TRANSPARENT }
    };
    public static final int[][][] RABBIT_JUMP_RIGHT = {
            JUMP_RIGHT_1, JUMP_RIGHT_2, JUMP_RIGHT_3
    };

    // ----------------
    // Прыжок влево (зеркальное отражение вправо)
    // ----------------
    public static final int[][][] RABBIT_JUMP_LEFT = mirrorHorizontally(RABBIT_JUMP_RIGHT);

    // ----------------
    // Прыжок спиной (от нас)
    // ----------------
    public static final int[][] JUMP_BACK_1 = {
            { TRANSPARENT, TRANSPARENT, PINK, TRANSPARENT, TRANSPARENT, TRANSPARENT },
            { TRANSPARENT, DARK_BROWN, DARK_BROWN, DARK_BROWN, DARK_BROWN, TRANSPARENT },
            { TRANSPARENT, BROWN, BROWN, BROWN, BROWN, TRANSPARENT },
            { TRANSPARENT, BROWN, BROWN, BROWN, BROWN, TRANSPARENT },
            { TRANSPARENT, TRANSPARENT, DARK_BROWN, DARK_BROWN, TRANSPARENT, TRANSPARENT },
            { TRANSPARENT, TRANSPARENT, TRANSPARENT, TRANSPARENT, TRANSPARENT, TRANSPARENT }
    };
    public static final int[][] JUMP_BACK_2 = {
            { TRANSPARENT, TRANSPARENT, PINK, TRANSPARENT, TRANSPARENT, TRANSPARENT },
            { TRANSPARENT, DARK_BROWN, DARK_BROWN, DARK_BROWN, DARK_BROWN, TRANSPARENT },
            { TRANSPARENT, BROWN, BROWN, BROWN, BROWN, TRANSPARENT },
            { TRANSPARENT, TRANSPARENT, BROWN, BROWN, TRANSPARENT, TRANSPARENT },
            { TRANSPARENT, TRANSPARENT, DARK_BROWN, DARK_BROWN, TRANSPARENT, TRANSPARENT },
            { TRANSPARENT, TRANSPARENT, TRANSPARENT, TRANSPARENT, TRANSPARENT, TRANSPARENT }
    };
    public static final int[][] JUMP_BACK_3 = {
            { TRANSPARENT, TRANSPARENT, PINK, TRANSPARENT, TRANSPARENT, TRANSPARENT },
            { TRANSPARENT, DARK_BROWN, DARK_BROWN, DARK_BROWN, DARK_BROWN, TRANSPARENT },
            { TRANSPARENT, BROWN, BROWN, BROWN, BROWN, TRANSPARENT },
            { TRANSPARENT, TRANSPARENT, TRANSPARENT, TRANSPARENT, TRANSPARENT, TRANSPARENT },
            { TRANSPARENT, TRANSPARENT, DARK_BROWN, DARK_BROWN, TRANSPARENT, TRANSPARENT },
            { TRANSPARENT, TRANSPARENT, TRANSPARENT, TRANSPARENT, TRANSPARENT, TRANSPARENT }
    };
    public static final int[][][] RABBIT_JUMP_BACK = {
            JUMP_BACK_1, JUMP_BACK_2, JUMP_BACK_3
    };

    // Хелпер для зеркального отражения
    private static int[][][] mirrorHorizontally(int[][][] src) {
        int n = src.length, h = src[0].length, w = src[0][0].length;
        int[][][] dst = new int[n][h][w];
        for (int f = 0; f < n; f++) {
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    dst[f][y][x] = src[f][y][w - 1 - x];
                }
            }
        }
        return dst;
    }
}
