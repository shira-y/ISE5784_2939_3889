package unittests.geometries;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import primitives.*;
import geometries.Cylinder;

/**
 * Test method for {@link geometries.Cylinder#getNormal(primitives.Point)}.
 */
class CylinderTest {

	@Test
	void testGetNormal() {
		Ray ray = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        Cylinder cylinder = new Cylinder(5, 1, ray);

        // Point on the bottom base
        Point p1 = new Point(0, 0, 0);
        Vector normal1 = cylinder.getNormal(p1);
        assertEquals(new Vector(0, 0, -1), normal1, "Bad normal to the bottom base of the cylinder");

        // Point on the top base
        Point p2 = new Point(0, 0, 5);
        Vector normal2 = cylinder.getNormal(p2);
        assertEquals(new Vector(0, 0, 1), normal2, "Bad normal to the top base of the cylinder");

        // Point on the curved surface
        Point p3 = new Point(1, 0, 2.5);
        Vector normal3 = cylinder.getNormal(p3);
        assertEquals(new Vector(1, 0, 0), normal3, "Bad normal to the curved surface of the cylinder");
	}

}
