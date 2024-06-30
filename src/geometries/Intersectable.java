package geometries;

import java.util.List;

import primitives.*;

/**
 * An interface where there is a method for finding intersection points with the
 * geometries
 */
public abstract class Intersectable {

	/**
	 * Finds all intersection points between the given ray and the geometry.
	 *
	 * @param ray- the ray to intersect with the geometry
	 * @return a list of points that representing the intersection points, or null
	 *         if there are no intersections.
	 */

	public final List<Point> findIntersections(Ray ray) {
		var geoList = findGeoIntersections(ray);
		return geoList == null ? null : geoList.stream().map(gp -> gp.point).toList();
	}

	/**
	 * Helper method to find all geometric intersection points between the given ray
	 * and the geometry.
	 *
	 * @param ray - the ray to intersect with the geometry
	 * @return a list of GeoPoint
	 */
	protected abstract List<GeoPoint> findGeoIntersectionsHelper(Ray ray);

	/**
	 * Finds all geometric intersection points between the given ray and the
	 * geometry.
	 *
	 * @param ray - the ray to intersect with the geometry
	 * @return a list of GeoPoint
	 */
	public final List<GeoPoint> findGeoIntersections(Ray ray) {
		return findGeoIntersectionsHelper(ray);
	}

	/**
	 * PDS Geometry Point class
	 */
	public static class GeoPoint {
		/**
		 * The geometry involved in the intersection.
		 */
		public Geometry geometry;
		/**
		 * The point of intersection.
		 */
		public Point point;

		/**
		 * Constructor for GeoPoint
		 * 
		 * @param geometry the geometry
		 * @param point    the point itself
		 */
		public GeoPoint(Geometry geometry, Point point) {
			this.geometry = geometry;
			this.point = point;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			return (obj instanceof GeoPoint other) && //
					(this.point.equals(other.point)) && (this.geometry == other.geometry);
		}

		@Override
		public String toString() {
			return point + " " + geometry;
		}
	}

}
