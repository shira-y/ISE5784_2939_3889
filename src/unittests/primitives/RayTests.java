package unittests.primitives;

import static org.junit.jupiter.api.Assertions.*;


import java.util.List;

import org.junit.jupiter.api.Test;

import primitives.*;

/**
 * Unit tests for the Ray class.
 */
class RayTests {

	/**
	 * Test the getPoint method for various distances: - Positive distance -
	 * Negative distance - Zero distance (boundary case)
	 */
	@Test
	void testGetPoint() {
		Point p0 = new Point(1, 2, 3);
		Vector v = new Vector(1, 0, 0);
		Ray ray = new Ray(p0, v);
		// ============ Equivalence Partitions Tests ==============
		// TC01: Positive distance
		assertEquals(new Point(6, 2, 3), ray.getPoint(5), "ERROR: getPoint() wrong result for positive distance");

		// Negative distance
		assertEquals(new Point(-4, 2, 3), ray.getPoint(-5), "ERROR: getPoint() wrong result for negative distance");
		// =============== Boundary Values Tests ==================
		// TC10: Zero distance
		assertEquals(new Point(1, 2, 3), ray.getPoint(0), "ERROR: getPoint() wrong result for zero distance");
	}

	/**
	 * Test method for {@link primitives.Ray#findClosestPoint(java.util.List)}.
	 */
	@Test
	void testFindClosestPoint() {
		// ============ Equivalence Partitions Tests ==============

		// TC01: Test case for middle point
		Ray ray = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));
		// we create a list of points where one of the points is positioned between two
		// other points.
		List<Point> points = List.of(new Point(2, 2, 0), new Point(1, 1, 0), new Point(3, 3, 0));
		Point closest = ray.findClosestPoint(points);
		assertEquals(new Point(1, 1, 0), closest, "The closest point should be the middle point in the list.");

		// =============== Boundary Values Tests ==================

		// TC10: Test case for empty list
		points = List.of();
		closest = ray.findClosestPoint(points);
		assertNull(closest, "The closest point should be null for an empty list.");

		// TC11: Test case for first point
		points = List.of(new Point(1, 1, 0), new Point(2, 2, 0), new Point(3, 3, 0));
		closest = ray.findClosestPoint(points);
		assertEquals(new Point(1, 1, 0), closest, "The closest point should be the first point in the list.");

		// TC12: Test case for last point
		points = List.of(new Point(3, 3, 0), new Point(2, 2, 0), new Point(1, 1, 0));
		closest = ray.findClosestPoint(points);
		assertEquals(new Point(1, 1, 0), closest, "The closest point should be the last point in the list.");
	}
}
