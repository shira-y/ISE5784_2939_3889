package geometries;

import java.util.List;

import primitives.*;

/**
 * An interface where there is a method for finding intersection points with the
 * geometries
 */
public interface Intersectable {

	/**
	 * Finds all intersection points between the given ray and the geometry.
	 *
	 * @param ray- the ray to intersect with the geometry
	 * @return a list of points that representing the intersection points, or null
	 *         if there are no intersections.
	 */
	List<Point> findIntersections(Ray ray);

}
