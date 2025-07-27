public class Assets {
    public static SpriteSheet brownRabbitSheet;
    // … другие спрайт‑шиты

    /**
     * Вызывается один раз при запуске игры, до создания любых сущностей.
     */
    public static void init() {
        brownRabbitSheet = new SpriteSheet(
                "/sprites/brown_rabbit.png", // путь внутри JAR/resources
                32, 32);
    }
}
