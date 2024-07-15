package renderer;

import primitives.*;
import scene.Scene;
import static geometries.Intersectable.GeoPoint;
import lighting.LightSource;
import static primitives.Util.*;

import java.util.List;

import static primitives.Util.alignZero;

/**
 * SimpleRayTracer class extends the abstract base class RayTracerBase. This
 * class provides basic ray tracing functionality.
 */
public class SimpleRayTracer extends RayTracerBase {

	/**
	 * The maximum recursion level for color calculation. Limits the depth of
	 * recursive reflection and refraction calculations.
	 */
	private static final int MAX_CALC_COLOR_LEVEL = 10;

	/**
	 * The minimum attenuation factor for color calculation. Used to terminate
	 * recursion when the color contribution is negligible.
	 */
	protected static final double MIN_CALC_COLOR_K = 0.001;

	/**
	 * The initial attenuation factor for color calculation. Represents no
	 * attenuation.
	 */
	private static final Double3 INITIAL_K = Double3.ONE;



	/**
	 * Constructs a SimpleRayTracer object with the given scene.
	 *
	 * @param scene The scene to be traced.
	 */
	public SimpleRayTracer(Scene scene) {
		super(scene);
	}




	@Override
	public Color traceRay(Ray ray) {
		GeoPoint closestPoint = findClosestIntersection(ray); // return the closest GeoPoint that the ray hits
		return closestPoint == null ? scene.background : calcColor(closestPoint, ray);
	}

	/**
	 * Calculates the color at a given geometric point, including ambient light.
	 *
	 * @param point The geometric point to calculate the color for.
	 * @param ray   The ray that intersects the geometric point.
	 * @return The calculated color at the point.
	 */
	private Color calcColor(GeoPoint point, Ray ray) {
		return calcColor(point, ray, MAX_CALC_COLOR_LEVEL, INITIAL_K).add(scene.ambientLight.getIntensity());
	}

	/**
	 * Recursively calculates the color at a given geometric point.
	 *
	 * @param geoPoint The geometric point to calculate the color for.
	 * @param ray      The ray that intersects the geometric point.
	 * @param level    The recursion level for calculating reflections and
	 *                 refractions.
	 * @param k        The attenuation coefficient for the ray.
	 * @return The calculated color at the point.
	 */
	private Color calcColor(GeoPoint geoPoint, Ray ray, int level, Double3 k) {
		Color color = calcLocalEffects(geoPoint, ray, k);
		return 1 == level ? color : color.add(calcGlobalEffects(geoPoint, ray, level, k));
	}

	/**
	 * Calculates the global effects (reflections and refractions) on the color at a
	 * given geometric point.
	 *
	 * @param gp    The geometric point to calculate the global effects for.
	 * @param ray   The ray that intersects the geometric point.
	 * @param level The recursion level for calculating reflections and refractions.
	 * @param k     The attenuation coefficient for the ray.
	 * @return The color resulting from the global effects.
	 */
	private Color calcGlobalEffects(GeoPoint gp, Ray ray, int level, Double3 k) {
		Material material = gp.geometry.getMaterial();
		return calcGlobalEffect(constructRefractedRay(gp, ray), level, k, material.kT)
				.add(calcGlobalEffect(constructReflectedRay(gp, ray), level, k, material.kR));
	}

	/**
	 * Calculates the global effect (either reflection or refraction) for a given
	 * ray.
	 *
	 * @param ray   The ray to calculate the global effect for.
	 * @param level The recursion level for calculating reflections and refractions.
	 * @param k     The attenuation coefficient for the ray.
	 * @param kx    The attenuation coefficient for the specific global effect
	 *              (reflection or refraction).
	 * @return The color resulting from the global effect.
	 */
	private Color calcGlobalEffect(Ray ray, int level, Double3 k, Double3 kx) {
		Double3 kkx = k.product(kx);
		if (kkx.lowerThan(MIN_CALC_COLOR_K))
			return Color.BLACK;
		GeoPoint gp = findClosestIntersection(ray);
		return (gp == null ? scene.background : calcColor(gp, ray, level - 1, kkx)).scale(kx);
	}

	/**
	 * Constructs a refracted ray from a given geometric point.
	 *
	 * @param gp  The geometric point to construct the refracted ray from.
	 * @param ray The original ray.
	 * @return The constructed refracted ray.
	 */
	private Ray constructRefractedRay(GeoPoint gp, Ray ray) {
		return new Ray(gp.point, ray.getDirection(), gp.geometry.getNormal(gp.point));
	}

	/**
	 * Constructs a reflected ray from a given geometric point.
	 *
	 * @param gp  The geometric point to construct the reflected ray from.
	 * @param ray The original ray.
	 * @return The constructed reflected ray.
	 */
	private Ray constructReflectedRay(GeoPoint gp, Ray ray) {
		Vector v = ray.getDirection();
		Vector n = gp.geometry.getNormal(gp.point);
		double nv = alignZero(v.dotProduct(n));
		Vector r = v.subtract(n.scale(2d * nv)).normalize();

		return new Ray(gp.point, r, n);
	}

	/**
	 * Finds the closest intersection point between a given ray and the geometries
	 * in the scene.
	 *
	 * @param ray The ray for which to find the closest intersection.
	 * @return The closest intersection point as a {@link GeoPoint}, or null if no
	 *         intersections are found.
	 */
	private GeoPoint findClosestIntersection(Ray ray) {
		List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray);
		return intersections == null ? null : ray.findClosestGeoPoint(intersections);
	}

	   /**
     * method to calculate the local effects of the closest point to the camera
     *
     * @param geoPoint the closest point to the camera
     * @param ray
     * @param k - the level of k
     * @return the color of the closest point to the camera which on the ray
     */
    private Color calcLocalEffects(GeoPoint geoPoint, Ray ray, Double3 k) {
        var color = geoPoint.geometry.getEmission();
        Vector v = ray.getDirection();
        Vector n = geoPoint.geometry.getNormal(geoPoint.point);
        double nv = alignZero(n.dotProduct(v));

        //====================================
        int nShininess = geoPoint.geometry.getMaterial().nShininess;
        Double3 kD = geoPoint.geometry.getMaterial().kD;
        Double3 kS = geoPoint.geometry.getMaterial().kS;

        //====================================
        // if the ray is orthogonal to the point's normal it does not make any effect:
        if (isZero(nv))
            return color;

//        Material mat = geoPoint.geometry.getMaterial();
        for (LightSource light : scene.lights) {
            Vector l = light.getL(geoPoint.point);
            double nl = n.dotProduct(l);

            // Make sure that the perspective (camera) and the light source, are both in
            // the same side of the point's tangent plane sign(nl) == sign(nv)
            if (alignZero(nl * nv) > 0) {
                Double3 ktr = transparency(geoPoint, light, l, n);
                //================================================
                //if soft shadow is not activated, get the regular transparency
                if (!isSoftShadow) {
                    ktr = transparency(geoPoint, light, l, n);
                    //otherwise get the transparency level according to soft shadow
                } else {
                    ktr = transparencySS(geoPoint, light, n);
                }
                if (!(ktr.product(k)).lowerThan(MIN_CALC_COLOR_K)) {
                    Color lightIntensity = light.getIntensity(geoPoint.point).scale(ktr);
                    color = color.add(calcDiffusive(kD, l, n, lightIntensity),
                            calcSpecular(kS, l, n, v, nShininess, lightIntensity));
                }
            }
        }
        return color;

        //=================================================
    }
    private Double3 transparency(GeoPoint geoPoint, LightSource light, Vector l, Vector n) { //++
        Vector lightDirection = l.scale(-1); // from point to light source
        Ray lightRay = new Ray(geoPoint.point, lightDirection, n);
        var intersections = scene.geometries.findGeoIntersections(lightRay);
        if (intersections == null)
            return Double3.ONE;

        Double3 ktr = Double3.ONE;
        for (GeoPoint gp : intersections) {
            ktr = ktr.product(gp.geometry.getMaterial().kT);
            if (ktr.lowerThan(MIN_CALC_COLOR_K))
                return Double3.ZERO;
        }
        return ktr;
    }

    private boolean isSoftShadow = false;

    private int numOfSSRays = 10;


    private double radiusBeamSS = 10;


    public SimpleRayTracer useSoftShadow(boolean flag) {
        this.isSoftShadow = flag;
        return this;
    }

    public SimpleRayTracer setNumOfSSRays(int num) {
        if (num <= 0) {
            throw new IllegalArgumentException(
                    "Number of rays must be greater than 0");
        }

        this.numOfSSRays = num;
        return this;
    }

    public SimpleRayTracer setRadiusBeamSS(double r) {
        if (r <= 0) {
            throw new IllegalArgumentException(
                    "Radius of beam must be greater than 0");
        }
        this.radiusBeamSS = r;
        return this;
    }

    /**
     * Returns transparency level with soft shadow effect
     * Constructs random rays around the light and gets the average of all the levels
     *
     * @param gp the point to check
     * @param ls the current light source
     * @param n  normal to the point
     * @return average ktr
     */
    private Double3 transparencySS(GeoPoint gp, LightSource ls, Vector n) {
        Double3 ktr = Double3.ZERO;
        List<Vector> vecs = ls.getLCircle(gp.point, radiusBeamSS, numOfSSRays);

        for (Vector v : vecs) { //for each vector, add the transparency level there
            ktr = ktr.add(transparency(gp, ls, v, n));
        }

        ktr = ktr.reduce(vecs.size()); //get the average of all the transparency levels of all the vectors

        return ktr;
    }

    /** Calculate the diffuse light effect on the point
     * @param kd diffuse attenuation factor
     * @param l the direction of the light
     * @param n normal from the point
     * @param lightIntensity the intensity of the light source at this point
     * @return the color
     */
    private Color calcDiffusive(Double3 kd, Vector l, Vector n, Color lightIntensity) {
//        double ln = alignZero(abs(l.dotProduct(n))); //ln=|l*n|
//        return lightIntensity.scale(kd.scale(ln)); //Kd * |l * n| * Il
        double nl = n.dotProduct(l);
        double abs_nl = Math.abs(nl);
        Double3 amount = kd.scale(abs_nl);
        return lightIntensity.scale(amount);
    }

//    private Double3 calcDiffusive(Material mat, double nl) {
//        return mat.kD.scale(nl >= 0 ? nl : -nl);
//    }

    /** Calculate the specular light at this point
     * @param ks specular attenuation factor
     * @param l the direction of the light
     * @param n normal from the point
     * @param v direction of the viewer
     * @param nShininess shininess factor of the material at the point
     * @param lightIntensity the intensity of the light source at the point
     * @return the color of the point
     */
    private Color calcSpecular(Double3 ks, Vector l, Vector n, Vector v, int nShininess, Color lightIntensity) {

        double nl = n.dotProduct(l);
        Vector r = l.add(n.scale(-2 * nl)); // nl must not be zero!
        double minusVR = -alignZero(r.dotProduct(v));
        if (minusVR <= 0)
            return Color.BLACK; // view from direction opposite to r vector
        Double3 amount = ks.scale(Math.pow(minusVR, nShininess));
        return lightIntensity.scale(amount);
    }
	
}
