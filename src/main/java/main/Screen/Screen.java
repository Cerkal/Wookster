package main.Screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.Constants;

public class Screen {
    public Screen screen;
    public String title;
    public List<Option> options = new ArrayList<>();
    public boolean centerText = true;
    public int xAlign = Constants.SCREEN_WIDTH / 2 - (Constants.TILE_SIZE * 2) - 100;
    public int longestOption;

    public Screen(String title) {
        this.title = title;
    }

    public Screen(String title, List<Option> options) {
        this(title);
        this.options = options;
    }

    public Screen(String title, List<Option> options, boolean centerText, int xAlign) {
        this(title, options);
        this.centerText = centerText;
        this.xAlign = xAlign;
    }

    public void addOption(Option option) {
        this.options.add(option);
    }

    public List<String> getOptionTitles() {
        List<String> options = new ArrayList<>();
        for (Option option : this.options) {
            options.add(option.getNameWithValue());
        }
        return options;
    }

    public HashMap<String, Option> getOptionsMap() {
        HashMap<String, Option> options = new HashMap<>();
        for (Option option : this.options) {
            options.put(option.getNameWithValue(), option);
        }
        return options;
    }
}
