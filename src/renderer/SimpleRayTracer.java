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

	private static final double DELTA = 0.1;

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
		var intersections = scene.geometries.findGeoIntersections(ray);
		return intersections == null ? scene.background : calcColor(ray.findClosestGeoPoint(intersections), ray);
	}

	/**
	 * Calculates the color at the given geometric point by combining the ambient
	 * light and the emission of the geometry at that point.
	 * 
	 * @param intersection The geometric point where the color is to be calculated.
	 * @param ray          The ray that intersects the geometric point.
	 * @return The calculated color at the given geometric point.
	 */
	private Color calcColor(GeoPoint intersection, Ray ray) {
		return scene.ambientLight.getIntensity().add(calcLocalEffects(intersection, ray));
	}

	/**
	 * Calculates the local effects of lighting at the given geometric point.
	 * 
	 * @param gp  The geometric point where the local effects are to be calculated.
	 * @param ray The ray that intersects the geometric point.
	 * @return The color resulting from the local lighting effects.
	 */
	private Color calcLocalEffects(GeoPoint gp, Ray ray) {
		Color color = gp.geometry.getEmission();
		Vector v = ray.getDirection();
		Vector n = gp.geometry.getNormal(gp.point);
		double nv = alignZero(n.dotProduct(v));
		// if direction is perpendicular to the surface, return emission color
		if (nv == 0)
			return color;

		Material material = gp.geometry.getMaterial();
		// iterate through all the light sources in the scene
		for (LightSource lightSource : scene.lights) {
			Vector l = lightSource.getL(gp.point);
			double nl = alignZero(n.dotProduct(l)); // dot product of the vector's normal and vector's light source
			// check light source and view are on the same side of surface
			if ((nl * nv > 0) && unshaded(gp, lightSource, l, n, nl)) {
				Color iL = lightSource.getIntensity(gp.point);
				color = color.add(iL.scale(calcDiffusive(material, nl).add(calcSpecular(material, n, l, nl, v))));
			}
		}

		return color;
	}

	private boolean unshaded(GeoPoint gp, LightSource light, Vector l, Vector n, double nl) {
		Vector lightDirection = l.scale(-1);
		Vector delta = n.scale(nl < 0 ? DELTA : -DELTA);
		Point point = gp.point.add(delta);
		Ray shadowRay = new Ray(point, lightDirection);
		List<GeoPoint> intersections = scene.geometries.findGeoIntersections(shadowRay);
		if (intersections == null) return true;
		double lightDistance = light.getDistance(point);
		for(GeoPoint intersection : intersections) {
			double intersectionDistance = intersection.point.distance(point);
			if(intersectionDistance < lightDistance) return false;
			
		}
		return true;
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
