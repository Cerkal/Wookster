package main.Screen;

import main.GamePanel;

public class ToggleOption {
    public final static String ON = "On"; 
    public final static String OFF = "Off";
    public final static int INDEX_ON = 0;
    public final static int INDEX_OFF = 1;

    public String value;

    public ToggleOption(String value) {
        this.value = value;
    }

    public void action(GamePanel gamePanel){}

    public String getValue() {
        return this.value;
    }

    public static boolean isToggleOn(int value) {
        return value == 1;
    }
}