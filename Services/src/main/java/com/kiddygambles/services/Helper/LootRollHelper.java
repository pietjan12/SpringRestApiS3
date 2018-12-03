package com.kiddygambles.services.Helper;

import org.springframework.stereotype.Component;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class LootRollHelper {
    public double getRandomDoubleRoll(int min, int max) {
        double random = min + Math.random() * (max - min);
        return random;
    }

    public int getRandomIntRoll(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
