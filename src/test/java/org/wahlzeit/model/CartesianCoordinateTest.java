package org.wahlzeit.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class CartesianCoordinateTest {
    public static final double DELTA = 1e-6;

    @Test(expected = IllegalStateException.class)
    public void testConstructorPreconditions1() {
        new CartesianCoordinate(Double.NaN, 0, 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructorPreconditions2() {
        new CartesianCoordinate(0, Double.POSITIVE_INFINITY, 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructorPreconditions3() {
        new CartesianCoordinate(0, 0, Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testGetter() {
        CartesianCoordinate c = new CartesianCoordinate(1, 2, 3);

        assertEquals(1, c.getX(), DELTA);
        assertEquals(2, c.getY(), DELTA);
        assertEquals(3, c.getZ(), DELTA);
    }

    @Test
    public void testSetters() {
        CartesianCoordinate c = new CartesianCoordinate(1, 2, 3);

        c.setX(11);
        c.setY(12);
        c.setZ(13);

        assertEquals(11, c.getX(), DELTA);
        assertEquals(12, c.getY(), DELTA);
        assertEquals(13, c.getZ(), DELTA);

        assertTrue(c.isDirty());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetterPreconditions1() {
        CartesianCoordinate c = new CartesianCoordinate(1, 2, 3);
        c.setX(Double.NaN);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetterPreconditions2() {
        CartesianCoordinate c = new CartesianCoordinate(1, 2, 3);
        c.setY(Double.POSITIVE_INFINITY);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetterPreconditions3() {
        CartesianCoordinate c = new CartesianCoordinate(1, 2, 3);
        c.setZ(Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testDistance1() {
        CartesianCoordinate a = new CartesianCoordinate(100, 100, 100);
        CartesianCoordinate b = new CartesianCoordinate(100, 101, 101);

        double resultA = a.getDistance(b);
        double resultB = b.getDistance(a);
        double expected = Math.sqrt(2);

        assertEquals(expected, resultA, DELTA);
        assertEquals(expected, resultB, DELTA);
    }

    @Test
    public void testDistance2() {
        CartesianCoordinate a = new CartesianCoordinate(0, 0, 100);
        CartesianCoordinate b = new CartesianCoordinate(0, 0, -100);

        double resultA = a.getDistance(b);
        double resultB = b.getDistance(a);
        double expected = 200;

        assertEquals(expected, resultA, DELTA);
        assertEquals(expected, resultB, DELTA);
    }

    @Test
    public void testIsEqual() {
        CartesianCoordinate c = new CartesianCoordinate(1, 2, 3);

        assertTrue(c.isEqual(new CartesianCoordinate(1, 2, 3)));
        assertTrue(c.isEqual(c));

        assertFalse(c.isEqual(new CartesianCoordinate(1, 2, 0)));
        assertFalse(c.isEqual(new CartesianCoordinate(1, 0, 2)));
        assertFalse(c.isEqual(new CartesianCoordinate(0, 2, 2)));
        assertFalse(c.isEqual(new CartesianCoordinate(-1, -2, -3)));
    }

    @Test
    public void testEquals() {
        CartesianCoordinate c = new CartesianCoordinate(1, 2, 3);

        assertEquals(new CartesianCoordinate(1, 2, 3), c);

        assertNotEquals(null, c);
        assertNotEquals(new CartesianCoordinate(1, 2, -3), c);
        assertNotEquals(new CartesianCoordinate(1, -2, 3), c);
        assertNotEquals(new CartesianCoordinate(-1, 2, 3), c);
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

    void assertRadiansEq(double expected, double actual) {
        // -90° needs to be equal 270° 
        expected = (expected + Math.toRadians(360)) % Math.toRadians(360);
        actual = (actual + Math.toRadians(360)) % Math.toRadians(360);
        assertEquals(expected, actual, DELTA);
    }

    @Test
    public void testSphericConversion() {
        CartesianCoordinate c1 = new CartesianCoordinate(1, 0, 0);
        SphericCoordinate asSpheric = c1.asSphericCoordinate();

        // [0, 90°, 1]
        assertRadiansEq(0, asSpheric.getPhi());
        assertRadiansEq(Math.toRadians(90), asSpheric.getTheta());
        assertEquals(asSpheric.getRadius(), 1, DELTA);

        c1 = new CartesianCoordinate(-1, 0, 0);
        asSpheric = c1.asSphericCoordinate();

        // [180°, 90°, 1]
        assertRadiansEq(Math.toRadians(180), asSpheric.getPhi());
        assertRadiansEq(Math.toRadians(90), asSpheric.getTheta());
        assertEquals(asSpheric.getRadius(), 1, DELTA);

        c1 = new CartesianCoordinate(0, 2, 0);
        asSpheric = c1.asSphericCoordinate();

        // [90°, 90°, 2]
        assertRadiansEq(Math.toRadians(90), asSpheric.getPhi());
        assertRadiansEq(Math.toRadians(90), asSpheric.getTheta());
        assertEquals(asSpheric.getRadius(), 2, DELTA);

        c1 = new CartesianCoordinate(0, -2, 0);
        asSpheric = c1.asSphericCoordinate();

        // [270°, 90°, 2]
        assertRadiansEq(Math.toRadians(270), asSpheric.getPhi());
        assertRadiansEq(Math.toRadians(90), asSpheric.getTheta());
        assertEquals(asSpheric.getRadius(), 2, DELTA);

        c1 = new CartesianCoordinate(0, 0, 3);
        asSpheric = c1.asSphericCoordinate();

        // [0°, 0°, 3]
        assertRadiansEq(0, asSpheric.getPhi());
        assertRadiansEq(0, asSpheric.getTheta());
        assertEquals(asSpheric.getRadius(), 3, DELTA);

        c1 = new CartesianCoordinate(0, 0, -3);
        asSpheric = c1.asSphericCoordinate();

        // [0°, 180°, 3]
        assertRadiansEq(0, asSpheric.getPhi());
        assertRadiansEq(Math.toRadians(180), asSpheric.getTheta());
        assertEquals(asSpheric.getRadius(), 3, DELTA);
    }

    @Test
    public void testCartesianDistance() {
        CartesianCoordinate c1 = new CartesianCoordinate(1, 0, 0);
        CartesianCoordinate c2 = new CartesianCoordinate(0, 1, 0);

        assertEquals(Math.sqrt(2), c1.getCartesianDistance(c2.asSphericCoordinate()), DELTA);
        assertEquals(Math.sqrt(2), c2.getCartesianDistance(c1.asSphericCoordinate()), DELTA);

        c1 = new CartesianCoordinate(1, 10, 100);
        c2 = new CartesianCoordinate(200, 20, 2);

        assertEquals(c1.getDistance(c2), c1.getCartesianDistance(c2.asSphericCoordinate()), DELTA);
        assertEquals(c2.getDistance(c1), c2.getCartesianDistance(c1.asSphericCoordinate()), DELTA);
    }
}
