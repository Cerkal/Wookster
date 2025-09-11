package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.FontFormatException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import main.GamePanel.GameState;
import spells.SuperSpell.SpellType;

public class UI {
    
    GamePanel gamePanel;
    Selector selector;
    ScreenSelector screenSelector;

    boolean messageDisplay = false;
    long messageStartTime = 0;
    String currentMessage = "";
    Font customFontSmall;
    Font customFont;
    Font customFontMedium;
    Font customFontLarge;
    int currentCharacterIndex = 0;

    String currentDialogue = "";
    boolean dialogueDisplay = false;

    public String currentScreen;
    public boolean dialoguePrinting = false;
    public boolean dialogueSkip = false;
    public boolean debug = false;

    private long loadingDotUpdate = 0;
    private int loadingDotCount = 0;

    final static int PADDING_X = 20;
    final static int PADDING_Y = 10;

    int commandNumber = 0;
    int currentTab = 0;

    public UI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        // Screen Selector Test
        this.screenSelector = new ScreenSelector(this.gamePanel);

        for (int i = 0; i < Constants.INVENTORY_TABS.size(); i++) {
            screenSelector.addScreen(new ArrayList<>());
        }
        // Register KeyListener
        this.gamePanel.addKeyListener(screenSelector);

        // Load custom font
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

    public void draw(Graphics2D graphics2D) {
        graphics2D.setFont(this.customFont);
        
        drawHealth(graphics2D);
        drawDebug(graphics2D);
        drawWeapon(graphics2D);

        if (this.messageDisplay) {
            drawMessage(graphics2D, this.currentMessage, false);
            if (isMessageTimeElapsed()) {
                this.messageDisplay = false;
            }
        }

        if (dialogueDisplay) {
            drawMessage(graphics2D, this.currentDialogue, true);
        }

        drawPauseScreen(graphics2D);
        drawDeathScreen(graphics2D);
        drawInventory(graphics2D);
    }

    public void displayMessage(String message) {
        this.currentMessage = message;
        this.messageDisplay = true;
        this.dialogueDisplay = false;
        this.messageStartTime = System.currentTimeMillis();
    }

    public void displayDialog(String message) {
        this.gamePanel.gameState = GameState.DIALOGUE;
        this.currentDialogue = message;
        this.messageDisplay = false;
        this.dialogueDisplay = true;
        this.messageStartTime = System.currentTimeMillis();
        this.currentCharacterIndex = 0;
    }

    public void stopDialogue() {
        this.gamePanel.gameState = GamePanel.GameState.PLAY;
        this.dialogueDisplay = false;
        this.currentDialogue = "";
        this.currentCharacterIndex = 0;
    }

    public void titleScreen(Graphics2D graphics2D) {
        if (currentScreen == Constants.GAME_TITLE_SCREEN_CONTROLS) {
            drawControllsScreen(graphics2D);
            return;
        }
        if (currentScreen == Constants.GAME_TITLE_SCREEN_OPTIONS) {
            drawOptionsScreen(graphics2D);
            return;
        }
        graphics2D.setColor(Color.BLACK);
        graphics2D.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        graphics2D.setFont(this.customFontLarge);
        graphics2D.setColor(Color.WHITE);
        int x = getXForCenteredText(graphics2D, Constants.GAME_TITLE, this.customFontLarge);
        int y = getYForCenteredText();
        y = 160;
        graphics2D.drawString(Constants.GAME_TITLE, x, y);
        graphics2D.setFont(this.customFontMedium);

        int i = 0;
        y += 50;
        for (String menuOption : Constants.GAME_TITLE_MENU) {
            x = getXForCenteredText(graphics2D, menuOption, this.customFontMedium);
            y = y + 50;
            if (this.commandNumber == i) {
                setTitleCursor(graphics2D, y);
            }
            graphics2D.setColor(Color.WHITE);
            graphics2D.drawString(menuOption, x, y);
            i++;
        }
    }

    public void drawControllsScreen(Graphics2D graphics2D) {
        graphics2D.setColor(Color.BLACK);
        graphics2D.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        graphics2D.setFont(this.customFontLarge);
        graphics2D.setColor(Color.WHITE);
        int x = getXForCenteredText(graphics2D, Constants.GAME_TITLE_SCREEN_CONTROLS, this.customFontLarge);
        int y = getYForCenteredText();
        y = 160;
        graphics2D.drawString(Constants.GAME_TITLE_SCREEN_CONTROLS, x, y);
        graphics2D.setFont(this.customFontMedium);

        y += 50;
        for (String control : Constants.GAME_CONTROLS_LIST.keySet()) {
            String value = Constants.GAME_CONTROLS_LIST.get(control);
            x = 200;
            y = y + 50;
            graphics2D.setColor(Color.WHITE);
            graphics2D.drawString(control, x, y);
            graphics2D.drawString(value, 600, y);
        }
    }

    public void drawOptionsScreen(Graphics2D graphics2D) {
        graphics2D.setColor(Color.BLACK);
        graphics2D.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        graphics2D.setFont(this.customFontLarge);
        graphics2D.setColor(Color.WHITE);
        int x = getXForCenteredText(graphics2D, Constants.GAME_TITLE_SCREEN_OPTIONS, this.customFontLarge);
        int y = getYForCenteredText();
        y = 160;
        graphics2D.drawString(Constants.GAME_TITLE_SCREEN_OPTIONS, x, y);
        graphics2D.setFont(this.customFontMedium);

        y += 50;
        for (String control : Constants.GAME_OPTIONS_LIST.keySet()) {
            int value = Constants.GAME_OPTIONS_LIST.get(control);
            x = 200;
            y = y + 50;
            graphics2D.setColor(Color.WHITE);
            graphics2D.drawString(control, x, y);
            graphics2D.drawString(String.valueOf(value), 600, y);
        }
    }

    public void drawPauseScreen(Graphics2D graphics2D) {
        if (this.gamePanel.gameState == GamePanel.GameState.PAUSE) {
            titleScreen(graphics2D);
        }
    }

    public void drawLoadingScreen(Graphics2D graphics2D) {

        long now = System.currentTimeMillis();
        if (now - this.loadingDotUpdate >= Constants.DOT_UPDATE_INTERVAL) {
            this.loadingDotCount = (this.loadingDotCount + 1) % 4;
            this.loadingDotUpdate = now;
        }
        
        String dots = switch (this.loadingDotCount) {
            case 1 -> ".";
            case 2 -> "..";
            case 3 -> "...";
            default -> "";
        };
        String loadingText = Constants.GAME_LOADING + dots;
        
        graphics2D.setColor(Color.BLACK);
        graphics2D.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        graphics2D.setFont(this.customFontMedium);
        graphics2D.setColor(Color.WHITE);
        int x = getXForCenteredText(graphics2D, Constants.GAME_LOADING + "...", this.customFontMedium);
        int y = getYForCenteredText();
        graphics2D.drawString(loadingText, x, y);
    }

    public void drawDeathScreen(Graphics2D graphics2D) {
        if (this.gamePanel.gameState == GamePanel.GameState.DEATH) {
            graphics2D.setFont(this.customFontLarge);
            graphics2D.setColor(Color.WHITE);
            int x = getXForCenteredText(graphics2D, Constants.GAME_DEATH, this.customFontLarge);
            int y = getYForCenteredText();
            graphics2D.drawString(Constants.GAME_DEATH, x, y);
            this.gamePanel.stopMusic();
        }
    }

    private void drawInventoryBox(Graphics2D graphics2D, String screen) {
        graphics2D.setFont(this.customFontMedium);
        int x = getXForCenteredText(graphics2D, screen, this.customFontMedium);
        int y = getYForCenteredText();

        graphics2D.setColor(Color.BLACK);
        int width = Constants.SCREEN_WIDTH - Constants.TILE_SIZE * 2;
        int height = Constants.SCREEN_HEIGHT - Constants.TILE_SIZE * 2;
        graphics2D.fillRect(Constants.TILE_SIZE, Constants.TILE_SIZE, width, height);
        graphics2D.setColor(Color.WHITE);

        y = y - 160;
        graphics2D.drawString(screen, x, y);
        y += Constants.TILE_SIZE;
    }

    public void drawInventory(Graphics2D graphics2D) {
        
        if (this.gamePanel.gameState == GamePanel.GameState.INVENTORY) {

            String inventoryTitle = Constants.INVENTORY_TABS.get(this.currentTab);

            drawInventoryBox(graphics2D, inventoryTitle);

            // Set Inventory Items
            HashMap<String, InventoryItem> inventoryMap = this.gamePanel.player.getInventory();
            List<String> inventory = this.gamePanel.player.getInventoryString();
            for (int i = 0; i < inventory.size() - 1; i++) {
                if (this.gamePanel.player.weapon != null && inventory.get(i) == this.gamePanel.player.weapon.weaponType.name()) {
                    screenSelector.markedSelected(i);
                }
            }

            // Inventory Items Index
            screenSelector.set(Constants.INVENTORY_INDEX, inventory);

            // Set Quests
            HashMap<String, Quest> questMap = this.gamePanel.questManager.getAllQuests();
            List<String> allQuests = this.gamePanel.questManager.getAllQuestsString();

            screenSelector.set(Constants.QUEST_INDEX, allQuests);

            ScreenSelector.SelectionResult selectedItem = screenSelector.selector(
                graphics2D,
                Constants.TILE_SIZE * 2 + 20,
                Constants.TILE_SIZE * 5 - 30,
                Constants.NEW_LINE_SIZE
            );

            this.currentTab = selectedItem.selectedScreenIndex;

            System.out.println("Selected Item: " + selectedItem);

            // Handle extra display
            if (selectedItem != null && !selectedItem.selected) {

                int x = 450 + Constants.TILE_SIZE * 4;
                int y = Constants.TILE_SIZE * 5 - 20;
                            
                // Handle Inventory Item
                if (selectedItem.selectedScreenIndex == Constants.INVENTORY_INDEX) {
                    InventoryItem inventoryItem = inventoryMap.get(selectedItem.getSelectedName());
                    inventoryItem.drawInfo(graphics2D, x, y);
                }

                // Handle Quest Item
                if (selectedItem.selectedScreenIndex == Constants.QUEST_INDEX) {
                    Quest quest = questMap.get(selectedItem.selectedName);
                    if (quest != null) {
                        quest.drawInfo(this.gamePanel, graphics2D, 450, y);
                    }
                }
            }

            // Handle selected item
            if (selectedItem != null && selectedItem.selected) {

                // Handle Inventory Item
                if (selectedItem.selectedScreenIndex == Constants.INVENTORY_INDEX) {
                    InventoryItem inventoryItem = inventoryMap.get(selectedItem.getSelectedName());
                    inventoryItem.select();
                    this.gamePanel.gameState = GameState.PLAY;
                    screenSelector.clearSelection();
                }

                // Handle Quest Item
                if (selectedItem.selectedScreenIndex == Constants.QUEST_INDEX) {
                    this.gamePanel.gameState = GameState.PLAY;
                    screenSelector.clearSelection();
                }
            }
            return;
        }

            

            // // Selector
            // try {
            //     HashMap<String, InventoryItem> inventoryMap = this.gamePanel.player.getInventory();
            //     List<String> inventory = this.gamePanel.player.getInventoryString();
            //     for (int i = 0; i < inventory.size() - 1; i++) {
            //         if (this.gamePanel.player.weapon != null && inventory.get(i) == this.gamePanel.player.weapon.weaponType.name()) {
            //             selector.markedSelected(i);
            //         }
            //     }
            //     SelectionResult selectedItem = selector.selector(graphics2D, Constants.TILE_SIZE * 2 + 20, Constants.TILE_SIZE * 5 - 30, Constants.NEW_LINE_SIZE, inventory);
                
            //     if (selectedItem != null && !selectedItem.selectedName.isEmpty()) {
            //         InventoryItem inventoryItem = inventoryMap.get(selectedItem.getSelectedName());
            //         inventoryItem.drawInfo(graphics2D, x + Constants.TILE_SIZE * 4, Constants.TILE_SIZE * 5 - 20);
            //     }
            //     if (selectedItem != null && selectedItem.selected) {
            //         InventoryItem inventoryItem = inventoryMap.get(selectedItem.getSelectedName());
            //         inventoryItem.select();
            //         this.gamePanel.gameState = GameState.PLAY;
            //         selector.clear();
            //     }
            // } catch (Exception e) {
            //     e.printStackTrace();
            // }
    }

    public void deathScreen(Graphics2D graphics2D) {
        graphics2D.setFont(this.customFontLarge);
        graphics2D.setColor(Color.WHITE);
        int x = getXForCenteredText(graphics2D, Constants.GAME_TITLE, this.customFontLarge);
        int y = getYForCenteredText();
        graphics2D.drawString(Constants.GAME_TITLE, x, y - 100);
        graphics2D.setFont(this.customFontMedium);

        int i = 0;
        for (String menuOption : Constants.GAME_TITLE_MENU) {
            x = getXForCenteredText(graphics2D, menuOption, this.customFontMedium);
            y = y + 50;
            if (this.commandNumber == i) {
                setTitleCursor(graphics2D, y);
            }
            graphics2D.setColor(Color.WHITE);
            graphics2D.drawString(menuOption, x, y);
            i++;
        }
    }

    public void drawInventoryIcon(Graphics2D graphics2D, int x, int y, BufferedImage icon) {
        int iconX = x - Constants.TILE_SIZE * 3;
        int iconY = y - 14;
        graphics2D.drawImage(icon, iconX + 12, iconY + 12, null);
        graphics2D.drawRect(iconX - 3, iconY- 3, Constants.TILE_SIZE * 2 + 26, Constants.TILE_SIZE * 2 + 26);
    }

    private void drawDebug(Graphics2D graphics2D) {
        if (!this.debug) { return; }

        String playerLocation = locationToString(
            this.gamePanel.player.worldX/Constants.TILE_SIZE,
            this.gamePanel.player.worldY/Constants.TILE_SIZE
        );

        List<String> spells = new ArrayList<String>();
        for (SpellType spellType : this.gamePanel.player.spells.keySet()) {
            spells.add(spellType.toString());
        }
        
        graphics2D.setFont(this.customFontSmall);
        graphics2D.setColor(Color.WHITE);
        
        List<String> strings = Arrays.asList(
            Long.toString(this.gamePanel.gameTime / Constants.MILLISECOND),
            "FPS (" + String.valueOf(this.gamePanel.fps) + ")",
            playerLocation
        );

        List<String> inventoryStringList = new ArrayList<>();
        String inventoryString = "Inv: " + this.gamePanel.player.inventory.toString();

        int textWidth = graphics2D.getFontMetrics(this.customFontSmall).stringWidth(inventoryString);
        if (textWidth > Constants.SCREEN_WIDTH) {
            int mid = inventoryString.length() / 2;
            inventoryStringList.add(inventoryString.substring(mid));
            inventoryStringList.add(inventoryString.substring(0, mid));
        } else {
            inventoryStringList.add(inventoryString);
        }

        List<String> debugLines = new ArrayList<>();
        debugLines.addAll(inventoryStringList);
        debugLines.addAll(Arrays.asList(
                "Current Quests: " + this.gamePanel.questManager.currentQuests.toString(),
                "Completed Quests: " + this.gamePanel.questManager.completedQuests.toString(),
                "Spell: " + spells.toString(),
                combineToString(strings)
            )
        );

        int debugLine = Constants.SCREEN_HEIGHT;
        int debugLineDiff = 20;

        for (String line : debugLines) {
            graphics2D.drawString(line, 10, debugLine -= debugLineDiff);
        }
    }

    private String combineToString(List<String> strings) {
        return strings == null ? "" :
            strings.stream()
                .collect(Collectors.joining(" "));
    }

    private void drawMessage(Graphics2D graphics2D, String message, boolean slowType) {
        graphics2D.setFont(this.customFont);
        String visibleMessage = message;
        this.dialoguePrinting = true;
        if (slowType) {
            long elapsed = System.currentTimeMillis() - this.messageStartTime;
            int charsToShow = Math.min((int) (elapsed / Constants.MESSAGE_DISPLAY_SLOW_TYPE_TIME), message.length());
            visibleMessage = message.substring(0, charsToShow);
            try {
                if (charsToShow > currentCharacterIndex && charsToShow % 3 == 0) {
                    this.gamePanel.sound.playSoundEffect(Constants.SOUND_TEXT);
                    currentCharacterIndex = charsToShow;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        int messageWidth = graphics2D.getFontMetrics().stringWidth(visibleMessage);
        int messageHeight = graphics2D.getFontMetrics().getHeight();
        int x = getXForCenteredText(graphics2D, visibleMessage, this.customFont);
        int y = Constants.SCREEN_HEIGHT - 60;

        drawMessageBackground(graphics2D, x, y, messageWidth, messageHeight);
        drawMessageBorder(graphics2D, x, y, messageWidth, messageHeight);
        drawMessageText(graphics2D, x, y, visibleMessage);
    }

    private void drawMessageBackground(Graphics2D graphics2D, int x, int y, int width, int height) {
        graphics2D.setColor(Color.BLACK);
        graphics2D.fillRect(
            x - PADDING_X,
            y - height,
            width + 2 * PADDING_X,
            height + 2 * PADDING_Y
        );
    }

    private void drawMessageBorder(Graphics2D graphics2D, int x, int y, int width, int height) {
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawRect(
            x - PADDING_X,
            y - height,
            width + 2 * PADDING_X,
            height + 2 * PADDING_Y
        );
    }

    private void drawMessageText(Graphics2D graphics2D, int x, int y, String message) {
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString(message, x, y + 6);
    }

    private boolean isMessageTimeElapsed() {
        return System.currentTimeMillis() - this.messageStartTime > Constants.MESSAGE_DISPLAY_TIME;
    }

    private int getXForCenteredText(Graphics2D graphics2D, String text, Font font) {
        int textWidth = graphics2D.getFontMetrics(font).stringWidth(text);
        return (Constants.SCREEN_WIDTH - textWidth) / 2;
    }

    private int getYForCenteredText() {
        return Constants.SCREEN_HEIGHT / 2;
    }

    private void setTitleCursor(Graphics2D graphics2D, int y) {
        graphics2D.setColor(Color.RED);
        int x = 8 * Constants.TILE_SIZE;
        graphics2D.drawString(Constants.GAME_TITLE_SELECTOR, x-Constants.TILE_SIZE/2, y);
    }

    private void drawHealth(Graphics2D graphics2D) {
        int x = Constants.TILE_SIZE / 6;
        int y = 35;
        int width = Constants.TILE_SIZE * 2;
        int height = Constants.TILE_SIZE / 4;

        int currentHealth = this.gamePanel.player.getCurrentHealth();
        int maxHealth = this.gamePanel.player.maxHealth;

        float healthPercent = Math.max(0, Math.min(1f, (float) currentHealth / maxHealth));
        int healthBarWidth = (int) (width * healthPercent);

        graphics2D.setColor(Color.WHITE);
        graphics2D.drawRect(x, y, width, height);

        graphics2D.setColor(Color.GREEN);
        graphics2D.fillRect(x, y, healthBarWidth, height);

        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString(currentHealth + " / " + maxHealth, x, y - 10);
    }

    private void drawWeapon(Graphics2D graphics2D) {
        graphics2D.setFont(this.customFont);
        if (this.gamePanel.player.weapon != null) {
            this.gamePanel.player.weapon.drawWeaponInfo(graphics2D, 85);
        }
    }

    private static String locationToString(int x, int y) {
        return "(" + x + ", " + y + ") ";
    }
}