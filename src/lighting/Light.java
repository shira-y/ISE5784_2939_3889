package lighting;

import primitives.*;

/**
 * Class representing light in a scene
 */
public abstract class Light {
	/**
	 * The intensity (color) of the light.
	 */
	protected final Color intensity;

	/**
	 * Constructor for the light
	 * 
	 * @param c color of the light
	 */
	protected Light(Color c) {
		this.intensity = c;
	}

	/**
	 * Method to get the intensity of the light
	 * 
	 * @return intensity of the light
	 */
	public Color getIntensity() {
		return intensity;
	}
}
