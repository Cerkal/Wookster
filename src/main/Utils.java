package main;

import java.util.Random;

public class Utils {

    public static int generateRandomInt(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("Min cannot be greater than Max.");
        }
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }
}
