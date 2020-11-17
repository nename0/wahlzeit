package org.wahlzeit.model;

import org.wahlzeit.services.DataObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Coordinate extends DataObject {
    private static final double COMPARE_ACCURACY = 1e-6;

    private double x;
    private double y;
    private double z;

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

    public void setX(double x) {
        this.x = x;
        this.incWriteCount();
    }

    public void setY(double y) {
        this.y = y;
        this.incWriteCount();
    }

    public void setZ(double z) {
        this.z = z;
        this.incWriteCount();
    }

    public double getDistance(Coordinate distanceTo) {
        double dx = x - distanceTo.x;
        double dy = y - distanceTo.y;
        double dz = z - distanceTo.z;
        return Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
    }

    private static double normalizeDouble(double value) {
        // rounds so that the result is a multiple of COMPARE_ACCURACY closest to the original value 
        return Math.rint(value / COMPARE_ACCURACY) * COMPARE_ACCURACY;
    }

    private static boolean compareDoublesNormalized(double a, double b) {
        return normalizeDouble(a) == normalizeDouble(b);
    }

    public boolean isEqual(Coordinate other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        return compareDoublesNormalized(other.x, x) &&
                compareDoublesNormalized(other.y, y) &&
                compareDoublesNormalized(other.z, z);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                normalizeDouble(x),
                normalizeDouble(y),
                normalizeDouble(z)
        );
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Coordinate) {
            return isEqual((Coordinate) o);
        }
        return false;
    }

    @Override
    public void readFrom(ResultSet rset) throws SQLException {
        this.x = rset.getDouble(Location.COLUMN_NAME_PARAM_A);
        this.y = rset.getDouble(Location.COLUMN_NAME_PARAM_B);
        this.z = rset.getDouble(Location.COLUMN_NAME_PARAM_C);
    }

    @Override
    public void writeOn(ResultSet rset) throws SQLException {
        rset.updateDouble(Location.COLUMN_NAME_PARAM_A, this.x);
        rset.updateDouble(Location.COLUMN_NAME_PARAM_B, this.y);
        rset.updateDouble(Location.COLUMN_NAME_PARAM_C, this.z);
    }

    @Override
    public String getIdAsString() {
        throw new UnsupportedOperationException("Coordinate is not stored in a separate table. So no ids");
    }

    @Override
    public void writeId(PreparedStatement stmt, int pos) throws SQLException {
        throw new UnsupportedOperationException("Coordinate is not stored in a separate table. So no ids");
    }
}
