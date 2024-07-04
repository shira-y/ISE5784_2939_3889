package renderer;

import primitives.*;
import scene.Scene;
import geometries.Intersectable.GeoPoint;
import lighting.LightSource;
import static primitives.Util.alignZero;

import java.util.List;

/**
 * SimpleRayTracer class extends the abstract base class RayTracerBase. This
 * class provides basic ray tracing functionality.
 */
public class SimpleRayTracer extends RayTracerBase {

	
	/**
     * The maximum recursion level for color calculation.
     * Limits the depth of recursive reflection and refraction calculations.
     */
    private static final int MAX_CALC_COLOR_LEVEL = 10;

    /**
     * The minimum attenuation factor for color calculation.
     * Used to terminate recursion when the color contribution is negligible.
     */
    private static final double MIN_CALC_COLOR_K = 0.001;

    /**
     * The initial attenuation factor for color calculation.
     * Represents no attenuation.
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
	 * @param ray The ray that intersects the geometric point.
	 * @return The calculated color at the point.
	 */
	private Color calcColor(GeoPoint point, Ray ray) {
		return calcColor(point, ray, MAX_CALC_COLOR_LEVEL, INITIAL_K).add(scene.ambientLight.getIntensity());
	}

	/**
	 * Recursively calculates the color at a given geometric point.
	 * 
	 * @param geoPoint The geometric point to calculate the color for.
	 * @param ray The ray that intersects the geometric point.
	 * @param level The recursion level for calculating reflections and refractions.
	 * @param k The attenuation coefficient for the ray.
	 * @return The calculated color at the point.
	 */
	private Color calcColor(GeoPoint geoPoint, Ray ray, int level, Double3 k) {
		Color color = calcLocalEffects(geoPoint, ray, k);
		return 1 == level ? color : color.add(calcGlobalEffects(geoPoint, ray, level, k));
	}

	/**
	 * Calculates the global effects (reflections and refractions) on the color at a given geometric point.
	 * 
	 * @param gp The geometric point to calculate the global effects for.
	 * @param ray The ray that intersects the geometric point.
	 * @param level The recursion level for calculating reflections and refractions.
	 * @param k The attenuation coefficient for the ray.
	 * @return The color resulting from the global effects.
	 */
	private Color calcGlobalEffects(GeoPoint gp, Ray ray, int level, Double3 k) {
		Material material = gp.geometry.getMaterial();
		return calcGlobalEffect(constructRefractedRay(gp, ray), level, material.kT, k)
				.add(calcGlobalEffect(constructReflectedRay(gp, ray), level, material.kR, k));
	}

	/**
	 * Calculates the global effect (either reflection or refraction) for a given ray.
	 * 
	 * @param ray The ray to calculate the global effect for.
	 * @param level The recursion level for calculating reflections and refractions.
	 * @param k The attenuation coefficient for the ray.
	 * @param kx The attenuation coefficient for the specific global effect (reflection or refraction).
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
	 * @param gp The geometric point to construct the refracted ray from.
	 * @param ray The original ray.
	 * @return The constructed refracted ray.
	 */
	private Ray constructRefractedRay(GeoPoint gp, Ray ray) {
		return new Ray(gp.point, ray.getDirection(), gp.geometry.getNormal(gp.point));
	}

	/**
	 * Constructs a reflected ray from a given geometric point.
	 * 
	 * @param gp The geometric point to construct the reflected ray from.
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
	 * Finds the closest intersection point between a given ray and the geometries in the scene.
	 *
	 * @param ray The ray for which to find the closest intersection.
	 * @return The closest intersection point as a {@link GeoPoint}, or null if no intersections are found.
	 */
	private GeoPoint findClosestIntersection(Ray ray) {
		List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray);
		if (intersections == null) {
			return null;
		}
		return ray.findClosestGeoPoint(intersections);
	}

	/**
	 * Calculates the local effects of lighting at the given geometric point.
	 * 
	 * @param gp  The geometric point where the local effects are to be calculated.
	 * @param ray The ray that intersects the geometric point.
	 * @param k   The attenuation coefficient for the ray.
	 * @return The color resulting from the local lighting effects.
	 */
	private Color calcLocalEffects(GeoPoint gp, Ray ray, Double3 k) {
		Color color = gp.geometry.getEmission();
	    Vector v = ray.getDirection();
	    Vector n = gp.geometry.getNormal(gp.point);
	    double nv = alignZero(n.dotProduct(v));
	    if (nv == 0) return color; // Direction is perpendicular to the surface

	    Material mat = gp.geometry.getMaterial();
	    for (LightSource lightSource : scene.lights) {
	        Vector l = lightSource.getL(gp.point);
	        double nl = alignZero(n.dotProduct(l));
	        if (nl * nv > 0) {
	            Double3 ktr = transparency(gp, lightSource, l, n, nv);
	            if (!ktr.product(k).lowerThan(MIN_CALC_COLOR_K)) {
	                Color iL = lightSource.getIntensity(gp.point).scale(ktr);
	                color = color.add(
	                    iL.scale(calcDiffusive(mat, nl)),
	                    iL.scale(calcSpecular(mat, n, l, nl, v))
	                );
	            }
	        }
	    }
	    return color;
	}

	/**
	 * Calculates the transparency attenuation factor for a given geometric point with respect to a light source.
	 * This method determines how much light passes through transparent or semi-transparent materials 
	 * between the geometric point and the light source.
	 *
	 * @param gp  The geometric point where the transparency is calculated.
	 * @param light The light source affecting the transparency calculation.
	 * @param l   The vector from the geometric point to the light source.
	 * @param n   The normal vector at the geometric point.
	 * @param nv  The dot product of the normal vector and the view direction vector.
	 * @return The transparency attenuation factor as a {@link Double3}.
	 */
	private Double3 transparency(GeoPoint gp, LightSource light, Vector l, Vector n, double nv) {
	    Vector lightDirection = l.scale(-1);
	    Ray shadowRay = new Ray(gp.point, lightDirection, n);
	    List<GeoPoint> intersections = scene.geometries.findGeoIntersections(shadowRay);
	    if (intersections == null) return Double3.ONE; // No intersections, fully transparent

	    Double3 ktr = Double3.ONE; // Start with full transparency
	    double lightDistance = light.getDistance(gp.point);

	    for (GeoPoint intersection : intersections) {
	        double intersectionDistance = intersection.point.distance(gp.point);
	        if (intersectionDistance < lightDistance) {
	            ktr = ktr.product(intersection.geometry.getMaterial().kT); // Multiply by the transparency coefficient
	            if (ktr.lowerThan(MIN_CALC_COLOR_K)) return Double3.ZERO;	
	        }
	    }
	    return ktr;
	}



	/**
	 * Calculates the diffusive component of the light reflection.
	 * 
	 * @param mat The material of the geometry.
	 * @param nl  The dot product of the normal vector and the light vector.
	 * @return The diffusive reflection component.
	 */
	private Double3 calcDiffusive(Material mat, double nl) {
		return mat.kD.scale(Math.abs(nl));
	}

	/**
	 * Calculates the specular component of the light reflection.
	 * 
	 * @param mat The material of the geometry.
	 * @param n   The normal vector at the geometric point.
	 * @param l   The light vector.
	 * @param nl  The dot product of the normal vector and the light vector.
	 * @param v   The view vector (opposite of the ray direction).
	 * @return The specular reflection component.
	 */
	public Double3 calcSpecular(Material mat, Vector n, Vector l, double nl, Vector v) {
		
		Vector r = l.subtract(n.scale(2 * nl));

		double vr = -v.dotProduct(r);

		double specularFactor = 1.0;
		if (vr > 0) {
			for (int i = 0; i < mat.nShininess; i++) {
				specularFactor *= vr;
			}
		} else {
			specularFactor = 0;
		}

		return mat.kS.scale(specularFactor);
	}

}
