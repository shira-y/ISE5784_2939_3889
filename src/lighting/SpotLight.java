package lighting;

import primitives.*;
import static primitives.Util.*;

import java.util.LinkedList;
import java.util.List;

/**
 * SpotLight class represents a spotlight in a scene, which is a type of point
 * light source with a specific direction.
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
	 * @param position  the position of the spotlight in 3D space
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

	/**
	 * Returns the intensity of the light at a specified point in the scene, taking
	 * into account the distance attenuation factors.
	 * 
	 * @param p the point in the scene where the intensity is evaluated
	 * @return the intensity of the light at the specified point
	 */
	@Override
	public Color getIntensity(Point p) {
		double dirL = alignZero(direction.dotProduct(getL(p)));
		return dirL <= 0 ? Color.BLACK : super.getIntensity(p).scale(dirL);
	}
	
	@Override
    public List<Ray> getAreaLightRays(Point p, int numRays) {
	    List<Ray> rays = new LinkedList<>();
        if (numRays == 1) {
            rays.add(new Ray(p, getL(p)));
            return rays;
        }

        Vector baseVector1 = direction.findOrthogonalVector().normalize();
        Vector baseVector2 = direction.crossProduct(baseVector1).normalize();

        double radius = 1; // Adjust this value to change the size of the light source
        for (int i = 0; i < numRays; i++) {
            double angle = 2 * Math.PI * i / numRays;
            double x = radius * Math.cos(angle);
            double y = radius * Math.sin(angle);
            Point lightPoint = getPosition().add(baseVector1.scale(x)).add(baseVector2.scale(y));
            rays.add(new Ray(p, lightPoint.subtract(p)));
        }
        return rays;
    }

    // Add this method to get the position of the spotlight
    public Point getPosition() {
        return super.position;
    }

}
