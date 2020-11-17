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
        Location l = new Location(new Coordinate(0,0,0));
        
        l.setCoordinate(null);
    }
    
    @Test
    public void testGetter() {
        Coordinate c = new Coordinate(1, 2, 3);
        Location l = new Location(c); 
        
        assertEquals(c, l.getCoordinate());
    }
    
    @Test
    public void testSetter() {
        Coordinate c1 = new Coordinate(1, 2, 3);
        Coordinate c2 = new Coordinate(4, 5, 6);
        Location l = new Location(c1);
        
        l.setCoordinate(c2);

        assertNotEquals(c1, l.getCoordinate());
        assertEquals(c2, l.getCoordinate());
        
        assertTrue(l.isDirty());
    }
    
    @Test
    public void testType() {
        Coordinate c = new Coordinate(1, 2, 3);
        Location l = new Location(c);
        
        assertEquals(Location.CARTESIAN_COORDINATE_TYPE, l.getCoordinateType());
    }
    
    @Test
    public void testDirtyReset() {
        Coordinate c = new Coordinate(1, 2, 3);
        Location l = new Location(c);
        
        c.setX(4);

        assertTrue(l.isDirty()); 
        
        l.resetWriteCount();
        
        assertFalse(l.isDirty());
    }
}
