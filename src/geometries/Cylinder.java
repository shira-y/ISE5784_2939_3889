package geometries;

import java.util.List;

import primitives.*;

/**
 * The Cylinder class represents a cylinder shape in 3D space. It extends the
 * Tube class, adding a height attribute to the tube.
 */
public class Cylinder extends Tube {
	/**
	 * height: The height of the cylinder.
	 */
	@SuppressWarnings("unused")
	private final double height;

	/**
	 * Constructs a Cylinder with a given height, radius, and axis ray.
	 * 
	 * @param height- the height of the cylinder
	 * @param radius- the radius of the base of the cylinder
	 * @param ray-    the axis ray of the cylinder
	 */
	public Cylinder(double height, double radius, Ray ray) {
		super(radius, ray);
		this.height = height;
	}

	/**
	 * Calculates the normal vector to the surface of the cylinder at a given point.
	 * 
	 * @param p the point on the surface of the cylinder
	 * @return the normal vector at the given point
	 */
	@Override
	public Vector getNormal(Point p) {
		return null;
	}

	public List<Point> findIntersections(Ray ray) {
		return null;// bonus
	}
}
