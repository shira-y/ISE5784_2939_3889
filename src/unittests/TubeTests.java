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
	    void testGetNormal() {
	        Ray ray = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
	        Tube tube = new Tube(2, ray);

	        // ============ Equivalence Partitions Tests ==============

	        // TC01: Point on the side of the tube
	        Point sidePoint = new Point(2, 0, 5);
	        Vector expectedNormalSide = new Vector(1, 0, 0); // assuming the tube is along z-axis and radius is 2
	        assertEquals(expectedNormalSide, tube.getNormal(sidePoint), "Bad normal to tube on the side");

	        // TC02: Point on the top base of the tube
	        Point topBasePoint = new Point(1, 1, 10); // top base point
	        Vector expectedNormalTopBase = new Vector(0, 0, 1);
	        assertEquals(expectedNormalTopBase, tube.getNormal(topBasePoint), "Bad normal to tube on the top base");

	        // TC03: Point on the bottom base of the tube
	        Point bottomBasePoint = new Point(1, 1, 0); // bottom base point
	        Vector expectedNormalBottomBase = new Vector(0, 0, -1);
	        assertEquals(expectedNormalBottomBase, tube.getNormal(bottomBasePoint), "Bad normal to tube on the bottom base");

	        // =============== Boundary Values Tests ==================

	        // TC11: Point at the center of the top base
	        Point centerTopBase = new Point(0, 0, 10);
	        assertEquals(new Vector(0, 0, 1), tube.getNormal(centerTopBase), "Bad normal at the center of the top base");

	        // TC12: Point at the center of the bottom base
	        Point centerBottomBase = new Point(0, 0, 0);
	        assertEquals(new Vector(0, 0, -1), tube.getNormal(centerBottomBase), "Bad normal at the center of the bottom base");
	}

	


}