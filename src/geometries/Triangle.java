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
	 * @param ray - the ray to check for intersections
	 * @return list of intersection points or null if there are no intersections
	 */
	@Override
	public List<Point> findIntersections(Ray ray) {
		// Step 1: Find intersection with the plane
		// Find intersections of the ray with the plane
		List<Point> planeIntersections = plane.findIntersections(ray);
		// If there are no intersections with the plane, return null
		if (planeIntersections == null)
			return null;

		// Get the first intersection point with the plane
		Point p = planeIntersections.getFirst();
		Vector pVector = p.subtract(ray.getHead());

		// Step 2: Check if the intersection point is inside the triangle
		// Calculate vectors from the ray's origin to each vertex of the triangle
		Vector v1 = vertices.get(0).subtract(ray.getHead());
		Vector v2 = vertices.get(1).subtract(ray.getHead());
		// Calculate normal vectors of each edge of the triangle using cross product and
		// normalize them
		Vector n1 = v1.crossProduct(v2).normalize();
		// Calculate the dot product of each normal vector with the vector from the
		// ray's origin to the intersection point
		double s1 = alignZero(pVector.dotProduct(n1));
		if (s1 == 0) return null;
		
		Vector v3 = vertices.get(2).subtract(ray.getHead());
		Vector n2 = v2.crossProduct(v3).normalize();
		double s2 = alignZero(pVector.dotProduct(n2));
		if (s1 * s2 <= 0) return null;
		
		Vector n3 = v3.crossProduct(v1).normalize();
		double s3 = alignZero(pVector.dotProduct(n3));
		if (s1 * s3 <= 0) return null;

		// If all dot products have the same sign, the intersection point is inside the
		// triangle
		return List.of(p);
	}
}