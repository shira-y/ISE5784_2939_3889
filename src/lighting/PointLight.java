package lighting;

import java.util.LinkedList;
import java.util.List;

import primitives.*;

/**
 * PointLight class represents a light source that emits light uniformly in all
 * directions from a specific point in space.
 */
public class PointLight extends Light implements LightSource {
	/**
	 * The position of the point light source in 3D space.
	 */
	protected final Point position;

	/**
	 * Constant attenuation coefficient for the point light. Controls how much the
	 * light diminishes with distance.
	 */
	private double kC = 1;

	/**
	 * Linear attenuation coefficient for the point light. Controls how much the
	 * light diminishes linearly with distance.
	 */
	private double kL = 0;

	/**
	 * Quadratic attenuation coefficient for the point light. Controls how much the
	 * light diminishes quadratically with distance.
	 */
	private double kQ = 0;

	/**
	 * The radius for soft shadow sampling.
	 */
	private Double radius = 100d;

	/**
	 * Constructs a PointLight object with the given intensity, position, and
	 * radius.
	 * 
	 * @param intensity the intensity (color) of the light
	 * @param position  the position of the light source
	 * @param radius    the radius for soft shadow sampling
	 */
	public PointLight(Color intensity, Point position, Double radius) {
		super(intensity);
		this.position = position;
		this.radius = radius;
	}

	/**
	 * Constructs a PointLight object with the given intensity and position.
	 * 
	 * @param intensity the intensity (color) of the light
	 * @param position  the position of the light source
	 */
	public PointLight(Color intensity, Point position) {
		super(intensity);
		this.position = position;
	}

	/**
	 * Sets the radius for soft shadow sampling.
	 * 
	 * @param radius the radius to set
	 * @return the PointLight object itself
	 */
	public PointLight setRadius(Double radius) {
		this.radius = radius;
		return this;
	}

	/**
	 * Sets the constant attenuation coefficient of the light.
	 * 
	 * @param kC the constant attenuation coefficient to set
	 * @return the PointLight object itself
	 */
	public PointLight setKc(double kC) {
		this.kC = kC;
		return this;
	}

	/**
	 * Sets the linear attenuation coefficient of the light.
	 * 
	 * @param kL the linear attenuation coefficient to set
	 * @return the PointLight object itself
	 */
	public PointLight setKl(double kL) {
		this.kL = kL;
		return this;
	}

	/**
	 * Sets the quadratic attenuation coefficient of the light.
	 * 
	 * @param kQ the quadratic attenuation coefficient to set
	 * @return the PointLight object itself
	 */
	public PointLight setKq(double kQ) {
		this.kQ = kQ;
		return this;
	}

	/**
	 * Returns the intensity of the light at a specified point in the scene, taking
	 * into account the distance attenuation factors.
	 * 
	 * @param p the point in the scene where the intensity is evaluated
	 * @return the intensity of the light at the specified point
	 */
	public Color getIntensity(Point p) {
		double dist = position.distance(p);
		return intensity.scale(1 / (kC + kL * dist + kQ * dist * dist));
	}

	/**
	 * Returns the direction vector from the light source to a specified point in
	 * the scene.
	 * 
	 * @param p the point in the scene where the direction vector is evaluated
	 * @return the direction vector from the light source to the specified point
	 */
	public Vector getL(Point p) {
		return p.subtract(position).normalize();
	}

	@Override
	public double getDistance(Point point) {
		return position.distance(point);
	}

	/**
	 * Returns a list of direction vectors from the light source to a specified
	 * point in the scene for soft shadow sampling.
	 * 
	 * @param p the point in the scene where the direction vectors are evaluated
	 * @return a list of direction vectors from the light source to the specified
	 *         point
	 */
	@Override
	public List<Vector> getListL(Point p) {
		List<Vector> vectors = new LinkedList<>();
		for (double i = -radius; i < radius; i += radius / 10) {
			for (double j = -radius; j < radius; j += radius / 10) {
				if (i != 0 && j != 0) {
					Point point = position.add(new Vector(i, j, 0.1d));
					if (point.equals(position)) {
						vectors.add(p.subtract(point).normalize());
					} else {
						try {
							if (point.subtract(position).dotProduct(point.subtract(position)) <= radius * radius) {
								vectors.add(p.subtract(point).normalize());
							}
						} catch (Exception e) {
							vectors.add(p.subtract(point).normalize());
						}
					}
				}
			}
		}
		vectors.add(getL(p));
		return vectors;
	}
}
