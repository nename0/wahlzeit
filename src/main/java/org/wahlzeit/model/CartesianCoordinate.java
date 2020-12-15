package org.wahlzeit.model;

import org.wahlzeit.utils.Invariants;
import org.wahlzeit.utils.Preconditions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class CartesianCoordinate extends AbstractCoordinate {
    private static final CartesianCoordinate ORIGIN = new CartesianCoordinate(0, 0, 0);

    private double x;
    private double y;
    private double z;

    public CartesianCoordinate(double x, double y, double z) {
        Preconditions.assertScalar(x, "x must be scalar");
        Preconditions.assertScalar(y, "y must be scalar");
        Preconditions.assertScalar(z, "z must be scalar");
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    protected void assertClassInvariants() {
        Invariants.assertScalar(x, "x not scalar");
        Invariants.assertScalar(y, "y not scalar");
        Invariants.assertScalar(z, "z not scalar");
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
        Preconditions.assertScalar(x, "x must be scalar");
        this.x = x;
        this.incWriteCount();
    }

    public void setY(double y) {
        Preconditions.assertScalar(y, "y must be scalar");
        this.y = y;
        this.incWriteCount();
    }

    public void setZ(double z) {
        Preconditions.assertScalar(z, "z must be scalar");
        this.z = z;
        this.incWriteCount();
    }

    public double getDistance(CartesianCoordinate distanceTo) {
        double dx = x - distanceTo.x;
        double dy = y - distanceTo.y;
        double dz = z - distanceTo.z;
        return Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
    }

    @Override
    protected int doGetHashCode() {
        return Objects.hash(
                normalizeDouble(x),
                normalizeDouble(y),
                normalizeDouble(z)
        );
    }

    @Override
    protected void doReadFrom(ResultSet rset) throws SQLException {
        double x = rset.getDouble(Location.COLUMN_NAME_PARAM_A);
        double y = rset.getDouble(Location.COLUMN_NAME_PARAM_B);
        double z = rset.getDouble(Location.COLUMN_NAME_PARAM_C);
        Preconditions.assertScalar(x, "x not scalar in database");
        Preconditions.assertScalar(y, "y not scalar in database");
        Preconditions.assertScalar(z, "z not scalar in database");
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    protected void doWriteOn(ResultSet rset) throws SQLException {
        rset.updateDouble(Location.COLUMN_NAME_PARAM_A, this.x);
        rset.updateDouble(Location.COLUMN_NAME_PARAM_B, this.y);
        rset.updateDouble(Location.COLUMN_NAME_PARAM_C, this.z);
    }

    @Override
    protected CartesianCoordinate doGetAsCartesianCoordinate() {
        return this;
    }

    @Override
    protected SphericCoordinate doGetAsSphericCoordinate() {
        double radius = getDistance(ORIGIN);
        if (radius == 0) {
            return new SphericCoordinate(0, 0, 0);
        }
        double phi = Math.atan2(y, x);
        double theta = Math.atan2(Math.sqrt(x * x + y * y), z);
        return new SphericCoordinate(phi, theta, radius);
    }

    @Override
    protected double doGetCartesianDistance(Coordinate other) {
        return this.getDistance(other.asCartesianCoordinate());
    }

    @Override
    protected boolean doCheckEqual(Coordinate other) {
        if (other == this) {
            return true;
        }
        CartesianCoordinate otherCartesian = other.asCartesianCoordinate();
        return compareDoublesNormalized(otherCartesian.x, x) &&
                compareDoublesNormalized(otherCartesian.y, y) &&
                compareDoublesNormalized(otherCartesian.z, z);
    }

    @Override
    protected short doGetCoordinateType() {
        return Location.CARTESIAN_COORDINATE_TYPE;
    }
}
