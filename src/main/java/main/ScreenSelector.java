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

    private final List<List<String>> screens = new ArrayList<>();
    private int screenIndex = 0;
    private int commandNumber = 0;
    private int pageSize = 6;
    private int pageNumber = 0;

    private boolean markedSelected = false;
    private int markedSelectedIndex = 0;
    private String selectedItem;

    private Font customFont;

    class SelectionResult {
        public boolean selected = false;
        public String selectedName;
        public int selectedIndex = 0;
        public int selectedScreenIndex = 0;

        @Override
        public String toString() {
            return "{" +
                "selected: " + this.selected + ", " +
                "selectedName: " + this.selectedName + ", " +
                "selectedIndex: " + this.selectedIndex + ", " +
                "selectedScreenIndex: " + this.selectedScreenIndex +
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

    public void addScreen(List<String> items) {
        screens.add(new ArrayList<>(items));
    }

    public void clearScreens() {
        screens.clear();
        screenIndex = 0;
        clearSelection();
    }

    public void set(int index, List<String> list) {
        if (index >= 0 && index < screens.size()) {
            screens.set(index, list);
        } else {
            throw new IndexOutOfBoundsException("Invalid screen index: " + index);
        }
    }

    public void clearSelection() {
        this.result = new SelectionResult();
        this.commandNumber = 0;
        this.markedSelectedIndex = 0;
        this.markedSelected = false;
        this.selectedItem = null;
    }

    public void markedSelected(int index) {
        this.markedSelected = true;
        this.markedSelectedIndex = index;
    }

    public SelectionResult selector(Graphics2D graphics2D, int x, int y, int delimiter) {
        if (screens.isEmpty()) return null;

        List<String> items = screens.get(screenIndex);

        // Default
        result.selectedName = null;
        result.selectedIndex = -1;
        result.selectedScreenIndex = screenIndex;

        if (items == null || items.isEmpty()) {
            draw(graphics2D, x, y, delimiter, new ArrayList<>());
            return result;
        }

        List<List<String>> pages = chunk(items);

        // Clamp commandNumber within bounds
        if (commandNumber < 0) commandNumber = 0;
        if (commandNumber >= items.size()) commandNumber = items.size() - 1;

        pageNumber = commandNumber / pageSize;
        if (pageNumber >= pages.size()) pageNumber = pages.size() - 1;

        List<String> pageItems = pages.get(pageNumber);
        result.selectedName = items.get(commandNumber);
        result.selectedIndex = commandNumber;

        draw(graphics2D, x, y, delimiter, pageItems);
        return result;
    }



    private List<List<String>> chunk(List<String> items) {
        List<List<String>> pages = new ArrayList<>();
        for (int i = 0; i < items.size(); i += pageSize) {
            pages.add(items.subList(i, Math.min(i + pageSize, items.size())));
        }
        return pages;
    }

    private void draw(Graphics2D g2d, int x, int y, int delimiter, List<String> items) {
        g2d.setFont(customFont);
        for (int i = 0; i < items.size(); i++) {
            if (commandNumber % pageSize == i) {
                setCursor(g2d, x, y);
            }
            g2d.setColor(Color.WHITE);
            if (this.markedSelected && this.markedSelectedIndex - (this.pageNumber * this.pageSize) == i) {
                g2d.fillRoundRect(x + Constants.TILE_SIZE / 2, y - 10, 6, 6, 6, 6);
            }
            g2d.drawString(items.get(i), x + Constants.TILE_SIZE, y);
            y += delimiter;
        }
    }

    private void setCursor(Graphics2D g2d, int x, int y) {
        g2d.drawString(Constants.GAME_TITLE_SELECTOR, x, y);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (this.gamePanel.gameState != GameState.INVENTORY) return;
        if (screens.isEmpty()) return;

        int code = e.getKeyCode();
        List<String> currentItems = screens.get(screenIndex);

        switch (code) {
            case KeyEvent.VK_W -> {
                commandNumber--;
                if (commandNumber < 0) commandNumber = currentItems.size() - 1;
                this.gamePanel.playSoundEffect(Constants.SOUND_CURSOR);
            }
            case KeyEvent.VK_S -> {
                commandNumber++;
                if (commandNumber >= currentItems.size()) commandNumber = 0;
                this.gamePanel.playSoundEffect(Constants.SOUND_CURSOR);
            }
            case KeyEvent.VK_A -> {
                screenIndex = (screenIndex - 1 + screens.size()) % screens.size();
                commandNumber = 0;
                this.gamePanel.playSoundEffect(Constants.SOUND_CURSOR);
            }
            case KeyEvent.VK_D -> {
                screenIndex = (screenIndex + 1) % screens.size();
                commandNumber = 0;
                this.gamePanel.playSoundEffect(Constants.SOUND_CURSOR);
            }
            case KeyEvent.VK_ENTER, KeyEvent.VK_SPACE -> {
                this.selectedItem = currentItems.get(commandNumber);
                this.markedSelectedIndex = commandNumber;
                this.result.selected = true;
                this.gamePanel.playSoundEffect(Constants.SOUND_CURSOR);
            }
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}
