package unittests.geometries;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.*;

import geometries.*;
import primitives.*;

/**
 * Unit tests for primitives.Point class
 * 
 * @author Yael
 */
public class PolygonTests {
	/**
	 * Delta value for accuracy when comparing the numbers of type 'double' in
	 * assertEquals
	 */
	private final double DELTA = 0.000001;

	/** Test method for {@link geometries.Polygon#Polygon(primitives.Point...)}. */
	@Test
	public void testConstructor() {
		// ============ Equivalence Partitions Tests ==============

		// TC01: Correct concave quadrangular with vertices in correct order
		assertDoesNotThrow(
				() -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(-1, 1, 1)),
				"Failed constructing a correct polygon");

		// TC02: Wrong vertices order
		assertThrows(IllegalArgumentException.class, //
				() -> new Polygon(new Point(0, 0, 1), new Point(0, 1, 0), new Point(1, 0, 0), new Point(-1, 1, 1)), //
				"Constructed a polygon with wrong order of vertices");

		// TC03: Not in the same plane
		assertThrows(IllegalArgumentException.class, //
				() -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 2, 2)), //
				"Constructed a polygon with vertices that are not in the same plane");

		// TC04: Concave quadrangular
		assertThrows(IllegalArgumentException.class, //
				() -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0),
						new Point(0.5, 0.25, 0.5)), //
				"Constructed a concave polygon");

		// =============== Boundary Values Tests ==================

		// TC10: Vertex on a side of a quadrangular
		assertThrows(IllegalArgumentException.class, //
				() -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 0.5, 0.5)),
				"Constructed a polygon with vertix on a side");

		// TC11: Last point = first point
		assertThrows(IllegalArgumentException.class, //
				() -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 0, 1)),
				"Constructed a polygon with vertice on a side");

		// TC12: Co-located points
		assertThrows(IllegalArgumentException.class, //
				() -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 1, 0)),
				"Constructed a polygon with vertice on a side");

	}

	/** Test method for {@link geometries.Polygon#getNormal(primitives.Point)}. */
	@Test
	public void testGetNormal() {
		// ============ Equivalence Partitions Tests ==============
		// TC01: There is a simple single test here - using a quad
		Point[] pts = { new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(-1, 1, 1) };
		Polygon pol = new Polygon(pts);
		// ensure there are no exceptions
		assertDoesNotThrow(() -> pol.getNormal(new Point(0, 0, 1)), "");
		// generate the test result
		Vector result = pol.getNormal(new Point(0, 0, 1));
		// ensure |result| = 1
		assertEquals(1, result.length(), DELTA, "Polygon's normal is not a unit vector");
		// ensure the result is orthogonal to all the edges
		for (int i = 0; i < 3; ++i)
			assertEquals(0d, result.dotProduct(pts[i].subtract(pts[i == 0 ? 3 : i - 1])), DELTA,
					"Polygon's normal is not orthogonal to one of the edges");
	}

	/**
	 * Test method for {@link geometries.Polygon#findIntersections(primitives.Ray)}.
	 */
	@Test
	@Disabled
	public void testFindIntersections() {
		// Create a convex polygon
		Polygon polygon = new Polygon(new Point(1, 1, 1), new Point(2, 1, 1), new Point(2, 2, 1), new Point(1, 2, 1));

		// ============ Equivalence Partitions Tests ==============
		// TC01: Ray intersects inside the polygon (1 point)
		Point p1 = new Point(2, 1, 1);
		List<Point> result1 = polygon.findIntersections(new Ray(new Point(1.5, 1.5, 1), new Vector(1, 0, 0)));
		assertEquals(1, result1.size(), "Wrong number of points");
		assertEquals(List.of(p1), result1, "Ray intersects inside the polygon");

		// TC02: Ray intersects outside the polygon (opposite one of the sides) (0
		// points)
		assertNull(polygon.findIntersections(new Ray(new Point(3, 1.5, 0), new Vector(0, 0, 1))),
				"Ray intersects outside the polygon (opposite one of the sides)");

		// TC03: Ray intersects outside the polygon (opposite one of the vertices) (0
		// points)
		assertNull(polygon.findIntersections(new Ray(new Point(0.5, 0.5, 0), new Vector(0, 0, 1))),
				"Ray intersects outside the polygon (opposite one of the vertices)");

		// =============== Boundary Values Tests ==================
		// TC11: Ray intersects on one of the sides (1 point)
		Point p2 = new Point(1.5, 1, 1);
		List<Point> result2 = polygon.findIntersections(new Ray(new Point(1.5, 1, 0), new Vector(0, 0, 1)));
		assertEquals(1, result2.size(), "Wrong number of points");
		assertEquals(List.of(p2), result2, "Ray intersects on one of the sides");

		// TC12: Ray intersects at one of the vertices (1 point)
		Point p3 = new Point(1, 1, 1);
		List<Point> result3 = polygon.findIntersections(new Ray(new Point(1, 1, 0), new Vector(0, 0, 1)));
		assertEquals(1, result3.size(), "Wrong number of points");
		assertEquals(List.of(p3), result3, "Ray intersects at one of the vertices");

		// TC13: Ray intersects on the continuation of one of the sides (0 points)
		assertNull(polygon.findIntersections(new Ray(new Point(3, 2, 0), new Vector(0, 0, 1))),
				"Ray intersects on the continuation of one of the sides");
	}
}