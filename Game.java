import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.HashSet;
import java.util.Set;

public class Game extends JPanel implements Runnable, KeyListener {
    private final Set<Integer> keys = new HashSet<>();
    private final int width = 320;
    private final int height = 240;
    private final int scale = 4; // Scale factor (2x -> 640x480)

    private final BufferedImage image;
    private final int[] pixels;

    private Player player = new BrownRabbitPlayer(new WarriorAction(), new WarriorMissChance());

    public Game() {
        setPreferredSize(new Dimension(width * scale, height * scale));
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);
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
        // Clear screen
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = 0x2D2D2D; // Dark gray background
        }

        player.update(keys);
        player.render(pixels, width, height);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, width * scale, height * scale, null);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Pixel Game 320x240");
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
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys.remove(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {
    } // unused

}
