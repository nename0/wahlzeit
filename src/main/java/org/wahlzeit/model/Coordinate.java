package org.wahlzeit.model;

public class Coordinate {
    private final double x;
    private final double y;
    private final double z;

    public Coordinate(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getDistance(Coordinate distanceTo) {
        double dx = x - distanceTo.x;
        double dy = y - distanceTo.y;
        double dz = z - distanceTo.z;
        return Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
    }

    public boolean isEqual(Coordinate other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        return Double.compare(other.x, x) == 0 &&
                Double.compare(other.y, y) == 0 &&
                Double.compare(other.z, z) == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Coordinate) {
            return isEqual((Coordinate) o);
        }
        return false;
    }

}
