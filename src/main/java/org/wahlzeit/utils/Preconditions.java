package org.wahlzeit.utils;

public final class Preconditions {

    private Preconditions() {
    }

    public static void assertNotNull(Object o, String msg) {
        if (o == null) {
            throw new NullPointerException(msg);
        }
    }
    
    public static void fail(String msg) {
        throw new IllegalArgumentException(msg);
    }

    public static void assertNonNegative(double value, String msg) {
        if (value < 0) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static void assertScalar(double d, String msg) {
        if (Double.isInfinite(d) || Double.isNaN(d)) {
            throw new IllegalArgumentException(msg);
        }
    }
}
