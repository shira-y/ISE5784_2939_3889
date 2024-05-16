package geometries;

import primitives.Point;
import primitives.Vector;

public abstract class RadialGeometry implements Geometry {
	protected final double radius;
	public RadialGeometry(double newRadius) {
		this.radius = newRadius;
	}
	public default Vector getNormal(Point p) {
		return null;
	}
}
