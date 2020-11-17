package org.wahlzeit.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class CoordinateTest {
    public static final double DELTA = 1e-6;

    @Test
    public void testGetter() {
        Coordinate c = new Coordinate(1, 2, 3);

        assertEquals(1, c.getX(), DELTA);
        assertEquals(2, c.getY(), DELTA);
        assertEquals(3, c.getZ(), DELTA);
    }

    @Test
    public void testSetters() {
        Coordinate c = new Coordinate(1, 2, 3);

        c.setX(11);
        c.setY(12);
        c.setZ(13);

        assertEquals(11, c.getX(), DELTA);
        assertEquals(12, c.getY(), DELTA);
        assertEquals(13, c.getZ(), DELTA);

        assertTrue(c.isDirty());
    }

    @Test
    public void testDistance1() {
        Coordinate a = new Coordinate(100, 100, 100);
        Coordinate b = new Coordinate(100, 101, 101);

        double resultA = a.getDistance(b);
        double resultB = b.getDistance(a);
        double expected = Math.sqrt(2);

        assertEquals(expected, resultA, DELTA);
        assertEquals(expected, resultB, DELTA);
    }

    @Test
    public void testDistance2() {
        Coordinate a = new Coordinate(0, 0, 100);
        Coordinate b = new Coordinate(0, 0, -100);

        double resultA = a.getDistance(b);
        double resultB = b.getDistance(a);
        double expected = 200;

        assertEquals(expected, resultA, DELTA);
        assertEquals(expected, resultB, DELTA);
    }

    @Test
    public void testIsEqual() {
        Coordinate c = new Coordinate(1, 2, 3);

        assertTrue(c.isEqual(new Coordinate(1, 2, 3)));
        assertTrue(c.isEqual(c));

        assertFalse(c.isEqual(null));
        assertFalse(c.isEqual(new Coordinate(1, 2, 0)));
        assertFalse(c.isEqual(new Coordinate(1, 0, 2)));
        assertFalse(c.isEqual(new Coordinate(0, 2, 2)));
        assertFalse(c.isEqual(new Coordinate(-1, -2, -3)));
    }

    @Test
    public void testEquals() {
        Coordinate c = new Coordinate(1, 2, 3);

        assertEquals(new Coordinate(1, 2, 3), c);

        assertNotEquals(null, c);
        assertNotEquals(new Coordinate(1, 2, -3), c);
        assertNotEquals(new Coordinate(1, -2, 3), c);
        assertNotEquals(new Coordinate(-1, 2, 3), c);
    }

    @Test
    public void testDoubleComparison() {
        Coordinate c1 = new Coordinate(1.1, 2.2, 3.3);
        Coordinate c2 = new Coordinate(6.6 - 5.5, 22D / 10, 1.1 * 3);

        assertEquals(c1, c2);
        assertEquals(c2, c1);
        assertEquals(c1.hashCode(), c2.hashCode());

        c1 = new Coordinate(1D / 3, 1D / 5, 1D / 7);
        c2 = new Coordinate(100 - 299D / 3, 100 - 499D / 5, 100 - 699D / 7);

        assertEquals(c1, c2);
        assertEquals(c2, c1);
        assertEquals(c1.hashCode(), c2.hashCode());
    }
}
