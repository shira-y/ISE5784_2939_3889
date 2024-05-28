package unittests.geometries;

import static org.junit.jupiter.api.Assertions.*;

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
		Vector expectedNormalSide = new Vector(1, 0, 0); // the normal at this point should point outward perpendicular
															// to the surface
		assertEquals(expectedNormalSide, tube.getNormal(sidePoint), "Bad normal to tube on the side");

		// =============== Boundary Values Tests ==================

		// TC11: Point on the tube axis
		Point pointOnAxis = new Point(0, 0, 5);
		assertThrows(IllegalArgumentException.class, () -> tube.getNormal(pointOnAxis),
				"Expected exception for normal calculation at the tube axis");
	}
}
