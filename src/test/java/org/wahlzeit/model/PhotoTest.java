package org.wahlzeit.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class PhotoTest {

    @Test
    public void testLocationGetterSetter() {
        CartesianCoordinate c = new CartesianCoordinate(1, 2, 3);
        Location l = new Location(c);
        Photo p = new Photo();
        
        assertNull(p.getLocation());
        
        p.setLocation(l);
        
        assertEquals(l, p.getLocation());
        
        assertTrue(p.isDirty());
    }
    
    @Test
    public void testDirtyLocationReset() {
        CartesianCoordinate c1 = new CartesianCoordinate(1, 2, 3);
        CartesianCoordinate c2 = new CartesianCoordinate(3, 4, 5);
        Location l = new Location(c1);
        Photo p = new Photo();
        p.setLocation(l);
        p.resetWriteCount();

        l.setCoordinate(c2);

        assertTrue(p.isDirty());

        p.resetWriteCount();

        assertFalse(p.isDirty());
    }

    @Test
    public void testDirtyCoordinateReset() {
        CartesianCoordinate c = new CartesianCoordinate(1, 2, 3);
        Location l = new Location(c);
        Photo p = new Photo();
        p.setLocation(l);
        p.resetWriteCount();

        c.setX(4);

        assertTrue(p.isDirty());

        p.resetWriteCount();

        assertFalse(p.isDirty());
    }
}
