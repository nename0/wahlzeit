package org.wahlzeit.model;

import org.wahlzeit.utils.Invariants;
import org.wahlzeit.utils.Preconditions;

import java.sql.ResultSet;
import java.sql.SQLException;

import static java.lang.Math.*;

public class SphericCoordinate extends AbstractCoordinate {
    private double phi;
    private double theta;
    private double radius;

    public SphericCoordinate(double phi, double theta, double radius) {
        Preconditions.assertScalar(phi, "phi must be scalar");
        Preconditions.assertScalar(theta, "theta must be scalar");
        Preconditions.assertScalar(radius, "radius must be scalar");
        Preconditions.assertNonNegative(radius, "radius must be non-negative");
        this.phi = phi;
        this.theta = theta;
        this.radius = radius;
    }

    @Override
    protected void assertClassInvariants() {
        Invariants.assertScalar(phi, "phi not scalar");
        Invariants.assertScalar(theta, "theta not scalar");
        Invariants.assertScalar(radius, "radius not scalar");
        Invariants.assertNonNegative(radius, "radius negative");
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
        Preconditions.assertScalar(phi, "phi must be scalar");
        this.phi = phi;
        incWriteCount();
    }

    public void setTheta(double theta) {
        Preconditions.assertScalar(theta, "theta must be scalar");
        this.theta = theta;
        incWriteCount();
    }

    public void setRadius(double radius) {
        Preconditions.assertScalar(radius, "radius must be scalar");
        Preconditions.assertNonNegative(radius, "radius must be non-negative");
        this.radius = radius;
        incWriteCount();
    }

    @Override
    protected void doReadFrom(ResultSet rset) throws SQLException {
        double phi = rset.getDouble(Location.COLUMN_NAME_PARAM_A);
        double theta = rset.getDouble(Location.COLUMN_NAME_PARAM_B);
        double radius = rset.getDouble(Location.COLUMN_NAME_PARAM_C);
        Preconditions.assertScalar(phi, "phi not scalar in database");
        Preconditions.assertScalar(theta, "theta not scalar in database");
        Preconditions.assertScalar(radius, "radius not scalar in database");
        Preconditions.assertNonNegative(radius, "radius negative in database");
        this.phi = phi;
        this.theta = theta;
        this.radius = radius;
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

