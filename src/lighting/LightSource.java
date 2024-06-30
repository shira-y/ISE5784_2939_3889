package lighting;

import primitives.*;

/**
 * The LightSource interface represents a light source in a scene that can
 * provide intensity and direction information at a given point.
 */
public interface LightSource {

	/**
	 * Returns the intensity of the light at a specified point in the scene.
	 * 
	 * @param p the point in the scene where the intensity is evaluated
	 * @return the intensity of the light at the specified point
	 */
	public Color getIntensity(Point p);

	/**
	 * Returns the direction vector from the light source to a specified point in
	 * the scene.
	 * 
	 * @param p the point in the scene where the direction vector is evaluated
	 * @return the direction vector from the light source to the specified point
	 */
	public Vector getL(Point p);

}
