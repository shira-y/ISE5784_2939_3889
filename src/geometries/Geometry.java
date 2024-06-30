package geometries;

import primitives.*;

/**
 * An abstract class representing a geometric body.
 */
public abstract class Geometry extends Intersectable {
	/**
	 * The emission color of the geometry.
	 */
	protected Color emission = Color.BLACK;

	/**
	 * The material of the geometry.
	 */
	private Material material = new Material();

	/**
	 * A function that receives a point on the surface of the geometric body and
	 * returns the normal vector to the body at this point.
	 * 
	 * @param p the point on the surface of the Geometry
	 * @return the normal vector to the body at this point
	 */
	public abstract Vector getNormal(Point p);

	/**
	 * Gets the material of the geometry.
	 * 
	 * @return the material of the geometry
	 */
	public Material getMaterial() {
		return material;
	}

	/**
	 * Sets the material of the geometry.
	 * 
	 * @param material the material to set
	 * @return the geometry itself
	 */
	public Geometry setMaterial(Material material) {
		this.material = material;
		return this;
	}

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
	 * @param emission the emission color to set
	 * @return the geometry itself
	 */
	public Geometry setEmission(Color emission) {
		this.emission = emission;
		return this;
	}
}
