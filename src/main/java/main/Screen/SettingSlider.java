package main.Screen;

import main.GamePanel;

public class SettingSlider extends Option {
    private int max = 10;
    private int min = 0;
    private int currentValue = 5;

    public SettingSlider(String name) {
        super(name);
        this.name = name;
    }

    public void action(GamePanel gamePanel) {}

    public void increase() {
        this.currentValue += 1;
        if (this.currentValue > this.max) {
            this.currentValue = this.max;
        }
    }

    public void decrease() {
        this.currentValue -= 1;
        if (this.currentValue < this.min) {
            this.currentValue = this.min;
        }
    }

    public int getCurrentValue() {
        return this.currentValue;
    }

    @Override
    public void setDefaultValue(int value) {
        this.currentValue = value;
    }

    public String getNameWithValue() {
        int spacer = this.spacer - this.name.length();
        return this.name + " ".repeat(spacer) + "|".repeat(this.currentValue);
    }

    @Override
    public int getValue() {
        return this.currentValue;
    }
}