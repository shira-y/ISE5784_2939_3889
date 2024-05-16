package geometries;

/**
 * RadialGeometry abstract class that implements the Geometry interface
 */
public abstract class RadialGeometry implements Geometry {
	protected final double radius;
	
	/**
     * A constructor that initializes the field.
     * @param radius- accepts a value for the radius as a parameter
     */
	public RadialGeometry(double newRadius) {
		this.radius = newRadius;
	}

}
