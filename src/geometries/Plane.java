package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * Plane class which includes a point in space and a vertical vector
 */
public class Plane implements Geometry {
	/**
	 * p1: The reference point on the plane. normalVector: The normal vector
	 * perpendicular to the plane.
	 */
	private final Point p;
	private final Vector normalVector;

	/**
	 * A constructor who receives 3 points in the parameters and should calculate
	 * the normal. Also, the constructor will keep the points as the reference point
	 * of the plane
	 * 
	 * @param p1- point 1 in the parameters
	 * @param p2- point 2 in the parameters
	 * @param p3- point 3 in the parameters
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
	 * @param p1-           point
	 * @param normalVector- the normal vector
	 */
	public Plane(Point p1, Vector normalVec) {
		this.normalVector = normalVec.normalize();
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

}
