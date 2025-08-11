package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Config {

    GamePanel gamePanel;

    public Config(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void saveConfig() {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(
                new FileWriter("config.txt")
            );
            // List<InventoryItem> inventory = this.gamePanel.player.getInventory();
            // for (InventoryItem item : inventory) {
            //     String line = "";
            //     bufferedWriter.write(line);
            //     bufferedWriter.newLine();
            // }
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadConfig() {
        try {
            BufferedReader bufferedReader = new BufferedReader(
                new FileReader("config.txt")
            );
            String line = bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                // this.gamePanel.player.inventory.clear();
                // String itemName = line.split("=")[0];
            }
            bufferedReader.close();
        } catch (Exception e) {
        }
    }
}
