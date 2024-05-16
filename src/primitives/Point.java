package primitives;

public class Point {
	protected final Double3 xyz;
	public static final Point ZERO = new Point(0,0,0);
	 /**
     * Constructor
     * @param xyz- object of type Double3
     */
    Point(Double3 xyz) {
        this.xyz=xyz;
    }

    /**
     * Constructor with the values of the three coordinates of type double
     * @param x- coordinate 1 of type double
     * @param y- coordinate 2 of type double
     * @param z- coordinate 3 of type double
     */
    public Point(double x, double y, double z) {
        xyz = new Double3(x, y, z);
    }
    
    /**
     * A function that performs vector subtraction
     * @param newPoint -receives a second point in the parameter
     * @return  -returns a vector from the second point to the point on which the subtraction is performed
     *      * the action
     */

    public Vector subtract(Point newPoint) {
    	return new Vector(xyz.subtract(newPoint.xyz));
    }
    /**
     * A function that adds a vector to a point
     * @param vector- receives a vector
     * @return - returns a new point
     */
    public Point add(Vector vector) {
        return new Point(xyz.add(vector.xyz));
    }

    /**
     * A function that calculates the distance between two points in a square
     * @param newPoint- receives a point
     * @return the distance
     */
    public double distanceSquared(Point newPoint){
        return ((xyz.d1 - newPoint.xyz.d1) * (xyz.d1 - newPoint.xyz.d1)
                + (xyz.d2 - newPoint.xyz.d2) * (xyz.d2 - newPoint.xyz.d2)
                + (xyz.d3 - newPoint.xyz.d3) * (xyz.d3 - newPoint.xyz.d3));
    }

    /**
     * A function that calculates the distance between 2 points
     * @param newPoint- receives a point
     * @return the distance
     */
    public double distance(Point newPoint){
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