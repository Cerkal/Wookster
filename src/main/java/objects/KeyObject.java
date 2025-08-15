package objects;

import java.awt.Graphics2D;

import main.Constants;
import main.GamePanel;
import main.InventoryItem;

public class KeyObject extends SuperObject {

    public KeyObject(GamePanel gamePanel) {
        super(gamePanel);
        init();
    }
    
    public KeyObject(GamePanel gamePanel, int worldX, int worldY) {
        super(gamePanel, worldX, worldY);
        init();
    }

    public void activateObject() {
        super.activateObject();
        this.removeObject();
        this.gamePanel.player.addInventoryItem(this.inventoryItem);
    }

    private void init() {
        this.objectType = ObjectType.KEY;
        this.name = this.objectType.name();
        this.setImage(Constants.OBJECT_KEY_IMAGE);
        this.soundPrimary = Constants.SOUND_COIN;
        this.inventoryItem = new InventoryItem(this, 1, true);
    }

    @Override
    public void useObject() {}

    @Override
    public void drawDetails(Graphics2D graphics2D, int x, int y) {
        super.drawDetails(graphics2D, x, y);
        y += Constants.NEW_LINE_SIZE;
        graphics2D.drawString("Count: " + String.valueOf(this.gamePanel.player.getInventoryItem(this.objectType.name())), x, y);
    }
}
