package geometries;

/**
 * RadialGeometry abstract class that implements the Geometry interface
 */
public abstract class RadialGeometry extends Geometry {
	/**
	 * The radius of the radial geometry.
	 */
	protected final double radius;
	/**
	 * The squared radius of the radial geometry.
	 */
	protected final double radiusSquared;

	/**
	 * A constructor that initializes the field.
	 * 
	 * @param newRadius - accepts a value for the radius as a parameter
	 */
	public RadialGeometry(double newRadius) {
		this.radius = newRadius;
		this.radiusSquared = newRadius * newRadius;
	}

}
