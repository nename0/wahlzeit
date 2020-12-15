package org.wahlzeit.model;

import org.wahlzeit.services.DataObject;
import org.wahlzeit.utils.Preconditions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Location extends DataObject {
    public static final short CARTESIAN_COORDINATE_TYPE = 1;
    public static final short SPHERIC_COORDINATE_TYPE = 2;
                              
    public static final String COLUMN_NAME_TYPE = "location_coordinate_type";
    public static final String COLUMN_NAME_PARAM_A = "location_coordinate_a";
    public static final String COLUMN_NAME_PARAM_B = "location_coordinate_b";
    public static final String COLUMN_NAME_PARAM_C = "location_coordinate_c";

    private Coordinate coordinate;

    public Location(Coordinate coordinate) {
        if (coordinate == null) {
            throw new IllegalArgumentException("coordinate must be non-null");
        }
        this.coordinate = coordinate;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        if (coordinate == null) {
            throw new IllegalArgumentException("coordinate must be non-null");
        }
        this.coordinate = coordinate;
        incWriteCount();
    }

    public short getCoordinateType() {
        return coordinate.getCoordinateType();
    }

    @Override
    public boolean isDirty() {
        boolean selfDirty = super.isDirty();
        boolean coordinateDirty = coordinate.isDirty();

        return selfDirty || coordinateDirty;
    }

    @Override
    public void resetWriteCount() {
        super.resetWriteCount();
        coordinate.resetWriteCount();
    }

    @Override
    public void readFrom(ResultSet rset) throws SQLException {
        short coordinateType = rset.getShort(COLUMN_NAME_TYPE);
        if (coordinateType == CARTESIAN_COORDINATE_TYPE) {
            coordinate = new CartesianCoordinate(0, 0, 0);
        } else if (coordinateType == SPHERIC_COORDINATE_TYPE) {
            coordinate = new SphericCoordinate(0,0,0);
        } else {
            Preconditions.fail("Unknown coordinate type: " + coordinateType);
        }
        coordinate.readFrom(rset);
    }

    @Override
    public void writeOn(ResultSet rset) throws SQLException {
        rset.updateShort("location_coordinate_type", getCoordinateType());
        coordinate.writeOn(rset);
    }
    
    @Override
    public String getIdAsString() {
        throw new UnsupportedOperationException("Location is not stored in a separate table. So no ids");
    }

    @Override
    public void writeId(PreparedStatement stmt, int pos) throws SQLException {
        throw new UnsupportedOperationException("Location is not stored in a separate table. So no ids");
    }
}
