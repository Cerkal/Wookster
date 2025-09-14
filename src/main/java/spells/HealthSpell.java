package spells;

import java.awt.Color;
import java.awt.Graphics2D;

import main.Constants;
import main.Utils;

public class HealthSpell extends SuperSpell {

    static final int PRICE = 2;
    
    public HealthSpell() {
        super(SpellType.HEALTH_SPELL);
        randomHealth();
        init();
    }

    public HealthSpell(double healthAmount) {
        super(SpellType.HEALTH_SPELL);
        this.healthAmount = healthAmount;
        init();
    }

    private void init() {
        checkPositive();
        this.descriptionText.add("Modify player's health.");
        setPriceDescription();
    }

    public void randomHealthDamage() {
        this.healthAmount = Utils.generateRandomInt(-30, 0);
    }

    public int drawDescription(Graphics2D graphics2D, int x, int y, boolean clarity) {
        y = super.drawDescription(graphics2D, x, y, clarity);
        if (!clarity) return y;
        graphics2D.setColor(Color.YELLOW);
        String healthDiff = String.valueOf((int) Math.abs(this.healthAmount));
        graphics2D.drawString(
            (positiveSpell ? "Increases" : "Decreases") +
            " player's health by " + healthDiff, x, y
        );
        graphics2D.setColor(Color.WHITE);
        y += Constants.NEW_LINE_SIZE;
        return y;
    }

    private void randomHealth() {
        this.healthAmount = Utils.generateRandomInt(10, 30);
        if (Utils.generateRandomInt(0, 1) == 0) {
            this.healthAmount *= -1;
        }
    }

    private void checkPositive() {
        if (this.healthAmount > 0) {
            this.sellable = true;
            this.price = PRICE;
            this.positiveSpell = true;
            this.message = Constants.MESSAGE_POTION_GOOD;
        } else {
            this.message = Constants.MESSAGE_POTION_BAD;
        }
    }
}
