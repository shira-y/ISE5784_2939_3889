package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * A sphere class that includes a point and a radius that extends the
 * RadialGeometry class
 */
public class Sphere {

	private final Point center;
	private final double radius;

	/**
	 * Constructor for Sphere
	 * 
	 * @param center- receives a center point
	 * @param radius- receives a radius
	 */
	public Sphere(Point center, double radius) {
		this.center = center;
		this.radius = radius;
	}

	/**
	 * A function that normalizes the vector
	 * 
	 * @param p- receives a point
	 * @return the normal vector
	 */
	public Vector getNormal(Point p) {
		return null;
	}
}
