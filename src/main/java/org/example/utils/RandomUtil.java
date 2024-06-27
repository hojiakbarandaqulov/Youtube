package org.example.utils;

import java.util.Random;

public class RandomUtil {

    public static String getRandomSmsCode() {
        Random random = new Random();
        return String.valueOf(random.nextInt(10000, 99999));
    }

    public static String getRandomPassword() {
        Random random = new Random();
        return String.valueOf(random.nextInt(100000, 999999));
    }
}
