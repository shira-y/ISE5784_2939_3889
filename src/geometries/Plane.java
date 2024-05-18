package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * Plane class which includes a point in space and a vertical vector
 */
public class Plane {
	private final Point p;
	private final Vector normalVec;

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
		normalVec = null;
		p = p1;
	}

	/**
	 * A constructor that accepts as parameters a point and the normal vector
	 * 
	 * @param p1         - point
	 * @param normalVec- the normal vector
	 */
	public Plane(Point p1, Vector normalVec) {
		this.normalVec = normalVec;
		p = p1;
	}

	/**
	 * A function that normalizes the vector
	 * 
	 * @param p- receives a point
	 * @return the normal vector
	 */
	public Vector getNormal(Point p) {
		return normalVec;
	}

	/**
	 * A function that normalizes the vector
	 * 
	 * @return the normal vector
	 */
	public Vector getNormal() {
		return normalVec;
	}

}
