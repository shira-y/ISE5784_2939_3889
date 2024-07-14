//package renderer;
//
//import primitives.*;
//import scene.Scene;
//import lighting.LightSource;
//import static primitives.Util.*;
//import static geometries.Intersectable.GeoPoint;
//
//import java.util.List;
//
//public class SoftShadowsRayTracer extends SimpleRayTracer {
//    private SoftShadowsSettings settings;
//
//    public SoftShadowsRayTracer(Scene scene) {
//        super(scene);
//        this.settings = new SoftShadowsSettings();
//    }
//
//    protected Color calcLocalEffects(GeoPoint gp, Ray ray, Double3 k) {
//        Color color = gp.geometry.getEmission();
//        Vector v = ray.getDirection();
//        Vector n = gp.geometry.getNormal(gp.point);
//        double nv = alignZero(n.dotProduct(v));
//        if (nv == 0)
//            return color;
//
//        Material mat = gp.geometry.getMaterial();
//        for (LightSource lightSource : scene.lights) {
//            Vector l = lightSource.getL(gp.point);
//            double nl = alignZero(n.dotProduct(l));
//            if (nl * nv > 0) {
//                Double3 ktr;
//                if (settings.isSoftShadowsEnabled()) {
//                    ktr = calcSoftShadowTransparency(gp, lightSource, l, n, nv);
//                } else {
//                    ktr = transparency(gp, lightSource, l, n, nv);
//                }
//                if (!ktr.product(k).lowerThan(MIN_CALC_COLOR_K)) {
//                    Color iL = lightSource.getIntensity(gp.point).scale(ktr);
//                    color = color.add(iL.scale(calcDiffusive(mat, nl)), iL.scale(calcSpecular(mat, n, l, nl, v)));
//                }
//            }
//        }
//        return color;
//    }
//
//    private Double3 calcSoftShadowTransparency(GeoPoint gp, LightSource light, Vector l, Vector n, double nv) {
//        List<Ray> rays = light.getAreaLightRays(gp.point, settings.getNumSampleRays());
//        Double3 ktr = Double3.ZERO;
//        for (Ray ray : rays) {
//            ktr = ktr.add(transparency(gp, light, ray.getDirection(), n, nv));
//        }
//        return ktr.reduce(rays.size());
//    }
//
//    public SoftShadowsSettings getSettings() {
//        return settings;
//    }
//}