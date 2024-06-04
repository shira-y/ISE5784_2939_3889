package geometries;

import java.util.List;

import primitives.*;

/**
 * A Tube class that includes a radius and a beam
 */
public class Tube extends RadialGeometry {
	/**
	 * ray represents the central axis ray of the tube.
	 */
	protected final Ray ray;

	/**
	 * Constructor for Tube class with a ray and a radius
	 * 
	 * @param radius- receives a radius of the tube.
	 * @param ray-    receives a central axis ray of the tube
	 */
	public Tube(double radius, Ray ray) {
		super(radius);
		this.ray = ray;
	}

	/**
	 * A function that normalizes the vector
	 * 
	 * @param point- receives a point
	 * @return the normal vector
	 */
	public Vector getNormal(Point point) {
		// Project the point onto the tube's axis
		Point p0 = ray.getHead();
		Vector direction = ray.getDirection();

		// Calculate the projection of the point onto the axis ray
		Vector p0ToPoint = point.subtract(p0);
		double scalar = direction.dotProduct(p0ToPoint); // The scalar projection of p0ToPoint on direction
		Point projection = ray.getPoint(scalar);
		// Calculate the normal vector as the vector from the projection point to the
		// given point
		return point.subtract(projection).normalize();
	}

	public List<Point> findIntersections(Ray ray) {
		return null;// bonus
	}
}
