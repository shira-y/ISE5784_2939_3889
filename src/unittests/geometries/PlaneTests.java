/**
 * 
 */
package unittests.geometries;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

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

	@Test
	public void testFindIntersections() {
		Plane plane = new Plane(new Point(0, 0, 1), new Vector(0, 0, 1));

		// ========== Equivalence Partitions tests ==========
		// TC01: Ray that starts outside the plane, is not parallel to the plane,
		// is not makes a non-right angle with the plane, and cuts the plane (1 points)
		Ray ray1 = new Ray(new Point(0, 0, 2), new Vector(0, 0, -1));
		List<Point> result1 = plane.findIntersections(ray1);
		assertEquals(1, result1.size(), "Wrong number of points");
		assertEquals(new Point(0, 0, 1), result1.get(0), "Ray cuts the plane");

		// TC02: Ray that starts outside the plane, not parallel to the plane, makes a
		// non-right angle with the plane, and does not cut the plane (0 points)
		Ray ray2 = new Ray(new Point(0, 0, 2), new Vector(0, 0, 1));
		assertNull(plane.findIntersections(ray2), "Ray does not cut the plane");

		// =========== Boundary Values Tests ===========
		// **** Group: Ray parallel to the plane
		// TC03: Ray is parallel and outside the plane 
		Ray ray3 = new Ray(new Point(0, 0, 2), new Vector(1, 0, 0));
		assertNull(plane.findIntersections(ray3), "Ray is parallel and outside the plane");

		// TC04: Ray is parallel and inside the plane
		Ray ray4 = new Ray(new Point(0, 0, 1), new Vector(1, 0, 0));
		assertNull(plane.findIntersections(ray4), "Ray is parallel and inside the plane");

		// **** Group: Ray perpendicular to the plane
		// TC05: Ray is perpendicular and starts before the plane
		Ray ray5 = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
		List<Point> result2 = plane.findIntersections(ray5);
		assertEquals(1, result2.size(), "Wrong number of points");
		assertEquals(new Point(0, 0, 1), result2.get(0), "Ray is perpendicular and starts before the plane");

		// TC06: Ray is perpendicular and starts inside the plane
		Ray ray6 = new Ray(new Point(0, 0, 1), new Vector(0, 0, 1));
		assertNull(plane.findIntersections(ray6), "Ray is perpendicular and starts inside the plane");

		// TC07: Ray is perpendicular and starts after the plane
		Ray ray7 = new Ray(new Point(0, 0, 2), new Vector(0, 0, 1));
		assertNull(plane.findIntersections(ray7), "Ray is perpendicular and starts after the plane");

		// **** Group: Ray that is neither parallel nor perpendicular to the plane but
		// starts inside the plane
		// TC08: Ray starts inside the plane
		Ray ray8 = new Ray(new Point(0, 0, 1), new Vector(1, 1, 0));
		assertNull(plane.findIntersections(ray8), "Ray starts inside the plane");

		// TC09: Ray starts at the reference point of the plane
		Ray ray9 = new Ray(new Point(0, 0, 1), new Vector(1, 1, 1));
		List<Point> result3 = plane.findIntersections(ray9);
		assertEquals(1, result3.size(), "Wrong number of points");
		assertEquals(new Point(1, 1, 2), result3.get(0), "Ray starts at the reference point of the plane");

	}
}
