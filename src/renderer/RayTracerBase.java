package renderer;





import java.util.List;

import primitives.*;
import scene.Scene;

/**
 * Abstract class representing a base for ray tracing operations.
 */
public abstract class RayTracerBase {
	/** The scene to be traced */
	protected final Scene scene;

	/**
	 * Constructs for RayTracerBase
	 * 
	 * @param scene The scene to be traced.
	 */
	public RayTracerBase(Scene scene) {
		this.scene = scene;
	}

	/**
	 * Abstract method to trace a ray and calculate the color.
	 * 
	 * @param ray The ray to be traced.
	 * @return The color calculated by tracing the ray.
	 */
	public abstract Color traceRay(Ray ray);

    /**
     * Declaration of abstract function
     * @param rays rays to check the color
     * @return Average color of some rays at the pixel, using adaptive super-sampling
     */
    public abstract Color adaptiveTraceRays(List<Ray> rays);
   
}