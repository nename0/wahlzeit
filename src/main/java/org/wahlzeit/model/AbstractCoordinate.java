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
    
    protected abstract void assertClassInvariants();

    @Override
    public CartesianCoordinate asCartesianCoordinate() {
        assertClassInvariants();

        CartesianCoordinate coordinate = doGetAsCartesianCoordinate();

        assertNotNull(coordinate);
        assertClassInvariants();
        return coordinate;
    }

    protected abstract CartesianCoordinate doGetAsCartesianCoordinate();

    @Override
    public SphericCoordinate asSphericCoordinate() {
        assertClassInvariants();

        SphericCoordinate coordinate = doGetAsSphericCoordinate();

        assertNotNull(coordinate);
        assertClassInvariants();
        return coordinate;
    }

    protected abstract SphericCoordinate doGetAsSphericCoordinate();

    @Override
    public double getCartesianDistance(Coordinate other) {
        assertClassInvariants();
        assertNotNull(other);
        
        double distance = doGetCartesianDistance(other);
        
        assertNonNegative(distance);
        assertClassInvariants();
        return distance;
    }
    
    protected double doGetCartesianDistance(Coordinate other) {
        return asCartesianCoordinate().doGetCartesianDistance(other);
    }

    @Override
    public double getCentralAngle(Coordinate other) {
        assertClassInvariants();
        assertNotNull(other);

        double angle = doGetCentralAngle(other);

        assertValidCentralAngle(angle);
        assertClassInvariants();
        return angle;
    }

    protected double doGetCentralAngle(Coordinate other) {
        return asSphericCoordinate().doGetCentralAngle(other);
    }

    @Override
    public boolean isEqual(Coordinate other) {
        assertClassInvariants();
        assertNotNull(other);

        boolean result = doCheckEqual(other);
        
        assertClassInvariants();
        return result;
    }
    
    protected boolean doCheckEqual(Coordinate other) {
        return asCartesianCoordinate().isEqual(other);
    }

    @Override
    public int hashCode() {
        assertClassInvariants();

        int result = doGetHashCode();

        assertClassInvariants();
        return result;
    }
    
    protected int doGetHashCode() {
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
    public short getCoordinateType() {
        assertClassInvariants();
        
        short result = doGetCoordinateType();
        
        if (result != Location.CARTESIAN_COORDINATE_TYPE && result != Location.SPHERIC_COORDINATE_TYPE) {
            throw new IllegalArgumentException("Unknown Coordinate-Type");
        }
        assertClassInvariants();
        return result;
    }


    protected abstract short doGetCoordinateType();

    @Override
    public void readFrom(ResultSet rset) throws SQLException {
        assertClassInvariants();
        assertNotNull(rset);

        doReadFrom(rset);
        
        assertClassInvariants();
    }

    protected abstract void doReadFrom(ResultSet rset) throws SQLException;

    @Override
    public void writeOn(ResultSet rset) throws SQLException {
        assertClassInvariants();
        assertNotNull(rset);

        doWriteOn(rset);

        assertClassInvariants();
    }

    protected abstract void doWriteOn(ResultSet rset) throws SQLException;

    protected static void assertScalarValue(double d) {
        if (Double.isInfinite(d) || Double.isNaN(d)) {
            throw new IllegalStateException("should be neither Infinite nor NaN");
        }
    }
    
    protected static void assertNonNegative(double d) {
        if (d < 0) {
            throw new IllegalArgumentException("should be non-negative");
        }
    }
    
    protected static void assertValidCentralAngle(double angleRad) {
        if (angleRad < Math.toRadians(0) || angleRad > Math.toRadians(180)) {
            throw new IllegalArgumentException("should be between 0° and 180°");
        }
    }
    
    protected static void assertNotNull(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
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
