package objects;

import main.Constants;
import main.GamePanel;

public class ChestObject extends SuperObject {

    public ChestObject(GamePanel gamePanel, int worldX, int worldY) {
        super(gamePanel, worldX, worldY);
        init();
    }

    public void activateObject() {
        super.activateObject();
        this.gamePanel.ui.displayMessage("There is nothing in this chest.");
    }

    private void init() {
        this.objectType = ObjectType.CHEST;
        this.name = this.objectType.name();
        this.setImage(Constants.OBJECT_CHEST_IMAGE);
        this.collision = true;
    }
}
