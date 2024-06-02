package unittests.geometries;

import geometries.*;
import primitives.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GeometriesTests {

	/**
	 * Test the findIntersections method in various scenarios: - Empty collection
	 * (BVA) - No shape intersects (BVA) - Only one shape intersects (BVA) - Some
	 * shapes intersect (EP) - All shapes intersect (BVA)
	 */
	@Test
	void testFindIntersections() {
		// =========== Boundary Values Tests ===========
		// BVA: Empty collection
		Geometries geometries = new Geometries();
		Ray ray = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));
		assertNull(geometries.findIntersections(ray), "Expected no intersections with an empty collection");

		// BVA: No shape intersects
		geometries = new Geometries(new Sphere(new Point(0, 0, 5), 1),
				new Plane(new Point(0, 0, 5), new Vector(0, 0, 1)),
				new Triangle(new Point(0, 1, 5), new Point(1, 0, 5), new Point(0, 0, 6)));
		ray = new Ray(new Point(0, 0, 0), new Vector(0, 1, 0));
		assertNull(geometries.findIntersections(ray), "Expected no intersections");

		// BVA: Only one shape intersects
		geometries = new Geometries(new Sphere(new Point(0, 0, 5), 1),
				new Plane(new Point(0, 0, 10), new Vector(0, 0, 1)),
				new Triangle(new Point(0, 1, 10), new Point(1, 0, 10), new Point(0, 0, 11)));
		ray = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
		assertEquals(2, geometries.findIntersections(ray).size(), "Expected 2 intersections with the sphere");

		// EP: Some shapes intersect (but not all)
		geometries = new Geometries(new Sphere(new Point(0, 0, 5), 1),
				new Plane(new Point(0, 0, 5), new Vector(0, 0, 1)),
				new Triangle(new Point(0, 1, 10), new Point(1, 0, 10), new Point(0, 0, 11)));
		ray = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
		assertEquals(3, geometries.findIntersections(ray).size(), "Expected 3 intersections with the sphere and plane");

		// BVA: All shapes intersect
		geometries = new Geometries(new Sphere(new Point(0, 0, 5), 1),
				new Plane(new Point(0, 0, 6), new Vector(0, 0, 1)),
				new Triangle(new Point(0, 1, 2), new Point(1, 0, 2), new Point(0, 0, 3)));
		ray = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
		assertEquals(4, geometries.findIntersections(ray).size(),
				"Expected 4 intersections with the sphere, plane, and triangle");
	}
}