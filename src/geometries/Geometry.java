package geometries;

import primitives.Point;
import primitives.Vector;

public interface Geometry {
public default Vector getNormal(Point p) {
	return null;
}

}
