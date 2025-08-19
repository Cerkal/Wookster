package objects.projectiles;

import entity.Entity;
import main.Constants;
import main.GamePanel;

public class SwordProjectile extends MeleeProjectile {

    public static final int DAMAGE_MODIFIER = 2;

    public SwordProjectile(GamePanel gamePanel, Entity entity, int hold) {
        super(gamePanel, entity, hold);
        this.setImage(Constants.EFFECT_ALERT_LIGHT);
    }

    public SwordProjectile(GamePanel gamePanel, int hold) {
        this(gamePanel, gamePanel.player, hold);
    }
}
