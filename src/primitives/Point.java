package primitives;

/**
 * Point class is used for a point in space. A point with 3 coordinates.
 */
public class Point {

	/**
	 * A field of the coordinate values of type 3Double
	 */
	protected final Double3 xyz;

	/**
	 * A fixed field that is initialized with a point of the center of the
	 * coordinate system (origin of the axes)
	 */
	public static final Point ZERO = new Point(0, 0, 0);

	/**
	 * Constructor
	 * 
	 * @param xyz- object of type Double3
	 */
	Point(Double3 xyz) {
		this.xyz = xyz;
	}

	/**
	 * Constructor with the values of the three coordinates of type double3
	 * 
	 * @param x- coordinate 1 of type double
	 * @param y- coordinate 2 of type double
	 * @param z- coordinate 3 of type double
	 */
	public Point(double x, double y, double z) {
		xyz = new Double3(x, y, z);
	}

	/**
	 * A function that performs vector subtraction
	 * 
	 * @param newPoint -receives a second point in the parameter
	 * @return a vector from the second point to the point on which the subtraction
	 *         is performed * the action
	 */
	public Vector subtract(Point newPoint) {
		return new Vector(xyz.subtract(newPoint.xyz));
	}

	/**
	 * A function that adds a vector to a point
	 * 
	 * @param vector- receives a vector
	 * @return a new point
	 */
	public Point add(Vector vector) {
		return new Point(xyz.add(vector.xyz));
	}

	/**
	 * A function that calculates the distance between two points in a square
	 * 
	 * @param newPoint- receives a point
	 * @return the distance
	 */
	public double distanceSquared(Point newPoint) {
		double daltaX = (xyz.d1 - newPoint.xyz.d1);
		double deltaY = (xyz.d2 - newPoint.xyz.d2);
		double deltaZ = (xyz.d3 - newPoint.xyz.d3);
		return (daltaX * daltaX + deltaY * deltaY + deltaZ * deltaZ);
	}

	/**
	 * A function that calculates the distance between 2 points
	 * 
	 * @param newPoint- receives a point
	 * @return the distance
	 */
	public double distance(Point newPoint) {
		return (Math.sqrt(distanceSquared(newPoint)));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		return obj instanceof Point other && xyz.equals(other.xyz);
	}

	@Override
	public String toString() {
		return "" + xyz;
	}
}