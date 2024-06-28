package lighting;

import primitives.*;

/**
 * SpotLight class represents a spotlight in a scene, which is a type of point light source
 * with a specific direction.
 */
public class SpotLight extends PointLight {

    /**
     * The direction of the spotlight.
     */
    private final Vector direction;

    /**
     * Constructs a spotlight with the given intensity, position, and direction.
     *
     * @param intensity the intensity/color of the spotlight
     * @param position the position of the spotlight in 3D space
     * @param direction the direction of the spotlight (must be normalized)
     */
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
    }

    /**
     * Sets the constant attenuation coefficient for the spotlight.
     *
     * @param d the constant attenuation coefficient to set
     * @return the current SpotLight object
     */
    @Override
    public SpotLight setKc(double d) {
        super.setKc(d);
        return this;
    }

    /**
     * Sets the linear attenuation coefficient for the spotlight.
     *
     * @param d the linear attenuation coefficient to set
     * @return the current SpotLight object
     */
    @Override
    public SpotLight setKl(double d) {
        super.setKl(d);
        return this;
    }

    /**
     * Sets the quadratic attenuation coefficient for the spotlight.
     *
     * @param d the quadratic attenuation coefficient to set
     * @return the current SpotLight object
     */
    @Override
    public SpotLight setKq(double d) {
        super.setKq(d);
        return this;
    }
}
