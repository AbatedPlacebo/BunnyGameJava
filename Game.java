import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Game extends JPanel implements Runnable, KeyListener {
    private final Set<Integer> keys = new HashSet<>();
    private final int width = 353;
    private final int height = 180;
    private final int scale = 4; // Scale factor (2x -> 640x480)
    private int fadeAlpha = 0;
    private final int fadeSpeed = 3; // Controls fade speed (1–10 range is good)

    private final BufferedImage image;
    private final int[] pixels;

    private Level currentLevel;

    private final List<Block> blocks = new ArrayList<>();

    private Player player = new BrownRabbitPlayer(new WarriorAction(), new WarriorMissChance());

    private final List<Entity> entities = new ArrayList<>();

    private enum GameState {
        MAIN_MENU,
        PLAYING,
        GAME_OVER
    }

    private GameState gameState = GameState.MAIN_MENU;

    public Game() {
        setPreferredSize(new Dimension(width * scale, height * scale));
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);
        currentLevel = new Level("/maps/LevelA.png"); // assumes in resources
        entities.add(player);
        entities.add(new FoxNPC(200, 160, 150, 300, player));
        blocks.clear();
        blocks.add(new Block(100, 140)); // Пример платформы
        blocks.add(new Block(200, 100)); // Ещё одна
    }

    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        // Game loop
        while (true) {
            update();
            repaint();
            try {
                Thread.sleep(16); // ~60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {
        currentLevel.render(pixels, width, height);

        if (gameState == GameState.PLAYING) {
            for (Entity e : entities) {
                e.update(keys);
                player.checkBlockCollision(blocks);
                e.render(pixels, width, height);
                for (Block block : blocks) {
                    block.render(pixels, width, height);
                }
            }

            if (player.getHealth() <= 0) {
                gameState = GameState.GAME_OVER;
                fadeAlpha = 0; // Reset fade
            }
        } else if (gameState == GameState.GAME_OVER) {
            if (fadeAlpha < 255) {
                fadeAlpha = Math.min(255, fadeAlpha + fadeSpeed);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, width * scale, height * scale, null);

        if (gameState == GameState.MAIN_MENU) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("Brown Rabbit Adventure", 40, 80);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString("Press ENTER to Start", 90, 130);
        } else if (gameState == GameState.PLAYING) {
            drawHealthBar(g);
        } else if (gameState == GameState.GAME_OVER) {
            // Semi-transparent black overlay
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new Color(0, 0, 0, fadeAlpha));
            g2d.fillRect(0, 0, width * scale, height * scale);

            // Only show text once fully faded in
            if (fadeAlpha >= 200) {
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 32));
                g.drawString("Game Over", 100, 80);
                g.setFont(new Font("Arial", Font.BOLD, 16));
                g.drawString("Press R to Restart", 100, 130);
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Pixel Game 320x240");
        Assets.init();
        Game game = new Game();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(game);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        game.start();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys.add(e.getKeyCode());

        if (gameState == GameState.MAIN_MENU && e.getKeyCode() == KeyEvent.VK_ENTER) {
            gameState = GameState.PLAYING;
        } else if ((gameState == GameState.PLAYING || gameState == GameState.GAME_OVER)
                && e.getKeyCode() == KeyEvent.VK_R) {
            restartGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys.remove(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {
    } // unused

    private void drawHealthBar(Graphics g) {
        int health = player.getHealth(); // Assuming getter exists
        int maxHealth = player.getMaxHealth(); // Assuming getter exists

        int barWidth = 200;
        int barHeight = 20;
        int x = 20;
        int y = 20;

        int filledWidth = (int) ((health / (double) maxHealth) * barWidth);

        // Background
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, barWidth, barHeight);

        // Health Fill
        g.setColor(Color.RED);
        g.fillRect(x, y, filledWidth, barHeight);

        // Border
        g.setColor(Color.BLACK);
        g.drawRect(x, y, barWidth, barHeight);

        // Text
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("HP: " + health + " / " + maxHealth, x + 5, y + 15);
    }

    private void restartGame() {
        currentLevel = new Level("/maps/LevelA.png");
        player = new BrownRabbitPlayer(new WarriorAction(), new WarriorMissChance());
        entities.clear();
        entities.add(player);
        entities.add(new FoxNPC(200, 160, 150, 300, player));
        blocks.clear();
        blocks.add(new Block(100, 140)); // Пример платформы
        blocks.add(new Block(200, 100)); // Ещё одна
        gameState = GameState.PLAYING;
        fadeAlpha = 0;
    }

}
