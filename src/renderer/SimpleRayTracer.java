package renderer;

import primitives.*;
import scene.Scene;
import geometries.Intersectable.GeoPoint;
import lighting.LightSource;
import static primitives.Util.alignZero;

/**
 * SimpleRayTracer class extends the abstract base class RayTracerBase. This
 * class provides basic ray tracing functionality.
 */
public class SimpleRayTracer extends RayTracerBase {

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
			if ((nl * nv > 0)) {
				Color iL = lightSource.getIntensity(gp.point);
				color = color
						.add(iL.scale(calcDiffusive(material, nl).add(calcSpecular(material, n, l, nl, v))));
			}
		}

		return color;
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
	private Double3 calcSpecular(Material mat, Vector n, Vector l, double nl, Vector v) {
		// Calculate the specular coefficient raised to the power of the shininess
		// factor.
		return mat.kS.scale(Math.pow(-alignZero(v.dotProduct(l.subtract(n.scale(nl * 2)))), mat.nShininess));
	}
}
