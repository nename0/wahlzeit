package org.wahlzeit.model;

import org.wahlzeit.services.Persistent;

public interface Coordinate extends Persistent {
    CartesianCoordinate asCartesianCoordinate();
    
    SphericCoordinate asSphericCoordinate();
    
    double getCartesianDistance(Coordinate other);
    
    double getCentralAngle(Coordinate other);
    
    boolean isEqual(Coordinate other);
    
    short getCoordinateType();
}
