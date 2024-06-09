package lighting;

import primitives.Color;
import primitives.Double3;

public class AmbientLight {

    /**
     * Private field for the intensity of ambient light
     */
    private final Color intensity;

    /**
     * Constructor for AmbientLight
     * 
     * @param Ia the original light color intensity
     * @param Ka the attenuation factor
     */
    public AmbientLight(Color Ia, Double3 Ka) {
        this.intensity = Ia.scale(Ka);
    }

    /**
     * Constructor that accepts the light color and attenuation factor as double
     * 
     * @param Ia the original light color intensity
     * @param Ka the attenuation factor
     */
    public AmbientLight(Color Ia, double Ka) {
        this.intensity = Ia.scale(Ka);
    }

    /**
     *  field for ambient light with no light
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
