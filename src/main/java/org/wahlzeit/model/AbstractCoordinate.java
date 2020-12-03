package org.wahlzeit.model;

import org.wahlzeit.services.DataObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractCoordinate extends DataObject implements Coordinate {

    protected static final double COMPARE_ACCURACY = 1e-6;

    protected static double normalizeDouble(double value) {
        // rounds so that the result is a multiple of COMPARE_ACCURACY closest to the original value 
        return Math.rint(value / COMPARE_ACCURACY) * COMPARE_ACCURACY;
    }

    protected static boolean compareDoublesNormalized(double a, double b) {
        return normalizeDouble(a) == normalizeDouble(b);
    }

    @Override
    public double getCartesianDistance(Coordinate other) {
        return asCartesianCoordinate().getCartesianDistance(other);
    }

    @Override
    public double getCentralAngle(Coordinate other) {
        return asSphericCoordinate().getCentralAngle(other);
    }

    @Override
    public boolean isEqual(Coordinate other) {
        return asCartesianCoordinate().isEqual(other);
    }

    @Override
    public int hashCode() {
        return asCartesianCoordinate().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Coordinate) {
            return isEqual((Coordinate) obj);
        }
        return false;
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
