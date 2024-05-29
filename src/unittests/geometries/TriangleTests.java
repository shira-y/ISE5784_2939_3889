/**
 * 
 */
package unittests.geometries;

import primitives.*;
import geometries.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

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

		Point p1 = new Point(0, 0, 0);
		Point p2 = new Point(1, 0, 0);
		Point p3 = new Point(0, 1, 0);

        Triangle triangle = new Triangle(p1, p2, p3);

        Vector normal = triangle.getNormal(new Point(0, 0, 0));

        // Define the expected normals in 2 directions
        Vector expectedNormal1 = new Vector(0, 0, 1).normalize();
        Vector expectedNormal2 = new Vector(0, 0, -1).normalize();
        
        // TC01: Assert that the computed normal matches the expected normal
        assertTrue(normal.equals(expectedNormal1) || normal.equals(expectedNormal2),
                "getNormal did not return the expected normal vector");
	}

	@Test
	public void testFindIntersections() {
		Triangle triangle = new Triangle(new Point(0, 0, 1), new Point(1, 0, 1), new Point(0, 1, 1));

		// Equivalence Partitions Tests
		// TC01: The point of intersection with the "contained" plane is inside the
		// triangle
		Ray ray1 = new Ray(new Point(0.5, 0.5, 0), new Vector(0, 0, 1));
		List<Point> result1 = triangle.findIntersections(ray1);
		assertEquals(1, result1.size(), "Wrong number of points");
		assertEquals(new Point(0.5, 0.5, 1), result1.get(0), "Intersection inside the triangle");

		// TC02: The point of intersection with the "contained" plane is outside the
		// triangle - opposite one of the sides
		Ray ray2 = new Ray(new Point(1.5, 0.5, 0), new Vector(0, 0, 1));
		assertNull(triangle.findIntersections(ray2), "Intersection outside the triangle opposite one of the sides");

		// TC03: The point of intersection with the "contained" plane is outside the
		// triangle - opposite one of the vertices
		Ray ray3 = new Ray(new Point(1, 1, 0), new Vector(0, 0, 1));
		assertNull(triangle.findIntersections(ray3), "Intersection outside the triangle opposite one of the vertices");

		// Boundary Values Tests
		// TC04: The point of intersection with the "contained" plane is on one of the
		// sides
		Ray ray4 = new Ray(new Point(0.5, 0, 0), new Vector(0, 0, 1));
		List<Point> result2 = triangle.findIntersections(ray4);
		assertEquals(1, result2.size(), "Wrong number of points");
		assertEquals(new Point(0.5, 0, 1), result2.get(0), "Intersection on the side of the triangle");

		// TC05: The point of intersection with the "contained" plane is at one of the
		// vertices
		Ray ray5 = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
		List<Point> result3 = triangle.findIntersections(ray5);
		assertEquals(1, result3.size(), "Wrong number of points");
		assertEquals(new Point(0, 0, 1), result3.get(0), "Intersection at one of the vertices of the triangle");

		// TC06: The point of intersection with the "contained" plane is on a
		// continuation of one of the sides
		Ray ray6 = new Ray(new Point(2, 0, 0), new Vector(0, 0, 1));
		assertNull(triangle.findIntersections(ray6),
				"Intersection on a continuation of one of the sides of the triangle");
	}
}