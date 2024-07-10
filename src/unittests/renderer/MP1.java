package unittests.renderer;

import primitives.*;
import renderer.Camera;
import renderer.ImageWriter;
import renderer.SimpleRayTracer;
import scene.Scene;

import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;

public class MP1 {

    @Test
    public void ChessboardTest() {
        Scene scene = new Scene("Chessboard Scene");

        Vector towardsBoard = new Vector(1, -0.25, 1).normalize(); // Adjusted direction vector
        Vector upVector = new Vector(0, 1, 0);
        Vector rightVector = towardsBoard.crossProduct(upVector).normalize();
        Vector trueUpVector = rightVector.crossProduct(towardsBoard).normalize();

        Camera.Builder camera = Camera.getBuilder()
             .setLocation(new Point(-55, 8, -55)) // Adjusted camera location
            .setDirection(towardsBoard, trueUpVector)
            .setVpDistance(150) // Adjusted view plane distance
            .setVpSize(80, 80);

        // Create chessboard
        Material shinyMaterial = new Material().setKd(0.5).setKs(0.5).setShininess(100);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Color squareColor = (i + j) % 2 == 0 ? new Color(139, 69, 19) : new Color(169, 169, 169);

                double height = 0; // Make sure all squares are flat

                // Create a square using two triangles
                scene.geometries.add(
                        new Triangle(new Point(-40 + i * 10, height, -40 + j * 10),
                                new Point(-30 + i * 10, height, -40 + j * 10),
                                new Point(-30 + i * 10, height, -30 + j * 10)).setEmission(squareColor)
                                .setMaterial(shinyMaterial),
                        new Triangle(new Point(-40 + i * 10, height, -40 + j * 10),
                                new Point(-30 + i * 10, height, -30 + j * 10),
                                new Point(-40 + i * 10, height, -30 + j * 10)).setEmission(squareColor)
                                .setMaterial(shinyMaterial));
            }
        }

        // Add standing pawn to the board
        double pawnBaseY = 0.2; // Adjust according to board height
        Point baseCenter = new Point(-35, pawnBaseY, -35); // Position on the board

        // Base of the standing pawn (a larger sphere)
        scene.geometries.add(new Sphere(baseCenter, 2).setEmission(new Color(255, 0, 0))
                .setMaterial(shinyMaterial),

        // Body of the standing pawn (a series of smaller spheres)
        new Sphere(baseCenter.add(new Vector(0, 3, 0)), 1.5).setEmission(new Color(255, 0, 0))
                .setMaterial(shinyMaterial),

        // Head of the standing pawn (a small sphere)
        new Sphere(baseCenter.add(new Vector(0, 5, 0)), 1).setEmission(new Color(255, 0, 0))
                .setMaterial(shinyMaterial));
        
        
        // Add fallen pawn to the board
        Point fallenBaseCenter = new Point(-45, pawnBaseY, -35); // Position on the board for fallen pawn

        // Base of the fallen pawn (a larger sphere, rotated)
        scene.geometries.add(new Sphere(fallenBaseCenter.add(new Vector(0, 1, 0)), 2).setEmission(new Color(0, 0, 0))
                .setMaterial(shinyMaterial),

        // Body of the fallen pawn (a series of smaller spheres, rotated)
        new Sphere(fallenBaseCenter.add(new Vector(0, 1, 0).add(new Vector(2, 0, 0))), 1.5).setEmission(new Color(0, 0, 0))
                .setMaterial(shinyMaterial),

        // Head of the fallen pawn (a small sphere, rotated)
        new Sphere(fallenBaseCenter.add(new Vector(0, 1, 0).add(new Vector(4, 0, 0))), 1).setEmission(new Color(0, 0, 0))
                .setMaterial(shinyMaterial));


        // Add ambient light with reduced intensity
       // scene.setAmbientLight(new AmbientLight(new Color(255, 255, 255), 0.02));

        // Add three light sources with adjusted positions and intensities

        // Directional light from the right side
        scene.lights.add(new DirectionalLight(new Color(200, 200, 200), new Vector(1, -1, 0)));

        // Point light from the left side
        scene.lights.add(new PointLight(new Color(50, 50, 50), new Point(-20, 50, -35))
                .setKl(0.00001).setKq(0.000005));

        // Spot light (blue)
        scene.lights.add(new SpotLight(new Color(0, 0, 255), new Point(-30, 30, -35), new Vector(1, -1, 0))
                .setKl(0.00001).setKq(0.000005));

        ImageWriter imageWriter = new ImageWriter("chessboard", 1000, 1000);

        // Set up the camera with the image writer and ray tracer
        Camera camera1 = camera.setImageWriter(imageWriter).setRayTracer(new SimpleRayTracer(scene)).build();

        // Render the image
        camera1.renderImage();
        camera1.writeToImage();
    }

}
