package geometries;

import primitives.*;

/**
 * An interface named Geometry for some geometric body
 */
public interface Geometry {

	/**
	 * A function that receives one parameter of the point type [on the surface of
	 * the geometric body]
	 * 
	 * @param p the point on the surface of the Geometry
	 * @return the normal vector to the body at this point.
	 */
	public default Vector getNormal(Point p) {
		return null;
	}

}
