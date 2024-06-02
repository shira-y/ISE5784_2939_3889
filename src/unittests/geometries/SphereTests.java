package unittests.geometries;

import geometries.*;
import primitives.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for geometries.Sphere class
 */
class SphereTests {
	/**
	 * Define point for testing
	 */
	private static final Point p001 = new Point(0, 0, 1);
	/**
	 * Define point for testing
	 */
	private static final Point p100 = new Point(1, 0, 0);
	/**
	 * Define point for testing
	 */
	private static final Vector v001 = new Vector(0, 0, 1);

	/**
	 * Test method for {@link geometries.Sphere#getNormal(primitives.Point)}.
	 */
	@Test
	void testGetNormal() {
		// ========== Equivalence Partitions tests ==========
		// TC01: get normal of point on sphere
		Sphere sphere = new Sphere(new Point(0, 0, 0), 1);
		Vector normal = new Vector(1, 0, 0);
		assertEquals(normal, sphere.getNormal(new Point(1, 0, 0)), "Bad normal for sphere");
	}

	/**
	 * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}.
	 */
	@Test
	public void testFindIntersections() {
		Sphere sphere = new Sphere(p100, 1d);
		final Point gp1 = new Point(0.0651530771650466, 0.355051025721682, 0);
		final Point gp2 = new Point(1.53484692283495, 0.844948974278318, 0);
		final var exp = List.of(gp1, gp2);
		final Vector v310 = new Vector(3, 1, 0);
		final Vector v110 = new Vector(1, 1, 0);
		final Point p01 = new Point(-1, 0, 0);
		// ============ Equivalence Partitions Tests ==============
		// TC01: Ray's line is outside the sphere (0 points)
		assertNull(sphere.findIntersections(new Ray(p01, v110)), "Ray's line out of sphere");
		// TC02: Ray starts before and crosses the sphere (2 points)
		List<Point> result1 = sphere.findIntersections(new Ray(p01, v310)).stream()
				.sorted(Comparator.comparingDouble(p -> p.distance(p01))).toList();
		assertEquals(2, result1.size(), "Wrong number of points");
		assertEquals(exp, result1, "Ray crosses sphere");
		// TC03: Ray starts inside the sphere (1 point)
		List<Point> result2 = sphere.findIntersections(new Ray(new Point(0.5, 0, 0), new Vector(3, 1, 0)));
		final Point gp3 = new Point(1.8867496997597595, 0.4622498999199199, 0.0);
		assertEquals(1, result2.size(), "Wrong number of points");
		assertEquals(List.of(gp3), result2, "Ray starts inside the sphere");
		// TC04: Ray starts after the sphere (0 points)
		assertNull(sphere.findIntersections(new Ray(new Point(3, 0, 0), new Vector(1, 0, 0))),
				"Ray starts after the sphere");
		// =============== Boundary Values Tests ==================
		// **** Group: Ray's line crosses the sphere (but not the center)
		// TC11: Ray starts at sphere and goes inside (1 points)
		List<Point> result3 = sphere.findIntersections(new Ray(new Point(1.5, 0.5, 0), new Vector(-1, 0, 0)));
		assertEquals(1, result3.size(), "Wrong number of points");
		final Point gp4 = new Point(0.1339745962155614, 0.5, 0.0);
		assertEquals(List.of(gp4), result3, "Ray starts at sphere and goes inside");
		// TC12: Ray starts at sphere and goes outside (0 points)
		assertNull(sphere.findIntersections(new Ray(new Point(0, 0, 0), new Vector(-1, -1, 0))),
				"Ray starts at sphere and goes outside");

		// **** Group: Ray's line goes through the center
		// TC13: Ray starts before the sphere (2 points)
		List<Point> result4 = sphere.findIntersections(new Ray(new Point(-1, 0, 0), new Vector(2, 0, 0))).stream()
				.sorted(Comparator.comparingDouble(p -> p.distance(new Point(-1, 0, 0)))).toList();
		assertEquals(2, result4.size(), "Wrong number of points");
		assertEquals(List.of(new Point(0, 0, 0), new Point(2, 0, 0)), result4,
				"Ray starts before the sphere and goes through the center");

		// TC14: Ray starts at sphere and goes inside (1 points)
		List<Point> result5 = sphere.findIntersections(new Ray(new Point(0, 0, 0), new Vector(2, 0, 0)));
		assertEquals(1, result5.size(), "Wrong number of points");
		assertEquals(List.of(new Point(2, 0, 0)), result5, "Ray starts at sphere and goes through the center");

		// TC15: Ray starts inside (1 points)
		List<Point> result6 = sphere.findIntersections(new Ray(new Point(0.5, 0, 0), new Vector(2, 0, 0)));
		assertEquals(1, result6.size(), "Wrong number of points");
		assertEquals(List.of(new Point(2, 0, 0)), result6, "Ray starts inside and goes through the center");

		// TC16: Ray starts at the center (1 points)
		List<Point> result7 = sphere.findIntersections(new Ray(new Point(1, 0, 0), new Vector(1, 0, 0)));
		assertEquals(1, result7.size(), "Wrong number of points");
		assertEquals(List.of(new Point(2, 0, 0)), result7, "Ray starts at the center");

		// TC17: Ray starts at sphere and goes outside (0 points)
		assertNull(sphere.findIntersections(new Ray(new Point(2, 0, 0), new Vector(1, 0, 0))),
				"Ray starts at sphere and goes outside");

		// TC18: Ray starts after sphere (0 points)
		assertNull(sphere.findIntersections(new Ray(new Point(3, 0, 0), new Vector(1, 0, 0))),
				"Ray starts after sphere");

		// **** Group: Ray's line is tangent to the sphere (all tests 0 points)
		// TC19: Ray starts before the tangent point
		assertNull(sphere.findIntersections(new Ray(new Point(1, -1, 0), new Vector(1, 0, 0))),
				"Ray starts before the tangent point");

		// TC20: Ray starts at the tangent point
		assertNull(sphere.findIntersections(new Ray(new Point(2, -1, 0), new Vector(1, 0, 0))),
				"Ray starts at the tangent point");

		// TC21: Ray starts after the tangent point
		assertNull(sphere.findIntersections(new Ray(new Point(3, -1, 0), new Vector(1, 0, 0))),
				"Ray starts after the tangent point");

		// **** Group: Special cases
		// TC22: Ray's line is outside, ray is orthogonal to ray start to sphere's
		// center line
		assertNull(sphere.findIntersections(new Ray(new Point(1, 0, 2), v001)),
				"Ray's line is outside, ray is orthogonal to ray start to sphere's center line");
	}
}
