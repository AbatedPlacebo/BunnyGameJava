public class Assets {
    public static SpriteSheet brownRabbitSheet;
    public static SpriteSheet foxSheet;
    // … другие спрайт‑шиты

    /**
     * Вызывается один раз при запуске игры, до создания любых сущностей.
     */
    public static void init() {
        brownRabbitSheet = new SpriteSheet(
                "/sprites/bunny.png", // путь внутри JAR/resources
                32, 32);
        foxSheet = new SpriteSheet(
                "/sprites/fox.png", // путь внутри JAR/resources
                32, 32);
    }
}
