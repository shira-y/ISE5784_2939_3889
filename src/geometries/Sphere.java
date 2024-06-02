package geometries;

import java.util.ArrayList;
import java.util.List;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import static primitives.Util.*;

/**
 * A sphere class that includes a point and a radius that extends the
 * RadialGeometry class
 */
public class Sphere extends RadialGeometry {

	/**
	 * the center of the sphere
	 */
	private final Point center;

	/**
	 * Constructor for Sphere
	 * 
	 * @param center- receives a center point
	 * @param radius- receives a radius
	 */
	public Sphere(Point center, double radius) {
		super(radius);
		this.center = center;
	}

	/**
	 * A function that normalizes the vector
	 * 
	 * @param point- receives a point
	 * @return the normal vector
	 */
	public Vector getNormal(Point point) {
		return point.subtract(center).normalize();
	}

	/**
	 * Find the intersection points of the given ray with the sphere.
	 * 
	 * @param ray- the ray to check for intersections
	 * @return list of intersection points or null if there are no intersections
	 */
	@Override
	public List<Point> findIntersections(Ray ray) {
		Point p0 = ray.getHead();
		Vector v = ray.getDirection();

		// If the ray's starting point is the same as the sphere's center point,
		// the intersection point is along the direction of the ray at a distance equal
		// to the radius of the sphere.
		if (p0.equals(center)) {
			Point intersection = ray.getPoint(radius);
			return List.of(intersection);
		}

		// Vector from the ray's origin to the sphere's center
		Vector u = center.subtract(p0);

		// Calculate tm (distance from the ray's origin to the sphere's center along the
		// ray's direction)
		double tm = v.dotProduct(u);

		// If tm is negative and the ray starts outside the sphere, there are no
		// intersections
		if (tm < 0 && alignZero(u.length() - radius) != 0) {
			return null;
		}

		// Calculate dSquared (squared distance from the sphere's center to the ray)
		double dSquared = u.lengthSquared() - tm * tm;

		// If d^2 > r^2, the ray misses the sphere
		if (dSquared > radius * radius) {
			return null;
		}

		// Calculate th (distance from the intersection point to the point closest to
		// the sphere's center)
		double th = Math.sqrt(radius * radius - dSquared);

		// Calculate t1 (distance to the first intersection point) and t2 (distance to
		// the second intersection point)
		double t1 = tm - th;
		double t2 = tm + th;

		// Create a list to store the intersection points
		List<Point> intersections = new ArrayList<>();

		// Check if t1 is valid (t1 > 0 and the ray starts outside the sphere)
		if (t1 > 0 && alignZero(u.length() - radius) != 0) {
			intersections.add(ray.getPoint(t1));
		}

		// Check if t2 is valid (t2 > 0)
		if (t2 > 0) {
			intersections.add(ray.getPoint(t2));
		}

		// Return the list of intersection points
		return intersections.isEmpty() ? null : intersections;
	}
}