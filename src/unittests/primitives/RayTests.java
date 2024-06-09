package unittests.primitives;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

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
	
	 @Test
	    void testFindClosestPointMiddle() {
	        Ray ray = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));
	        List<Point> points = Arrays.asList(
	                new Point(2, 2, 0),
	                new Point(1, 1, 0),
	                new Point(3, 3, 0)
	        );
	        Point closest = ray.findClosestPoint(points);
	        assertEquals(new Point(1, 1, 0), closest, "The closest point should be the middle point in the list.");
	    }

	    @Test
	    void testFindClosestPointEmptyList() {
	        Ray ray = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));
	        List<Point> points = Arrays.asList();
	        Point closest = ray.findClosestPoint(points);
	        assertNull(closest, "The closest point should be null for an empty list.");
	    }

	    @Test
	    void testFindClosestPointFirst() {
	        Ray ray = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));
	        List<Point> points = Arrays.asList(
	                new Point(1, 1, 0),
	                new Point(2, 2, 0),
	                new Point(3, 3, 0)
	        );
	        Point closest = ray.findClosestPoint(points);
	        assertEquals(new Point(1, 1, 0), closest, "The closest point should be the first point in the list.");
	    }

	    @Test
	    void testFindClosestPointLast() {
	        Ray ray = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));
	        List<Point> points = Arrays.asList(
	                new Point(3, 3, 0),
	                new Point(2, 2, 0),
	                new Point(1, 1, 0)
	        );
	        Point closest = ray.findClosestPoint(points);
	        assertEquals(new Point(1, 1, 0), closest, "The closest point should be the last point in the list.");
	    }
}
