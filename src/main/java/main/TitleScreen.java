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

    final Option BACK_BUTTON = new Option("Back") {
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
                    new Option("Sound Music") {
                        @Override
                        void action(GamePanel gamePanel) {
                            gamePanel.sound.toggleMusic();
                            gamePanel.ui.titleScreen.switchScreen(gamePanel.ui.titleScreen.previousScreen);
                        }
                    },
                    new Option("Sound Effect") {
                        @Override
                        void action(GamePanel gamePanel) {
                            gamePanel.sound.toogleEffects();
                            gamePanel.ui.titleScreen.switchScreen(gamePanel.ui.titleScreen.previousScreen);
                        }
                    },
                    BACK_BUTTON
                )
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
    
    class Screen {
        public Screen screen;
        public String title;
        public List<Option> options = new ArrayList<>();

        public Screen(String title) {
            this.title = title;
        }

        public Screen(String title, List<Option> options) {
            this(title);
            this.options = options;
        }

        public void addOption(Option option) {
            this.options.add(option);
        }

        public List<String> getOptionTitles() {
            List<String> options = new ArrayList<>();
            for (Option option : this.options) {
                options.add(option.name);
            }
            return options;
        }

        public HashMap<String, Option> getOptionsMap() {
            HashMap<String, Option> options = new HashMap<>();
            for (Option option : this.options) {
                options.put(option.name, option);
            }
            return options;
        }
    }

    protected abstract class Option {
        public String name;
        abstract void action(GamePanel gamePanel);

        public Option(String name) {
            this.name = name;
        }
    }

}
