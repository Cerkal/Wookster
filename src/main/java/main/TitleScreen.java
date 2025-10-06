package main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.GamePanel.GameState;
import main.Screen.Option;
import main.Screen.Screen;
import main.Screen.SettingSlider;
import main.Screen.Toggle;
import main.Screen.ToggleOption;

public class TitleScreen {

    GamePanel gamePanel;
    Screen currentScreen;
    String previousScreen;
    HashMap<String, Screen> screens = new HashMap<>();
    HashMap<String, Integer> screenIndex = new HashMap<>();

    public static final String MAIN_SCREEN = Constants.GAME_TITLE;
    public final static String GAME_TITLE = Constants.GAME_TITLE;
    public final static String SETTINGS_SCREEN = Constants.GAME_TITLE_SCREEN_SETTINGS;
    public final static String LOAD_SCREEN = Constants.GAME_TITLE_SCREEN_LOAD_GAME;
    public final static String SAVE_SCREEN = Constants.GAME_TITLE_SCREEN_SAVE_GAME;
    public final static String DEFAULT_SCREEN = MAIN_SCREEN;

    final Option BACK_BUTTON = new Option(Constants.GAME_TITLE_BACK_BUTTON) {
        @Override
        public void action(GamePanel gamePanel) {
            if (gamePanel.ui.titleScreen.previousScreen != null) {
                gamePanel.ui.titleScreen.switchScreen(gamePanel.ui.titleScreen.previousScreen);
            }
        }
    };

    public TitleScreen(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        // Title Screen
        addScreen(
            new Screen(
                MAIN_SCREEN,
                new ArrayList<>(
                    List.of(
                        new Option(Constants.GAME_TITLE_SCREEN_CONTINUE) {
                            @Override
                            public void action(GamePanel gamePanel) {
                                if (gamePanel.gameState == GameState.PAUSE) {
                                    gamePanel.gameState = GameState.PLAY;
                                }
                                if (gamePanel.gameState == GameState.TITLE) {
                                    gamePanel.loadGame();
                                }
                            }
                        },
                        new Option(Constants.GAME_TITLE_SCREEN_NEW_GAME) {
                            @Override
                            public void action(GamePanel gamePanel) {
                                gamePanel.newGame();
                                gamePanel.config.saveConfig();
                            }
                        },
                        new Option(Constants.GAME_TITLE_SCREEN_LOAD_GAME) {
                            @Override
                            public void action(GamePanel gamePanel) {
                                // gamePanel.loadGame();
                                gamePanel.ui.titleScreen.switchScreen(LOAD_SCREEN);
                            }
                        },
                        new Option(Constants.GAME_TITLE_SCREEN_SAVE_GAME) {
                            @Override
                            public void action(GamePanel gamePanel) {
                                // gamePanel.config.saveConfig();
                                // gamePanel.gameState = GameState.PLAY;
                                gamePanel.ui.titleScreen.switchScreen(SAVE_SCREEN);
                            }
                        },
                        new Option(Constants.GAME_TITLE_SCREEN_SETTINGS) {
                            @Override
                            public void action(GamePanel gamePanel) {
                                gamePanel.ui.titleScreen.switchScreen(SETTINGS_SCREEN);
                            }
                        },
                        new Option(Constants.GAME_TITLE_SCREEN_QUIT_GAME) {
                            @Override
                            public void action(GamePanel gamePanel) {
                                gamePanel.quit();
                            }
                        }
                    )
                )
            )
        );

        // Settings
        addScreen(
            new Screen(
                SETTINGS_SCREEN,
                List.of(
                    new Toggle(
                        Constants.GAME_SETTINGS_MUSIC_TOGGLE,
                        List.of(
                            new ToggleOption(ToggleOption.ON) {
                                @Override
                                public void action(GamePanel gamePanel) {
                                    gamePanel.sound.toggleMusic();
                                }
                            },
                            new ToggleOption(ToggleOption.OFF) {
                                @Override
                                public void action(GamePanel gamePanel) {
                                    gamePanel.sound.toggleMusic();
                                }
                            }
                        )
                    ),
                    new Toggle(
                        Constants.GAME_SETTINGS_EFFECTS_TOGGLE,
                        List.of(
                            new ToggleOption(ToggleOption.ON) {
                                @Override
                                public void action(GamePanel gamePanel) {
                                    gamePanel.sound.toggleEffects();
                                }
                            },
                            new ToggleOption(ToggleOption.OFF) {
                                @Override
                                public void action(GamePanel gamePanel) {
                                    gamePanel.sound.toggleEffects();
                                }
                            }
                        )
                    ),
                    new SettingSlider(Constants.GAME_SETTINGS_MUSIC_SLIDER) {
                        @Override
                        public void action(GamePanel gamePanel) {
                            gamePanel.sound.setMusicVolume(this.getCurrentValue());
                        }
                    },
                    new SettingSlider(Constants.GAME_SETTINGS_EFFECTS_SLIDER) {
                        @Override
                        public void action(GamePanel gamePanel) {
                            gamePanel.sound.setEffectsVolume(this.getCurrentValue());
                        }
                    },
                    new Toggle(
                        Constants.GAME_SETTINGS_MOUSE_AIM,
                        List.of(
                            new ToggleOption(ToggleOption.ON) {
                                @Override
                                public void action(GamePanel gamePanel) {
                                    gamePanel.mouseAim = true;
                                }
                            },
                            new ToggleOption(ToggleOption.OFF) {
                                @Override
                                public void action(GamePanel gamePanel) {
                                    gamePanel.mouseAim = false;
                                }
                            }
                        )
                    ),
                    new Toggle(
                        Constants.GAME_SETTINGS_CURSOR_SIZE,
                        List.of(
                            new ToggleOption("Small") {
                                @Override
                                public void action(GamePanel gamePanel) {
                                    gamePanel.currentCursor = gamePanel.cursorSmall;
                                }
                            },
                            new ToggleOption("Large") {
                                @Override
                                public void action(GamePanel gamePanel) {
                                    gamePanel.currentCursor = gamePanel.cursorLarge;
                                }
                            }
                        )
                    ),
                    new Option(Constants.GAME_TITLE_BACK_BUTTON) {
                        @Override
                        public void action(GamePanel gamePanel) {
                            if (gamePanel.ui.titleScreen.previousScreen != null) {
                                gamePanel.config.saveConfig();
                                gamePanel.ui.titleScreen.switchScreen(gamePanel.ui.titleScreen.previousScreen);
                            }
                        }
                    }
                ),
                false,
                280
            )
        );

        List<Option> saveOptions = new ArrayList<>();
        for (int i = 0; i < Config.SAVE_SLOT_COUNT; i++) {
            final int slotIndex = i;
            saveOptions.add(new Option(getSlotInfo(i, gamePanel.config)) {
                @Override
                public void action(GamePanel gamePanel) {
                    gamePanel.config.setCurrentSlot(slotIndex);
                    gamePanel.config.saveConfig();
                }
            });
        }
        saveOptions.add(BACK_BUTTON);
        addScreen(new Screen(SAVE_SCREEN, saveOptions, false, 280));

        List<Option> loadOptions = new ArrayList<>();
        for (int i = 0; i < Config.SAVE_SLOT_COUNT; i++) {
            final int slotIndex = i;
            loadOptions.add(new Option(getSlotInfo(i, gamePanel.config)) {
                @Override
                public void action(GamePanel gamePanel) {
                    gamePanel.config.setCurrentSlot(slotIndex);
                    gamePanel.config.loadConfig();
                    // Optionally, start game or update UI here
                }
            });
        }
        loadOptions.add(BACK_BUTTON);
        addScreen(new Screen(LOAD_SCREEN, loadOptions, false, 280));

        this.currentScreen = this.screens.get(DEFAULT_SCREEN);
    }

    public void switchScreen(String title) {
        this.previousScreen = this.currentScreen.title;
        this.currentScreen = this.screens.get(title);
    }

    private void addScreen(Screen screen) {
        int index = this.screens.size();
        screen.screenIndex = index;
        this.screens.put(screen.title, screen);
        this.screenIndex.put(screen.title, index);
    }

    public HashMap<String, Integer> getSettings() {
        Screen settingsScreen = this.screens.get(SETTINGS_SCREEN);
        HashMap<String, Integer> settings = new HashMap<String, Integer>();
        if (settingsScreen == null) {
            System.err.println("Settings Not Found.");
            return settings;
        }
        for (Option option : settingsScreen.getOptionsMap().values()) {
            settings.put(option.name, option.getValue());
        }
        return settings;
    }

    private String getSlotInfo(int slot, Config config) {
        File file = new File(this.gamePanel.config.saveFile.getParentFile(), Constants.SAVE_FILE_SLOTS.get(slot));
        if (file.exists()) {
            long lastModified = file.lastModified();
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return "Slot " + (slot + 1) + ": Saved " + sdf.format(new java.util.Date(lastModified));
        } else {
            return "Slot " + (slot + 1) + ": Empty";
        }
    }
}
