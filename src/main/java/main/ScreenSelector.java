package main;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import main.GamePanel.GameState;

public class ScreenSelector implements KeyListener {

    private final GamePanel gamePanel;
    private final Color DEFAULT_COLOR = Color.WHITE;

    private final List<List<SelectionItem>> screens = new ArrayList<>();
    private int screenIndex = 0;
    private int commandNumber = 0;
    private int displayNumber = 0;
    private int pageSize = 6;
    private int pageNumber = 0;

    private Font customFont;

    static class SelectionItem {
        public String displayName;
        public Object displayValue;
        public boolean selected;
        public Color color = Color.WHITE;
        public SelectionItem(String displayName, Object displayValue) {
            this.displayName = displayName;
            this.displayValue = displayValue;
        }
    }

    class SelectionResult {
        public boolean selected = false;
        public Object selectedObject;
        public String selectedName;
        public int selectedIndex = 0;
        public int selectedScreenIndex = 0;
        public int customKeyPress = -1;

        @Override
        public String toString() {
            return "{" +
                "selected: " + this.selected + ", " +
                "selectedName: " + this.selectedName + ", " +
                "selectedIndex: " + this.selectedIndex + ", " +
                "selectedScreenIndex: " + this.selectedScreenIndex + ", " +
                "customKeyPress: " + this.customKeyPress +
            "}";
        }

        public String getSelectedName() {
            return this.selectedName != null
                    ? this.selectedName.replaceAll("\\(\\d+\\)", "").trim()
                    : null;
        }
    }

    private SelectionResult result = new SelectionResult();

    public ScreenSelector(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        loadFonts();
    }

    public void addScreen(List<SelectionItem> items) {
        screens.add(new ArrayList<>(items));
    }

    public void clearScreens() {
        screens.clear();
        screenIndex = 0;
        clearSelection();
    }

    public void set(int index, List<SelectionItem> list) {
        if (index >= 0 && index < screens.size()) {
            screens.set(index, list);
        } else {
            throw new IndexOutOfBoundsException("Invalid screen index: " + index);
        }
    }

    public void clearSelection() {
        this.result = new SelectionResult();
        this.commandNumber = 0;
    }

    public void clearSelectionLeaveHighlight() {
        this.result = new SelectionResult();
    }

    public SelectionResult selector(Graphics2D graphics2D, int x, int y, int delimiter, boolean center) {

        if (screens.isEmpty()) return null;

        List<SelectionItem> items = screens.get(screenIndex);

        // Default
        result.selectedObject = null;
        result.selectedIndex = -1;
        result.selectedScreenIndex = screenIndex;

        if (items == null || items.isEmpty()) {
            draw(graphics2D, x, y, delimiter, new ArrayList<>(), center);
            return result;
        }

        List<List<SelectionItem>> pages = chunk(items);

        // Clamp commandNumber within bounds
        if (commandNumber < 0) commandNumber = 0;
        if (commandNumber >= items.size()) commandNumber = items.size() - 1;

        pageNumber = commandNumber / pageSize;
        if (pageNumber >= pages.size()) pageNumber = pages.size() - 1;

        List<SelectionItem> pageItems = pages.get(pageNumber);
        result.selectedObject = items.get(commandNumber).displayValue;
        result.selectedIndex = commandNumber;

        draw(graphics2D, x, y, delimiter, pageItems, center);
        return result;
    }

    public SelectionResult display(Graphics2D graphics2D, int x, int y, int delimiter, boolean center, int screenIndex) {

        if (screens.isEmpty()) return null;

        List<SelectionItem> items = screens.get(screenIndex);

        if (items == null || items.isEmpty()) {
            drawDisplay(graphics2D, x, y, delimiter, new ArrayList<>(), center);
            return result;
        }

        List<List<SelectionItem>> pages = chunk(items);

        // Clamp commandNumber within bounds
        if (displayNumber < 0) displayNumber = 0;
        if (displayNumber >= items.size()) displayNumber = items.size() - 1;

        pageNumber = displayNumber / pageSize;
        if (pageNumber >= pages.size()) pageNumber = pages.size() - 1;

        List<SelectionItem> pageItems = pages.get(pageNumber);

        drawDisplay(graphics2D, x, y, delimiter, pageItems, center);
        return result;
    }

    private List<List<SelectionItem>> chunk(List<SelectionItem> items) {
        List<List<SelectionItem>> pages = new ArrayList<>();
        for (int i = 0; i < items.size(); i += pageSize) {
            pages.add(items.subList(i, Math.min(i + pageSize, items.size())));
        }
        return pages;
    }

    private void draw(Graphics2D graphics2D, int x, int y, int delimiter, List<SelectionItem> items, boolean center) {
        graphics2D.setFont(customFont);
        graphics2D.setColor(Color.WHITE);

        for (int i = 0; i < items.size(); i++) {
            if (commandNumber % pageSize == i) {
                setCursor(graphics2D, x, y);
            }
            if (items.get(i).selected) {
                graphics2D.fillRoundRect(x + Constants.TILE_SIZE / 2, y - 10, 6, 6, 6, 6);
            }
            if (!center) {
                graphics2D.setColor(items.get(i).color);
                graphics2D.drawString(items.get(i).displayName, x + Constants.TILE_SIZE, y);
            } else {
                int centerX = this.gamePanel.ui.getXForCenteredText(graphics2D, items.get(i).displayName, customFont);
                graphics2D.setColor(items.get(i).color);
                graphics2D.drawString(items.get(i).displayName, centerX, y);
            }
            graphics2D.setColor(DEFAULT_COLOR);
            y += delimiter;
        }
    }

    private void drawDisplay(Graphics2D graphics2D, int x, int y, int delimiter, List<SelectionItem> items, boolean center) {
        graphics2D.setFont(customFont);
        graphics2D.setColor(Color.WHITE);

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).selected) {
                graphics2D.fillRoundRect(x + Constants.TILE_SIZE / 2, y - 10, 6, 6, 6, 6);
            }
            if (!center) {
                graphics2D.setColor(items.get(i).color);
                graphics2D.drawString(items.get(i).displayName, x + Constants.TILE_SIZE, y);
            } else {
                int centerX = this.gamePanel.ui.getXForCenteredText(graphics2D, items.get(i).displayName, customFont);
                graphics2D.setColor(items.get(i).color);
                graphics2D.drawString(items.get(i).displayName, centerX, y);
            }
            graphics2D.setColor(DEFAULT_COLOR);
            y += delimiter;
        }
    }

    public void setScreen(int screenIndex) {
        this.screenIndex = screenIndex;
    }

    private void setCursor(Graphics2D g2d, int x, int y) {
        g2d.drawString(Constants.GAME_TITLE_SELECTOR, x, y);
    }

    public int getScreenIndex() {
        return this.screenIndex;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
        if (screens.isEmpty()) return;

        int code = e.getKeyCode();
        List<SelectionItem> currentItems = screens.get(screenIndex);

        switch (this.gamePanel.gameState) {
            case GameState.INVENTORY:
                this.gamePanel.playSoundEffect(Constants.SOUND_CURSOR);
                switch (code) {
                    case KeyEvent.VK_W -> up(currentItems);
                    case KeyEvent.VK_S -> down(currentItems);
                    case KeyEvent.VK_A -> left(currentItems);
                    case KeyEvent.VK_D -> right(currentItems);
                    case KeyEvent.VK_R -> {
                        this.result.selected = true;
                        this.result.customKeyPress = KeyEvent.VK_R;
                    }
                    case KeyEvent.VK_ENTER, KeyEvent.VK_SPACE -> select();
                }
                break;
            case GameState.PAUSE:
            case GameState.TITLE:
                this.gamePanel.playSoundEffect(Constants.SOUND_CURSOR);
                switch (code) {
                    case KeyEvent.VK_W -> up(currentItems);
                    case KeyEvent.VK_S -> down(currentItems);
                    case KeyEvent.VK_A -> {
                        this.result.selected = true;
                        this.result.customKeyPress = KeyEvent.VK_A;
                    }
                    case KeyEvent.VK_D -> {
                        this.result.selected = true;
                        this.result.customKeyPress = KeyEvent.VK_D;
                    }
                    case KeyEvent.VK_ENTER, KeyEvent.VK_SPACE -> select();
                }
                break;
            case GameState.VENDOR:
                this.gamePanel.playSoundEffect(Constants.SOUND_CURSOR);
                switch (code) {
                    case KeyEvent.VK_W -> up(currentItems);
                    case KeyEvent.VK_S -> down(currentItems);
                    case KeyEvent.VK_A -> left(currentItems);
                    case KeyEvent.VK_D -> right(currentItems);
                    case KeyEvent.VK_ENTER, KeyEvent.VK_SPACE -> select();
                    case KeyEvent.VK_E -> {
                        this.gamePanel.gameState = GameState.PLAY;
                    }
                }
                break;
            default:
                return;
        }
    }

    private void up(List<SelectionItem> currentItems) {
        commandNumber--;
        if (commandNumber < 0) commandNumber = currentItems.size() - 1;
    }

    private void down(List<SelectionItem> currentItems) {
        commandNumber++;
        if (commandNumber >= currentItems.size()) commandNumber = 0;
    }

    private void left(List<SelectionItem> currentItems) {
        screenIndex = (screenIndex - 1 + screens.size()) % screens.size();
        commandNumber = 0;
    }

    private void right(List<SelectionItem> currentItems) {
        screenIndex = (screenIndex + 1) % screens.size();
        commandNumber = 0;
    }

    private void select() {
        this.result.selected = true;
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    private void loadFonts() {
        try {
            this.customFont = Font
                .createFont(Font.TRUETYPE_FONT,
                    getClass()
                        .getResourceAsStream(Constants.FONT_DOS))
                        .deriveFont(Font.PLAIN, Constants.FONT_SIZE);
        } catch (FontFormatException | IOException e) {
            this.customFont = new Font(Constants.FONT_ARIAL, Font.PLAIN, 20);
        }
    }
}
