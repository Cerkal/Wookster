package objects.projectiles;

import main.Constants;
import main.GamePanel;

public class PunchProjectile extends MeleeProjectile {

    public static final int DAMAGE_MODIFIER = 2;

    public PunchProjectile(GamePanel gamePanel, int hold) {
        super(gamePanel, hold);
        this.setImage(Constants.EFFECT_ALERT);
    }
}
