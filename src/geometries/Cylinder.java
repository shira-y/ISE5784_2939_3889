package geometries;

import primitives.*;

/**
 * The Cylinder class represents a cylinder shape in 3D space. It extends the
 * Tube class, adding a height attribute to the tube.
 */
public class Cylinder extends Tube {
	/**
	 * height: The height of the cylinder.
	 */
	private final double height;

	/**
	 * Constructs a Cylinder with a given height, radius, and axis ray.
	 * 
	 * @param height the height of the cylinder
	 * @param radius the radius of the base of the cylinder
	 * @param ray    the axis ray of the cylinder
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
	    Point p0 = this.ray.getHead();
	    Vector dir = this.ray.getDirection();

	    // Project point p onto the axis of the cylinder to find the projection point
	    Vector p0ToP = p.subtract(p0);
	    double t = p0ToP.dotProduct(dir);

	    // If the point is on the bottom base
	    if (t <= 0) {
	        return dir.scale(-1).normalize();
	    }

	    // If the point is on the top base
	    if (t >= height) {
	        return dir.normalize();
	    }

	    // Point is on the curved surface
	    Point o = p0.add(dir.scale((float) t)); // Projection point on the axis
	    Vector normal = p.subtract(o);

	    // Check if normal is zero vector (point lies on the axis)
	    if (normal.length() == 0) {
	        throw new IllegalArgumentException("Point cannot be exactly on the cylinder axis");
	    }

	    return normal.normalize();
	}

}
