package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.FontFormatException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import main.GamePanel.GameState;
import spells.SuperSpell.SpellType;

public class UI {
    
    GamePanel gamePanel;
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
    public boolean dialoguePrinting = false;
    public boolean dialogueSkip = false;

    final static int PADDING_X = 20;
    final static int PADDING_Y = 10;

    int commandNumber = 0;

    public UI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

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

    public void drawPauseScreen(Graphics2D graphics2D) {
        if (this.gamePanel.gameState == GamePanel.GameState.PAUSE) {
            graphics2D.setFont(this.customFontLarge);
            graphics2D.setColor(Color.WHITE);
            int x = getXForCenteredText(graphics2D, Constants.GAME_PAUSED, this.customFontLarge);
            int y = getYForCenteredText();
            graphics2D.drawString(Constants.GAME_PAUSED, x, y);
        }
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

    public void drawInventory(Graphics2D graphics2D) {
        if (this.gamePanel.gameState == GamePanel.GameState.INVENTORY) {
            graphics2D.setFont(this.customFontLarge);
            int x = getXForCenteredText(graphics2D, Constants.GAME_INVENTORY, this.customFontLarge);
            int y = getYForCenteredText();

            graphics2D.setColor(Color.BLACK);
            int width = Constants.SCREEN_WIDTH - Constants.TILE_SIZE * 2;
            int height = Constants.SCREEN_HEIGHT - Constants.TILE_SIZE * 2;
            graphics2D.fillRect(Constants.TILE_SIZE, Constants.TILE_SIZE, width, height);
            graphics2D.setColor(Color.WHITE);

            y = y - 150;
            graphics2D.drawString(Constants.GAME_INVENTORY, x, y);
            y += Constants.TILE_SIZE;
            int originalY = y + 50;
            
            List<InventoryItem> items = this.gamePanel.player.getInventory();
            InventoryItem selectedItem = null;
            for (int i = 0; i < items.size(); i++) {
                InventoryItem item = items.get(i);
                int quantity = items.get(i).count;
                graphics2D.setFont(this.customFont);
                y += 50;
                graphics2D.drawString(item.name + " (" + quantity + ") ", Constants.TILE_SIZE * 3, y);
                if (this.commandNumber == i) {
                    selectedItem = item;
                    graphics2D.drawString(">", Constants.TILE_SIZE * 2, y);
                }
            }

            items = this.gamePanel.player.getInventoryNonSelectable();
            for (int i = 0; i < items.size(); i++) {
                InventoryItem item = items.get(i);
                int quantity = items.get(i).count;
                graphics2D.setFont(this.customFont);
                y += 50;
                graphics2D.drawString(item.name + " (" + quantity + ")", Constants.TILE_SIZE * 3, y);
            }

            // Details
            if (selectedItem.weapon != null) {
                if (selectedItem.weapon.range) {
                    graphics2D.drawString("Ammo: " + String.valueOf(selectedItem.weapon.ammo), Constants.TILE_SIZE * 9, originalY);
                    originalY += 50;
                }
                graphics2D.drawString("Max Damage: " + String.valueOf(selectedItem.weapon.maxDamage), Constants.TILE_SIZE * 9, originalY);
            }
            if (selectedItem.object != null) {
                graphics2D.drawString(String.valueOf(selectedItem.object.spell.positiveSpell), Constants.TILE_SIZE * 9, originalY);
            }
        }
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

    private void drawDebug(Graphics2D graphics2D) {
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
        graphics2D.drawString(Long.toString(this.gamePanel.gameTime / Constants.MILLISECOND) + " " + playerLocation, 10, Constants.SCREEN_HEIGHT - 50);
        graphics2D.drawString("Spell: " + spells.toString(), 10, Constants.SCREEN_HEIGHT - 30);
        graphics2D.drawString("Inv: " + this.gamePanel.player.inventory.toString(), 10, Constants.SCREEN_HEIGHT - 10);
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
        int x = 6 * Constants.TILE_SIZE;
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