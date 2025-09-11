package main;

import java.util.HashMap;
import java.util.List;

public class QuestDescriptions {

    public static final String PIGS = "Pigs";
    public static final String INVENTORY = "Inventory";
    public static final String MOM = "Find mom.";

    public static final HashMap<String, List<String>> DESCRIPTIONS = new HashMap<>(){{
        put(
            PIGS, 
            List.of("Help the old wookie with his pigs.")
        );
        put(
            INVENTORY,
            List.of("Do that inventory thing.")
        );
        put(
            MOM,
            List.of("Find your mom.")
        );
    }};
}
