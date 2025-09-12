package main;

import java.io.*;
import com.google.gson.Gson;

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
        String os = System.getProperty("os.name").toLowerCase();
        String baseDir;

        if (os.contains("win")) {
            // Windows: %APPDATA%\Wookster\
            baseDir = System.getenv("APPDATA");
        } else if (os.contains("mac")) {
            // macOS: ~/Library/Application Support/Wookster/
            baseDir = System.getProperty("user.home") + "/Library/Application Support";
        } else {
            // Linux/Unix: ~/.Wookster/
            baseDir = System.getProperty("user.home");
        }

        File dir = new File(baseDir, "Wookster");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new File(dir, Constants.SAVE_FILE);
    }

    public void saveConfig() {
        String data = this.dataWrapper.getDataForSave(this.gamePanel);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile))) {
            writer.write(data);
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the save file: " + saveFile.getAbsolutePath());
            e.printStackTrace();
        }
    }

    public void loadConfig() {
        if (!hasSavedFile()) { return; }
        try (BufferedReader reader = new BufferedReader(new FileReader(saveFile))) {
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            Gson gson = new Gson();
            this.dataWrapper = gson.fromJson(jsonBuilder.toString(), DataWrapper.class);
        } catch (IOException e) {
            System.err.println("An error occurred while reading the save file: " + saveFile.getAbsolutePath());
            e.printStackTrace();
        }
    }

    public boolean hasSavedFile() {
        if (!saveFile.exists()) {
            System.out.println("No saved file, must be a new game");
            return false;
        }
        return true;
    }
}
