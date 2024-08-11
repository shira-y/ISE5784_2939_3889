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

/**
 * Test class for creating and rendering a complex 3D scene with random geometries and various lighting conditions.
 * This class demonstrates the use of multi-threading and adaptive super-sampling techniques to improve rendering performance and image quality.
 * 
 * <p>The scene consists of randomly placed spheres and triangles, illuminated by multiple light sources.
 * The camera renders the scene with different configurations, including options for soft shadows, multi-threading, and adaptive super-sampling.</p>
 */
public class MP2 {

    /** The scene object that contains all geometries and lights for the complex scene. */
    private final Scene scene = new Scene("Complex Scene");
    /** Random number generator for creating random geometries in the scene. */
    private final Random random = new Random();
    
    /**
     * Creates a {@link Camera.Builder} instance configured to look towards the scene.
     * The camera is positioned at (0, 0, 1000) and looks towards the origin with an up vector pointing upwards.
     * 
     * @return a configured {@link Camera.Builder} instance.
     */
    private Camera.Builder createCamera() {
        Vector towardsScene = new Vector(0, 0, -1).normalize();
        Vector upVector = new Vector(0, 1, 0);
        return Camera.getBuilder()
                .setLocation(new Point(0, 0, 1000))
                .setDirection(towardsScene, upVector)
                .setVpDistance(1000)
                .setVpSize(200, 200)
                .setRayTracer(new SimpleRayTracer(scene));
    }

    /**
     * Adds random spheres and triangles to the scene. 
     * The geometries are randomly positioned and colored, with a fixed material property for shininess.
     * 
     * <p>This method creates a more visually interesting and complex scene by populating it with a variety of shapes and colors.</p>
     */
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

    /**
     * Adds multiple light sources to the scene to illuminate the geometries.
     * The scene is lit by a combination of directional lights, point lights, and spotlights, each with different colors and positions.
     * 
     * <p>The variety of light sources creates complex shading and shadow effects, enhancing the realism of the rendered scene.</p>
     */
    private void addLights() {
        scene.lights.add(new DirectionalLight(new Color(255, 255, 255), new Vector(1, -1, -1)));
        scene.lights.add(new PointLight(new Color(255, 0, 0), new Point(-50, 50, 50)).setKl(0.00001).setKq(0.000005));
        scene.lights.add(new SpotLight(new Color(0, 255, 0), new Point(50, 50, 50), new Vector(-1, -1, -1)).setKl(0.00001).setKq(0.000005));
        scene.lights.add(new PointLight(new Color(0, 0, 255), new Point(0, -50, 50)).setKl(0.00001).setKq(0.000005));
        scene.lights.add(new SpotLight(new Color(255, 255, 0), new Point(-50, -50, 50), new Vector(1, 1, -1)).setKl(0.00001).setKq(0.000005));
    }

    /**
     * Test method for rendering the complex scene with various configurations.
     * 
     * The scene is rendered three times, each with different settings:
     * <ul>
     * <li>With soft shadows, without multi-threading, and without adaptive super-sampling (commented out).</li>
     * <li>With soft shadows, with multi-threading, and without adaptive super-sampling (commented out).</li>
     * <li>With soft shadows, with multi-threading, and with adaptive super-sampling.</li>
     * </ul>
     * 
     * The method measures and prints the rendering time for each configuration.
     */
    @Test
    public void testComplexScene() {
        addRandomGeometries();
        addLights();

        // Render with soft shadows with multi-threading and with adaptive super-sampling:
        long startTime = System.currentTimeMillis();
        Camera camera = createCamera()
                .setImageWriter(new ImageWriter("complexScene_withSoftShadows_withMultithreading_withAdaptive", 1000, 1000))
                .setRayTracer(new SimpleRayTracer(scene).setUseSoftShadow(true))
                .setSuperSampling(500)  // Enable super-sampling
                .setAdaptive(true)  // Enable adaptive super-sampling
                .build();
        camera.setMultithreading(3)  // Enable multi-threading
              .setDebugPrint(0.1);
        camera.renderImage();  // Render with multi-threading and adaptive super-sampling
        camera.writeToImage();
        long endTime = System.currentTimeMillis();
        System.out.println("Rendering time with soft shadows with multi-threading and adaptive: " + (endTime - startTime) + " ms");
    }
}
