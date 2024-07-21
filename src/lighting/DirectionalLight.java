package lighting;

import java.util.List;

import primitives.*;


/**
 * DirectionalLight class represents a light source with a specific direction,
 * which is considered to be at an infinite distance, meaning its direction does
 * not change over the scene.
 */
public class DirectionalLight extends Light implements LightSource {
	/**
	 * The direction vector of the directional light, normalized to unit length.
	 */
	private final Vector direction;

	/**
	 * Constructor for directional light.
	 * 
	 * @param color intensity of the light
	 * @param dir   direction of the light
	 */
	public DirectionalLight(Color color, Vector dir) {
		super(color);
		this.direction = dir.normalize();
	}

	@Override
	public Vector getL(Point p) {
		return direction.normalize();
	}

	@Override
	public Color getIntensity(Point p) {
		return getIntensity();
	}

	@Override
	public double getDistance(Point point) {
		return Double.POSITIVE_INFINITY;
	}

	  /**
     * Gets vectors from the given point to the light source
     *
     * @param p the point
     * @return all vectors who created
     */
    public List<Vector> getLBeam(Point p) {
        return List.of(getL(p));
    }

   

}