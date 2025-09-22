package main;

import objects.PotionObject;

public class Dialogue {

    private static final String PLAYER_NAME = "Wookster";

    // Defalts
    public static final String[] DEFAULT_WARNING = {
        "Don't try that again."
    };

    // Tutorial
    public static final String[] TUTORIAL_PIGS_START = {
        "Can you help me with these pigs?",
        "Just deal with them...",
        "Sometimes they just need a good punch.",
        "Press and hold the " + KeyHandler.SPACEBAR + " to punch.",
        "Come back when you are done."
    };

    public static final String[] TUTORIAL_PIGS_START_MOUSE_AIM = {
        "Can you help me with these pigs?",
        "Just deal with them...",
        "Sometimes they just need a good punch.",
        "Click and hold the " + KeyHandler.LEFT_MOUSE_BUTTON + " to punch.",
        "Come back when you are done."
    };

    public static final String[] TUTORIAL_PIGS_POSITIVE = {
        "Well thats one way to do it.",
        "I'd have just killed em' all.",
        "But look at you...",
        "\"Savior of the Pigs\""
    };

    public static final String[] TUTORIAL_PIGS_NEUTRAL = {
        "I guess that works.",
        "I feel pretty bad for Onkie.",
        "He was my favorite...",
    };

    public static final String[] TUTORIAL_PIGS_NEGATIVE = {
        "Oof. Well...",
        "I guess that solves the problem.",
        "Don't know why they all had to die...",
        "Pretty grim...",
        "I don't want to talk to you anymore..."
    };
    
    public static final String[] TUTORIAL_PIGS_END = {
        "Leave me alone."
    };

    public static final String[] TUTORIAL_INVENTORY_START = {
        "Hey, I'm over here now. I'm very fast.",
        "Potions can make you fast.",
        "Here, take one.",
        "You can find them all over the place.",
        "I'm not sure why but people leave them laying around.",
        "Press " + KeyHandler.TAB + " to open your inventory.",
    };

    public static final String[] TUTORIAL_INVENTORY_REMINDER = {
        "Press " + KeyHandler.TAB + " to open your inventory.",
        "Then drink the potion by pressing " + KeyHandler.SPACEBAR,
        "It's not that hard.",
    };

    public static final String[] TUTORIAL_INVENTORY_COMPLETE = {
        "Wow look at you run.",
        "You can use inventory to select usable items.",
        "But I don't have to tell you that. You inventory pro you.",
        "Careful with the " + PotionObject.COLOR + " potions though.",
        "They can be good or bad. It's a risk."
    };

    public static final String[] TUTORIAL_COMPLETE = {
        PLAYER_NAME + ", about your mom...",
        "She went out for berries or something.",
        "I'm worried about her. Please find her.",
        "Take this crossbow.",
        "Press and hold " + KeyHandler.SPACEBAR + " to shoot.",
        "I'm your dad by the way."
    };

    public static final String[] TUTORIAL_COMPLETE_MOUSE_AIM = {
        PLAYER_NAME + ", about your mom...",
        "She went out for berries or something.",
        "I'm worried about her. Please find her.",
        "Take this crossbow.",
        "Press and hold " + KeyHandler.LEFT_MOUSE_BUTTON + " to shoot.",
        "I'm your dad by the way."
    };

    // Level 01
    public static final String[] LEVEL_01_MOM = {
        "Trees hu?",
        "It's easy to get lost in here.",
        "I was going to make this my home.",
        "Thank God you found me.",
        "Lead the way. I'll follow you."
    };

    // Level 03
    public static final String[] LEVEL_03_VENDOR_INTRO = {
        "I sell stuff, if you want stuff I got it.",
        "Do you want the stuff or not?",
    };
}
