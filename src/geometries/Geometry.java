package geometries;

import primitives.*;

/**
 * An interface named Geometry for some geometric body
 */
public abstract class Geometry extends Intersectable {
	/**
     * The emission color of the geometry
     */
	protected Color emission = Color.BLACK;

	/**
	 * A function that receives one parameter of the point type [on the surface of
	 * the geometric body]
	 * 
	 * @param p the point on the surface of the Geometry
	 * @return the normal vector to the body at this point.
	 */
	public abstract Vector getNormal(Point p);
	
    /**
     * Gets the emission color of the geometry.
     *
     * @return the emission color of the geometry
     */
	public Color getEmission() {
		return emission;
	}
	
	/**
     * Sets the emission color of the geometry.
     *
     * @param emission - the emission color to set
     * @return the geometry itself 
     */
	public Geometry setEmission(Color emission) {
		this.emission = emission;
		return this;
	}

}
