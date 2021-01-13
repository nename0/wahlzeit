package org.wahlzeit.model;

import org.wahlzeit.utils.Invariants;
import org.wahlzeit.utils.PatternInstance;
import org.wahlzeit.utils.Preconditions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.lang.Math.*;

@PatternInstance(
        patternName = "Value Object",
        participants = {})
public class SphericCoordinate extends AbstractCoordinate {

    // Because we are value objects hashCode() and equals() work on reference identity.
    // However for this cache we need to compare the actually attribute vales therefore we need the ValueHolder class
    private static final Map<ValueHolder, SphericCoordinate> sharedObjectsCache = new HashMap<>();

    private final ValueHolder valueHolder;

    private SphericCoordinate(ValueHolder valueHolder) {
        this.valueHolder = valueHolder;
    }

    public static SphericCoordinate get(double phi, double theta, double radius) {
        Preconditions.assertScalar(phi, "phi must be scalar");
        Preconditions.assertScalar(theta, "theta must be scalar");
        Preconditions.assertScalar(radius, "radius must be scalar");
        Preconditions.assertNonNegative(radius, "radius must be non-negative");
        ValueHolder valueHolder = new ValueHolder(phi, theta, radius);
        return sharedObjectsCache.computeIfAbsent(valueHolder, SphericCoordinate::new);
    }

    public static SphericCoordinate getFromSQL(ResultSet rset) throws SQLException {
        double phi = rset.getDouble(Location.COLUMN_NAME_PARAM_A);
        double theta = rset.getDouble(Location.COLUMN_NAME_PARAM_B);
        double radius = rset.getDouble(Location.COLUMN_NAME_PARAM_C);
        Preconditions.assertScalar(phi, "phi not scalar in database");
        Preconditions.assertScalar(theta, "theta not scalar in database");
        Preconditions.assertScalar(radius, "radius not scalar in database");
        Preconditions.assertNonNegative(radius, "radius negative in database");
        return get(phi, theta, radius);
    }

    @Override
    protected void assertClassInvariants() {
        Invariants.assertScalar(valueHolder.phi, "phi not scalar");
        Invariants.assertScalar(valueHolder.theta, "theta not scalar");
        Invariants.assertScalar(valueHolder.radius, "radius not scalar");
        Invariants.assertNonNegative(valueHolder.radius, "radius negative");
    }

    public double getPhi() {
        return valueHolder.phi;
    }

    public double getTheta() {
        return valueHolder.theta;
    }

    public double getRadius() {
        return valueHolder.radius;
    }

    @Override
    protected void doWriteOn(ResultSet rset) throws SQLException {
        rset.updateDouble(Location.COLUMN_NAME_PARAM_A, valueHolder.phi);
        rset.updateDouble(Location.COLUMN_NAME_PARAM_B, valueHolder.theta);
        rset.updateDouble(Location.COLUMN_NAME_PARAM_C, valueHolder.radius);
    }

    @Override
    protected CartesianCoordinate doGetAsCartesianCoordinate() {
        double sinTheta = sin(valueHolder.theta);
        double x = valueHolder.radius * sinTheta * cos(valueHolder.phi);
        double y = valueHolder.radius * sinTheta * sin(valueHolder.phi);
        double z = valueHolder.radius * cos(valueHolder.theta);
        return CartesianCoordinate.get(x, y, z);
    }

    @Override
    protected SphericCoordinate doGetAsSphericCoordinate() {
        return this;
    }

    @Override
    protected double doGetCentralAngle(Coordinate other) {
        SphericCoordinate otherSpheric = other.asSphericCoordinate();
        double deltaPhi = abs(valueHolder.phi - otherSpheric.valueHolder.phi);
        // we need to convert here as in the formula latitude is used
        // and latitude = 0 is the equator, While theta = 0 is the north pole
        double latitudeA = Math.toRadians(90) - valueHolder.theta;
        double latitudeB = Math.toRadians(90) - otherSpheric.valueHolder.theta;
        double ratio = sin(latitudeA) * sin(latitudeB) + cos(latitudeA) * cos(latitudeB) * cos(deltaPhi);
        // there might be rounding issues
        ratio = max(-1, min(1, ratio));
        return acos(ratio);
    }

    @Override
    protected short doGetCoordinateType() {
        return Location.SPHERIC_COORDINATE_TYPE;
    }

    private static class ValueHolder {
        final double phi;
        final double theta;
        final double radius;

        public ValueHolder(double phi, double theta, double radius) {
            this.phi = phi;
            this.theta = theta;
            this.radius = radius;
        }

        @Override
        public int hashCode() {
            return Objects.hash(
                    normalizeDouble(phi),
                    normalizeDouble(theta),
                    normalizeDouble(radius)
            );
        }

        @Override
        public boolean equals(Object o) {
            if ((!(o instanceof ValueHolder))) {
                return false;
            }
            if (o == this) {
                return true;
            }
            ValueHolder other = (ValueHolder) o;
            return compareDoublesNormalized(other.phi, phi) &&
                    compareDoublesNormalized(other.theta, theta) &&
                    compareDoublesNormalized(other.radius, radius);
        }
    }
}

