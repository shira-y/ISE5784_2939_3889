/**
 * 
 */
package unittests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import primitives.*;
import geometries.*;

/**
 * Unit tests for geometries.Tube class
 */
class TubeTests {
	private final double DELTA = 0.000001;

	/**
	 * Test method for {@link geometries.Tube#getNormal(primitives.Point)}.
	 */
	@Test
	public void testGetNormal() {
		// ============ Equivalence Class Tests ==============
		// TC01: Test the normal vector at a point on the surface of the tube
		Ray axisRay = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)); // Define the axis of the tube
		Tube tube = new Tube(1, axisRay); // Create a tube with radius 1 and axisRay

		// Choose a point on the surface of the tube
		Point surfacePoint = new Point(1, 0, 0); // Example point on the surface

		// Get the normal vector at the surface point
		Vector normal = tube.getNormal(surfacePoint);

		// Verify that the normal vector is perpendicular to the axis of the tube
		assertEquals(0, normal.dotProduct(axisRay.direction), DELTA,
				"Normal vector should be perpendicular to tube axis");
		assertEquals(1, normal.length(), DELTA, "Normal vector should have unit length");

		// =============== Boundary Case Test ==================
		// TC02: Test when the point is in front of the head of the ray (right angle
		// with the axis)
		Point frontPoint = axisRay.getPoint(1); // A point in front of the head of the ray

		// Get the normal vector at the front point
		Vector normalFront = tube.getNormal(frontPoint);

		// Verify that the normal vector is perpendicular to the axis of the tube
		assertEquals(0, normalFront.dotProduct(axisRay.direction), DELTA,
				"Normal vector should be perpendicular to tube axis");
		assertEquals(1, normalFront.length(), DELTA, "Normal vector should have unit length");
	}

}