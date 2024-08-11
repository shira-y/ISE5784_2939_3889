package unittests.renderer;

import primitives.*;
import renderer.Camera;
import renderer.ImageWriter;
import renderer.SimpleRayTracer;
import scene.Scene;
import org.junit.jupiter.api.Test;
import geometries.*;
import lighting.*;

import java.util.Random;

public class MP2 {

    private final Scene scene = new Scene("Complex Scene");
    private final Random random = new Random();

    private Camera.Builder createCamera() {
        Vector towardsScene = new Vector(0, 0, -1).normalize();
        Vector upVector = new Vector(0, 1, 0);
        return Camera.getBuilder()
                .setLocation(new Point(0, 0, 1000))
                .setDirection(towardsScene, upVector)
                .setVpDistance(1000)
                .setVpSize(200, 200)
                .setRayTracer(new SimpleRayTracer(scene)).setSuperSampling(500).setAdaptive(true);
    }

    private void addRandomGeometries() {
        Material material = new Material().setKd(0.5).setKs(0.5).setShininess(100);

        for (int i = 0; i < 70; i++) {
            Point center = new Point(random.nextDouble() * 200 - 100, random.nextDouble() * 200 - 100, random.nextDouble() * 200 - 100);
            double radius = random.nextDouble() * 10 + 1;
            Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            scene.geometries.add(new Sphere(center, radius).setEmission(color).setMaterial(material));
        }

        for (int i = 0; i < 30; i++) {
            Point p1 = new Point(random.nextDouble() * 200 - 100, random.nextDouble() * 200 - 100, random.nextDouble() * 200 - 100);
            Point p2 = new Point(random.nextDouble() * 200 - 100, random.nextDouble() * 200 - 100, random.nextDouble() * 200 - 100);
            Point p3 = new Point(random.nextDouble() * 200 - 100, random.nextDouble() * 200 - 100, random.nextDouble() * 200 - 100);
            Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            scene.geometries.add(new Triangle(p1, p2, p3).setEmission(color).setMaterial(material));
        }
    }

    private void addLights() {
        scene.lights.add(new DirectionalLight(new Color(255, 255, 255), new Vector(1, -1, -1)));
        scene.lights.add(new PointLight(new Color(255, 0, 0), new Point(-50, 50, 50)).setKl(0.00001).setKq(0.000005));
        scene.lights.add(new SpotLight(new Color(0, 255, 0), new Point(50, 50, 50), new Vector(-1, -1, -1)).setKl(0.00001).setKq(0.000005));
        scene.lights.add(new PointLight(new Color(0, 0, 255), new Point(0, -50, 50)).setKl(0.00001).setKq(0.000005));
        scene.lights.add(new SpotLight(new Color(255, 255, 0), new Point(-50, -50, 50), new Vector(1, 1, -1)).setKl(0.00001).setKq(0.000005));
    }

    @Test
    public void testComplexScene() {
        addRandomGeometries();
        addLights();

        Camera.Builder cameraBuilder = createCamera();

        // Render with soft shadows OFF
//        long startTime = System.currentTimeMillis();
//        Camera camera = cameraBuilder
//                .setImageWriter(new ImageWriter("complexScene_noSoftShadows", 1000, 1000))
//                .setRayTracer(new SimpleRayTracer(scene).setUseSoftShadow(false))
//                .build();
//        camera.setMultithreading(3) 
//		.setDebugPrint(0.1);
//        camera.renderImage();
//        camera.writeToImage();
//        long endTime = System.currentTimeMillis();
//        System.out.println("Rendering time without soft shadows: " + (endTime - startTime) + " ms");

        // Render with soft shadows ON
        long startTime = System.currentTimeMillis();
        Camera camera = cameraBuilder
                .setImageWriter(new ImageWriter("complexScene_withSoftShadows", 1000, 1000))
                .setRayTracer(new SimpleRayTracer(scene) .setUseSoftShadow(true)).setAdaptive(true)
                .build();
        camera.setMultithreading(3) 
		.setDebugPrint(0.1);
        camera.renderImage();
        camera.writeToImage();
        long endTime = System.currentTimeMillis();   
        System.out.println("Rendering time with soft shadows: " + (endTime - startTime) + " ms");}
}
