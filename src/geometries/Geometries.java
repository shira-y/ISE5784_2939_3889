package geometries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import primitives.*;

/**
 * A class for a collection of geometric bodies
 */
public class Geometries extends Intersectable {
	/**
	 * Private immutable field for the list of geometries
	 */
	private final List<Intersectable> geometries = new LinkedList<>();

	/**
	 * Default constructor
	 */
	public Geometries() {
	}

	/**
	 * Constructor with the following signature
	 * 
	 * @param geometries- the list of geometries
	 */
	public Geometries(Intersectable... geometries) {
		add(geometries);
	}

	/**
	 * Method to add geometries
	 * 
	 * @param geometries- the list of geometries
	 */
	public void add(Intersectable... geometries) {
		Collections.addAll(this.geometries, geometries);
	}

	/**
	 * Finds all intersection points of the given ray with all geometries in the
	 * collection.
	 *
	 * @param ray - the ray to check for intersections.
	 * @return list of intersection points or null if there are no intersections.
	 */
	@Override
	public List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
		List<GeoPoint>  intersections = null;

		for (Intersectable geometry : geometries) {
			List<GeoPoint> tempIntersections = geometry.findGeoIntersections(ray);

			if (tempIntersections != null) {
				if (intersections == null)
					intersections = new LinkedList<>(tempIntersections);
				else
					intersections.addAll(tempIntersections);
			}
		}

		return intersections;
	}
	


}
