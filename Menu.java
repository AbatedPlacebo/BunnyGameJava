import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.FontMetrics;

public class Menu {
    private Game game;

    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 40;
    private static final int START_BUTTON_Y = 130;
    private static final int EXIT_BUTTON_Y = 180;
    private static final int TITLE_Y = 80;
    private static final int TITLE_FONT_SIZE = 24;
    private static final Color TITLE_COLOR = Color.BLACK;

    public enum MenuOption {
        START,
        EXIT
    }

    MenuOption selectedOption = MenuOption.START;

    public Menu(Game game) {
        this.game = game;
    }

    public void render(Graphics g) {
        g.setColor(TITLE_COLOR);
        g.setFont(new Font("Arial", Font.BOLD, TITLE_FONT_SIZE));
        g.drawString("Brown Rabbit Adventure", game.getWidth() / 2 - 130, TITLE_Y);
        drawButton(g, "Start", game.getWidth() / 2 - 100, START_BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT, selectedOption == MenuOption.START);
        drawButton(g, "Exit", game.getWidth() / 2 - 100, EXIT_BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT, selectedOption == MenuOption.EXIT);
    }

    public void drawButton(Graphics g, String text, int x, int y, int width, int height, boolean isSelected) {
        g.setColor(isSelected ? Color.DARK_GRAY : Color.GRAY);
        g.fillRect(x, y, width, height);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        g.drawString(text, x + (width - textWidth) / 2, y + (height + textHeight) / 2 - fm.getDescent());
    }
    public MenuOption getSelectedOption() {
        return selectedOption;
    }
    public void setSelectedOption(MenuOption option) {
        this.selectedOption = option;
    }

    public void select() {
        if (selectedOption == MenuOption.START) {
            game.startGame();
        } else if (selectedOption == MenuOption.EXIT) {
            System.exit(0);
        }
    }

    public void buttonColor(Graphics g, MenuOption option) {
        if (option == selectedOption) {
            g.setColor(Color.DARK_GRAY);
        } else {
            g.setColor(Color.GRAY);
        }
    }
}
