package unittests.primitives;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import primitives.Point;
import primitives.Vector;

/**
 * Unit tests for primitives.Point class
 */
class PointTests {

	/**
	 * The allowed difference between 2 values
	 */
	private final double DELTA = 0.000001;
	/**
	 * Define vector for testing
	 */
	private static final Vector v1 = new Vector(1, 2, 3);

	/**
	 * Test method for {@link primitives.Point#subtract(primitives.Point)}.
	 */
	@Test
	void testSubtract() {
		Point p1 = new Point(1, 2, 3);
		Point p2 = new Point(4, 5, 6);
		// ============ Equivalence Partitions Tests ==============
		// TC01: A test that checks that the subtraction of the points is correct
		assertEquals(new Vector(-3, -3, -3), p1.subtract(p2), "ERROR: Point - Point does not work correctly");

		// =============== Boundary Values Tests ==================
		// TC11:A test that tests subtraction from the same point
		assertThrows(IllegalArgumentException.class, () -> p1.subtract(p1),
				"Subtracting a point from a proper point must throw an exception");
	}

	/**
	 * Test method for {@link primitives.Point#add(primitives.Vector)}.
	 */
	@Test
	void testAdd() {
		// ============ Equivalence Partitions Tests ==============
		// TC01: A test that checks that the connection of the points is correct
		Point p1 = new Point(1, 2, 3);
		assertEquals(new Point(2, 4, 6), p1.add(v1), "ERROR: Connection of point and vector is incorrect");
	}

	/**
	 * Test method for {@link primitives.Point#distanceSquared(primitives.Point)}.
	 */
	@Test
	void testDistanceSquared() {
		// ============ Equivalence Partitions Tests ==============
		// TC01: A test that checks that the squared distance between the points is
		// correct
		Point p1 = new Point(1, 2, 3);
		assertEquals(2, p1.distanceSquared(new Point(1, 3, 4)),
				"Error: The squared distance between the points is incorrect");

		// ============ Equivalence Partitions Tests ==============
		// TC11: Test distanceSquared with the same point
		assertEquals(0, p1.distance(p1), DELTA, "worng distanceSquared between the point and itself");
	}

	/**
	 * Test method for {@link primitives.Point#distance(primitives.Point)}.
	 */
	@Test
	void testDistance() {
		// ============ Equivalence Partitions Tests ==============
		// TC01: A test that checks that the distance between the points is correct
		Point p1 = new Point(1, 2, 0);
		assertEquals(1, p1.distance(new Point(1, 3, 0)), "Error: The distance between the points is incorrect.");

		// ============ Equivalence Partitions Tests ==============
		// TC11: Test distance with the same point
		assertEquals(0, p1.distance(p1), DELTA, "worng distance between the point and itself");
	}

}