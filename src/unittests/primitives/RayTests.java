package unittests.primitives;

import static org.junit.jupiter.api.Assertions.*;

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
		// Positive distance
		assertEquals(new Point(6, 2, 3), ray.getPoint(5), "ERROR: getPoint() wrong result for positive distance");

		// Negative distance
		assertEquals(new Point(-4, 2, 3), ray.getPoint(-5), "ERROR: getPoint() wrong result for negative distance");
		// =============== Boundary Values Tests ==================
		// Zero distance
		assertEquals(new Point(1, 2, 3), ray.getPoint(0), "ERROR: getPoint() wrong result for zero distance");
	}
}
