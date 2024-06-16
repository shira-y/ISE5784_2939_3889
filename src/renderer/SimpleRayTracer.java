package renderer;

import java.util.List;

import primitives.*;

import scene.Scene;

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
		List<Point> intersection = scene.geometries.findIntersections(ray);
		return intersection == null ? scene.background //
				: calcColor(ray.findClosestPoint(intersection));
	}

	/**
	 * Calculates the color at a given intersection point.
	 * 
	 * @param point the intersection point
	 * @return the color at the intersection point
	 */
	private Color calcColor(Point point) {
		return scene.ambientLight.getIntensity();
	}
}
