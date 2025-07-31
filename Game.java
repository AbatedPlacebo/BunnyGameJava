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

    private final BufferedImage image;
    private final int[] pixels;

    private Level currentLevel;

    private Player player = new BrownRabbitPlayer(new WarriorAction(), new WarriorMissChance());

    private final List<Entity> entities = new ArrayList<>();

    private enum GameState {
        MAIN_MENU,
        PLAYING
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
        entities.add(new FoxNPC(200, 110, 150, 300, player));
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
        if (gameState == GameState.PLAYING) {
            currentLevel.render(pixels, width, height);
            for (Entity e : entities) {
                e.update(keys);
                e.render(pixels, width, height);
            }
        } else {
            for (int i = 0; i < pixels.length; i++) {
                pixels[i] = 0xFFFFFF; // menu background
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
            g.setFont(new Font("Arial", Font.PLAIN, 16));
            g.drawString("Press ENTER to Start", 90, 130);
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
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys.remove(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {
    } // unused

}
