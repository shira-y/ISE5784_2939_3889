package lighting;

import java.util.ArrayList;
import java.util.List;
import primitives.*;

/**
 * The AreaLight class represents a light source that emits light from a specified area.
 */
public class AreaLight implements LightSource {
    private Point position;
    private Vector u, v;
    private Color intensity;

    /**
     * Constructs an AreaLight with specified position, dimensions, and orientation.
     * 
     * @param intensity the intensity of the light
     * @param position the position of the center of the area light
     * @param width the width of the area light
     * @param height the height of the area light
     * @param u the vector representing one dimension of the area light
     * @param v the vector representing the other dimension of the area light
     */
    public AreaLight(Color intensity, Point position, double width, double height, Vector u, Vector v) {
        this.intensity = intensity;
        this.position = position;
        this.u = u.normalize().scale(width / 2);
        this.v = v.normalize().scale(height / 2);
    }

    @Override
    public double getDistance(Point point) {
        return position.distance(point);
    }

    @Override
    public Color getIntensity(Point p) {
        return intensity;
    }

    @Override
    public Vector getL(Point p) {
        return position.subtract(p).normalize();
    }

    @Override
    public List<Vector> getListL(Point p) {
        List<Vector> directionVectors = new ArrayList<>();
        int numSamples = 81; // For example, a 9x9 grid of sample rays
        for (int i = 0; i < numSamples; i++) {
            double randX = Math.random() - 0.5;
            double randY = Math.random() - 0.5;
            Point samplePoint = position.add(u.scale(randX)).add(v.scale(randY));
            directionVectors.add(samplePoint.subtract(p).normalize());
        }
        return directionVectors;
    }

	@Override
	public List<Ray> getAreaLightRays(Point p, int numRays) {
		// TODO Auto-generated method stub
		return null;
	}

	public SpotLight setKl(double d) {
		// TODO Auto-generated method stub
		return null;
	}
}