package objects;

import main.Constants;
import main.GamePanel;

public class SignObject extends SuperObject {

    public final String NAME  = Constants.OBJECT_SIGN;
    
    String message;

    public SignObject(GamePanel gamePanel, int worldX, int worldY, String message) {
        super(gamePanel, worldX, worldY);
        this.message = message;
        init();
    }

    public void activateObject() {
        super.activateObject();
        this.gamePanel.ui.displayMessage(Constants.MESSAGE_SIGN_READS + "\"" + this.message + "\"");
    }

    private void init() {
        this.name = NAME;
        this.setImage(Constants.OBJECT_SIGN_IMAGE);
        this.collision = true;
    }
}
