/**
 * 
 */
package unittests.geometries;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import geometries.*;
import primitives.*;

/**
 * Unit tests for geometries.Plane class
 */
class PlaneTests {

	/**
	 * Test method for
	 * {@link geometries.Plane#Plane(primitives.Point, primitives.Point, primitives.Point)}.
	 */
	@Test
	void testConstructor() {

		// =========== Boundary Values Tests ===========
		// TC11: two points are equal
		assertThrows(IllegalArgumentException.class,
				() -> new Plane(new Point(0, 0, 1), new Point(0, 0, 1), new Point(0, 0, 2)),
				"constructed Plane with equal points");

		// TC12: points are all on the same line
		assertThrows(IllegalArgumentException.class,
				() -> new Plane(new Point(0, 0, 1), new Point(0, 0, 3), new Point(0, 0, 2)),
				"constructed plane must have Vectors in different directions");

	}

	/**
	 * Test method for {@link geometries.Plane#getNormal(primitives.Point)}.
	 */
	@Test
	void getNormal() {
		// ========== Equivalence Partitions tests ==========
		// TC01: test if normal vector is correct
		Plane plane = new Plane(new Point(0, 0, 0), new Point(0, 5, 0), new Point(5, 0, 0));
		Vector expectedNormal = new Vector(0, 0, 1).normalize();
		Vector normal = plane.getNormal(new Point(1, 1, 0));

		// Assert that the normal is either the expected normal or its inverse
		assertTrue(expectedNormal.equals(normal) || expectedNormal.equals(normal.scale(-1)), "bad normal to plane");
	}
}
