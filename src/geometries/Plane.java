package geometries;

import java.util.LinkedList;
import java.util.List;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import static primitives.Util.*;

/**
 * The {@code Plane} class represents a plane in 3D space, defined by a point on the plane and a normal vector perpendicular to the plane.
 * It provides methods for calculating the normal vector at a point on the plane and for finding intersections between the plane and rays.
 */
public class Plane extends Geometry {
	/**
	 * The reference point on the plane.
	 */
	private final Point p;

	/**
	 * The normal vector perpendicular to the plane.
	 */
	private final Vector normalVector;

	/**
	 * Constructs a {@code Plane} from three points in space. The normal vector is calculated based on these points.
	 * 
	 * @param p1 the first point on the plane
	 * @param p2 the second point on the plane
	 * @param p3 the third point on the plane
	 * 
	 * @throws IllegalArgumentException if the points are collinear or if any two points are identical
	 */
	public Plane(Point p1, Point p2, Point p3) {
		p = p1;
		// Calculate the normal vector using the mathematical model of a normal to a triangle
		Vector v1 = p2.subtract(p1);
		Vector v2 = p3.subtract(p1);
		normalVector = v1.crossProduct(v2).normalize();
	}

	/**
	 * Constructs a {@code Plane} from a point and a normal vector.
	 * 
	 * @param p1 the reference point on the plane
	 * @param normalVector1 the normal vector perpendicular to the plane
	 */
	public Plane(Point p1, Vector normalVector1) {
		this.normalVector = normalVector1.normalize();
		p = p1;
	}

	/**
	 * Returns the normal vector at a given point on the plane.
	 * 
	 * @param p the point on the plane (not used in this implementation, as the normal is constant for a plane)
	 * @return the normal vector of the plane
	 */
	@Override
	public Vector getNormal(Point p) {
		return normalVector;
	}

	/**
	 * Returns the normal vector of the plane.
	 * 
	 * @return the normal vector of the plane
	 */
	public Vector getNormal() {
		return normalVector;
	}

	/**
	 * Finds two vectors on the plane that are orthogonal to the normal vector.
	 * These vectors can be used to define a local coordinate system on the plane.
	 * 
	 * @return a list of vectors on the plane, orthogonal to the normal vector
	 */
	public List<Vector> findVectorsOfPlane() {
        List<Vector> vectors = new LinkedList<>();

        double nX = normalVector.getX(),
               nY = normalVector.getY(),
               nZ = normalVector.getZ();

        double pX = p.getX(),
               pY = p.getY(),
               pZ = p.getZ();

        double d = -(nX * pX + nY * pY + nZ * pZ);

        int amount = 0;
        // Calculate points on the plane to create vectors
        if (nX != 0) {
            double x1 = (d / nX);
            vectors.add((new Point(x1, 0, 0)).subtract(p));
            amount++;
        }
        if (nY != 0) {
            double y2 = (d / nY);
            vectors.add((new Point(0, y2, 0)).subtract(p));
            amount++;
        }
        if (nZ != 0 && amount < 2) {
            double z3 = (d / nZ);
            vectors.add((new Point(0, 0, z3)).subtract(p));
        }
        return vectors;
    }

	/**
	 * Finds the intersection points of a given ray with the plane.
	 * 
	 * @param ray the ray to check for intersections
	 * @return a list of intersection points wrapped in {@code GeoPoint} objects, or {@code null} if there are no intersections
	 */
	@Override
	protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
		Point p0 = ray.getHead();
		Vector v = ray.getDirection();

		// If the ray's origin is on the plane, return null
		if (p0.equals(p))
			return null;

		// Calculate the numerator and denominator for the intersection equation
		double numerator = normalVector.dotProduct(p.subtract(p0));
		double denominator = normalVector.dotProduct(v);

		// If the denominator is zero, the ray is parallel to the plane
		if (isZero(denominator))
			return null;

		// Calculate the intersection distance t
		double t = alignZero(numerator / denominator);

		// If t <= 0, the intersection point is behind the ray's origin or at the origin
		if (t <= 0)
			return null;

		// Calculate the intersection point
		Point intersectionPoint = ray.getPoint(t);

		// Return a list containing the intersection point as a GeoPoint
		return List.of(new GeoPoint(this, intersectionPoint));
	}
}
