package org.wahlzeit.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class SphericCoordinateTest {
    public static final double DELTA = 1e-6;

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorPreconditions1() {
        SphericCoordinate.get(Double.NaN, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorPreconditions2() {
        SphericCoordinate.get(0, Double.POSITIVE_INFINITY, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorPreconditions3() {
        SphericCoordinate.get(0, 0, -1);
    }
    
    @Test
    public void testGetter() {
        SphericCoordinate c = SphericCoordinate.get(1, 2, 3);

        assertEquals(1, c.getPhi(), DELTA);
        assertEquals(2, c.getTheta(), DELTA);
        assertEquals(3, c.getRadius(), DELTA);
    }

    @Test
    public void testDistance() {
        SphericCoordinate a = SphericCoordinate.get(0.33, 0.44, 100);
        SphericCoordinate b = SphericCoordinate.get(0.33, 0.44, 101);

        double resultA = a.getCartesianDistance(b);
        double resultB = b.getCartesianDistance(a);
        double expected = 1;

        assertEquals(expected, resultA, DELTA);
        assertEquals(expected, resultB, DELTA);

        a = SphericCoordinate.get(0, 0, 1);
        b = SphericCoordinate.get(Math.toRadians(180), 0, 1);

        resultA = a.getCartesianDistance(b);
        resultB = b.getCartesianDistance(a);
        // GIMBAL LOCK !
        expected = 0;

        assertEquals(expected, resultA, DELTA);
        assertEquals(expected, resultB, DELTA);

        a = SphericCoordinate.get(0, 0, 1);
        b = SphericCoordinate.get(0, Math.toRadians(180), 1);

        resultA = a.getCartesianDistance(b);
        resultB = b.getCartesianDistance(a);
        expected = 2;

        assertEquals(expected, resultA, DELTA);
        assertEquals(expected, resultB, DELTA);
    }

    @Test
    public void testIsEqual() {
        SphericCoordinate c = SphericCoordinate.get(1, 2, 3);

        assertTrue(c.isEqual(SphericCoordinate.get(1, 2, 3)));
        assertSame(c, SphericCoordinate.get(1, 2, 3));
        assertTrue(c.isEqual(c));
        assertEquals(c, c);
        
        assertNotSame(c, null);
        assertFalse(c.isEqual(SphericCoordinate.get(1, 2, 0)));
        assertNotSame(c, SphericCoordinate.get(1, 2, 0));
        assertFalse(c.isEqual(SphericCoordinate.get(1, 0, 2)));
        assertNotSame(c, SphericCoordinate.get(1, 0, 2));
        assertFalse(c.isEqual(SphericCoordinate.get(0, 2, 2)));
        assertNotSame(c, SphericCoordinate.get(0, 2, 2));
        assertFalse(c.isEqual(SphericCoordinate.get(-1, -2, 3)));
        assertNotSame(c, SphericCoordinate.get(-1, -2, 3));
    }

    @Test
    public void testDoubleComparison() {
        CartesianCoordinate c1 = CartesianCoordinate.get(1.1, 2.2, 3.3);
        CartesianCoordinate c2 = CartesianCoordinate.get(6.6 - 5.5, 22D / 10, 1.1 * 3);

        assertSame(c1, c2);
        assertSame(c2, c1);
        assertEquals(c1.hashCode(), c2.hashCode());

        c1 = CartesianCoordinate.get(1D / 3, 1D / 5, 1D / 7);
        c2 = CartesianCoordinate.get(100 - 299D / 3, 100 - 499D / 5, 100 - 699D / 7);

        assertSame(c1, c2);
        assertSame(c2, c1);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    public void testCartesianConversion() {
        CartesianCoordinate c1 = CartesianCoordinate.get(1, 0, 0);
        SphericCoordinate c2 = SphericCoordinate.get(0, Math.toRadians(90), 1);

        assertEquals(c1, c2);
        assertTrue(c1.isEqual(c2));
        assertEquals(c2, c1);
        assertTrue(c2.isEqual(c1));
        assertEquals(c1.hashCode(), c2.hashCode());

        c1 = CartesianCoordinate.get(0, 1, 0);
        c2 = SphericCoordinate.get(Math.toRadians(90), Math.toRadians(90), 1);

        assertEquals(c1, c2);
        assertTrue(c1.isEqual(c2));
        assertEquals(c2, c1);
        assertTrue(c2.isEqual(c1));
        assertEquals(c1.hashCode(), c2.hashCode());

        c1 = CartesianCoordinate.get(0, 0, 1);
        c2 = SphericCoordinate.get(0, 0, 1);

        assertEquals(c1, c2);
        assertTrue(c1.isEqual(c2));
        assertEquals(c2, c1);
        assertTrue(c2.isEqual(c1));
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    public void testCentralAngleSpheric() {
        SphericCoordinate c1 = SphericCoordinate.get(0, Math.toRadians(90), 1);
        SphericCoordinate c2 = SphericCoordinate.get(0, Math.toRadians(270), 1);

        assertEquals(Math.toRadians(180), c1.getCentralAngle(c2), DELTA);
        assertEquals(Math.toRadians(180), c2.getCentralAngle(c1), DELTA);

        c1 = SphericCoordinate.get(0, 0, 10);
        c2 = SphericCoordinate.get(0, Math.toRadians(180), 10);

        assertEquals(Math.toRadians(180), c1.getCentralAngle(c2), DELTA);
        assertEquals(Math.toRadians(180), c2.getCentralAngle(c1), DELTA);

        c1 = SphericCoordinate.get(0, 0, 1);
        c2 = SphericCoordinate.get(Math.toRadians(180), 0, 1);

        // GIMBAL LOCK !
        assertEquals(0, c1.getCentralAngle(c2), DELTA);
        assertEquals(0, c2.getCentralAngle(c1), DELTA);
    }

    @Test
    public void testCentralAngleCartesian() {
        CartesianCoordinate c1 = CartesianCoordinate.get(1, 0, 0);
        CartesianCoordinate c2 = CartesianCoordinate.get(0, 1, 0);

        assertEquals(Math.toRadians(90), c1.getCentralAngle(c2), DELTA);
        assertEquals(Math.toRadians(90), c2.getCentralAngle(c1), DELTA);

        c1 = CartesianCoordinate.get(1, 1, 0);
        c2 = CartesianCoordinate.get(0, 0, 1);

        assertEquals(Math.toRadians(90), c1.getCentralAngle(c2), DELTA);
        assertEquals(Math.toRadians(90), c2.getCentralAngle(c1), DELTA);

        for (int i = 0; i < 500; i++) {
            // x-y plane to z
            c1 = CartesianCoordinate.get(Math.random(), Math.random(), 0);
            c2 = CartesianCoordinate.get(0, 0, 1);

            assertEquals(Math.toRadians(90), c1.getCentralAngle(c2), DELTA);
            assertEquals(Math.toRadians(90), c2.getCentralAngle(c1), DELTA);

            // x-z plane to y
            c1 = CartesianCoordinate.get(Math.random(), 0, Math.random());
            c2 = CartesianCoordinate.get(0, 1, 0);

            assertEquals(Math.toRadians(90), c1.getCentralAngle(c2), DELTA);
            assertEquals(Math.toRadians(90), c2.getCentralAngle(c1), DELTA);

            // y-z plane to x
            c1 = CartesianCoordinate.get(0, Math.random(), Math.random());
            c2 = CartesianCoordinate.get(1, 0, 0);

            assertEquals(Math.toRadians(90), c1.getCentralAngle(c2), DELTA);
            assertEquals(Math.toRadians(90), c2.getCentralAngle(c1), DELTA);
        }

        c1 = CartesianCoordinate.get(0, 0, -1);
        c2 = CartesianCoordinate.get(0, 0, 1);

        assertEquals(Math.toRadians(180), c1.getCentralAngle(c2), DELTA);
        assertEquals(Math.toRadians(180), c2.getCentralAngle(c1), DELTA);

        for (int i = 0; i < 500; i++) {
            double x = Math.random(), y = Math.random(), z = Math.random();

            c1 = CartesianCoordinate.get(x, -y, z);
            c2 = CartesianCoordinate.get(-x, y, -z);

            assertEquals(Math.toRadians(180), c1.getCentralAngle(c2), DELTA);
            assertEquals(Math.toRadians(180), c2.getCentralAngle(c1), DELTA);
        }
    }
}
