package unittests.geometries;

import geometries.*;
import primitives.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Geometries class
 */
class GeometriesTests {

	/**
	 * Test the findIntersections method in various scenarios: - Empty collection
	 * (BVA) - No shape intersects (BVA) - Only one shape intersects (BVA) - Some
	 * shapes intersect (EP) - All shapes intersect (BVA)
	 */
	@Test
	void testFindIntersections() {
		// =========== Boundary Values Tests ===========
		// TC10: Empty collection
		Geometries geometries = new Geometries();
		Ray ray = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));
		assertNull(geometries.findIntersections(ray), "Expected no intersections with an empty collection");

		// TC11: No shape intersects
		geometries = new Geometries(new Sphere(new Point(0, 0, 5), 1),
				new Plane(new Point(0, 0, 5), new Vector(0, 0, 1)),
				new Triangle(new Point(0, 1, 5), new Point(1, 0, 5), new Point(0, 0, 6)));
		ray = new Ray(new Point(0, 0, 0), new Vector(0, 1, 0));
		assertNull(geometries.findIntersections(ray), "Expected no intersections");

		// TC12: Only one shape intersects
		geometries = new Geometries(new Sphere(new Point(0, 0, 5), 1), // Sphere at (0, 0, 5) with radius 1
				new Plane(new Point(0, 0, 15), new Vector(0, 0, 1)), // Plane moved further along the z-axis
				new Triangle(new Point(-1, -1, 2), new Point(1, -1, 2), new Point(0, 1, 2)) // Triangle intersected by
																							// the ray
		);
		ray = new Ray(new Point(0, 9, 0), new Vector(0, 8, 1));

		assertEquals(1, geometries.findIntersections(ray).size(), "Expected 1 intersections with the Triangle");

		// TC13: All shapes intersect
		geometries = new Geometries(new Sphere(new Point(0, 0, 5), 1),
				new Plane(new Point(0, 0, 6), new Vector(0, 0, 1)),
				new Triangle(new Point(0, 1, 2), new Point(1, 0, 2), new Point(0, 0, 3)));
		ray = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
		assertEquals(3, geometries.findIntersections(ray).size(),
				"Expected 3 intersections with the sphere, plane, and triangle");

		// ============ Equivalence Partitions Tests ==============
		// TC01: Some shapes intersect (but not all)
		geometries = new Geometries(new Sphere(new Point(0, 0, 5), 1),
				new Plane(new Point(0, 0, 5), new Vector(0, 0, 1)),
				new Triangle(new Point(0, 1, 10), new Point(1, 0, 10), new Point(0, 0, 11)));
		ray = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
		assertEquals(3, geometries.findIntersections(ray).size(), "Expected 3 intersections with the sphere and plane");
	}
}