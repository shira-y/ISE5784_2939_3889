package primitives;

/**
 * Vector class is used for a kind of point, defined by the end point. The
 * actual class inherits from the Point class.
 */
public class Vector extends Point {

	/**
	 * Constructor with the values of the three coordinates of type double
	 * 
	 * @param x- coordinate 1 of type double
	 * @param y- coordinate 2 of type double
	 * @param z- coordinate 3 of type double
	 */
	public Vector(double x, double y, double z) {
		super(x, y, z);
		if (xyz.equals(Double3.ZERO))
			throw new IllegalArgumentException("This is the zero vector");
	}

	/**
	 * Constructor
	 * 
	 * @param xyz- an object of type Double3
	 */
	public Vector(Double3 xyz) {
		this(xyz.d1, xyz.d2, xyz.d3);
		if (xyz.equals(Double3.ZERO))
			throw new IllegalArgumentException("This is the zero vector");
	}

	/**
	 * A function that performs vector addition
	 * 
	 * @param vector- receives a vector
	 * @return a new vector
	 */
	public Vector add(Vector vector) {
		return new Vector(xyz.add(vector.xyz));
	}

	/**
	 * A function that multiplies a vector by a number - a scalar
	 * 
	 * @param number- receives a number as the scalar
	 * @return a new vector
	 */
	public Vector scale(float number) {
		return new Vector(xyz.scale(number));
	}

	/**
	 * A function that performs scalar multiplication
	 * 
	 * @param vector- receives a vector to be duplicated
	 * @return the result
	 */
	public double dotProduct(Vector vector) {
		return (xyz.d1 * vector.xyz.d1) + (xyz.d2 * vector.xyz.d2) + (xyz.d3 * vector.xyz.d3);
	}

	/**
	 * A function that performs vector multiplication
	 * 
	 * @param vector- receives a vector
	 * @return a new vector perpendicular to the two existing vectors
	 */
	public Vector crossProduct(Vector vector) {
		return new Vector(this.xyz.d2 * vector.xyz.d3 - this.xyz.d3 * vector.xyz.d2,
				this.xyz.d3 * vector.xyz.d1 - this.xyz.d1 * vector.xyz.d3,
				this.xyz.d1 * vector.xyz.d2 - this.xyz.d2 * vector.xyz.d1);
	}

	/**
	 * A function that calculates the squared length of the vector
	 * 
	 * @return the length
	 */
	public double lengthSquared() {
		return (xyz.d1 * xyz.d1 + xyz.d2 * xyz.d2 + xyz.d3 * xyz.d3);
	}

	/**
	 * A function that calculates the length of the vector
	 * 
	 * @return length of the vector
	 */
	public double length() {
		return (Math.sqrt(lengthSquared()));
	}

	/**
	 * A function that returns a new normalized vector
	 * 
	 * @return - a unit vector in the same direction as the original vector
	 */
	public Vector normalize() {
		return new Vector(xyz.reduce(this.length()));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		return obj instanceof Vector other && super.equals(other);
	}

	@Override
	public String toString() {
		return "->" + super.toString();
	}

}
