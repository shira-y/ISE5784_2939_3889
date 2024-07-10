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
    private boolean softShadowsEnabled = false;
    private int numSampleRays = 81;

    public SoftShadowRayTracer(Scene scene) {
        super(scene);
    }

    public void setSoftShadowsEnabled(boolean enabled) {
        softShadowsEnabled = enabled;
    }

    public void setNumSampleRays(int numSampleRays) {
        this.numSampleRays = numSampleRays;
    }

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
     * Overrides transparency method to include soft shadows logic.
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
