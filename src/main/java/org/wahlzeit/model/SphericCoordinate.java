package org.wahlzeit.model;

import org.wahlzeit.services.DataObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.lang.Math.*;

public class SphericCoordinate extends DataObject implements Coordinate {
    private double phi;
    private double theta;
    private double radius;

    public SphericCoordinate(double phi, double theta, double radius) {
        this.phi = phi;
        this.theta = theta;
        this.radius = radius;
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
        this.phi = phi;
        incWriteCount();
    }

    public void setTheta(double theta) {
        this.theta = theta;
        incWriteCount();
    }

    public void setRadius(double radius) {
        this.radius = radius;
        incWriteCount();
    }

    @Override
    public int hashCode() {
        return asCartesianCoordinate().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Coordinate) {
            return asCartesianCoordinate().isEqual((Coordinate) o);
        }
        return false;
    }

    @Override
    public void readFrom(ResultSet rset) throws SQLException {
        this.phi = rset.getDouble(Location.COLUMN_NAME_PARAM_A);
        this.theta = rset.getDouble(Location.COLUMN_NAME_PARAM_B);
        this.radius = rset.getDouble(Location.COLUMN_NAME_PARAM_C);
    }

    @Override
    public void writeOn(ResultSet rset) throws SQLException {
        rset.updateDouble(Location.COLUMN_NAME_PARAM_A, this.phi);
        rset.updateDouble(Location.COLUMN_NAME_PARAM_B, this.theta);
        rset.updateDouble(Location.COLUMN_NAME_PARAM_C, this.radius);
    }

    @Override
    public String getIdAsString() {
        throw new UnsupportedOperationException("Coordinate is not stored in a separate table. So no ids");
    }

    @Override
    public void writeId(PreparedStatement stmt, int pos) throws SQLException {
        throw new UnsupportedOperationException("Coordinate is not stored in a separate table. So no ids");
    }

    @Override
    public CartesianCoordinate asCartesianCoordinate() {
        double sinTheta = sin(theta);
        double x = radius * sinTheta * cos(phi);
        double y = radius * sinTheta * sin(phi);
        double z = radius * cos(theta);
        return new CartesianCoordinate(x, y, z);
    }

    @Override
    public SphericCoordinate asSphericCoordinate() {
        return this;
    }

    @Override
    public double getCartesianDistance(Coordinate other) {
        return asCartesianCoordinate().getCartesianDistance(other);
    }

    @Override
    public double getCentralAngle(Coordinate other) {
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
    public boolean isEqual(Coordinate other) {
        return asCartesianCoordinate().isEqual(other);
    }

    @Override
    public short getCoordinateType() {
        return Location.SPHERIC_COORDINATE_TYPE;
    }
}

