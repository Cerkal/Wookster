package effects;

import entity.Entity;
import main.Constants;
import main.GamePanel;

public class AlertEffect extends Effect {

    public AlertEffect(GamePanel gamePanel, Entity entity) {
        super(gamePanel, entity.worldX, entity.worldY);
        this.effectTime = 2;
        setImage();
    }

    protected void setImage() {
        setImage(Constants.EFFECT_ALERT);
    }
}
