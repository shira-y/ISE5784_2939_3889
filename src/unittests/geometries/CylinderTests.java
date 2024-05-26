package unittests.geometries;

import org.junit.jupiter.api.Test;
import primitives.*;
import geometries.*;

import static org.junit.jupiter.api.Assertions.*;

class CylinderTest {

    /**
     * Test method for {@link geometries.Cylinder#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        Cylinder cylinder = new Cylinder(3d, 1d, new Ray(new Point(1, 1, 0), new Vector(0, 0, 1)));

        // ============ Equivalence Partitions Tests ==============
        // TC01: Test with point on the top of the cylinder
        assertEquals(new Vector(0, 0, 1), cylinder.getNormal(new Point(1, 1, 3)), "Bad normal to the top of the cylinder");
        
        // TC02: Test with point on the bottom of the cylinder
        assertEquals(new Vector(0, 0, -1), cylinder.getNormal(new Point(1, 1, 0)), "Bad normal to the bottom of the cylinder");

        // TC03: Test with point on the side of the cylinder
        assertEquals(new Vector(0, -1, 0), cylinder.getNormal(new Point(1, 0, 1)), "Bad normal to the side of the cylinder");

        // =============== Boundary Values Tests ==================
        // TC04: Test with point on the top edge of the cylinder
        assertEquals(new Vector(0, 0, 1), cylinder.getNormal(new Point(1, 0, 3)), "Bad normal to the top-edge of the cylinder");

        // TC05: Test with point on the bottom edge of the cylinder
        assertEquals(new Vector(0, 0, -1), cylinder.getNormal(new Point(0, 1, 0)), "Bad normal to the bottom-edge of the cylinder");
    }
}
