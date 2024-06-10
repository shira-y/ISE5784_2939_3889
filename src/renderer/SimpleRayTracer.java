package renderer;
import java.util.List;

import geometries.Geometry;
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

//    @Override
//    public Color traceRay(Ray ray) {
//        List<Geometry> geometries = scene.getGeometries();
//        Intersection intersection = findClosestIntersection(ray, geometries);
//
//        if (intersection == null) {
//            return scene.getBackground(); // No intersection found, return background color
//        }
//
//        Point intersectionPoint = intersection.getPoint();
//        return calcColor(intersectionPoint); // Calculate color at intersection point
//    }
//
//    private Intersection findClosestIntersection(Ray ray, List<Geometry> geometries) {
//        double closestDistance = Double.POSITIVE_INFINITY;
//        Intersection closestIntersection = null;
//
//        for (Geometry geometry : geometries) {
//            List<Point> intersectionPoints = geometry.findIntersections(ray);
//            for (Point intersectionPoint : intersectionPoints) {
//                double distance = ray.getHead().distance(intersectionPoint);
//                if (distance < closestDistance) {
//                    closestDistance = distance;
//                    closestIntersection = new Intersection(geometry, intersectionPoint);
//                }
//            }
//        }
//
//        return closestIntersection;
//    }
//     /**
//     * Calculates the color at a given intersection point.
//     * 
//     * @param geoPoint the intersection point
//     * @return the color at the intersection point
//     */
//    private Color calcColor(Point point) {
//        // At this stage, return the ambient light color intensity
//        return scene.getAmbientLight().getIntensity();
//    }
}

