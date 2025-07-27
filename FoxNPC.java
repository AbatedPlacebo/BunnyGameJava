import java.util.Set;

public class FoxNPC extends Entity {
    private int dir = 1, speed = 1;
    private int patrolMinX, patrolMaxX;

    public FoxNPC(int startX, int startY, int minX, int maxX) {
        super(startX, startY, Assets.foxSheet);
        this.patrolMinX = minX;
        this.patrolMaxX = maxX;
    }

    @Override
    public void update(Set<Integer> keys) {
        x += dir * speed;
        if (x < patrolMinX || x > patrolMaxX)
            dir *= -1;
        animationTick++;
        if (animationTick % 15 == 0)
            animationFrame = (animationFrame + 1) % 4;
    }

    @Override
    public void render(int[] pixels, int screenW, int screenH) {
        int ts = sheet.getTileSize() * (animationFrame % 4);
        sheet.render(x, y, ts, 0, pixels, screenW, screenH);
    }

}
