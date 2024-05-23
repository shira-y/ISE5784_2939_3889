/**
 * 
 */
package unittests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import geometries.Plane;
import primitives.Point;
import primitives.Vector;

/**
 * Unit tests for geometries.Plane class
 */
class PlaneTests {

	private final double DELTA = 0.000001;

	/**
	 * Test method for
	 * {@link geometries.Plane#Plane(primitives.Point, primitives.Point, primitives.Point)}.
	 */
	@Test
	void testConstructor() {

		// ===========Boundary Value Analysis tests
		// ====================================================
		// TC01 two points are equal
		assertThrows(IllegalArgumentException.class,
				() -> new Plane(new Point(0, 0, 1), new Point(0, 0, 1), new Point(0, 0, 2)),
				"constructed Plane with equal points");

		// TC02 points are all on the same line
		assertThrows(IllegalArgumentException.class,
				() -> new Plane(new Point(0, 0, 1), new Point(0, 0, 3), new Point(0, 0, 2)),
				"constructed plane must have Vectors in different directions");

	}

	/**
	 * Test method for {@link geometries.Plane#getNormal(primitives.Point)}.
	 */
	@Test
	void getNormal() {
		// ========== Equivalence Partitions tests
		// ======================================================
		// EP01 test if normal vector is correct
		Plane p1 = new Plane(new Point(0, 0, 0), new Point(0, 5, 0), new Point(5, 0, 0));
		Vector normal = new Vector(0, 0, 1);
		assertTrue(normal.equals(p1.getNormal(new Point(1, 1, 0))) || normal.equals(p1.getNormal(new Point(-1, -1, 0))),
				"bad normal to plane");
	}
}