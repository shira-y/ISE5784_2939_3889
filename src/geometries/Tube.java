package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

public class Tube {

private final double radius;
private final Ray ray;

public Tube(double radius, Ray ray) {
	this.radius = radius;
	this.ray = ray;
}
public default Vector getNormal(Point p) {
	return null;
}
}
