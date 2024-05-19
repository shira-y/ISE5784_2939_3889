package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * A Tube class that includes a radius and a beam
 */
public class Tube {

	private final double radius;
	private final Ray ray;

	/**
	 * Constructor for Tube class with a ray and a radius
	 * 
	 * @param radius- receives a radius
	 * @param ray-    receives a ray
	 */
	public Tube(double radius, Ray ray) {
		this.radius = radius;
		this.ray = ray;
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
