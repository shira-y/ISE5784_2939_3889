package renderer;

import primitives.*;
import scene.Scene;
import geometries.Intersectable.GeoPoint;
import lighting.LightSource;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import static primitives.Util.*;

/**
 * SoftShadowRayTracer class extends SimpleRayTracer to add soft shadows functionality.
 */
public class SoftShadowRayTracer extends SimpleRayTracer {
	/**
     * Flag to enable or disable soft shadows.
     * When true, soft shadows are calculated; when false, hard shadows are used.
     */
    private boolean softShadowsEnabled = false;
    
    /**
     * The number of sample rays used for soft shadows calculation.
     * Higher values result in smoother shadows but increase rendering time.
     */
    private int numSampleRays = 81;

    /**
     * Constructs a SoftShadowRayTracer object with the given scene.
     *
     * @param scene The scene to be traced.
     */
    public SoftShadowRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * Enables or disables the soft shadows feature.
     *
     * @param enabled true to enable soft shadows, false to disable.
     */
    public void setSoftShadowsEnabled(boolean enabled) {
        softShadowsEnabled = enabled;
    }

    /**
     * Sets the number of sample rays used for soft shadows.
     *
     * @param numSampleRays The number of sample rays.
     */
    public void setNumSampleRays(int numSampleRays) {
        this.numSampleRays = numSampleRays;
    }

    /**
     * Calculates the local effects of lighting at the given geometric point.
     *
     * @param gp  The geometric point where the local effects are to be calculated.
     * @param ray The ray that intersects the geometric point.
     * @param k   The attenuation coefficient for the ray.
     * @return The color resulting from the local lighting effects.
     */
    protected Color calcLocalEffects(GeoPoint gp, Ray ray, Double3 k) {
        Color color = gp.geometry.getEmission();
        Vector v = ray.getDirection();
        Vector n = gp.geometry.getNormal(gp.point);
        double nv = alignZero(n.dotProduct(v));
        if (nv == 0)
            return color; // Direction is perpendicular to the surface

        Material mat = gp.geometry.getMaterial();
        for (LightSource lightSource : scene.lights) {
            Vector l = lightSource.getL(gp.point);
            double nl = alignZero(n.dotProduct(l));
            if (nl * nv > 0) {
                Double3 ktr = transparency(gp, lightSource, l, n, nv);
                if (!ktr.product(k).lowerThan(MIN_CALC_COLOR_K)) {
                    Color iL = lightSource.getIntensity(gp.point).scale(ktr);
                    color = color.add(iL.scale(calcDiffusive(mat, nl)), iL.scale(calcSpecular(mat, n, l, nl, v)));
                }
            }
        }
        return color;
    }

    /**
     * Calculates the transparency attenuation factor for a given geometric point
     * with respect to a light source, taking soft shadows into account.
     *
     * @param gp    The geometric point where the transparency is calculated.
     * @param light The light source affecting the transparency calculation.
     * @param l     The vector from the geometric point to the light source.
     * @param n     The normal vector at the geometric point.
     * @param nv    The dot product of the normal vector and the view direction vector.
     * @return The transparency attenuation factor as a {@link Double3}.
     */
    @Override
    protected Double3 transparency(GeoPoint gp, LightSource light, Vector l, Vector n, double nv) {
        if (!softShadowsEnabled) {
            return super.transparency(gp, light, l, n, nv);
        }

        List<Ray> rays = createSampleRays(light, gp.point, l, n, numSampleRays);
        Double3 ktr = Double3.ZERO;

        for (Ray shadowRay : rays) {
            List<GeoPoint> intersections = scene.geometries.findGeoIntersections(shadowRay);
            Double3 sampleKtr = Double3.ONE; // Start with full transparency

            if (intersections != null) {
                double lightDistance = light.getDistance(gp.point);
                for (GeoPoint intersection : intersections) {
                    double intersectionDistance = intersection.point.distance(gp.point);
                    if (intersectionDistance < lightDistance) {
                        sampleKtr = sampleKtr.product(intersection.geometry.getMaterial().kT);
                        if (sampleKtr.lowerThan(MIN_CALC_COLOR_K)) {
                            sampleKtr = Double3.ZERO;
                            break;
                        }
                    }
                }
            }
            ktr = ktr.add(sampleKtr);
        }
        return ktr.reduce(rays.size());
    }

    /**
     * Creates multiple sample rays for soft shadows.
     *
     * @param lightSource   The light source.
     * @param point         The point from which the sample rays originate.
     * @param l             The original light direction vector.
     * @param n             The normal vector at the point.
     * @param numSampleRays The number of sample rays to create.
     * @return A list of sample rays.
     */
    private List<Ray> createSampleRays(LightSource lightSource, Point point, Vector l, Vector n, int numSampleRays) {
        List<Ray> rays = new LinkedList<>();
        Random rand = new Random();

        for (int i = 0; i < numSampleRays; i++) {
            Vector offset = new Vector(rand.nextDouble() - 0.5, rand.nextDouble() - 0.5, rand.nextDouble() - 0.5).normalize();
            Vector direction = l.add(offset).normalize();
            rays.add(new Ray(point, direction, n));
        }
        return rays;
    }
}
