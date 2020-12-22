package org.wahlzeit.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class LocationTest {
    
    @Test(expected = IllegalArgumentException.class)
    public void testNullCoordinateConstructor() {
        new Location(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullCoordinateSetter() {
        Location l = new Location(CartesianCoordinate.get(0,0,0));
        
        l.setCoordinate(null);
    }
    
    @Test
    public void testGetter() {
        CartesianCoordinate c = CartesianCoordinate.get(1, 2, 3);
        Location l = new Location(c); 
        
        assertEquals(c, l.getCoordinate());
    }
    
    @Test
    public void testSetter() {
        CartesianCoordinate c1 = CartesianCoordinate.get(1, 2, 3);
        CartesianCoordinate c2 = CartesianCoordinate.get(4, 5, 6);
        Location l = new Location(c1);
        
        l.setCoordinate(c2);

        assertNotEquals(c1, l.getCoordinate());
        assertEquals(c2, l.getCoordinate());
        
        assertTrue(l.isDirty());
    }
    
    @Test
    public void testType() {
        CartesianCoordinate c = CartesianCoordinate.get(1, 2, 3);
        Location l = new Location(c);
        
        assertEquals(Location.CARTESIAN_COORDINATE_TYPE, l.getCoordinateType());
    }
}
