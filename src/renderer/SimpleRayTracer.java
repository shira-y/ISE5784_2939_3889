package renderer;
import primitives.Color;
import primitives.Ray;
import scene.Scene;


/**
 * SimpleRayTracer class extends the abstract base class RayTracerBase.
 * This class provides basic ray tracing functionality.
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
		// TODO Auto-generated method stub
		return null;
	}
}

