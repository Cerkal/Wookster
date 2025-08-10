package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.GamePanel.GameState;

public class KeyHandler implements KeyListener {

    GamePanel gamePanel;

    public KeyHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public boolean upPressed, downPressed, leftPressed, rightPressed, spacePressed, enterPressed, iPressed;

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        switch (this.gamePanel.gameState) {
            case GameState.TITLE:
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
                            this.gamePanel.gameState = GameState.PLAY;
                            this.gamePanel.stopMusic();
                            this.gamePanel.playMusic(Constants.SOUND_BG_01);
                            break;
                        case Constants.GAME_TITLE_SCREEN_LOAD_GAME:
                            break;
                        case Constants.GAME_TITLE_SCREEN_QUIT_GAME:
                            break;
                    }
                }
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
                    this.gamePanel.gameState = GameState.INVENTORY;
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
                // if (code == KeyEvent.VK_W) {
                //     this.gamePanel.ui.commandNumber--;
                //     if (this.gamePanel.ui.commandNumber < 0) {
                //         this.gamePanel.ui.commandNumber = this.gamePanel.player.getInventory().size() - 1;
                //         this.gamePanel.playSoundEffect(Constants.SOUND_CURSOR);
                //     }
                // }
                // if (code == KeyEvent.VK_S) {
                //     this.gamePanel.ui.commandNumber++;
                //     if (this.gamePanel.ui.commandNumber >= this.gamePanel.player.getInventory().size()) {
                //         this.gamePanel.ui.commandNumber = 0;
                //         this.gamePanel.playSoundEffect(Constants.SOUND_CURSOR);
                //     }
                // }
                // if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
                //     InventoryItem selection = this.gamePanel.player.getInventory().get(this.gamePanel.ui.commandNumber);
                //     selection.select();
                //     this.gamePanel.playSoundEffect(Constants.SOUND_CURSOR);
                //     this.gamePanel.gameState = GameState.PLAY;
                // }
                break;
            case GameState.DEATH:
                if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
                    this.gamePanel.restartLevel();
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

}
