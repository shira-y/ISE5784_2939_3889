package renderer;

import primitives.*;
import scene.Scene;
import static geometries.Intersectable.GeoPoint;
import lighting.LightSource;
import static primitives.Util.*;

import java.util.LinkedList;
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

	private boolean useSoftShadow = false;

	public SimpleRayTracer setUseSoftShadow(boolean useSoftShadow) {
		this.useSoftShadow = useSoftShadow;
		return this;
	}

	

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
	 * Calculates the local effects of lighting at the given geometric point.
	 *
	 * @param gp  The geometric point where the local effects are to be calculated.
	 * @param ray The ray that intersects the geometric point.
	 * @param k   The attenuation coefficient for the ray.
	 * @return The color resulting from the local lighting effects.
	 */

//	private Color calcLocalEffects(GeoPoint gp, Ray ray, Double3 k) {
//		Color color = gp.geometry.getEmission();
//		Vector v = ray.getDirection();
//		Vector n = gp.geometry.getNormal(gp.point);
//		double nv = alignZero(n.dotProduct(v));
//		if (nv == 0)
//			return color; // Direction is perpendicular to the surface
//
//		Material mat = gp.geometry.getMaterial();
//		if (useSoftShadow) {
//			for (var lightSource : scene.lights) {
//				Color colorBeam = Color.BLACK;
//				var vectors = lightSource.getLBeam(gp.point);
//				for (var l : vectors) {
//					double nl = alignZero(n.dotProduct(l));
//					if (nl * nv > 0) { // sign(nl) == sign(nv)
//						Double3 ktr = transparency(gp, lightSource, l, n, nv);
//						if (!ktr.product(k).lowerThan(MIN_CALC_COLOR_K)) {
//							Color lightIntensity = lightSource.getIntensity(gp.point).scale(ktr);
//							colorBeam = colorBeam.add(lightIntensity.scale(calcDiffusive(mat, nl)),
//									lightIntensity.scale(calcSpecular(mat, n, l, nl, v)));
//						}
//					}
//				}
//				color = color.add(colorBeam.reduce(vectors.size()));
//			}
//		} else {
//			for (var lightSource : scene.lights) {
//				Vector l = lightSource.getL(gp.point);
//				double nl = alignZero(n.dotProduct(l));
//				if (nl * nv > 0) { // sign(nl) == sign(nv)
//					Double3 ktr = transparency(gp, lightSource, l, n, nv);
//					if (!ktr.product(k).lowerThan(MIN_CALC_COLOR_K)) {
//						Color lightIntensity = lightSource.getIntensity(gp.point).scale(ktr);
//						color = color.add(lightIntensity.scale(calcDiffusive(mat, nl)),
//								lightIntensity.scale(calcSpecular(mat, n, l, nl, v)));
//					}
//				}
//			}
//		}
//		return color;
//	}
	private Color calcLocalEffects(GeoPoint gp, Ray ray, Double3 k) {
	    Color color = gp.geometry.getEmission();
	    Vector v = ray.getDirection();
	    Vector n = gp.geometry.getNormal(gp.point);
	    double nv = alignZero(n.dotProduct(v));
	    if (nv == 0)
	        return color; // Direction is perpendicular to the surface

	    Material mat = gp.geometry.getMaterial();
	    
	    for (var lightSource : scene.lights) {
	        color = color.add(calcLightContribution(gp, lightSource, n, v, nv, mat, k));
	    }
	    
	    return color;
	}

	private Color calcLightContribution(GeoPoint gp, LightSource lightSource, Vector n, Vector v, double nv, Material mat, Double3 k) {
	    if (useSoftShadow) {
	        Color colorBeam = Color.BLACK;
	        var vectors = lightSource.getLBeam(gp.point);
	        for (var l : vectors) {
	            colorBeam = colorBeam.add(calcSingleLightContribution(gp, lightSource, l, n, v, nv, mat, k));
	        }
	        return colorBeam.reduce(vectors.size());
	    } else {
	        Vector l = lightSource.getL(gp.point);
	        return calcSingleLightContribution(gp, lightSource, l, n, v, nv, mat, k);
	    }
	}

	private Color calcSingleLightContribution(GeoPoint gp, LightSource lightSource, Vector l, Vector n, Vector v, double nv, Material mat, Double3 k) {
	    double nl = alignZero(n.dotProduct(l));
	    if (nl * nv > 0) { // sign(nl) == sign(nv)
	        Double3 ktr = transparency(gp, lightSource, l, n, nv);
	        if (!ktr.product(k).lowerThan(MIN_CALC_COLOR_K)) {
	            Color lightIntensity = lightSource.getIntensity(gp.point).scale(ktr);
	            return lightIntensity.scale(calcDiffusive(mat, nl))
	                .add(lightIntensity.scale(calcSpecular(mat, n, l, nl, v)));
	        }
	    }
	    return Color.BLACK;
	}
	/**
	 * The function calculates the transparency light to the point.
	 *
	 * @param geoPoint The point
	 * @param ls       The light source
	 * @param l        The vector from the light source to the point
	 * @param n        The normal vector at the point
	 * @return The transparency light
	 */
	protected Double3 transparency(GeoPoint gp, LightSource light, Vector l, Vector n, double nv) {
		Vector lightDirection = l.scale(-1);
		Ray shadowRay = new Ray(gp.point, lightDirection, n);
		List<GeoPoint> intersections = scene.geometries.findGeoIntersections(shadowRay);
		Double3 ktr = Double3.ONE; // Start with full transparency
		if (intersections == null)
			return ktr; // No intersections, fully transparent

		double lightDistance = light.getDistance(gp.point);
		for (GeoPoint intersection : intersections) {
			double intersectionDistance = intersection.point.distance(gp.point);
			if (intersectionDistance < lightDistance) {
				ktr = ktr.product(intersection.geometry.getMaterial().kT); // Multiply by the transparency coefficient
				if (ktr.lowerThan(MIN_CALC_COLOR_K))
					return Double3.ZERO;
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

	protected Double3 calcDiffusive(Material mat, double nl) {
		return mat.kD.scale(Math.abs(nl));
	}

	/**
	 * Calculate the specular light at this point
	 * 
	 * @param ks             specular attenuation factor
	 * @param l              the direction of the light
	 * @param n              normal from the point
	 * @param v              direction of the viewer
	 * @param nShininess     shininess factor of the material at the point
	 * @param lightIntensity the intensity of the light source at the point
	 * @return the color of the point
	 */
	public Double3 calcSpecular(Material mat, Vector n, Vector l, double nl, Vector v) {
		Vector r = l.subtract(n.scale(2 * nl));
		double minusVR = -alignZero(v.dotProduct(r));
		return minusVR <= 0 ? Double3.ZERO : mat.kS.scale(Math.pow(minusVR, mat.nShininess));
	}

}
