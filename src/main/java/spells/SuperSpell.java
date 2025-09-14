package spells;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import entity.Player;
import main.Constants;
import main.GamePanel;

public class SuperSpell {

    public SpellType spellType;
    public long startTime = 0;
    public int spellTime = 0;
    public boolean positiveSpell = false;
    public String message;
    public List<String> descriptionText = new ArrayList<>();
    public double healthAmount;
    public int speed = Player.DEFAULT_SPEED;

    public boolean sellable = false;
    public int price;

    public SuperSpell(SpellType spellType) {
        this.spellType = spellType;
    }

    public SpellType getSpellType() {
        return this.spellType;
    }

    public void removeSpell(Player player) {
        player.spells.remove(this.spellType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SuperSpell that = (SuperSpell) o;
        return spellType == that.spellType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(spellType);
    }

    @Override
    public String toString() {
        return "Spell{" +
            "spellType='" + this.spellType + '\'' +
        '}';
    }

    public void setSpell(GamePanel gamePanel) {
        if (this.spellTime > 0) {
            this.startTime = gamePanel.gameTime;
        }
        gamePanel.player.spells.put(this.spellType, this);
    }

    public int drawDescription(Graphics2D graphics2D, int x, int y, boolean clarity) {
        if (this.spellTime > 0) {
            y += Constants.NEW_LINE_SIZE;
            graphics2D.drawString("Spell Time: " + String.valueOf(this.spellTime) + "s", x, y);
        }
        for (String description : this.descriptionText) {
            y += Constants.NEW_LINE_SIZE;
            graphics2D.drawString(description, x, y);
        }
        y += Constants.NEW_LINE_SIZE;
        return y;
    }
    
    public void setPriceDescription() {
        if (this.positiveSpell) {
            // this.descriptionText.add("Price: " + this.price);
        }
    }

    public SuperSpell copy() {
        if (this.spellType == null) return null;
        SuperSpell copySpell = SpellType.create(this);
        copySpell.startTime = this.startTime;
        copySpell.spellTime = this.spellTime;
        copySpell.positiveSpell = this.positiveSpell;
        copySpell.message = this.message;
        copySpell.healthAmount = this.healthAmount;
        copySpell.speed = this.speed;
        copySpell.sellable = this.sellable;
        copySpell.price = this.price;
        copySpell.descriptionText = new ArrayList<>(this.descriptionText);
        return copySpell;
    }

    public enum SpellType {
        KEY_SPELL           (spell -> new KeySpell()),
        HEALTH_SPELL        (spell -> new HealthSpell(spell.healthAmount)),
        SPEED_SPELL         (spell -> new SpeedSpell(spell.speed, spell.spellTime)),
        CLARITY_SPELL       (spell -> new ClaritySpell()),
        INVINCIBILITY_SPELL (spell -> new InvincibilitySpell());

        @FunctionalInterface
        public interface ObjectCreator {
            SuperSpell create(SuperSpell spell);
        }

        private final ObjectCreator creator;

        SpellType(ObjectCreator creator) {
            this.creator = creator;
        }

        public static SuperSpell create(SuperSpell template) {
            if (template == null || template.spellType == null) {
                throw new IllegalArgumentException("Template or its spellType cannot be null");
            }
            return template.spellType.creator.create(template);
        }
    }
}
