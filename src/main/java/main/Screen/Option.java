package main.Screen;

import main.GamePanel;

public class Option {
    public String name;
    protected int spacer = 30;
    
    public void action(GamePanel gamePanel) {}

    public Option(String name) {
        this.name = name;
    }

    protected String getNameWithValue() {
        return this.name;
    }

    public void setDefaultValue(int value) {}

    public int getValue() {
        return 0;
    }
}