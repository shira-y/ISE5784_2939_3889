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
	     
	
		Camera.Builder camera = Camera.getBuilder().setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
				.setLocation(new Point(0, 50, 300)).setVpDistance(500).setVpSize(200, 200);



		// Create chessboard
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Color squareColor = (i + j) % 2 == 0 ? new Color(255, 255, 255) : new Color(0, 0, 0);
//                Material squareMaterial = new Material().setKd(0.8).setKs(0.2).setShininess(30);

				double height = (i + j) % 2 == 0 ? 0 : 0.2; // Increased elevation for dark squares

				// Create a square using two triangles
				scene.geometries.add(
						new Triangle(new Point(-40 + i * 10, height, -40 + j * 10),
								new Point(-30 + i * 10, height, -40 + j * 10),
								new Point(-30 + i * 10, height, -30 + j * 10)).setEmission(squareColor),
						// .setMaterial(squareMaterial),

						new Triangle(new Point(-40 + i * 10, height, -40 + j * 10),
								new Point(-30 + i * 10, height, -30 + j * 10),
								new Point(-40 + i * 10, height, -30 + j * 10)).setEmission(squareColor));
				// .setMaterial(squareMaterial));

			}
		}
		 // Add a pawn piece to the board using only spheres and triangles
        double pawnBaseY = 0.2; // Adjust according to board height
        Point baseCenter = new Point(-35, pawnBaseY, -35); // Position on the board

        // Base of the pawn (a larger sphere)
        scene.geometries.add(new Sphere(baseCenter, 2).setEmission(new Color(255, 0, 0)),

        // Body of the pawn (a series of smaller spheres)
        new Sphere(baseCenter.add(new Vector(0, 3, 0)), 1.5).setEmission(new Color(255, 0, 0)),

        // Head of the pawn (a small sphere)
        new Sphere(baseCenter.add(new Vector(0, 5, 0)), 1).setEmission(new Color(255, 0, 0)));

        // Adding a triangular top to the pawn
        Point p1 = baseCenter.add(new Vector(0, 6, 0)); // Top point
        Point p2 = baseCenter.add(new Vector(-1, 5, -1)); // Bottom left point
        Point p3 = baseCenter.add(new Vector(1, 5, -1)); // Bottom right point
        Point p4 = baseCenter.add(new Vector(1, 5, 1)); // Bottom back right point
        Point p5 = baseCenter.add(new Vector(-1, 5, 1)); // Bottom back left point

        scene.geometries.add( new Triangle(p1, p2, p3).setEmission(new Color(255, 0, 0)),
        new Triangle(p1, p3, p4).setEmission(new Color(255, 0, 0)),
        new Triangle(p1, p4, p5).setEmission(new Color(255, 0, 0)),
        new Triangle(p1, p5, p2).setEmission(new Color(255, 0, 0)));

     
        
        
	

		// Add ambient light
		scene.setAmbientLight(new AmbientLight(new Color(255, 255, 255), 0.1));

		// Add main light source
		scene.lights.add(new SpotLight(new Color(800, 800, 800), new Point(0, 100, 0), new Vector(0, -1, 0))
				.setKl(0.00001).setKq(0.000005));

		// Add secondary light source for better illumination
//        scene.lights.add(new PointLight(new Color(500, 500, 500), new Point(100, 100, 100))
//            .setKl(0.00001).setKq(0.000005));

		// Create ImageWriter
		ImageWriter imageWriter = new ImageWriter("chessboard", 1000, 1000);

		// Set up the camera with the image writer and ray tracer
		Camera camera1 = camera.setImageWriter(imageWriter).setRayTracer(new SimpleRayTracer(scene)).build();

		// Render the image
		camera1.renderImage();
		camera1.writeToImage();
	}

}