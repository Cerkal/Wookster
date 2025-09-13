package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.GamePanel.GameState;

public class TitleScreen {

    GamePanel gamePanel;
    String previousScreen;
    Screen currentScreen;
    ScreenSelector screenSelector;
    HashMap<String, Screen> screens = new HashMap<>();

    public static final String MAIN_SCREEN = "Main";
    final static String GAME_TITLE = Constants.GAME_TITLE;
    final static String SETTINGS_SCREEN = "Settings";
    final static String LOAD_SCREEN = "Load";
    final static String DEFAULT_SCREEN = MAIN_SCREEN;

    final Option BACK_BUTTON = new Option(Constants.GAME_TITLE_BACK_BUTTON) {
        @Override
        void action(GamePanel gamePanel) {
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
                        new Option("New Game") {
                            @Override
                            void action(GamePanel gamePanel) {
                                gamePanel.newGame();
                            }
                        },
                        new Option("Load Game") {
                            @Override
                            void action(GamePanel gamePanel) {
                                gamePanel.loadGame();
                            }
                        },
                        new Option("Save Game") {
                            @Override
                            void action(GamePanel gamePanel) {
                                gamePanel.config.saveConfig(); 
                                gamePanel.gameState = GameState.PLAY;
                            }
                        },
                        new Option("Settings") {
                            @Override
                            void action(GamePanel gamePanel) {
                                gamePanel.ui.titleScreen.switchScreen(SETTINGS_SCREEN);
                            }
                        },
                        new Option("Quit") {
                            @Override
                            void action(GamePanel gamePanel) {
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
                                void action(GamePanel gamePanel) {
                                    gamePanel.sound.toggleMusic();
                                }
                            },
                            new ToggleOption(ToggleOption.OFF) {
                                @Override
                                void action(GamePanel gamePanel) {
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
                                void action(GamePanel gamePanel) {
                                    gamePanel.sound.toggleEffects();
                                }
                            },
                            new ToggleOption(ToggleOption.OFF) {
                                @Override
                                void action(GamePanel gamePanel) {
                                    gamePanel.sound.toggleEffects();
                                }
                            }
                        )
                    ),
                    new SettingSlider(Constants.GAME_SETTINGS_MUSIC_SLIDER) {
                        @Override
                        void action(GamePanel gamePanel) {
                            gamePanel.sound.setMusicVolume(this.getCurrentValue());
                        }
                    },
                    new SettingSlider(Constants.GAME_SETTINGS_EFFECTS_SLIDER) {
                        @Override
                        void action(GamePanel gamePanel) {
                            gamePanel.sound.setEffectsVolume(this.getCurrentValue());
                        }
                    },
                    new Option(Constants.GAME_TITLE_BACK_BUTTON) {
                        @Override
                        void action(GamePanel gamePanel) {
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

        this.currentScreen = this.screens.get(DEFAULT_SCREEN);
    }

    public void switchScreen(String title) {
        this.previousScreen = this.currentScreen.title;
        this.currentScreen = this.screens.get(title);
    }

    private void addScreen(Screen screen) {
        this.screens.put(screen.title, screen);
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
    
    class Screen {
        public Screen screen;
        public String title;
        public List<Option> options = new ArrayList<>();
        public boolean centerText = true;
        public int xAlign = Constants.SCREEN_WIDTH / 2 - (Constants.TILE_SIZE * 2) - 100;
        public int longestOption;

        public Screen(String title) {
            this.title = title;
        }

        public Screen(String title, List<Option> options) {
            this(title);
            this.options = options;
        }

        public Screen(String title, List<Option> options, boolean centerText, int xAlign) {
            this(title, options);
            this.centerText = centerText;
            this.xAlign = xAlign;
        }

        public void addOption(Option option) {
            this.options.add(option);
        }

        public List<String> getOptionTitles() {
            List<String> options = new ArrayList<>();
            for (Option option : this.options) {
                options.add(option.getNameWithValue());
            }
            return options;
        }

        public HashMap<String, Option> getOptionsMap() {
            HashMap<String, Option> options = new HashMap<>();
            for (Option option : this.options) {
                options.put(option.getNameWithValue(), option);
            }
            return options;
        }
    }

    protected class Option {
        public String name;
        protected int spacer = 30;
        
        void action(GamePanel gamePanel) {}

        public Option(String name) {
            this.name = name;
        }

        protected String getNameWithValue() {
            return this.name;
        }

        public void setDefaultValue(int value) {}

        public int getValue() {
            return 0;
        }
    }

    protected class SettingSlider extends Option {
        private int max = 10;
        private int min = 0;
        private int currentValue = 5;

        public SettingSlider(String name) {
            super(name);
            this.name = name;
        }

        void action(GamePanel gamePanel) {}

        public void increase() {
            this.currentValue += 1;
            if (this.currentValue > this.max) {
                this.currentValue = this.max;
            }
        }

        public void decrease() {
            this.currentValue -= 1;
            if (this.currentValue < this.min) {
                this.currentValue = this.min;
            }
        }

        public int getCurrentValue() {
            return this.currentValue;
        }

        @Override
        public void setDefaultValue(int value) {
            this.currentValue = value;
        }

        public String getNameWithValue() {
            int spacer = this.spacer - this.name.length();
            return this.name + " ".repeat(spacer) + "|".repeat(this.currentValue);
        }

        @Override
        public int getValue() {
            return this.currentValue;
        }
    }

    protected class Toggle extends Option {
        private List<ToggleOption> values = new ArrayList<>();
        private ToggleOption currentValue;
        private int currentIndex = 0;

        public Toggle(String name) {
            super(name);
            this.name = name;
        }

        public Toggle(String name, List<ToggleOption> values) {
            this(name);
            this.values = values;
            this.currentValue = values.get(0);
        }

        @Override
        void action(GamePanel gamePanel) {
            cycle();
            getCurrentSettingOption().action(gamePanel);
        }

        public ToggleOption getCurrentSettingOption() {
            return this.currentValue;
        }

        public int getCurrentIndex() {
            return this.currentIndex;
        }

        @Override
        public void setDefaultValue(int index) {
            this.currentIndex = index;
            this.currentValue = this.values.get(this.currentIndex);
        }

        private void cycle() {
            this.currentIndex = (currentIndex + 1) % this.values.size();
            this.currentValue = this.values.get(this.currentIndex);
        }

        public String getNameWithValue() {
            int spacer = this.spacer - this.name.length();
            return this.name + " ".repeat(spacer) + this.currentValue.value;
        }

        @Override
        public int getValue() {
            return this.currentValue.value == ToggleOption.OFF ? 0 : 1;
        }
    }

    protected class ToggleOption {
        public final static String ON = "On"; 
        public final static String OFF = "Off";
        public final static int INDEX_ON = 0;
        public final static int INDEX_OFF = 1;

        private String value;

        public ToggleOption(String value) {
            this.value = value;
        }

        void action(GamePanel gamePanel){}

        public String getValue() {
            return this.value;
        }

        public static boolean isToggleOn(int value) {
            return value == 1;
        }
    }
}
