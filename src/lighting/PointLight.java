package lighting;

import java.util.LinkedList;
import java.util.List;

import primitives.*;

/**
 * PointLight class represents a light source that emits light uniformly in all
 * directions from a specific point in space.
 */
public class PointLight extends Light implements LightSource {
    protected final Point position;

    private double kC = 1;
    private double kL = 0;
    private double kQ = 0;
    private double radius = 100d;

    public PointLight(Color intensity, Point position, double radius) {
        super(intensity);
        this.position = position;
        this.radius = radius;
    }

    public PointLight(Color intensity, Point position) {
        super(intensity);
        this.position = position;
    }

    public PointLight setRadius(double radius) {
        this.radius = radius;
        return this;
    }

    public PointLight setKc(double kC) {
        this.kC = kC;
        return this;
    }

    public PointLight setKl(double kL) {
        this.kL = kL;
        return this;
    }

    public PointLight setKq(double kQ) {
        this.kQ = kQ;
        return this;
    }

    @Override
    public Color getIntensity(Point p) {
        double dist = position.distance(p);
        return intensity.scale(1 / (kC + kL * dist + kQ * dist * dist));
    }

    @Override
    public Vector getL(Point p) {
        return p.subtract(position).normalize();
    }

    @Override
    public double getDistance(Point point) {
        return position.distance(point);
    }

    @Override
    public List<Vector> getListL(Point p) {
        List<Vector> vectors = new LinkedList<>();
        int numSamples = 10; // Number of samples per axis, change this value to adjust the quality

        for (int i = 0; i < numSamples; i++) {
            for (int j = 0; j < numSamples; j++) {
                double offsetX = (Math.random() - 0.5) * 2 * radius;
                double offsetY = (Math.random() - 0.5) * 2 * radius;
                double offsetZ = (Math.random() - 0.5) * 2 * radius;
                Point samplePoint = position.add(new Vector(offsetX, offsetY, offsetZ));
                vectors.add(p.subtract(samplePoint).normalize());
            }
        }
        return vectors;
    }

	@Override
	public List<Ray> getAreaLightRays(Point p, int numRays) {
		// TODO Auto-generated method stub
		return null;
	}
}