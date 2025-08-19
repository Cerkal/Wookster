package objects.projectiles;

import entity.Entity;
import main.Constants;
import main.GamePanel;

public class PunchProjectile extends MeleeProjectile {

    public static final int DAMAGE_MODIFIER = 2;

    public PunchProjectile(GamePanel gamePanel, Entity entity, int hold) {
        super(gamePanel, entity, hold);
        this.setImage(Constants.EFFECT_ALERT);
    }

    public PunchProjectile(GamePanel gamePanel, int hold) {
        super(gamePanel, gamePanel.player, hold);
    }
}
