package main;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.GamePanel.GameState;
import spells.SuperSpell.SpellType;
import tile.Tile;
import tile.TileLoader;

public class KeyHandler implements KeyListener {

    GamePanel gamePanel;

    public static String SPACEBAR = "[spacebar]";
    public static String I = "[i]";

    public KeyHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public boolean upPressed, downPressed, leftPressed, rightPressed, spacePressed, enterPressed, iPressed;

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        switch (this.gamePanel.gameState) {
            case GameState.TITLE:
                gameTileKeys(code);
                break;
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
                    this.gamePanel.ui.selector.commandNumber = 0;
                    this.gamePanel.gameState = GameState.INVENTORY;
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
                gameTileKeys(code);
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
            case GameState.DEATH:
                if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
                    startGame();
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
    public void keyTyped(KeyEvent e) {
    }

    private void gameTileKeys(int code) {
        if (code == KeyEvent.VK_W) {
            this.gamePanel.ui.commandNumber--;
            this.gamePanel.playSoundEffect(Constants.SOUND_CURSOR);
            if (this.gamePanel.ui.commandNumber < 0) {
                this.gamePanel.ui.commandNumber = Constants.GAME_TITLE_MENU.size() - 1;
            }
        }
        if (code == KeyEvent.VK_S) {
            this.gamePanel.ui.commandNumber++;
            this.gamePanel.playSoundEffect(Constants.SOUND_CURSOR);
            if (this.gamePanel.ui.commandNumber >= Constants.GAME_TITLE_MENU.size()) {
                this.gamePanel.ui.commandNumber = 0;
            }
        }
        if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
            String selection = Constants.GAME_TITLE_MENU.get(this.gamePanel.ui.commandNumber);
            this.gamePanel.playSoundEffect(Constants.SOUND_CURSOR);
            switch (selection) {
                case Constants.GAME_TITLE_SCREEN_NEW_GAME:
                    newGame();
                    break;
                case Constants.GAME_TITLE_SCREEN_SAVE_GAME:
                    this.gamePanel.config.saveConfig();
                    System.out.println("Saved game.");
                    break;
                case Constants.GAME_TITLE_SCREEN_LOAD_GAME:
                    startGame();
                    break;
                case Constants.GAME_TITLE_SCREEN_QUIT_GAME:
                    System.exit(0);
                    break;
            }
        }
    }

    private void startGame() {
        if (this.gamePanel.config.dataWrapper.player == null) {
            newGame();
        } else {
            loadGame();
        }
    }

    private void loadGame() {
        this.gamePanel.config.loadConfig();
        this.gamePanel.restartLevel();
        this.gamePanel.levelManager.loadLevel(this.gamePanel.config.dataWrapper.currentLevelIndex);
        this.gamePanel.gameState = GameState.PLAY;
        this.gamePanel.stopMusic();
        // this.gamePanel.playMusic(Constants.SOUND_BG_01);
        System.out.println("Loaded game.");
    }

    private void newGame() {
        this.gamePanel.gameState = GameState.PLAY;
        this.gamePanel.config.dataWrapper = new DataWrapper();
        this.gamePanel.setupGame();
        this.gamePanel.gameState = GameState.PLAY;
        this.gamePanel.stopMusic();
        // this.gamePanel.playMusic(Constants.SOUND_BG_01);
        System.out.println("New game.");
    }
}
