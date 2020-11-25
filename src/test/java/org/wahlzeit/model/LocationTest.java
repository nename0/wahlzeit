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
        Location l = new Location(new CartesianCoordinate(0,0,0));
        
        l.setCoordinate(null);
    }
    
    @Test
    public void testGetter() {
        CartesianCoordinate c = new CartesianCoordinate(1, 2, 3);
        Location l = new Location(c); 
        
        assertEquals(c, l.getCoordinate());
    }
    
    @Test
    public void testSetter() {
        CartesianCoordinate c1 = new CartesianCoordinate(1, 2, 3);
        CartesianCoordinate c2 = new CartesianCoordinate(4, 5, 6);
        Location l = new Location(c1);
        
        l.setCoordinate(c2);

        assertNotEquals(c1, l.getCoordinate());
        assertEquals(c2, l.getCoordinate());
        
        assertTrue(l.isDirty());
    }
    
    @Test
    public void testType() {
        CartesianCoordinate c = new CartesianCoordinate(1, 2, 3);
        Location l = new Location(c);
        
        assertEquals(Location.CARTESIAN_COORDINATE_TYPE, l.getCoordinateType());
    }
    
    @Test
    public void testDirtyReset() {
        CartesianCoordinate c = new CartesianCoordinate(1, 2, 3);
        Location l = new Location(c);
        
        c.setX(4);

        assertTrue(l.isDirty()); 
        
        l.resetWriteCount();
        
        assertFalse(l.isDirty());
    }
}
