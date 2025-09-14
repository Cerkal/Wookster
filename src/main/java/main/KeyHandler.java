package main;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import main.GamePanel.GameState;
import spells.SuperSpell.SpellType;
import tile.Tile;
import tile.TileLoader;

public class KeyHandler implements KeyListener {

    GamePanel gamePanel;

    public static String SPACEBAR = "[spacebar]";
    public static String I = "[i]";
    public static String R = "[r]";

    public KeyHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public boolean upPressed, downPressed, leftPressed, rightPressed, spacePressed, enterPressed, iPressed;

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        switch (this.gamePanel.gameState) {
            case GameState.PLAY:
                if (code == KeyEvent.VK_W) {
                    this.upPressed = true;
                }
                if (code == KeyEvent.VK_S) {
                    this.downPressed = true;
                }
                if (code == KeyEvent.VK_A) {
                    this.leftPressed = true;
                }
                if (code == KeyEvent.VK_D) {
                    this.rightPressed = true;
                }
                if (code == KeyEvent.VK_ESCAPE) {
                    this.gamePanel.gameState = GameState.PAUSE;
                }
                if (code == KeyEvent.VK_SPACE) {
                    this.spacePressed = true;
                }
                if (code == KeyEvent.VK_ENTER) {
                    this.enterPressed = true;
                }
                if (code == KeyEvent.VK_I) {
                    this.gamePanel.ui.screenSelector.clearScreens();
                    for (int i = 0; i < Constants.INVENTORY_TABS.size(); i++) {
                        this.gamePanel.ui.screenSelector.addScreen(new ArrayList<>());
                    }
                    this.gamePanel.gameState = GameState.INVENTORY;
                }
                if (code == KeyEvent.VK_BACK_QUOTE) {
                    this.gamePanel.ui.debug = !this.gamePanel.ui.debug;
                }

                // Debug controls
                if (this.gamePanel.debugMap) {
                    if (code == KeyEvent.VK_L) {
                        this.gamePanel.tileManager.loadMap(this.gamePanel.levelManager.getCurrentLevel().mapPath);
                        System.out.println("Loaded map.");
                    }
                    if (code == KeyEvent.VK_SPACE) {
                        Point location = this.gamePanel.player.getLocation();
                        Tile currentTile = this.gamePanel.tileManager.getTile(location);
                        Tile replaceTile = TileLoader.getTile(currentTile.indexNumber+1);
                        this.gamePanel.tileManager.tileMap.put(location, replaceTile);
                    }
                }
                break;
            case GameState.PAUSE:
                if (code == KeyEvent.VK_ESCAPE) {
                    this.gamePanel.gameState = GameState.PLAY;
                }
                break;
            case GameState.DIALOGUE:
                if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
                    if (this.gamePanel.player.entityInDialogue != null) {
                        this.gamePanel.player.entityInDialogue.speak();
                    } else {
                        this.gamePanel.ui.stopDialogue();
                    }
                }
                break;
            case GameState.INVENTORY:
                if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
                    if (this.gamePanel.player.spells.containsKey(SpellType.CLARITY_SPELL)) {
                        this.gamePanel.player.spells.get(SpellType.CLARITY_SPELL).removeSpell(this.gamePanel.player);
                    }
                }
                if (code == KeyEvent.VK_I) {
                    this.gamePanel.gameState = GameState.PLAY;
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code =  e.getKeyCode();
        if (code == KeyEvent.VK_W) {
            this.upPressed = false;
        }
        if (code == KeyEvent.VK_S) {
            this.downPressed = false;
        }
        if (code == KeyEvent.VK_A) {
            this.leftPressed = false;
        }
        if (code == KeyEvent.VK_D) {
            this.rightPressed = false;
        }
        if (code == KeyEvent.VK_SPACE) {
            this.spacePressed = false;
        }
        if (code == KeyEvent.VK_ENTER) {
            this.enterPressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
