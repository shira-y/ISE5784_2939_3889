package geometries;

import java.util.List;

import primitives.*;
import static primitives.Util.*;

/**
 * The Triangle class represents a triangle shape in a 3D space. It extends the
 * Polygon class, which is a more general representation of a polygon.
 */
public class Triangle extends Polygon {

	/**
	 * Constructs a Triangle with three points.
	 * 
	 * @param p1- the first point of the triangle
	 * @param p2- the second point of the triangle
	 * @param p3- the third point of the triangle
	 */
	public Triangle(Point p1, Point p2, Point p3) {
		super(p1, p2, p3);
	}

	/**
	 * Find the intersection points of the given ray with the triangle.
	 * 
	 * @param ray- the ray to check for intersections
	 * @return list of intersection points or null if there are no intersections
	 */
    @Override
    public List<Point> findIntersections(Ray ray) {
        // Step 1: Find intersection with the plane
        Plane plane = new Plane(vertices.get(0), vertices.get(1), vertices.get(2));
        List<Point> planeIntersections = plane.findIntersections(ray);

        if (planeIntersections == null) {
            return null; // No intersection with the plane
        }

        Point p = planeIntersections.get(0);

        // Step 2: Check if the intersection point is inside the triangle
        Vector v1 = vertices.get(0).subtract(ray.getHead());
        Vector v2 = vertices.get(1).subtract(ray.getHead());
        Vector v3 = vertices.get(2).subtract(ray.getHead());

        Vector n1 = v1.crossProduct(v2).normalize();
        Vector n2 = v2.crossProduct(v3).normalize();
        Vector n3 = v3.crossProduct(v1).normalize();

        Vector pVector = p.subtract(ray.getHead());

        double s1 = alignZero(pVector.dotProduct(n1));
        double s2 = alignZero(pVector.dotProduct(n2));
        double s3 = alignZero(pVector.dotProduct(n3));

        if ((s1 > 0 && s2 > 0 && s3 > 0) || (s1 < 0 && s2 < 0 && s3 < 0)) {
            return List.of(p);
        }

        return null; // The intersection point is outside the triangle
    }

}
