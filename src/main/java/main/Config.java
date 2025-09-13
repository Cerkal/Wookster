package main;

import java.io.*;
import com.google.gson.Gson;

import main.GamePanel.GameState;
import main.TitleScreen.Option;
import main.TitleScreen.Screen;
import main.TitleScreen.Toggle;
import main.TitleScreen.ToggleOption;

public class Config {

    private final GamePanel gamePanel;
    public DataWrapper dataWrapper;

    private final File saveFile;

    public Config(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.dataWrapper = new DataWrapper();
        this.saveFile = getSaveFile();
    }

    private File getSaveFile() {
        File dir;

        if (new File(Constants.SAVE_FILE).exists() || System.getProperty("portable") != null) {
            dir = new File("."); // Same dir as portable
        } else {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                dir = new File(System.getenv("APPDATA"), "Wookster");
            } else if (os.contains("mac")) {
                dir = new File(System.getProperty("user.home") + "/Library/Application Support/Wookster");
            } else {
                dir = new File(System.getProperty("user.home"), ".Wookster");
            }
            if (!dir.exists()) dir.mkdirs();
        }
        return new File(dir, Constants.SAVE_FILE);
    }

    public void saveConfig() {
        GameState currentState = this.gamePanel.gameState;
        this.gamePanel.gameState = GameState.SAVING;
        String data = this.dataWrapper.getDataForSave(this.gamePanel);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile))) {
            writer.write(data);
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the save file: " + saveFile.getAbsolutePath());
            e.printStackTrace();
        }
        this.gamePanel.gameState = currentState;
    }

    public void loadConfig() {
        if (!hasSavedFile()) {
            this.dataWrapper = new DataWrapper();
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(saveFile))) {
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            Gson gson = new Gson();
            this.dataWrapper = gson.fromJson(jsonBuilder.toString(), DataWrapper.class);
            loadSettings();
        } catch (IOException e) {
            System.err.println("Error reading save file: " + saveFile.getAbsolutePath());
            e.printStackTrace();
        }
    }

    public void titleLoader() {
        GameState originalState = this.gamePanel.gameState;
        this.gamePanel.gameState = GameState.LOADING;
        long loadingStartTime = System.currentTimeMillis();
        new Thread(() -> {
            loadConfig();
            long elapsed = System.currentTimeMillis() - loadingStartTime;
            if (elapsed < Constants.MIN_LOADING) {
                try {
                    Thread.sleep(Constants.MIN_LOADING - elapsed);
                } catch (InterruptedException ignored) {
                }
            }
            this.gamePanel.gameState = originalState;
        }).start();
    }

    public boolean hasSavedFile() {
        if (!saveFile.exists()) {
            System.out.println("No saved file, must be a new game");
            return false;
        }
        return true;
    }

    public void loadSettings() {
        if (this.dataWrapper.settingsMap != null) {
            for (String name : this.dataWrapper.settingsMap.keySet()) {
                int value = this.dataWrapper.settingsMap.get(name);
                loadSetting(name, value);
                updateSetting(name, value);
            }
        }
    }

    public void loadSetting(String name, int value) {
        switch (name) {
            case Constants.GAME_SETTINGS_MUSIC_TOGGLE:
                boolean musicCheck = ToggleOption.isToggleOn(value);
                if (!musicCheck) {                    
                    this.gamePanel.sound.muteMusic = true;
                    this.gamePanel.sound.muteMusic();
                } else {
                    this.gamePanel.sound.muteMusic = false;
                }
                break;
            case Constants.GAME_SETTINGS_EFFECTS_TOGGLE:
                boolean effectCheck = ToggleOption.isToggleOn(value);
                if (!effectCheck) {
                    this.gamePanel.sound.muteEffects = true;
                    this.gamePanel.sound.muteEffects();
                } else {
                    this.gamePanel.sound.muteEffects = false;
                }
                break;
            case Constants.GAME_SETTINGS_MUSIC_SLIDER:
                this.gamePanel.sound.setMusicVolume(value);
                break;
            case Constants.GAME_SETTINGS_EFFECTS_SLIDER:
                this.gamePanel.sound.setEffectsVolume(value);
                break;
            default:
                break;
        }
    }

    public void updateSetting(String name, int value) {
        Screen settingsScreen = this.gamePanel.ui.titleScreen.screens.get(TitleScreen.SETTINGS_SCREEN);
        if (settingsScreen == null) { return; }
        for (Option option : settingsScreen.options) {
            if (option.name.equalsIgnoreCase(name)) {
                if (option instanceof Toggle) {
                    int indexForToggle = ToggleOption.isToggleOn(value) ? ToggleOption.INDEX_ON : ToggleOption.INDEX_OFF;
                    option.setDefaultValue(indexForToggle);
                } else {
                    option.setDefaultValue(value);
                }
            }
        }
    }
}
