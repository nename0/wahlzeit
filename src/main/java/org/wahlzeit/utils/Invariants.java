package org.wahlzeit.utils;

public final class Invariants {
    private Invariants() {}

    public static void assertNotNull(Object o, String msg) {
        if (o == null) {
            throw new NullPointerException(msg);
        }
    }

    public static void assertScalar(double d, String msg) {
        if (Double.isInfinite(d) || Double.isNaN(d)) {
            throw new IllegalStateException(msg);
        }
    }
    
    public static void assertNonNegative(double value, String msg) {
        if (value < 0) {
            throw new IllegalStateException(msg);
        }
    }

    public static void assertBetween(double value, double lowerEnd, double upperEnd, String msg) {
        if (value < lowerEnd || value > upperEnd) {
            throw new IllegalStateException(msg);
        }
    }
}
