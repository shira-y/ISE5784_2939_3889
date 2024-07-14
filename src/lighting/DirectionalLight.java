package lighting;

import java.util.List;
import primitives.*;

/**
 * DirectionalLight class represents a light source with a specific direction,
 * which is considered to be at an infinite distance, meaning its direction does
 * not change over the scene.
 */
public class DirectionalLight extends Light implements LightSource {
    private final Vector direction;

    public DirectionalLight(Color color, Vector dir) {
        super(color);
        this.direction = dir.normalize();
    }

    @Override
    public Vector getL(Point p) {
        return direction;
    }

    @Override
    public Color getIntensity(Point p) {
        return getIntensity();
    }

    @Override
    public double getDistance(Point point) {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public List<Vector> getListL(Point p) {
        return List.of(direction);
    }

	@Override
	public List<Ray> getAreaLightRays(Point p, int numRays) {
		// TODO Auto-generated method stub
		return null;
	}
}