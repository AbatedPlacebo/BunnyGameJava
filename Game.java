import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
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

    private int xScroll = 0;
    private int yScroll = 0;

    private final BufferedImage image;
    private final int[] pixels;

    private Level currentLevel;

    private final List<Block> blocks = new ArrayList<>();

    private Player player = new BrownRabbitPlayer(new WarriorAction(), new WarriorMissChance());

    private final List<Entity> entities = new ArrayList<>();

    private Clip bgMusic;

    private Menu menu = new Menu(this);

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
        loadMusic("/audio/background.wav");
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
        currentLevel.render(pixels, width, height, xScroll, yScroll);
        if (gameState == GameState.PLAYING) {
            // Follow player with camera
            xScroll = player.getX() - width / 2;
            yScroll = player.getY() - height / 2;

            //// Clamp camera to level boundaries
            xScroll = Math.max(0, Math.min(xScroll, currentLevel.getWidth() - width));
            yScroll = Math.max(0, Math.min(yScroll, currentLevel.getHeight() - height));

            // Render background/level with scroll
            currentLevel.render(pixels, width, height, xScroll, yScroll);

            // Update and render entities
            for (Entity e : entities) {
                e.update(keys);
                player.checkBlockCollision(blocks);
                e.render(pixels, width, height, xScroll, yScroll); // pass scroll to entity rendering
            }

            for (Block block : blocks) {
                block.render(pixels, width, height, xScroll, yScroll);
            }
        } else if (gameState == GameState.GAME_OVER) {
            stopMusic();
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
            menu.render(g);
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

        if (gameState == GameState.MAIN_MENU) {

            if (keys.contains(KeyEvent.VK_UP) || keys.contains(KeyEvent.VK_W)) {
                menu.setSelectedOption(Menu.MenuOption.START);
            } else if (keys.contains(KeyEvent.VK_DOWN) || keys.contains(KeyEvent.VK_S)) {
                menu.setSelectedOption(Menu.MenuOption.EXIT);
            }
        }

        if (gameState == GameState.MAIN_MENU && e.getKeyCode() == KeyEvent.VK_ENTER) {
            menu.select();
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

    private void loadMusic(String path) {
        try (AudioInputStream ais = AudioSystem.getAudioInputStream(getClass().getResource(path))) {
            bgMusic = AudioSystem.getClip();
            bgMusic.open(ais);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void stopMusic() {
        if (bgMusic != null && bgMusic.isRunning()) {
            bgMusic.stop();
        }
    }

    public void startGame() {
        gameState = GameState.PLAYING;
        if (bgMusic != null) {
            bgMusic.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
}
