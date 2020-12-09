package org.wahlzeit.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import static java.lang.Math.*;

public class SphericCoordinate extends AbstractCoordinate {
    private double phi;
    private double theta;
    private double radius;

    public SphericCoordinate(double phi, double theta, double radius) {
        assertValidSphericCoordinates(phi, theta, radius);
        this.phi = phi;
        this.theta = theta;
        this.radius = radius;
    }

    protected static void assertValidSphericCoordinates(double phi, double theta, double radius) {
        assertScalarValue(phi);
        assertScalarValue(theta);
        assertScalarValue(radius);
        assertNonNegative(radius);
    }

    @Override
    protected void assertClassInvariants() {
        assertValidSphericCoordinates(phi, theta, radius);
    }

    public double getPhi() {
        return phi;
    }

    public double getTheta() {
        return theta;
    }

    public double getRadius() {
        return radius;
    }

    public void setPhi(double phi) {
        assertScalarValue(phi);
        this.phi = phi;
        incWriteCount();
    }

    public void setTheta(double theta) {
        assertScalarValue(theta);
        this.theta = theta;
        incWriteCount();
    }

    public void setRadius(double radius) {
        assertScalarValue(radius);
        assertNonNegative(radius);
        this.radius = radius;
        incWriteCount();
    }

    @Override
    protected void doReadFrom(ResultSet rset) throws SQLException {
        this.phi = rset.getDouble(Location.COLUMN_NAME_PARAM_A);
        this.theta = rset.getDouble(Location.COLUMN_NAME_PARAM_B);
        this.radius = rset.getDouble(Location.COLUMN_NAME_PARAM_C);
    }

    @Override
    protected void doWriteOn(ResultSet rset) throws SQLException {
        rset.updateDouble(Location.COLUMN_NAME_PARAM_A, this.phi);
        rset.updateDouble(Location.COLUMN_NAME_PARAM_B, this.theta);
        rset.updateDouble(Location.COLUMN_NAME_PARAM_C, this.radius);
    }

    @Override
    protected CartesianCoordinate doGetAsCartesianCoordinate() {
        double sinTheta = sin(theta);
        double x = radius * sinTheta * cos(phi);
        double y = radius * sinTheta * sin(phi);
        double z = radius * cos(theta);
        return new CartesianCoordinate(x, y, z);
    }

    @Override
    protected SphericCoordinate doGetAsSphericCoordinate() {
        return this;
    }

    @Override
    protected double doGetCentralAngle(Coordinate other) {
        SphericCoordinate otherSpheric = other.asSphericCoordinate();
        double deltaPhi = abs(phi - otherSpheric.phi);
        // we need to convert here as in the formula latitude is used
        // and latitude = 0 is the equator, While theta = 0 is the north pole
        double latitudeA = Math.toRadians(90) - theta;
        double latitudeB = Math.toRadians(90) - otherSpheric.theta;
        double ratio = sin(latitudeA) * sin(latitudeB) + cos(latitudeA) * cos(latitudeB) * cos(deltaPhi);
        // there might be rounding issues
        ratio = max(-1, min(1, ratio));
        return acos(ratio);
    }

    @Override
    protected short doGetCoordinateType() {
        return Location.SPHERIC_COORDINATE_TYPE;
    }
}

