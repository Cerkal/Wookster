package main;

public class Dialogue {

    // Tutorial
    public static final String[] TUTORIAL_PIGS_START = {
        "Can you help me with these pigs?",
        "Just deal with them...",
        "Sometimes they just need a good punch.",
        "Press and hold the " + KeyHandler.SPACEBAR + " to punch."
    };

    public static final String[] TUTORIAL_PIGS_POSITIVE = {
        "Well thats one way to do it.",
        "I'd have just killed em' all.",
        "But look at you...",
        "Savior of the pigs."
    };

    public static final String[] TUTORIAL_PIGS_NEUTRAL = {
        "I guess that works.",
        "I feel pretty bad for Onkie.",
        "He was my favorite...",
    };

    public static final String[] TUTORIAL_PIGS_NEGITIVE = {
        "Oof. Well...",
        "I guess that solves the problem.",
        "Don't know why they all had to die...",
        "Pretty grim...",
        "I don't want to talk to you anymore..."
    };

    public static final String[] TUTORIAL_INVENTORY_START = {
        "Hey, I'm over here now. I'm very fast.",
        "Potions can make you fast.",
        "Here, take one.",
        "You can find them all over the place.",
        "I'm not sure why but people leave them laying around.",
        "Press [i] to open your inventory.",
    };

    public static final String[] TUTORIAL_INVENTORY_REMINDER = {
        "Press " + KeyHandler.I + " to open your inventory.",
        "Then drink the potion by pressing " + KeyHandler.SPACEBAR,
        "Its not that hard.",
    };
}
