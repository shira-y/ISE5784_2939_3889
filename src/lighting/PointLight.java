package lighting;

import java.util.LinkedList;
import java.util.List;


import geometries.Plane;
import static primitives.Util.*;

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
     * square edge size parameter
     */
    private int lengthOfTheSide = 9;

    /**
     * The amount of rays of the soft shadow.
     */
    public static int softShadowsRays = 36;

    /**
     * Setter of the square edge size parameter
     *
     * @param lengthOfTheSide square edge size
     * @return the updated point light
     */
    public PointLight setLengthOfTheSide(int lengthOfTheSide) {
        if (lengthOfTheSide < 0)
            throw new IllegalArgumentException("LengthOfTheSide must be greater then 0");
        this.lengthOfTheSide = lengthOfTheSide;
        return this;
    }

    /**
     * Set the number of `soft shadows` rays
     *
     * @param numOfRays the number of `soft shadows` rays
     * @return the updated camera object
     */
    public PointLight setSoftShadowsRays(int numOfRays) {
        if (numOfRays < 0)
            throw new IllegalArgumentException("numOfRays must be greater then 0!");
        softShadowsRays = numOfRays;
        return this;
    }


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
	 /**
     * Calculates the distance between the shape and a given point.
     *
     * @param point the point to calculate the distance from
     * @return the distance between the shape and the point
     */
	@Override
	public double getDistance(Point point) {
		return position.distance(point);
	}
	
    @Override
    public List<Vector> getLBeam(Point p) {
        if (lengthOfTheSide == 0) return List.of(getL(p));

        List<Vector> vectors = new LinkedList<>();
        // help vectors
        Vector v0, v1;

        // A variable that tells how many divide each side
        double divided = Math.sqrt(softShadowsRays);

        // plane of the light
        Plane plane = new Plane(position, getL(p));

        // vectors of the plane
        List<Vector> vectorsOfThePlane = plane.findVectorsOfPlane();

        // Starting point of the square around the lighting
        Point startPoint = position.add(vectorsOfThePlane.get(0).normalize().scale(-lengthOfTheSide / 2.0))
                .add(vectorsOfThePlane.get(1).normalize().scale(-lengthOfTheSide / 2.0));

        // A loop that runs as the number of vectors and in each of its runs it brings a vector around the lamp
        for (double i = 0; i < lengthOfTheSide; i += lengthOfTheSide / divided) {
            for (double j = 0; j < lengthOfTheSide; j += lengthOfTheSide / divided) {
                v0 = vectorsOfThePlane.get(0).normalize()
                        .scale(random(i, i + lengthOfTheSide / divided));
                v1 = vectorsOfThePlane.get(1).normalize()
                        .scale(random(j, j + lengthOfTheSide / divided));
                vectors.add(p.subtract(startPoint.add(v0).add(v1)).normalize());
            }
        }
        return vectors;
    }
}
