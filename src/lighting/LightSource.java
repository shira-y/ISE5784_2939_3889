package lighting;

import java.util.List;

import primitives.*;


/**
 * The LightSource interface represents a light source in a scene that can
 * provide intensity and direction information at a given point.
 */
public interface LightSource {
	/**
	 * Calculates the distance from the light source to a given point. This method
	 * is useful for determining the attenuation of light based on the distance from
	 * the light source.
	 *
	 * @param point The point to which the distance is calculated.
	 * @return The distance from the light source to the specified point.
	 */

	double getDistance(Point point);

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
	  /**
     * Creates a list of vectors from the given point to random points around the light within radius
     * for soft shadows
     *
     * @param p the given point
     * @param r the radius
     * @param amount the amount of vectors to create
     * @return list of vectors
     */
    List<Vector> getLCircle(Point p, double r, int amount);

	
}
