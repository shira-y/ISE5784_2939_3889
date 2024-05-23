package geometries;

import primitives.*;

/**
 * The Triangle class represents a triangle shape in a 3D space. It extends the
 * Polygon class, which is a more general representation of a polygon.
 */
public class Triangle extends Polygon {

	/**
	 * Constructs a Triangle with three points.
	 * 
	 * @param p1 the first point of the triangle
	 * @param p2 the second point of the triangle
	 * @param p3 the third point of the triangle
	 */
	public Triangle(Point p1, Point p2, Point p3) {
		super(p1, p2, p3);
	}
}
