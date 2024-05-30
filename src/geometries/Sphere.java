package geometries;

import java.util.List;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import static primitives.Util.*;

/**
 * A sphere class that includes a point and a radius that extends the
 * RadialGeometry class
 */
public class Sphere extends RadialGeometry {

	/**
	 * the center of the sphere
	 */
	private final Point center;

	/**
	 * Constructor for Sphere
	 * 
	 * @param center- receives a center point
	 * @param radius- receives a radius
	 */
	public Sphere(Point center, double radius) {
		super(radius);
		this.center = center;
	}

	/**
	 * A function that normalizes the vector
	 * 
	 * @param point- receives a point
	 * @return the normal vector
	 */
	public Vector getNormal(Point point) {
		return point.subtract(center).normalize();
	}


    @Override
    public List<Point> findIntersections(Ray ray) {
        Point p0 = ray.getHead();
        Vector v = ray.getDirection();

        // Vector from the ray's origin to the sphere's center
        Vector u = center.subtract(p0);

        // tm = v · u
        double tm = alignZero(v.dotProduct(u));

        // d = sqrt(|u|^2 - tm^2)
        double dSquared = u.lengthSquared() - tm * tm;
        double rSquared = radius * radius;

        // If d^2 >= r^2, no intersections
        if (alignZero(dSquared - rSquared) >= 0) {
            return null;
        }

        // th = sqrt(r^2 - d^2)
        double th = Math.sqrt(rSquared - dSquared);

        // t1 = tm - th
        double t1 = alignZero(tm - th);
        // t2 = tm + th
        double t2 = alignZero(tm + th);

        // Only consider t > 0
        if (t1 > 0 && t2 > 0) {
            Point p1 = p0.add(v.scale(t1));
            Point p2 = p0.add(v.scale(t2));
            return List.of(p1, p2);
        } else if (t1 > 0) {
            Point p1 = p0.add(v.scale(t1));
            return List.of(p1);
        } else if (t2 > 0) {
            Point p2 = p0.add(v.scale(t2));
            return List.of(p2);
        }

        return null;
    }
}
