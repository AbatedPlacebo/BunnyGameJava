public class Assets {
        public static SpriteSheet brownRabbitSheet;
        public static SpriteSheet foxSheet;
        public static SpriteSheet blockSheet;
        // … другие спрайт‑шиты

        /**
         * Вызывается один раз при запуске игры, до создания любых сущностей.
         */
        public static void init() {
                brownRabbitSheet = new SpriteSheet(
                                "/sprites/bunny2.png", // путь внутри JAR/resources
                                32, 23);
                foxSheet = new SpriteSheet(
                                "/sprites/fox2.png", // путь внутри JAR/resources
                                48, 48);
                blockSheet = new SpriteSheet(
                                "/sprites/block.png", // путь внутри JAR/resources
                                64, 12);
        }
}
