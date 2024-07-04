package unittests.renderer;

import static java.awt.Color.*;

import org.junit.jupiter.api.Test;

import geometries.Sphere;
import geometries.Triangle;
import lighting.AmbientLight;
import lighting.SpotLight;
import primitives.*;
import renderer.*;
import scene.Scene;

/** Test class for creating an image of ice cream with one triangle and three spheres */
public class NewImageTest {

    /** Scene for the test */
    private final Scene scene = new Scene("Ice Cream Scene");

    /** Camera builder for the test */
    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
            .setRayTracer(new SimpleRayTracer(scene));

    /** Test method to create an image of ice cream */
    @Test
    public void createIceCreamImage() {
        scene.setBackground(new Color(75, 127, 190))
                .setAmbientLight(new AmbientLight(new Color(WHITE), new Double3(0.15)));

        // Adding the cone (triangle) and flipping it
        scene.geometries.add(
                new Triangle(new Point(-50, -50, -1000), new Point(50, -50, -1000), new Point(0, -100, -1000))
                        .setEmission(new Color(ORANGE))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(300))
        );

        // Adding the ice cream scoops (spheres) with adjusted positions and properties
        scene.geometries.add(
                new Sphere(new Point(0, -30, -1000), 30) // First scoop
                        .setEmission(new Color(PINK))
                        .setMaterial(new Material().setKd(0.5).setKs(0.1).setShininess(30).setKt(0.1)),
                new Sphere(new Point(-35, -30, -1000), 30) // Second scoop
                        .setEmission(new Color(CYAN))
                        .setMaterial(new Material().setKd(0.5).setKs(0.3).setShininess(50).setKt(0.1)),
                new Sphere(new Point(35, -30, -1000), 30) // Third scoop
                        .setEmission(new Color(MAGENTA))
                        .setMaterial(new Material().setKd(0.5).setKs(0.3).setShininess(50).setKt(0.1))
        );

        // Adding a light source
        scene.lights.add(new SpotLight(new Color(700, 400, 400), new Point(-100, -100, -500), new Vector(1, 1, -2))
                .setKl(4E-5).setKq(2E-7));

        Camera camera1 = cameraBuilder.setLocation(new Point(0, 0, 1000))
                .setVpDistance(1000)
                .setVpSize(150, 150)
                .setImageWriter(new ImageWriter("iceCreamImage", 600, 600))
                .build();

        camera1.renderImage();
        camera1.writeToImage();
    }
}







//package unittests.renderer;
//
//import static java.awt.Color.*;
//
//import org.junit.jupiter.api.Test;
//
//import geometries.Sphere;
//import geometries.Triangle;
//import lighting.AmbientLight;
//import lighting.SpotLight;
//import primitives.*;
//import renderer.*;
//import scene.Scene;
//
///** Test class for creating a new image with specified geometries and lighting */
//public class NewImageTest {
//
//    /** Scene for the test */
//    private final Scene scene = new Scene("Test Scene");
//
//    /** Camera builder for the test */
//    private final Camera.Builder cameraBuilder = Camera.getBuilder()
//            .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
//            .setRayTracer(new SimpleRayTracer(scene));
//
//    /** Test method to create a new image */
//    @Test
//    public void createNewImage() {
//        scene.setBackground(new Color(75, 127, 190))
//                .setAmbientLight(new AmbientLight(new Color(WHITE), new Double3(0.15)));
//
//        scene.geometries.add(
//                new Sphere(new Point(0, 0, -1000), 50)
//                        .setEmission(new Color(RED))
//                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(100).setKt(0.3)),
//                new Sphere(new Point(50, 0, -1050), 50)
//                        .setEmission(new Color(GREEN))
//                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(100).setKt(0.4)),
//                new Triangle(new Point(-50, -50, -1000), new Point(-50, 50, -1000), new Point(0, 0, -1000))
//                        .setEmission(new Color(BLUE))
//                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(300)),
//                new Triangle(new Point(-50, -50, -1000), new Point(50, -50, -1000), new Point(0, -100, -1000))
//                        .setEmission(new Color(YELLOW))
//                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(300))
//        );
//
//        scene.lights.add(new SpotLight(new Color(700, 400, 400), new Point(-100, -100, -500), new Vector(1, 1, -2))
//                .setKl(4E-5).setKq(2E-7));
//
//        Camera camera1 = cameraBuilder.setLocation(new Point(0, 0, 1000))
//                .setVpDistance(1000)
//                .setVpSize(150, 150)
//                .setImageWriter(new ImageWriter("newImage", 600, 600))
//                .build();
//
//        camera1.renderImage();
//        camera1.writeToImage();
//    }
//}
