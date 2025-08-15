package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;

public class Config {

    GamePanel gamePanel;
    public DataWrapper dataWrapper;

    public Config(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.dataWrapper = new DataWrapper();
    }

    public void saveConfig() {
        String data = this.dataWrapper.getDataForSave(this.gamePanel);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Constants.SAVE_FILE))) {
            writer.write(data);
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    public void loadConfig() {
        try (BufferedReader reader = new BufferedReader(new FileReader(Constants.SAVE_FILE))) {
            String json = "";
            String line;
            while ((line = reader.readLine()) != null) {
                json += line;
            }
            Gson gson = new Gson();
            this.dataWrapper = gson.fromJson(json, DataWrapper.class);
        } catch (IOException e) {
            System.out.println("No saved file, must be a new game");
        }
    }
}
