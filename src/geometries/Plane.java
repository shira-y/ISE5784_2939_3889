package geometries;

import primitives.Point;
import primitives.Vector;

public class Plane {
	private final Point p;
	private final Vector normalVec;
	public Plane(Point p1, Point p2, Point p3) {
		normalVec = null;
		p = p1;
	}
	public Plane(Point p1, Vector normalVec) {
		this.normalVec = normalVec;
		p = p1;
	}
	public Vector getNormal(Point p) {
		return normalVec;
	}
}
