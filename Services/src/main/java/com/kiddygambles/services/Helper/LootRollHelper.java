package com.kiddygambles.services.Helper;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class LootRollHelper {
    private Random random;

    public LootRollHelper() {
        this.random = new Random();
    }

    public double getRandomDoubleRoll(int min, int max) {
        double random = min + Math.random() * (max - min);
        return random;
    }
}
