package main.Screen;

import java.util.ArrayList;
import java.util.List;

import main.GamePanel;

public class Toggle extends Option {
    private List<ToggleOption> values = new ArrayList<>();
    private ToggleOption currentValue;
    private int currentIndex = 0;

    public Toggle(String name) {
        super(name);
        this.name = name;
    }

    public Toggle(String name, List<ToggleOption> values) {
        this(name);
        this.values = values;
        this.currentValue = values.get(0);
    }

    @Override
    public void action(GamePanel gamePanel) {
        cycle();
        getCurrentSettingOption().action(gamePanel);
    }

    public ToggleOption getCurrentSettingOption() {
        return this.currentValue;
    }

    public int getCurrentIndex() {
        return this.currentIndex;
    }

    @Override
    public void setDefaultValue(int index) {
        if (index < 0) index = 0;
        if (index >= this.values.size()) index = this.values.size() - 1;
        this.currentIndex = index;
        this.currentValue = this.values.get(this.currentIndex);
    }

    private void cycle() {
        this.currentIndex = (currentIndex + 1) % this.values.size();
        this.currentValue = this.values.get(this.currentIndex);
    }

    public String getNameWithValue() {
        int spacer = this.spacer - this.name.length();
        return this.name + " ".repeat(spacer) + this.currentValue.value;
    }

    @Override
    public int getValue() {
        return this.currentIndex;
    }
}