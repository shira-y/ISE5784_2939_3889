package geometries;

import java.util.List;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import static primitives.Util.*;

/**
 * Plane class which includes a point in space and a vertical vector
 */
public class Plane extends Geometry {
	/**
	 * p: The reference point on the plane.
	 */
	private final Point p;
	/**
	 * normalVector: The normal vector perpendicular to the plane.
	 */
	private final Vector normalVector;

	/**
	 * A constructor who receives 3 points in the parameters and should calculate
	 * the normal. Also, the constructor will keep the points as the reference point
	 * of the plane
	 * 
	 * @param p1- point 1 in the parameters
	 * @param p2- point 2 in the parameters
	 * @param p3- point 3 in the parameters
	 * 
	 * @throws IllegalArgumentException when there is a pair of converged points or
	 *                                  the points are co-linear
	 */
	public Plane(Point p1, Point p2, Point p3) {
		p = p1;
		// Calculate the normal vector using the mathematical model of a normal to a
		// triangle
		Vector v1 = p2.subtract(p1);
		Vector v2 = p3.subtract(p1);
		normalVector = v1.crossProduct(v2).normalize();
	}

	/**
	 * A constructor that accepts as parameters a point and the normal vector
	 * 
	 * @param p1-            point
	 * @param normalVector1- the normal vector
	 */
	public Plane(Point p1, Vector normalVector1) {
		this.normalVector = normalVector1.normalize();
		p = p1;
	}

	@Override
	public Vector getNormal(Point p) {
		return normalVector;
	}

	/**
	 * A function that normalizes the vector
	 * 
	 * @return the normal vector
	 */
	public Vector getNormal() {
		return normalVector;
	}

	/**
	 * Find the intersection points of the given ray with the plane.
	 * 
	 * @param ray the ray to check for intersections
	 * @return list of intersection points or null if there are no intersections
	 */


	@Override
	protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
		Point p0 = ray.getHead();
		Vector v = ray.getDirection();

		// If the intersection point is the same as the plane's reference point, return
		// null
		if (p0.equals(p))
			return null;

		// Calculate the numerator and denominator
		double numerator = normalVector.dotProduct(p.subtract(p0));
		double denominator = normalVector.dotProduct(v);

		// Check if denominator is zero (ray is parallel to the plane)
		if (isZero(denominator))
			return null;

		// Calculate t
		double t = alignZero(numerator / denominator);

		// If t <= 0, the intersection point is behind the ray's origin or at the origin
		if (t <= 0)
			return null;

		// Calculate the intersection point
		Point intersectionPoint = ray.getPoint(t);

		// Return a list containing the GeoPoint
		return List.of(new GeoPoint(this, intersectionPoint));
	}

}
