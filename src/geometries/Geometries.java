package geometries;

import java.util.LinkedList;
import java.util.List;

import primitives.*;

public class Geometries implements Intersectable {
	// Private immutable field for the list of geometries
	private final List<Intersectable> geometries = new LinkedList<>();

	// Default constructor
	public Geometries() {
		// No code needed in the body of the constructor
	}

	// Constructor with the following signature
	public Geometries(Intersectable... geometries) {
		add(geometries);
	}

	// Method to add geometries
	public void add(Intersectable... geometries) {
		for (Intersectable geo : geometries) {
			this.geometries.add(geo);
		}
	}

	/**
	 * Finds all intersection points of the given ray with all geometries in the
	 * collection.
	 *
	 * @param ray - the ray to check for intersections.
	 * @return list of intersection points or null if there are no intersections.
	 */
	@Override
	public List<Point> findIntersections(Ray ray) {
		List<Point> intersections = null;

		// Iterate over all geometries in the collection
		for (Intersectable geometry : geometries) {
			List<Point> tempIntersections = geometry.findIntersections(ray);

			// If intersections are found, add them to the list
			if (tempIntersections != null) {
				if (intersections == null) {
					intersections = new LinkedList<>();
				}
				intersections.addAll(tempIntersections);
			}
		}

		return intersections;
	}

}
