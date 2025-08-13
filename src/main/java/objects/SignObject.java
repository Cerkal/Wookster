package objects;

import main.Constants;
import main.GamePanel;

public class SignObject extends SuperObject {

    
    String message;

    public SignObject(GamePanel gamePanel, int worldX, int worldY, String message) {
        super(gamePanel, worldX, worldY);
        this.message = message;
        this.objectType = Object_Type.SIGN;
        init();
    }

    public void activateObject() {
        super.activateObject();
        this.gamePanel.ui.displayMessage(Constants.MESSAGE_SIGN_READS + "\"" + this.message + "\"");
    }

    private void init() {
        this.name = objectType.name();
        this.collision = true;
        this.setImage(Constants.OBJECT_SIGN_IMAGE);
    }
}
