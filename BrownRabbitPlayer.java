import java.awt.event.KeyEvent;
import java.util.Set;

public class BrownRabbitPlayer extends Player {

    private enum Direction {
        NONE, LEFT, RIGHT, BACK
    }

    private Direction currentDirection = Direction.NONE;
    private boolean isJumping = false;

    public BrownRabbitPlayer(ActionStrategy action, MissStrategy miss) {
        super(action, miss);
    }

    @Override
    public void update(Set<Integer> keys) {
        // Сначала базовое движение + тайминг анимации
        super.update(keys);

        // Определяем, прыгаем ли мы (пробел)
        isJumping = keys.contains(KeyEvent.VK_SPACE);

        // Запоминаем последнее направление стрелок
        if (keys.contains(KeyEvent.VK_LEFT)) {
            currentDirection = Direction.LEFT;
        } else if (keys.contains(KeyEvent.VK_RIGHT)) {
            currentDirection = Direction.RIGHT;
        } else if (keys.contains(KeyEvent.VK_UP)) {
            currentDirection = Direction.BACK;
        } else if (keys.contains(KeyEvent.VK_DOWN)) {
            currentDirection = Direction.NONE;
        }
        // если ни одна стрелка не нажата — направление не меняется
    }

    @Override
    public void render(int[] pixels, int screenW, int screenH) {
        int frame = getAnimationFrame(); // 0,1 или 2
        int[][] sprite;

        if (isJumping) {
            // выбираем «прыжковый» спрайт в зависимости от направления
            switch (currentDirection) {
                case RIGHT:
                    sprite = BrownRabbitSprite.RABBIT_JUMP_RIGHT[frame];
                    break;
                case LEFT:
                    sprite = BrownRabbitSprite.RABBIT_JUMP_LEFT[frame];
                    break;
                case BACK:
                    sprite = BrownRabbitSprite.RABBIT_JUMP_BACK[frame];
                    break;
                default:
                    // если direction==NONE — прыгаем вперед лицом к нам
                    sprite = BrownRabbitSprite.RABBIT_FACE[frame];
                    break;
            }
        } else {
            // обычная ходовая анимация лицом к нам
            sprite = BrownRabbitSprite.RABBIT_FACE[frame];
        }

        // рисуем там, где базовый класс хранит свои x/y
        Sprite.drawSprite(pixels, screenW, screenH, getX(), getY(), sprite);
    }
}
