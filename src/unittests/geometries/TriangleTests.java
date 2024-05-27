/**
 * 
 */
package unittests.geometries;

import primitives.*;
import geometries.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for geometries.Triangle class
 */
class TriangleTests {

	/**
	 * Test method for {@link geometries.Triangle#getNormal(primitives.Point)}.
	 */
	@Test
	void testGetNormal() {
		// ========== Equivalence Partitions tests ==========

		// TC01: Assert that the computed normal matches the expected normal
		Point p1 = new Point(0, 0, 0);
		Point p2 = new Point(1, 0, 0);
		Point p3 = new Point(0, 1, 0);

		Triangle triangle = new Triangle(p1, p2, p3);

		Vector normal = triangle.getNormal(new Point(0, 0, 0));

		Vector expectedNormal = new Vector(0, 0, 1).normalize();

		assertEquals(expectedNormal, normal, "getNormal did not return the expected normal vector");

	}
}