package geometries;

import primitives.Point;
import primitives.Vector;

public class Triangle extends Polygon{
	public default Vector getNormal(Point p) {
		return null;
	}
}
