package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import main.GamePanel.GameState;

public class Selector implements KeyListener {

    GamePanel gamePanel;
    
    List<String> selectorItems = new ArrayList<>();
    List<String> originalSelectorItems = new ArrayList<>();
    List<List<String>> selectorItemsLong = new ArrayList<>();

    public int commandNumber = 0;
    public int tabNumber = 0;

    int pageSize = 6;
    int pageNumber = 0;
    int tabSize = 2;
    String selectedItem;
    int markedSelectedIndex;
    boolean markedSelected;
    SelectionResult result = new SelectionResult();

    Font customFontSmall;
    Font customFont;
    Font customFontMedium;
    Font customFontLarge;

    boolean selectorEnabled = true;

    class SelectionResult {
        public boolean selected = false;
        public String selectedName;
        public int selectedIndex = 0;

        public String getSelectedName() {
            return this.selectedName.replaceAll("\\(\\d+\\)", "").trim();
        }
    }

    public Selector(GamePanel gamePanel, int tabSize) {
        this(gamePanel);
        this.tabSize = tabSize;
    }

    public Selector(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        try {
            this.customFontSmall = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream(Constants.FONT_DOS)).deriveFont(Font.PLAIN, Constants.FONT_SIZE_SMALL);
            this.customFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream(Constants.FONT_DOS)).deriveFont(Font.PLAIN, Constants.FONT_SIZE);
            this.customFontMedium = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream(Constants.FONT_DOS)).deriveFont(Font.PLAIN, Constants.FONT_SIZE_MEDIUM);
            this.customFontLarge = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream(Constants.FONT_DOS)).deriveFont(Font.PLAIN, Constants.FONT_SIZE_LARGE);
        } catch (FontFormatException | IOException e) {
            this.customFont = new Font(Constants.FONT_ARIAL, Font.PLAIN, 20);
            this.customFontMedium = new Font(Constants.FONT_ARIAL, Font.PLAIN, 35);
            this.customFontLarge = new Font(Constants.FONT_ARIAL, Font.PLAIN, 50);
            e.printStackTrace();
        }
    }

    public void markedSelected(int index) {
        this.markedSelected = true;
        this.markedSelectedIndex = index;
    }

    public SelectionResult selector(Graphics2D graphics2D, int x, int y, int delimiter, List<String> selectorItems) {
        if (selectorItems == null || selectorItems.isEmpty()) {
            return null;
        }
        this.originalSelectorItems = selectorItems;

        if (selectorItemsLong == null || selectorItemsLong.isEmpty() || !selectorItemsLong.get(0).equals(selectorItems)) {
            selectorItemsLong = new ArrayList<>();
            for (int i = 0; i < selectorItems.size(); i += pageSize) {
                selectorItemsLong.add(new ArrayList<>(
                    selectorItems.subList(i, Math.min(i + pageSize, selectorItems.size()))
                ));
            }
        }

        try {
            if (commandNumber < 0) { commandNumber = 0; }
            if (commandNumber >= originalSelectorItems.size()) {
                commandNumber = originalSelectorItems.size() - 1;
            }
            this.pageNumber = commandNumber / pageSize;
            if (pageNumber >= selectorItemsLong.size()) {
                pageNumber = selectorItemsLong.size() - 1;
            }
            this.selectorItems = selectorItemsLong.get(pageNumber);
            this.result.selectedName = originalSelectorItems.get(commandNumber);
            this.result.selectedIndex = commandNumber;
        } catch (Exception e) {
            System.err.println("Error in selector:");
            e.printStackTrace();
            return this.result;
        }

        draw(graphics2D, x, y, delimiter);
        return this.result;
    }

    public void disableSelection() {
        this.commandNumber = 0;
    }

    public void enableSelection() {
        this.commandNumber = 0;
    }

    public void draw(Graphics2D graphics2D, int x, int y, int delimiter) {
        graphics2D.setFont(this.customFont);
        int i = 0;
        for (String menuOption : this.selectorItems) {
            if (this.commandNumber - (this.pageNumber * this.pageSize) == i) {
                setTitleCursor(graphics2D, x, y);
            }
            graphics2D.setColor(Color.WHITE);
            if (this.markedSelected && this.markedSelectedIndex - (this.pageNumber * this.pageSize) == i) {
                graphics2D.fillRoundRect(x + Constants.TILE_SIZE / 2, y - 10, 6, 6, 6, 6);
            }
            graphics2D.drawString(menuOption, x + Constants.TILE_SIZE, y);
            y += delimiter;
            i++;
        }
        if (this.selectorItemsLong.size() - 1 != this.pageNumber) {
            graphics2D.drawString("...", x + Constants.TILE_SIZE, y);
        }
    }

    public void setTitleCursor(Graphics2D graphics2D, int x, int y) {
        graphics2D.drawString(Constants.GAME_TITLE_SELECTOR, x, y);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (this.gamePanel.gameState == GameState.PLAY) { return; }
        if (!this.selectorEnabled) { return; }
        int code = e.getKeyCode();
        if (selectorItems == null || selectorItems.isEmpty()) { return; }
        if (code == KeyEvent.VK_W) {
            this.commandNumber--;
            this.gamePanel.playSoundEffect(Constants.SOUND_CURSOR);
            if (this.commandNumber < 0) {
                this.commandNumber = this.originalSelectorItems.size() - 1;
                System.out.println(this.commandNumber);
            }
        }
        if (code == KeyEvent.VK_S) {
            this.commandNumber++;
            this.gamePanel.playSoundEffect(Constants.SOUND_CURSOR);
            if (this.commandNumber >= this.originalSelectorItems.size()) {
                this.commandNumber = 0;
                System.out.println(this.commandNumber);
            }
        }
        if (this.tabSize > 1) {
            if (code == KeyEvent.VK_A) {
                this.tabNumber--;
                this.gamePanel.playSoundEffect(Constants.SOUND_CURSOR);
                if (this.tabNumber < 0) {
                    this.tabNumber = this.originalSelectorItems.size() - 1;
                    this.commandNumber = 0;
                }
            }
            if (code == KeyEvent.VK_D) {
                this.tabNumber++;
                this.gamePanel.playSoundEffect(Constants.SOUND_CURSOR);
                if (this.tabNumber > this.tabSize) {
                    this.tabNumber = 0;
                    this.commandNumber = 0;
                }
            }
        }
        if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
            System.out.println(this.commandNumber);
            this.selectedItem = this.originalSelectorItems.get(this.commandNumber);
            this.markedSelectedIndex = this.commandNumber;
            this.result.selected = true;
            this.gamePanel.playSoundEffect(Constants.SOUND_CURSOR);
            this.clear();
        }
    }

    public void clear() {
        this.result = new SelectionResult();
        this.commandNumber = 0;
        this.markedSelectedIndex = 0;
        this.markedSelected = false;
        this.selectedItem = null;
        this.selectorItems.clear();
        this.selectorItemsLong.clear();
    }

    @Override
    public void keyReleased(KeyEvent e) { }

    @Override
    public void keyTyped(KeyEvent e) { }

}
