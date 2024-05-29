package geometries;

import java.util.List;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

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

	public List<Point> findIntersections(Ray ray) {
		return null;
	}
}
