package main;

import java.util.HashMap;
import java.util.List;

public class QuestDescriptions {

    public static final String TUTORIAL_COMPLETE = "TUTORIAL COMPLETE";

    public static final String PIGS = "PIG HELPER";
    public static final String INVENTORY = "INVENTORY PRO";
    public static final String MOM = "MISSING MOM";
    public static final String MOM_HOME = "MOM HOME";

    public static final HashMap<String, List<String>> DESCRIPTIONS = new HashMap<>(){{
        put(
            PIGS, 
            List.of("Help the old wookie with his pigs.")
        );
        put(
            INVENTORY,
            List.of(
                "Learn how to use your inventory.",
                "Its not that hard."
            )
        );
        put(
            MOM,
            List.of(
                "Your mom went to look for berries",
                "and is missing. Find her."
            )
        );
        put(
            MOM_HOME,
            List.of(
                "Get your mom back to her hut."
            )
        );
    }};
}
