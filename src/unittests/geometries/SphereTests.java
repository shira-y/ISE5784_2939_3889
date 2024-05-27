package unittests.geometries;

import geometries.*;
import primitives.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for geometries.Sphere class
 */
class SphereTests {

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
}
