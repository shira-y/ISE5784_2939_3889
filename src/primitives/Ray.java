package primitives;

import static primitives.Util.*;

import java.util.List;
import geometries.Intersectable.GeoPoint;

/**
 * Ray class is used for the set of points on a line that are on one side of a
 * given point on the line called the head of the fund. Defined by point and
 * direction/
 */
public class Ray {
	/**
	 * head- the head point of the ray
	 */
	private final Point head;
	/**
	 * direction- the direction vector of the ray
	 */
	private final Vector direction;
	/**
	 * Small offset to move the ray's origin to avoid self-intersection.
	 */
	private static final double DELTA = 0.1;

	/**
	 * A constructor with a point and a direction vector
	 * 
	 * @param head      the head point of the ray
	 * @param direction the direction vector of the ray
	 */
	public Ray(Point head, Vector direction) {
		this.head = head;
		this.direction = direction.normalize();
	}

	/**
	 * Constructor to initialize ray
	 *
	 * @param head      point of the ray
	 * @param n         normal vector
	 * @param direction direction vector of the ray
	 */
	public Ray(Point head, Vector direction, Vector n) {
		double delta = direction.dotProduct(n) >= 0 ? DELTA : -DELTA;
		this.head = head.add(n.scale(delta));
		this.direction = direction;
	}

	/**
	 * a getter method for head
	 * 
	 * @return the head
	 */
	public Point getHead() {
		return head;
	}

	/**
	 * a getter method for direction
	 * 
	 * @return the direction
	 */
	public Vector getDirection() {
		return direction;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		return (obj instanceof Ray other) && //
				this.head.equals(other.head) && this.direction.equals(other.direction);

	}

	@Override
	public String toString() {
		return "Ray{" + head + direction + '}';
	}

	/**
	 * Calculates a point on the ray at a given distance from the head of the ray.
	 *
	 * @param t the distance from the head of the ray
	 * @return the point on the ray at the given distance
	 */
	public Point getPoint(double t) {
		return isZero(t) ? head : head.add(direction.scale(t));
	}

	/**
	 * Finds the closest point to the ray's origin from a list of points.
	 *
	 * @param points- the list of points
	 * @return the closest point to the ray's origin, or null if the list is empty
	 */
	public Point findClosestPoint(List<Point> points) {
		return points == null || points.isEmpty() ? null
				: findClosestGeoPoint(points.stream().map(p -> new GeoPoint(null, p)).toList()).point;
	}

	/**
	 * returns the closest geo point from list of GeoPoints
	 *
	 * @param intersections GeoPoints to check
	 * @return closest GeoPoint
	 */
	public GeoPoint findClosestGeoPoint(List<GeoPoint> intersections) {
		if (intersections == null || intersections.isEmpty())
			return null;

		GeoPoint closest = null;
		double closestDistance = Double.POSITIVE_INFINITY;
		for (GeoPoint point : intersections) {
			if (point.point.distanceSquared(head) < closestDistance) {
				closest = point;
				closestDistance = point.point.distanceSquared(head);
			}
		}
		return closest;
	}
}
