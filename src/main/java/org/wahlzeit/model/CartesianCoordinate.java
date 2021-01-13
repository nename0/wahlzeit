package org.wahlzeit.model;

import org.wahlzeit.utils.Invariants;
import org.wahlzeit.utils.PatternInstance;
import org.wahlzeit.utils.Preconditions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@PatternInstance(
        patternName = "Value Object", 
        participants = {})
public class CartesianCoordinate extends AbstractCoordinate {
    // Because we are value objects hashCode() and equals() work on reference identity.
    // However for this cache we need to compare the actually attribute vales therefore we need the ValueHolder class
    private static final Map<ValueHolder, CartesianCoordinate> sharedObjectsCache = new HashMap<>();

    public static final CartesianCoordinate ORIGIN = get(0, 0, 0);
    
    private final ValueHolder valueHolder;

    private CartesianCoordinate(ValueHolder valueHolder) {
        this.valueHolder = valueHolder;
    }

    public static CartesianCoordinate get(double x, double y, double z) {
        Preconditions.assertScalar(x, "x must be scalar");
        Preconditions.assertScalar(y, "y must be scalar");
        Preconditions.assertScalar(z, "z must be scalar");
        ValueHolder valueHolder = new ValueHolder(x, y, z);
        return sharedObjectsCache.computeIfAbsent(valueHolder, CartesianCoordinate::new);
    }

    public static CartesianCoordinate getFromSQL(ResultSet rset) throws SQLException {
        double x = rset.getDouble(Location.COLUMN_NAME_PARAM_A);
        double y = rset.getDouble(Location.COLUMN_NAME_PARAM_B);
        double z = rset.getDouble(Location.COLUMN_NAME_PARAM_C);
        Preconditions.assertScalar(x, "x not scalar in database");
        Preconditions.assertScalar(y, "y not scalar in database");
        Preconditions.assertScalar(z, "z not scalar in database");
        return get(x, y, z);
    }

    @Override
    protected void assertClassInvariants() {
        Invariants.assertScalar(valueHolder.x, "x not scalar");
        Invariants.assertScalar(valueHolder.y, "y not scalar");
        Invariants.assertScalar(valueHolder.z, "z not scalar");
    }

    public double getX() {
        return valueHolder.x;
    }

    public double getY() {
        return valueHolder.y;
    }

    public double getZ() {
        return valueHolder.z;
    }

    public double getDistance(CartesianCoordinate distanceTo) {
        double dx = valueHolder.x - distanceTo.valueHolder.x;
        double dy = valueHolder.y - distanceTo.valueHolder.y;
        double dz = valueHolder.z - distanceTo.valueHolder.z;
        return Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
    }

    @Override
    protected void doWriteOn(ResultSet rset) throws SQLException {
        rset.updateDouble(Location.COLUMN_NAME_PARAM_A, this.valueHolder.x);
        rset.updateDouble(Location.COLUMN_NAME_PARAM_B, this.valueHolder.y);
        rset.updateDouble(Location.COLUMN_NAME_PARAM_C, this.valueHolder.z);
    }

    @Override
    protected CartesianCoordinate doGetAsCartesianCoordinate() {
        return this;
    }

    @Override
    protected SphericCoordinate doGetAsSphericCoordinate() {
        double radius = getDistance(ORIGIN);
        if (radius == 0) {
            return SphericCoordinate.get(0,0,0);
        }
        double phi = Math.atan2(valueHolder.y, valueHolder.x);
        double theta = Math.atan2(Math.sqrt(valueHolder.x * valueHolder.x + valueHolder.y * valueHolder.y), valueHolder.z);
        return SphericCoordinate.get(phi, theta, radius);
    }

    @Override
    protected double doGetCartesianDistance(Coordinate other) {
        return this.getDistance(other.asCartesianCoordinate());
    }

    @Override
    protected short doGetCoordinateType() {
        return Location.CARTESIAN_COORDINATE_TYPE;
    }

    // Because we are value objects hashCode() and equals() work on reference identity.
    // However for the sharedCache we need to compare the actually attribute vales therefore we need this ValueHolder class
    private static class ValueHolder {
        final double x;
        final double y;
        final double z;

        public ValueHolder(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
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
            if ((!(o instanceof ValueHolder))) {
                return false;
            }
            if (o == this) {
                return true;
            }
            ValueHolder other = (ValueHolder) o;
            return compareDoublesNormalized(other.x, x) &&
                    compareDoublesNormalized(other.y, y) &&
                    compareDoublesNormalized(other.z, z);
        }
    }
}
