package effects;

import java.util.ArrayList;
import java.util.List;

import main.Constants;
import main.GamePanel;
import main.Utils;

public class BloodEffect extends Effect {

    public BloodEffect(GamePanel gamePanel, int worldX, int worldY) {
        super(gamePanel, worldX, worldY);
        setImage();
    }

    protected void setImage() {
        List<String> randomImage = new ArrayList<>();
        randomImage.add(Constants.EFFECT_BLOOD_0);
        randomImage.add(Constants.EFFECT_BLOOD_1);
        randomImage.add(Constants.EFFECT_BLOOD_2);
        int index = Utils.generateRandomInt(0, randomImage.size() - 1);
        setImage(randomImage.get(index));
    }
}
