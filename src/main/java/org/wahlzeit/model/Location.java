package org.wahlzeit.model;

public class Location {
    public static final short NONE_COORDINATE_TYPE = 0;
    public static final short CARTESIAN_COORDINATE_TYPE = 1;
    
    private final Coordinate coordinate;

    public Location(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    /**
     * Create Location from Database
     * @param coordinateType The Type of the Coordinate from the Database
     * @param paramA The first Parameter from the Database
     * @param paramB The second Parameter from the Database
     * @param paramC The thrid Parameter from the Database
     */
    public Location(short coordinateType, double paramA, double paramB, double paramC) {
        if (coordinateType == CARTESIAN_COORDINATE_TYPE) {
            this.coordinate = new Coordinate(paramA, paramB, paramC);
        } else {
            throw new IllegalArgumentException("Unknown Coordinate Type");
        }
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public short getCoordinateType() {
        return CARTESIAN_COORDINATE_TYPE;
    }

    public double getParameterAForDB(short coordinateType) {
        if (coordinateType == CARTESIAN_COORDINATE_TYPE) {
            return this.coordinate.getX();
        }
        throw new IllegalArgumentException("Unknown Coordinate Type");
    }

    public double getParameterBForDB(short coordinateType) {
        if (coordinateType == CARTESIAN_COORDINATE_TYPE) {
            return this.coordinate.getY();
        }
        throw new IllegalArgumentException("Unknown Coordinate Type");
    }

    public double getParameterCForDB(short coordinateType) {
        if (coordinateType == CARTESIAN_COORDINATE_TYPE) {
            return this.coordinate.getZ();
        }
        throw new IllegalArgumentException("Unknown Coordinate Type");
    }
}
