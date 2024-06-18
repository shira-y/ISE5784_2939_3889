package renderer;

import java.util.List;

import primitives.*;

import scene.Scene;
import geometries.Intersectable.GeoPoint;

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
		return intersections == null ? scene.background : calcColor(ray.findClosestGeoPoint(intersections));
	}
	/**
     * Calculates the color at the given geometric point by combining the ambient light
     * and the emission of the geometry at that point.
     * 
     * @param gp The geometric point where the color is to be calculated.
     * @return The calculated color at the given geometric point.
     */
	private Color calcColor(GeoPoint gp) {
		return scene.ambientLight.getIntensity().add(gp.geometry.getEmission());
	}

}
