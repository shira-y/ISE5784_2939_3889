package lighting;

import primitives.Color;
import primitives.Double3;

/**
 * Class representing ambient light in a scene.
 */
public class AmbientLight extends Light {
	/**
	 * field for ambient light with no light
	 */
	public static final AmbientLight NONE = new AmbientLight(Color.BLACK, Double3.ZERO);

	/**
	 * Constructor for AmbientLight
	 * 
	 * @param iA the original light color intensity
	 * @param kA the attenuation factor
	 */
	public AmbientLight(Color iA, Double3 kA) {
		super(iA.scale(kA));
	}

	/**
	 * Constructor that accepts the light color and attenuation factor as double
	 * 
	 * @param iA the original light color intensity
	 * @param kA the attenuation factor
	 */
	public AmbientLight(Color iA, double kA) {
		super(iA.scale(kA));
	}

}
