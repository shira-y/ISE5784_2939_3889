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
 * Test class for creating and rendering a scene with a chessboard and pawns.
 * This class demonstrates the use of various geometric shapes, materials, and
 * lighting techniques to create a realistic 3D scene, which is then rendered
 * using the {@link Camera} and {@link SimpleRayTracer}.
 * 
 * <p>
 * The chessboard is created using two alternating colors for the squares, and
 * pawns of different colors are placed on the board, including a fallen pawn.
 * The scene is illuminated by three different light sources to create various
 * lighting effects.
 * </p>
 */
public class MP1 {
	
	/**
	 * The {@link Scene} object that holds all the geometries and lights for the chessboard scene.
	 */
	private final Scene scene = new Scene("Chessboard Scene");

	/**
	 * Camera direction and orientation vector
	 */
	Vector towardsBoard = new Vector(1, -0.25, 1).normalize();
	/**
	 * Camera direction and orientation vector
	 */
	Vector upVector = new Vector(0, 1, 0);
	/**
	 * Camera direction and orientation vector
	 */
	Vector rightVector = towardsBoard.crossProduct(upVector).normalize();
	/**
	 * Camera direction and orientation vector
	 */
	Vector trueUpVector = rightVector.crossProduct(towardsBoard).normalize();

	/**
	 * {@link Camera.Builder} instance used to build the camera for rendering the scene.
	 * The camera is directed towards the chessboard with soft shadows enabled.
	 */
	private final Camera.Builder cameraBuilder = Camera.getBuilder()
			.setDirection(towardsBoard, trueUpVector)
			.setRayTracer(new SimpleRayTracer(scene).setUseSoftShadow(true));

	/**
	 * Test method for rendering a chessboard scene with pawns and various light
	 * sources. The method sets up the chessboard, adds pawns, and places three
	 * different light sources in the scene. Finally, it renders the scene from a
	 * specified camera position and writes the resulting image to a file.
	 */
	@Test
	public void ChessboardTest() {
		// Material with shininess for the chessboard and pawns
		Material shinyMaterial = new Material().setKd(0.5).setKs(0.5).setShininess(100);

		// Create the chessboard using alternating colors for the squares
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Color squareColor = (i + j) % 2 == 0 ? new Color(139, 69, 19) : new Color(169, 169, 169);
				double height = 0;
				scene.geometries.add(
						new Triangle(new Point(-40 + i * 10, height, -40 + j * 10),
								new Point(-30 + i * 10, height, -40 + j * 10),
								new Point(-30 + i * 10, height, -30 + j * 10))
								.setEmission(squareColor).setMaterial(shinyMaterial),
						new Triangle(new Point(-40 + i * 10, height, -40 + j * 10),
								new Point(-30 + i * 10, height, -30 + j * 10),
								new Point(-40 + i * 10, height, -30 + j * 10))
								.setEmission(squareColor).setMaterial(shinyMaterial));
			}
		}

		// Add a red standing pawn
		double pawnBaseY = 0.2;
		Point baseCenter = new Point(-35, pawnBaseY, -35);
		scene.geometries.add(
				new Sphere(baseCenter, 2).setEmission(new Color(255, 0, 0)).setMaterial(shinyMaterial),
				new Sphere(baseCenter.add(new Vector(0, 3, 0)), 1.5).setEmission(new Color(255, 0, 0))
						.setMaterial(shinyMaterial),
				new Sphere(baseCenter.add(new Vector(0, 5, 0)), 1).setEmission(new Color(255, 0, 0))
						.setMaterial(shinyMaterial));

		// Add a black fallen pawn
		Point fallenBaseCenter = new Point(-45, pawnBaseY, -35);
		scene.geometries.add(
				new Sphere(fallenBaseCenter.add(new Vector(0, 1, 0)), 2).setEmission(new Color(0, 0, 0))
						.setMaterial(shinyMaterial),
				new Sphere(fallenBaseCenter.add(new Vector(0, 1, 0).add(new Vector(2, 0, 0))), 1.5)
						.setEmission(new Color(0, 0, 0)).setMaterial(shinyMaterial),
				new Sphere(fallenBaseCenter.add(new Vector(0, 1, 0).add(new Vector(4, 0, 0))), 1)
						.setEmission(new Color(0, 0, 0)).setMaterial(shinyMaterial));

		// Add three light sources to the scene
		scene.lights.add(new DirectionalLight(new Color(200, 200, 200), new Vector(1, -1, 0)));
		scene.lights.add(new PointLight(new Color(50, 50, 50), new Point(-20, 50, -35))
				.setKl(0.00001).setKq(0.000005));
		scene.lights.add(new SpotLight(new Color(0, 0, 255), new Point(-30, 30, -35), new Vector(1, -1, 0))
				.setKl(0.00001).setKq(0.000005));

		// Set up the camera with specific parameters and render the image
		Camera camera1 = cameraBuilder.setLocation(new Point(-55, 8, -55))
				.setVpDistance(150)
				.setVpSize(80, 80)
				.setImageWriter(new ImageWriter("chessboard", 1000, 1000))
				.build();

		// Enable multithreading and debug printing, render the image, and save it
		camera1.setMultithreading(0)
				.setDebugPrint(0.1);
		camera1.renderImage();
		camera1.writeToImage();
	}
}
