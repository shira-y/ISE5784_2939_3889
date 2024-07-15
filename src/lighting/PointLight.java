package lighting;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import static primitives.Util.isZero;
import static java.lang.Math.sqrt;
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
	   //for random number of rays to create for soft shadows
    private static final Random RND = new Random();
    @Override
    public List<Vector> getLCircle(Point p, double r, int amount) {
        if (p.equals(position))
            return null;

        List<Vector> result = new LinkedList<>();

        Vector l = getL(p); //vector to the center of the point light
        result.add(l);

        if (amount < 2) {
            return result;
        }

        Vector vAcross;
        //if l is parallel to z axis, then the normal is across z on x-axis
        if (isZero(l.getX()) && isZero(l.getY())) {
            //switch z and x places
            vAcross = new Vector(0, 0, -1 * l.getZ()).normalize();
            //otherwise get the normal using x and y
        } else {//switched x and y places
            vAcross = new Vector(l.getX(),-1 * l.getY(),  0).normalize();
        }

        //the vector to the other direction
        Vector vForward = vAcross.crossProduct(l).normalize();

        double cosAngle, sinAngle, moveX, moveY, d;

        for (int i = 0; i < amount; i++) {
            Point movedPoint = this.position;

            //random cosine of angle between (-1,1)
            cosAngle = 2 * RND.nextDouble() - 1;

            //sin(angle)=1-cos^2(angle)
            sinAngle = sqrt(1 - cosAngle * cosAngle);

            //d is between (-r,r)
            d = r * (2 * RND.nextDouble() - 1);
            //if we got 0 then try again, because it will just be the same as the center
            if (isZero(d)) {
                i--;
                continue;
            }

            //says how much to move across and down
            moveX = d * cosAngle;
            moveY = d * sinAngle;

            //moving the point according to the value
            if (!isZero(moveX)) {
                movedPoint = movedPoint.add(vAcross.scale(moveX));
            }
            if (!isZero(moveY)) {
                movedPoint = movedPoint.add(vForward.scale(moveY));
            }

            //adding the vector from the new point to the light position
            result.add(p.subtract(movedPoint).normalize());
        }
        return result;
    }

	
}
