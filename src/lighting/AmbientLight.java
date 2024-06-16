package lighting;

import primitives.Color;
import primitives.Double3;

/**
 * Class representing ambient light in a scene.
 */
public class AmbientLight {

	/**
	 * Private field for the intensity of ambient light
	 */
	private final Color intensity;

	/**
	 * Constructor for AmbientLight
	 * 
	 * @param iA the original light color intensity
	 * @param kA the attenuation factor
	 */
	public AmbientLight(Color iA, Double3 kA) {
		this.intensity = iA.scale(kA);
	}

	/**
	 * Constructor that accepts the light color and attenuation factor as double
	 * 
	 * @param iA the original light color intensity
	 * @param kA the attenuation factor
	 */
	public AmbientLight(Color iA, double kA) {
		this.intensity = iA.scale(kA);
	}

	/**
	 * field for ambient light with no light
	 */
	public static final AmbientLight NONE = new AmbientLight(Color.BLACK, Double3.ZERO);

	/**
	 * Method to get the intensity of the ambient light
	 * 
	 * @return the intensity of the ambient light
	 */
	public Color getIntensity() {
		return intensity;
	}
}
