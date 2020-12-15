package org.wahlzeit.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class SphericCoordinateTest {
    public static final double DELTA = 1e-6;

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorPreconditions1() {
        new SphericCoordinate(Double.NaN, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorPreconditions2() {
        new SphericCoordinate(0, Double.POSITIVE_INFINITY, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorPreconditions3() {
        new SphericCoordinate(0, 0, -1);
    }
    
    @Test
    public void testGetter() {
        SphericCoordinate c = new SphericCoordinate(1, 2, 3);

        assertEquals(1, c.getPhi(), DELTA);
        assertEquals(2, c.getTheta(), DELTA);
        assertEquals(3, c.getRadius(), DELTA);
    }

    @Test
    public void testSetters() {
        SphericCoordinate c = new SphericCoordinate(1, 2, 3);

        c.setPhi(11);
        c.setTheta(12);
        c.setRadius(13);

        assertEquals(11, c.getPhi(), DELTA);
        assertEquals(12, c.getTheta(), DELTA);
        assertEquals(13, c.getRadius(), DELTA);

        assertTrue(c.isDirty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetterPreconditions1() {
        SphericCoordinate c = new SphericCoordinate(1, 2, 3);
        c.setPhi(Double.NaN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetterPreconditions2() {
        SphericCoordinate c = new SphericCoordinate(1, 2, 3);
        c.setTheta(Double.POSITIVE_INFINITY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetterPreconditions3() {
        SphericCoordinate c = new SphericCoordinate(1, 2, 3);
        c.setRadius(-1);
    }

    @Test
    public void testDistance() {
        SphericCoordinate a = new SphericCoordinate(0.33, 0.44, 100);
        SphericCoordinate b = new SphericCoordinate(0.33, 0.44, 101);

        double resultA = a.getCartesianDistance(b);
        double resultB = b.getCartesianDistance(a);
        double expected = 1;

        assertEquals(expected, resultA, DELTA);
        assertEquals(expected, resultB, DELTA);

        a = new SphericCoordinate(0, 0, 1);
        b = new SphericCoordinate(Math.toRadians(180), 0, 1);

        resultA = a.getCartesianDistance(b);
        resultB = b.getCartesianDistance(a);
        // GIMBAL LOCK !
        expected = 0;

        assertEquals(expected, resultA, DELTA);
        assertEquals(expected, resultB, DELTA);

        a = new SphericCoordinate(0, 0, 1);
        b = new SphericCoordinate(0, Math.toRadians(180), 1);

        resultA = a.getCartesianDistance(b);
        resultB = b.getCartesianDistance(a);
        expected = 2;

        assertEquals(expected, resultA, DELTA);
        assertEquals(expected, resultB, DELTA);
    }

    @Test
    public void testIsEqual() {
        SphericCoordinate c = new SphericCoordinate(1, 2, 3);

        assertTrue(c.isEqual(new SphericCoordinate(1, 2, 3)));
        assertEquals(c, new SphericCoordinate(1, 2, 3));
        assertTrue(c.isEqual(c));
        assertEquals(c, c);
        
        assertNotEquals(c, null);
        assertFalse(c.isEqual(new SphericCoordinate(1, 2, 0)));
        assertNotEquals(c, new SphericCoordinate(1, 2, 0));
        assertFalse(c.isEqual(new SphericCoordinate(1, 0, 2)));
        assertNotEquals(c, new SphericCoordinate(1, 0, 2));
        assertFalse(c.isEqual(new SphericCoordinate(0, 2, 2)));
        assertNotEquals(c, new SphericCoordinate(0, 2, 2));
        assertFalse(c.isEqual(new SphericCoordinate(-1, -2, 3)));
        assertNotEquals(c, new SphericCoordinate(-1, -2, 3));
    }

    @Test
    public void testDoubleComparison() {
        CartesianCoordinate c1 = new CartesianCoordinate(1.1, 2.2, 3.3);
        CartesianCoordinate c2 = new CartesianCoordinate(6.6 - 5.5, 22D / 10, 1.1 * 3);

        assertEquals(c1, c2);
        assertEquals(c2, c1);
        assertEquals(c1.hashCode(), c2.hashCode());

        c1 = new CartesianCoordinate(1D / 3, 1D / 5, 1D / 7);
        c2 = new CartesianCoordinate(100 - 299D / 3, 100 - 499D / 5, 100 - 699D / 7);

        assertEquals(c1, c2);
        assertEquals(c2, c1);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    public void testCartesianConversion() {
        CartesianCoordinate c1 = new CartesianCoordinate(1, 0, 0);
        SphericCoordinate c2 = new SphericCoordinate(0, Math.toRadians(90), 1);

        assertEquals(c1, c2);
        assertTrue(c1.isEqual(c2));
        assertEquals(c2, c1);
        assertTrue(c2.isEqual(c1));
        assertEquals(c1.hashCode(), c2.hashCode());

        c1 = new CartesianCoordinate(0, 1, 0);
        c2 = new SphericCoordinate(Math.toRadians(90), Math.toRadians(90), 1);

        assertEquals(c1, c2);
        assertTrue(c1.isEqual(c2));
        assertEquals(c2, c1);
        assertTrue(c2.isEqual(c1));

        c1 = new CartesianCoordinate(0, 0, 1);
        c2 = new SphericCoordinate(0, 0, 1);

        assertEquals(c1, c2);
        assertTrue(c1.isEqual(c2));
        assertEquals(c2, c1);
        assertTrue(c2.isEqual(c1));
    }

    @Test
    public void testCentralAngleSpheric() {
        SphericCoordinate c1 = new SphericCoordinate(0, Math.toRadians(90), 1);
        SphericCoordinate c2 = new SphericCoordinate(0, Math.toRadians(270), 1);

        assertEquals(Math.toRadians(180), c1.getCentralAngle(c2), DELTA);
        assertEquals(Math.toRadians(180), c2.getCentralAngle(c1), DELTA);

        c1 = new SphericCoordinate(0, 0, 10);
        c2 = new SphericCoordinate(0, Math.toRadians(180), 10);

        assertEquals(Math.toRadians(180), c1.getCentralAngle(c2), DELTA);
        assertEquals(Math.toRadians(180), c2.getCentralAngle(c1), DELTA);

        c1 = new SphericCoordinate(0, 0, 1);
        c2 = new SphericCoordinate(Math.toRadians(180), 0, 1);

        // GIMBAL LOCK !
        assertEquals(0, c1.getCentralAngle(c2), DELTA);
        assertEquals(0, c2.getCentralAngle(c1), DELTA);
    }

    @Test
    public void testCentralAngleCartesian() {
        CartesianCoordinate c1 = new CartesianCoordinate(1, 0, 0);
        CartesianCoordinate c2 = new CartesianCoordinate(0, 1, 0);

        assertEquals(Math.toRadians(90), c1.getCentralAngle(c2), DELTA);
        assertEquals(Math.toRadians(90), c2.getCentralAngle(c1), DELTA);

        c1 = new CartesianCoordinate(1, 1, 0);
        c2 = new CartesianCoordinate(0, 0, 1);

        assertEquals(Math.toRadians(90), c1.getCentralAngle(c2), DELTA);
        assertEquals(Math.toRadians(90), c2.getCentralAngle(c1), DELTA);

        for (int i = 0; i < 500; i++) {
            // x-y plane to z
            c1 = new CartesianCoordinate(Math.random(), Math.random(), 0);
            c2 = new CartesianCoordinate(0, 0, 1);

            assertEquals(Math.toRadians(90), c1.getCentralAngle(c2), DELTA);
            assertEquals(Math.toRadians(90), c2.getCentralAngle(c1), DELTA);

            // x-z plane to y
            c1 = new CartesianCoordinate(Math.random(), 0, Math.random());
            c2 = new CartesianCoordinate(0, 1, 0);

            assertEquals(Math.toRadians(90), c1.getCentralAngle(c2), DELTA);
            assertEquals(Math.toRadians(90), c2.getCentralAngle(c1), DELTA);

            // y-z plane to x
            c1 = new CartesianCoordinate(0, Math.random(), Math.random());
            c2 = new CartesianCoordinate(1, 0, 0);

            assertEquals(Math.toRadians(90), c1.getCentralAngle(c2), DELTA);
            assertEquals(Math.toRadians(90), c2.getCentralAngle(c1), DELTA);
        }

        c1 = new CartesianCoordinate(0, 0, -1);
        c2 = new CartesianCoordinate(0, 0, 1);

        assertEquals(Math.toRadians(180), c1.getCentralAngle(c2), DELTA);
        assertEquals(Math.toRadians(180), c2.getCentralAngle(c1), DELTA);

        for (int i = 0; i < 500; i++) {
            double x = Math.random(), y = Math.random(), z = Math.random();

            c1 = new CartesianCoordinate(x, -y, z);
            c2 = new CartesianCoordinate(-x, y, -z);

            assertEquals(Math.toRadians(180), c1.getCentralAngle(c2), DELTA);
            assertEquals(Math.toRadians(180), c2.getCentralAngle(c1), DELTA);
        }
    }
}
