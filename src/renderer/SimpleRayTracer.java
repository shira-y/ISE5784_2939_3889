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

	
	private static final int MAX_CALC_COLOR_LEVEL = 10;
	private static final double MIN_CALC_COLOR_K = 0.001;
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

	private Color calcColor(GeoPoint point, Ray ray) {
		return calcColor(point, ray, MAX_CALC_COLOR_LEVEL, INITIAL_K).add(scene.ambientLight.getIntensity());
	}

	private Color calcColor(GeoPoint geoPoint, Ray ray, int level, Double3 k) {
		Color color = calcLocalEffects(geoPoint, ray, k);
		return 1 == level ? color : color.add(calcGlobalEffects(geoPoint, ray, level, k));
	}

	private Color calcGlobalEffects(GeoPoint gp, Ray ray, int level, Double3 k) {
		Material material = gp.geometry.getMaterial();
		return calcGlobalEffect(constructRefractedRay(gp, ray), level, material.kT, k)
				.add(calcGlobalEffect(constructReflectedRay(gp, ray), level, material.kR, k));
	}

	private Color calcGlobalEffect(Ray ray, int level, Double3 k, Double3 kx) {
		Double3 kkx = k.product(kx);
		if (kkx.lowerThan(MIN_CALC_COLOR_K))
			return Color.BLACK;
		GeoPoint gp = findClosestIntersection(ray);
		return (gp == null ? scene.background : calcColor(gp, ray, level - 1, kkx)).scale(kx);
	}

	private Ray constructRefractedRay(GeoPoint gp, Ray ray) {
		return new Ray(gp.point, ray.getDirection(), gp.geometry.getNormal(gp.point));
	}

	private Ray constructReflectedRay(GeoPoint gp, Ray ray) {
		Vector v = ray.getDirection();
		Vector n = gp.geometry.getNormal(gp.point);
		double nv = alignZero(v.dotProduct(n));
//		if (vn == 0)
//		return null; // Ray is perpendicular to the normal, no reflection
		// r = v - 2 * (v * n) * n
		Vector r = v.subtract(n.scale(2d * nv)).normalize();

		return new Ray(gp.point, r, n);
	}

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
	 * @return The color resulting from the local lighting effects.
	 */
//	private Color calcLocalEffects(GeoPoint gp, Ray ray, Double3 k) {
//		Color color = gp.geometry.getEmission();
//		Vector v = ray.getDirection();
//		Vector n = gp.geometry.getNormal(gp.point);
//		double nv = alignZero(n.dotProduct(v));
//		// if direction is perpendicular to the surface, return emission color
//		if (nv == 0)
//			return color;
//
//		Material material = gp.geometry.getMaterial();
//		// iterate through all the light sources in the scene
//		for (LightSource lightSource : scene.lights) {
//			Vector l = lightSource.getL(gp.point);
//			double nl = alignZero(n.dotProduct(l)); // dot product of the vector's normal and vector's light source
//			// check light source and view are on the same side of surface
//
////			if ((nl * nv > 0)) {
////				Double3 ktr = transparency(gp, lightSource, l, n);
////				if (ktr.product(k).lowerThan(MIN_CALC_COLOR_K)) {
////					Color iL = lightSource.getIntensity(gp.point).scale(ktr);
////					color = color.add(iL.scale(calcDiffusive(material, nl)),
////							iL.scale(calcSpecular(material, n, l, nl, v)));
////				}
//
//			if ((nl * nv > 0) && unshaded(gp, lightSource, l, n)) {
//				Color iL = lightSource.getIntensity(gp.point);
//				color = color.add(iL.scale(calcDiffusive(material, nl).add(calcSpecular(material, n, l, nl, v))));
//			}
//			
//		}
//		return color;
//
//	}
	private Color calcLocalEffects(GeoPoint gp, Ray ray, Double3 k) {
	    Color color = Color.BLACK;
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
	            if (ktr.product(k).greaterThan(MIN_CALC_COLOR_K)) {
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
	        }
	    }
	    return ktr;
	}

//	private boolean unshaded(GeoPoint gp, LightSource light, Vector l, Vector n) {
//	    Vector lightDirection = l.scale(-1);
//	    Ray shadowRay = new Ray(gp.point, lightDirection,n);
//	    List<GeoPoint> intersections = scene.geometries.findGeoIntersections(shadowRay);
//	    if (intersections == null) return true;
//	    double lightDistance = light.getDistance(gp.point);
//
//	    for (GeoPoint intersection : intersections) {
//	        double intersectionDistance = intersection.point.distance(gp.point);
//	        if (intersectionDistance < lightDistance) {
//	            if (intersection.geometry.getMaterial().kT.equals(Double3.ZERO)) {
//	                return false;
//	            }
//	        }
//	    }
//	    return true;
//	}

//	private Double3 transparency(GeoPoint gp, LightSource light, Vector l, Vector n) {
//		// Create a shadow ray in the direction opposite to the light source
//		Vector lightDirection = l.scale(-1);
//		
//		Ray shadowRay = new Ray(gp.point, lightDirection);
//
//		// Find all intersections of the shadow ray with geometries in the scene
//		List<GeoPoint> intersections = scene.geometries.findGeoIntersections(shadowRay);
//		// If there are no intersections, return full transparency
//		if (intersections == null)
//			return Double3.ONE;
//		// Initialize the transparency factor to 1 (full transparency)
//		Double3 ktr = Double3.ONE;
//		// Iterate through all the intersections
//		double lightDistance = light.getDistance(gp.point);
//		for (GeoPoint intersection : intersections) {
//			double intersectionDistance = intersection.point.distance(gp.point);
//			// Only consider intersections closer to the point than the light source
//			if (intersectionDistance < lightDistance) {
//				ktr = ktr.product(intersection.geometry.getMaterial().kT);
//				// If the transparency factor is very small, return zero transparency
//				if (ktr.lowerThan(MIN_CALC_COLOR_K))
//					return Double3.ZERO;
//			}
//		}
//
//		return ktr;
//	}

//	private Double3 transparency(GeoPoint gp, LightSource light, Vector l, Vector n) {
//
//		// make a new light ray in the opposite direction
//		Vector lightDir = l.scale(-1);
//		Ray lightRay = new Ray(gp.point, lightDir, n);
//
//		// find the intersections with this new light ray
//		List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay);
//		Double3 ktr = new Double3(1.0); // full transparency
//
//		if (intersections == null)
//			return ktr;
//
//		// loop over intersections and for each intersection which is closer to the
//		// point than the light source,
//		// multiply ktr by kT of its geometry
//
//		// Compute the maximum distance to the light source
//		double maxDistance = light.getDistance(gp.point);
//
//		for (GeoPoint geopoint : intersections) {
//			if (gp.point.distance(geopoint.point) <= maxDistance) {
//				// Multiply the transparency coefficient by the transparency factor of the
//				// intersected geometry's material
//				ktr = ktr.product(geopoint.geometry.getMaterial().kT);
//
//				// If the transparency coefficient falls below the minimum calculation
//				// threshold, return a fully opaque value (0 transparency)
//				if (ktr.lowerThan(MIN_CALC_COLOR_K)) {
//					return Double3.ZERO;
//				}
//			}
//
//		}
//		return ktr;
//
//	}

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
		// Calculate the reflection vector r
		Vector r = l.subtract(n.scale(2 * nl));

		// Calculate the dot product of view vector v and reflection vector r
		double vr = -v.dotProduct(r);

		// Calculate the specular component using a manual power calculation
		double specularFactor = 1.0;
		if (vr > 0) {
			for (int i = 0; i < mat.nShininess; i++) {
				specularFactor *= vr;
			}
		} else {
			specularFactor = 0;
		}

		// Return the specular color scaled by the specular factor and the specular
		// coefficient
		return mat.kS.scale(specularFactor);
	}

}
