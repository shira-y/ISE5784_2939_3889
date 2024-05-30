package unittests.geometries;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import primitives.*;
import geometries.*;

/**
 * Unit tests for geometries.Tube class
 */
class TubeTests {

	/**
	 * Test method for {@link geometries.Tube#getNormal(primitives.Point)}.
	 */
	@Test
	void testGetNormal() {

		Ray ray = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
		Tube tube = new Tube(2, ray);

		// ============ Equivalence Partitions Tests ==============

		// TC01: Point on the side of the tube
		Point sidePoint = new Point(2, 0, 5);
		Vector expectedNormalSide = new Vector(1, 0, 0); // the normal at this point should point outward perpendicular to the surface
		assertEquals(expectedNormalSide, tube.getNormal(sidePoint), "Bad normal to tube on the side");

		// =============== Boundary Values Tests ==================

		 // TC11: Point on the tube surface very close to the axis
        Point nearAxisPoint = new Point(0.0001, 2, 5);
        Vector expectedNormalNearAxis = new Vector(0, 1, 0).normalize(); // Should normalize since we're slightly off-axis
        assertEquals(expectedNormalNearAxis, tube.getNormal(nearAxisPoint), "Bad normal to tube near the axis");

	}
        /**
         * Test method for {@link geometries.Tube#findIntersections(primitives.Ray)}.
         */
        @Test
        public void testFindIntersections() {
            // Create a tube
            Ray axisRay = new Ray(new Point(1, 1, 1), new Vector(0, 0, 1));
            Tube tube = new Tube(1.0, axisRay);

            // ============ Equivalence Partitions Tests ==============
            // TC01: Ray intersects the tube (2 points)
            Point p1 = new Point(2, 1, 2);
            Point p2 = new Point(0, 1, 2);
            List<Point> result1 = tube.findIntersections(new Ray(new Point(1, 1, 2), new Vector(1, 0, 0)));
            assertEquals(2, result1.size(), "Wrong number of points");
            assertTrue(result1.contains(p1) && result1.contains(p2), "Ray intersects the tube");

            // TC02: Ray is parallel to the tube axis and outside the tube (0 points)
            assertNull(tube.findIntersections(new Ray(new Point(3, 1, 2), new Vector(0, 0, 1))), 
                    "Ray is parallel to the tube axis and outside the tube");

            // TC03: Ray is parallel to the tube axis and inside the tube (0 points)
            assertNull(tube.findIntersections(new Ray(new Point(1.5, 1, 2), new Vector(0, 0, 1))), 
                    "Ray is parallel to the tube axis and inside the tube");

            // =============== Boundary Values Tests ==================
            // TC11: Ray intersects the tube tangentially (1 point)
            Point p3 = new Point(2, 1, 1);
            List<Point> result2 = tube.findIntersections(new Ray(new Point(2, 0, 1), new Vector(0, 1, 0)));
            assertEquals(1, result2.size(), "Wrong number of points");
            assertEquals(List.of(p3), result2, "Ray intersects the tube tangentially");

            // TC12: Ray starts inside the tube and goes outside (1 point)
            Point p4 = new Point(2, 1, 2);
            List<Point> result3 = tube.findIntersections(new Ray(new Point(1.5, 1, 2), new Vector(1, 0, 0)));
            assertEquals(1, result3.size(), "Wrong number of points");
            assertEquals(List.of(p4), result3, "Ray starts inside the tube and goes outside");

            // TC13: Ray starts on the surface of the tube and goes outside (0 points)
            assertNull(tube.findIntersections(new Ray(new Point(2, 1, 2), new Vector(1, 0, 0))),
                    "Ray starts on the surface of the tube and goes outside");

            // TC14: Ray starts on the surface of the tube and goes inside (1 point)
            Point p5 = new Point(0, 1, 2);
            List<Point> result4 = tube.findIntersections(new Ray(new Point(2, 1, 2), new Vector(-1, 0, 0)));
            assertEquals(1, result4.size(), "Wrong number of points");
            assertEquals(List.of(p5), result4, "Ray starts on the surface of the tube and goes inside");

            // TC15: Ray is orthogonal to the tube axis and intersects the tube at two points (2 points)
            Point p6 = new Point(2, 1, 3);
            Point p7 = new Point(0, 1, 3);
            List<Point> result5 = tube.findIntersections(new Ray(new Point(1, 1, 3), new Vector(1, 0, 0)));
            assertEquals(2, result5.size(), "Wrong number of points");
            assertTrue(result5.contains(p6) && result5.contains(p7), "Ray is orthogonal to the tube axis and intersects the tube at two points");

            // TC16: Ray is orthogonal to the tube axis and misses the tube (0 points)
            assertNull(tube.findIntersections(new Ray(new Point(1, 3, 3), new Vector(1, 0, 0))),
                    "Ray is orthogonal to the tube axis and misses the tube");
        }
}
