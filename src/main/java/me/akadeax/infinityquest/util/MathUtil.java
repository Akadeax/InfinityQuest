package me.akadeax.infinityquest.util;

public class MathUtil {
    public static int map(int value, int oldMin, int oldMax, int newMin, int newMax) {
        return newMin + (value - oldMin) * (newMax - newMin) / (oldMax - oldMin);
    }
}
