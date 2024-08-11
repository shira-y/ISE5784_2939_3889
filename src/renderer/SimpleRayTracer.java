package renderer;

import primitives.*;
import scene.Scene;
import static geometries.Intersectable.GeoPoint;
import lighting.LightSource;
import static primitives.Util.*;

import java.util.List;

/**
 * The {@code SimpleRayTracer} class extends the abstract {@link RayTracerBase} class and provides basic ray tracing functionality.
 * It includes methods for calculating the color of points in a scene by tracing rays, considering effects like reflections, refractions, shadows, and lighting.
 */
public class SimpleRayTracer extends RayTracerBase {

    /**
     * The maximum recursion level for color calculation, limiting the depth of recursive reflection and refraction calculations.
     */
    private static final int MAX_CALC_COLOR_LEVEL = 10;

    /**
     * The minimum attenuation factor for color calculation, used to terminate recursion when the color contribution is negligible.
     */
    protected static final double MIN_CALC_COLOR_K = 0.001;

    /**
     * The initial attenuation factor for color calculation, representing no attenuation.
     */
    private static final Double3 INITIAL_K = Double3.ONE;

    /**
     * Flag to indicate whether soft shadows should be used in the ray tracing process.
     */
    private boolean useSoftShadow = false;

    /**
     * Sets the soft shadow flag for this {@code SimpleRayTracer}.
     *
     * @param useSoftShadow {@code true} to enable soft shadows, {@code false} otherwise.
     * @return This {@code SimpleRayTracer} instance, for method chaining.
     */
    public SimpleRayTracer setUseSoftShadow(boolean useSoftShadow) {
        this.useSoftShadow = useSoftShadow;
        return this;
    }

    /**
     * Constructs a {@code SimpleRayTracer} object with the given scene.
     *
     * @param scene The scene to be traced.
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * Traces a ray and returns the color of the closest object it intersects.
     *
     * @param ray The ray to trace.
     * @return The color at the intersection point, or the background color if no intersection is found.
     */
    @Override
    public Color traceRay(Ray ray) {
        GeoPoint closestPoint = findClosestIntersection(ray); // Finds the closest GeoPoint that the ray hits
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
     * @param level    The recursion level for calculating reflections and refractions.
     * @param k        The attenuation coefficient for the ray.
     * @return The calculated color at the point.
     */
    private Color calcColor(GeoPoint geoPoint, Ray ray, int level, Double3 k) {
        Color color = calcLocalEffects(geoPoint, ray, k);
        return 1 == level ? color : color.add(calcGlobalEffects(geoPoint, ray, level, k));
    }

    /**
     * Calculates the global effects (reflections and refractions) on the color at a given geometric point.
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
     * Calculates the global effect (either reflection or refraction) for a given ray.
     *
     * @param ray   The ray to calculate the global effect for.
     * @param level The recursion level for calculating reflections and refractions.
     * @param k     The attenuation coefficient for the ray.
     * @param kx    The attenuation coefficient for the specific global effect (reflection or refraction).
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
     * Finds the closest intersection point between a given ray and the geometries in the scene.
     *
     * @param ray The ray for which to find the closest intersection.
     * @return The closest intersection point as a {@link GeoPoint}, or {@code null} if no intersections are found.
     */
    private GeoPoint findClosestIntersection(Ray ray) {
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray);
        return intersections == null ? null : ray.findClosestGeoPoint(intersections);
    }

    /**
     * Calculates the local effects (diffuse and specular) on the color at a given geometric point.
     *
     * @param gp  The geometric point to calculate the local effects for.
     * @param ray The ray that intersects the geometric point.
     * @param k   The attenuation coefficient for the ray.
     * @return The color resulting from the local effects.
     */
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

    /**
     * Calculates the contribution of a light source to the color at a given geometric point.
     *
     * @param gp         The geometric point to calculate the light contribution for.
     * @param lightSource The light source contributing to the color.
     * @param n          The normal vector at the geometric point.
     * @param v          The direction vector of the ray.
     * @param nv         The dot product of the normal and direction vectors.
     * @param mat        The material of the geometry.
     * @param k          The attenuation coefficient for the ray.
     * @return The color resulting from the light contribution.
     */
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

    /**
     * Calculates the contribution of a single light source to the color at a given geometric point.
     *
     * @param gp         The geometric point to calculate the light contribution for.
     * @param lightSource The light source contributing to the color.
     * @param l          The light vector from the light source to the geometric point.
     * @param n          The normal vector at the geometric point.
     * @param v          The direction vector of the ray.
     * @param nv         The dot product of the normal and direction vectors.
     * @param mat        The material of the geometry.
     * @param k          The attenuation coefficient for the ray.
     * @return The color resulting from the single light contribution.
     */
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
     * Calculates the transparency of the light passing through the geometry.
     *
     * @param gp        The geometric point.
     * @param light     The light source.
     * @param l         The vector from the light source to the point.
     * @param n         The normal vector at the point.
     * @param nv        The dot product of the normal and direction vectors.
     * @return The transparency factor for the light.
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
     * Calculates the specular component of the light reflection.
     *
     * @param mat       The material of the geometry.
     * @param n         The normal vector at the geometric point.
     * @param l         The light vector from the light source to the geometric point.
     * @param nl        The dot product of the normal and light vectors.
     * @param v         The direction vector of the ray.
     * @return The specular reflection component.
     */
    public Double3 calcSpecular(Material mat, Vector n, Vector l, double nl, Vector v) {
        Vector r = l.subtract(n.scale(2 * nl));
        double minusVR = -alignZero(v.dotProduct(r));
        return minusVR <= 0 ? Double3.ZERO : mat.kS.scale(Math.pow(minusVR, mat.nShininess));
    }

    /**
     * Returns the color of the point the ray hits using adaptive super sampling.
     *
     * @param rays Rays to check the color.
     * @return The color of the point.
     */
    public Color adaptiveTraceRays(List<Ray> rays) {
        int numOfSampleRays = (int) sqrt(rays.size());
        int ray1Index = (numOfSampleRays - 1) * numOfSampleRays + (numOfSampleRays - 1); // the index of the top right ray
        int ray2Index = (numOfSampleRays - 1) * numOfSampleRays;  // the index of the top left ray
        int ray3Index = 0;  // the index of the bottom left ray
        int ray4Index = (numOfSampleRays - 1);  // the index of the bottom right ray

        Color color = adaptiveSuperSampling(rays, numOfSampleRays / 2, ray1Index, ray2Index, ray3Index, ray4Index, numOfSampleRays); // calculate the color for the pixel
        return color;
    }

    /**
     * Helper method for adaptive super sampling.
     * Recursively checks if the corners around a center point are the same and, if so, doesn't check the rest of the rays, just picking the color of the center.
     *
     * @param rays           All the rays that were shot.
     * @param level_of_adaptive Number of levels of adaptiveness to go.
     * @param ray1Index      The index of the top right corner ray.
     * @param ray2Index      The index of the top left corner ray.
     * @param ray3Index      The index of the bottom left corner ray.
     * @param ray4Index      The index of the bottom right corner ray.
     * @param numOfSampleRays Number of rays to sample.
     * @return The color of the point.
     */
    private Color adaptiveSuperSampling(List<Ray> rays, int level_of_adaptive, int ray1Index, int ray2Index, int ray3Index, int ray4Index, int numOfSampleRays) {
        int numOfAdaptiveRays = 5;

        Ray centerRay = rays.get(rays.size() - 1); // Get the center screen ray
        Color centerColor = traceRay(centerRay); // Get the color of the center
        Ray ray1 = rays.get(ray1Index);  // Get the top right screen ray
        Color color1 = traceRay(ray1); // Get the color of the top right
        Ray ray2 = rays.get(ray2Index); // Get the top left ray
        Color color2 = traceRay(ray2); // Get the color of the top left
        Ray ray3 = rays.get(ray3Index); // Get the bottom left ray
        Color color3 = traceRay(ray3); // Get the color of the bottom left
        Ray ray4 = rays.get(ray4Index); // Get the bottom right ray
        Color color4 = traceRay(ray4); // Get the color of the bottom right

        if (level_of_adaptive == 0) {
            // Calculate the average color of the corners and the center
            centerColor = centerColor.add(color1, color2, color3, color4);
            return centerColor.reduce(numOfAdaptiveRays);
        }

        // If the corner color is the same as the center color, return the center color
        if (color1.isColorsEqual(centerColor) && color2.isColorsEqual(centerColor) && color3.isColorsEqual(centerColor) && color4.isColorsEqual(centerColor)) {
            return centerColor;
        }

        // Otherwise, for each color that is different from the center, the recursion goes down to the depth of the pixel and sums up the colors until it gets the same color as the center color.
        else {
            if (!color1.isColorsEqual(centerColor)) {
                color1 = color1.add(adaptiveSuperSampling(rays, level_of_adaptive - 1, ray1Index - (numOfSampleRays + 1), ray2Index, ray3Index, ray4Index, numOfSampleRays));
                color1 = color1.reduce(2);
            }
            if (!color2.isColorsEqual(centerColor)) {
                color2 = color2.add(adaptiveSuperSampling(rays, level_of_adaptive - 1, ray1Index, ray2Index - (numOfSampleRays - 1), ray3Index, ray4Index, numOfSampleRays));
                color2 = color2.reduce(2);
            }
            if (!color3.isColorsEqual(centerColor)) {
                color3 = color3.add(adaptiveSuperSampling(rays, level_of_adaptive - 1, ray1Index, ray2Index, ray3Index + (numOfSampleRays + 1), ray4Index, numOfSampleRays));
                color3 = color3.reduce(2);
            }
            if (!color4.isColorsEqual(centerColor)) {
                color4 = color4.add(adaptiveSuperSampling(rays, level_of_adaptive - 1, ray1Index, ray2Index, ray3Index, ray4Index + (numOfSampleRays - 1), numOfSampleRays));
                color4 = color4.reduce(2);
            }
            // Calculate and return the average color
            centerColor = centerColor.add(color1, color2, color3, color4);

            return centerColor.reduce(numOfAdaptiveRays);
        }
    }

    /**
     * Returns the average of colors of all the points the rays hit.
     *
     * @param rays Rays to check the color.
     * @return The average color.
     */
    public Color traceRays(List<Ray> rays) {
        Color avgColor = Color.BLACK;

        // For each ray in the list, return the color at that point
        for (Ray ray : rays) {
            avgColor = avgColor.add(traceRay(ray));
        }

        // Return the average color
        return avgColor.reduce(rays.size()); // Reduce by the number of rays to get the average color
    }
}
