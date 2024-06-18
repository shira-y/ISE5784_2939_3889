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

//	/**
//	 * Find the intersection points of the given ray with the sphere.
//	 * 
//	 * @param ray- the ray to check for intersections
//	 * @return list of intersection points or null if there are no intersections
//	 */
//	@Override
//	public List<Point> findGeoIntersections(Ray ray) {
//		Point p0 = ray.getHead();
//		Vector v = ray.getDirection();
//
//		// If the ray's starting point is the same as the sphere's center point,
//		// the intersection point is along the direction of the ray at a distance equal
//		// to the radius of the sphere.
//		if (p0.equals(center))
//			return List.of(ray.getPoint(radius));
//
//		// Vector from the ray's origin to the sphere's center
//		Vector u = center.subtract(p0);
//
//		// Calculate tm (distance from the ray's origin to the sphere's center along the
//		// ray's direction)
//		double tm = v.dotProduct(u);
//
//		// Calculate dSquared (squared distance from the sphere's center to the ray)
//		double dSquared = u.lengthSquared() - tm * tm;
//		double thSquared = radiusSquared - dSquared;
//		// If d^2 > r^2, the ray misses the sphere
//		if (alignZero(thSquared) <= 0)
//			return null;
//
//		// Calculate th (distance from the intersection point to the point closest to
//		// the sphere's center)
//		double th = Math.sqrt(thSquared);
//
//		// Calculate t1 (distance to the first intersection point) and t2 (distance to
//		// the second intersection point)
//		double t2 = tm + th; // t2 is always greater than t1
//		if (alignZero(t2) <= 0)
//			return null;
//
//		double t1 = tm - th;
//		return alignZero(t1) <= 0 //
//				? List.of(ray.getPoint(t2))
//				: List.of(ray.getPoint(t1), ray.getPoint(t2));
//	}
	/**
	 * Method to find the intersections of a sphere
	 */
	@Override
	protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
		Point p0 = ray.getHead();
		Vector v = ray.getDirection();

		// If the ray's starting point is the same as the sphere's center point,
		// the intersection point is along the direction of the ray at a distance equal
		// to the radius of the sphere.
		if (p0.equals(center))
			return List.of(new GeoPoint(this, ray.getPoint(radius)));

		// Vector from the ray's origin to the sphere's center
		Vector u = center.subtract(p0);

		// Calculate tm (distance from the ray's origin to the sphere's center along the
		// ray's direction)
		double tm = v.dotProduct(u);

		// Calculate dSquared (squared distance from the sphere's center to the ray)
		double dSquared = u.lengthSquared() - tm * tm;
		double thSquared = radiusSquared - dSquared;
		// If d^2 > r^2, the ray misses the sphere
		if (alignZero(thSquared) <= 0)
			return null;

		// Calculate th (distance from the intersection point to the point closest to
		// the sphere's center)
		double th = Math.sqrt(thSquared);

		// Calculate t1 (distance to the first intersection point) and t2 (distance to
		// the second intersection point)
		double t2 = tm + th; // t2 is always greater than t1
		if (alignZero(t2) <= 0)
			return null;

		double t1 = tm - th;

		// Return the intersection points as GeoPoints
		if (alignZero(t1) <= 0) {
			return List.of(new GeoPoint(this, ray.getPoint(t2)));
		} else {
			return List.of(new GeoPoint(this, ray.getPoint(t1)), new GeoPoint(this, ray.getPoint(t2)));
		}
	}
}