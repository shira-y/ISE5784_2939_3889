package geometries;

import primitives.Point;
import primitives.Vector;

public class Cylinder {
private final double height;

public Cylinder(double height) {
	this.height = height;
}
public default Vector getNormal(Point p) {
	return null;
}
}
