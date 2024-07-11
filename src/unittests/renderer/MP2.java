package unittests.renderer;

import primitives.*;
import renderer.Camera;
import renderer.ImageWriter;
import renderer.SimpleRayTracer;
import scene.Scene;

import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;

/**
 * Test class for creating a scene with multiple objects and light sources.
 */
public class MP2 {

    /**
     * Test method for rendering a scene with over 100 objects and more than 5 light sources.
     */
    @Test
    public void MultipleObjectsTest() {
        Scene scene = new Scene("Multiple Objects Scene");

        // Camera setup (unchanged)
        Vector towardsScene = new Vector(1, -0.25, 1).normalize();
        Vector upVector = new Vector(0, 1, 0);
        Vector rightVector = towardsScene.crossProduct(upVector).normalize();
        Vector trueUpVector = rightVector.crossProduct(towardsScene).normalize();
        Camera.Builder camera = Camera.getBuilder()
            .setLocation(new Point(-55, 8, -55))
            .setDirection(towardsScene, trueUpVector)
            .setVpDistance(150)
            .setVpSize(80, 80);

        Material shinyMaterial = new Material().setKd(0.5).setKs(0.5).setShininess(100);

        // Create more than 100 objects (triangles and spheres)
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                // Create a triangle
                scene.geometries.add(
                    new Triangle(
                        new Point(-40 + i * 8, 0, -40 + j * 8),
                        new Point(-36 + i * 8, 2, -40 + j * 8),
                        new Point(-32 + i * 8, 0, -40 + j * 8)
                    ).setEmission(new Color(i * 25, j * 25, (i + j) * 12))
                    .setMaterial(shinyMaterial)
                );

                // Create a sphere
                scene.geometries.add(
                    new Sphere(new Point(-36 + i * 8, 3, -36 + j * 8), 1)
                    .setEmission(new Color((i + j) * 12, i * 25, j * 25))
                    .setMaterial(shinyMaterial)
                );
            }
        }

        // Add more than 5 light sources
        scene.setAmbientLight(new AmbientLight(new Color(255, 255, 255), 0.1));
        
        scene.lights.add(new DirectionalLight(new Color(200, 200, 200), new Vector(1, -1, 0)));
        scene.lights.add(new PointLight(new Color(255, 0, 0), new Point(-20, 50, -35)).setKl(0.00001).setKq(0.000005));
        scene.lights.add(new SpotLight(new Color(0, 255, 0), new Point(-30, 30, -35), new Vector(1, -1, 0)).setKl(0.00001).setKq(0.000005));
        scene.lights.add(new PointLight(new Color(0, 0, 255), new Point(-50, 20, -50)).setKl(0.00001).setKq(0.000005));
        scene.lights.add(new SpotLight(new Color(255, 255, 0), new Point(-10, 40, -10), new Vector(0, -1, 0)).setKl(0.00001).setKq(0.000005));
        scene.lights.add(new DirectionalLight(new Color(0, 255, 255), new Vector(-1, -1, -1)));

        ImageWriter imageWriter = new ImageWriter("multipleObjects", 1000, 1000);

        Camera camera1 = camera.setImageWriter(imageWriter).setRayTracer(new SimpleRayTracer(scene)).build();

        camera1.renderImage();
        camera1.writeToImage();
    }
}