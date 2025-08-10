package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
            Set set = this.gamePanel.player.inventory.entrySet();
            Iterator iterator = set.iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry)iterator.next();
                String line = entry.getKey() + "=" + entry.getValue();
                System.out.println(line);
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
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
                // TODO: Fix loading inventory item usable / visible
                this.gamePanel.player.inventory.clear();
                String itemName = line.split("=")[0];
                int itemCount = Integer.parseInt(line.split("=")[1]);
                InventoryItem item = new InventoryItem(itemName, itemCount, true, true);
                this.gamePanel.player.addInventoryItem(item);
            }
            bufferedReader.close();
        } catch (Exception e) {
        }
    }
}
