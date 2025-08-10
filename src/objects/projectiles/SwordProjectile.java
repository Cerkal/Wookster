package objects.projectiles;

import main.Constants;
import main.GamePanel;

public class SwordProjectile extends MeleeProjectile {

    public static final int DAMAGE_MODIFIER = 2;

    public SwordProjectile(GamePanel gamePanel, int hold) {
        super(gamePanel, hold);
        this.setImage(Constants.EFFECT_ALERT_LIGHT);
    }
}
