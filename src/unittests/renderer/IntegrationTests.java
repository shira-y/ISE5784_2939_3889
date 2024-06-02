package unittests.renderer;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import geometries.*;
import primitives.*;
import renderer.Camera;

public class IntegrationTests {

    /**
     * Helper method to create rays from the camera and count intersections with a given geometry.
     */
    private int countIntersections(Camera camera, Intersectable geometry, int nX, int nY) {
        int count = 0;
        for (int i = 0; i < nX; i++) {
            for (int j = 0; j < nY; j++) {
                Ray ray = camera.constructRay(nX, nY, j, i);
                List<Point> intersections = geometry.findIntersections(ray);
                if (intersections != null) {
                    count += intersections.size();
                }
            }
        }
        return count;
    }

    @Test
    public void testSphereIntegration() {
        Camera camera = Camera.getBuilder()
                              .setLocation(new Point(0, 0, 0))
                              .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                              .setVpSize(3, 3)
                              .setVpDistance(1)
                              .build();
        
        Sphere sphere = new Sphere(new Point(0, 0, -3), 1);

        int intersections = countIntersections(camera, sphere, 3, 3);
        assertEquals("Wrong number of intersections with sphere", 2, intersections);
    }

    @Test
    public void testPlaneIntegration() {
        Camera camera = Camera.getBuilder()
                              .setLocation(new Point(0, 0, 0))
                              .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                              .setVpSize(3, 3)
                              .setVpDistance(1)
                              .build();

        Plane plane = new Plane(new Point(0, 0, -5), new Vector(0, 0, 1));

        int intersections = countIntersections(camera, plane, 3, 3);
        assertEquals("Wrong number of intersections with plane", 9, intersections);
    }

    @Test
    public void testTriangleIntegration() {
        Camera camera = Camera.getBuilder()
                              .setLocation(new Point(0, 0, 0))
                              .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                              .setVpSize(3, 3)
                              .setVpDistance(1)
                              .build();

        Triangle triangle = new Triangle(new Point(0, 1, -2), new Point(1, -1, -2), new Point(-1, -1, -2));

        int intersections = countIntersections(camera, triangle, 3, 3);
        assertEquals("Wrong number of intersections with triangle", 1, intersections);
    }
}
